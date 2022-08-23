import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введи строку");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        System.out.println("Итог " + getNum(input));
        letter();
    }

    public static String getNum(String input) {
        StringBuilder sb = new StringBuilder();
        String pattern = "\\(-?\\d+[*+-/]\\d+\\)";
        Pattern р = Pattern.compile(pattern);
        Matcher m = р.matcher(input);
        if (m.find()) {
            String newInput = input.substring(input.indexOf('(') + 1, input.lastIndexOf(')'));
            newInput = getNum(newInput);
            input = input.replaceAll("\\(-?\\d+[*+-/]\\d+\\)", newInput);

        }
        Stack<Double> stackNumb = new Stack<>();
        Stack<Character> stackChar = new Stack<>();
        /* необходимые переменные */
        double result = 0;
        double y = 0;
        boolean minus = false;
        /* для смещения индексов */
        int i = 0;
        int I = 0;
        /* проверка на отрицательный знак в начале выражение.Если да-начинаем цикл с первого индекса */
        if (input.charAt(0) == '-') {
            minus = true;
            i = 1;
        }
        for (; i < input.length(); i++) {
            /* если найдено число */
            if (input.charAt(i) > 47 && input.charAt(i) < 58 || input.charAt(i) == '.') {
                /* кладем в буферридер */
                sb.append(input.charAt(i));
                /* если закончилась строка парсим ридер в double и кладем в стек */
                if (i == input.length() - 1 && minus == true) {
                    stackNumb.push(Double.parseDouble(sb.toString()) * (-1));
                } else if (i == input.length() - 1) stackNumb.push(Double.parseDouble(sb.toString()));
            } else {
                /* если есть минус перед числом, то набираем все число в стринг */
                if (minus == true) {
                    i++;
                    while (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.') {
                        sb.append(input.charAt(i));
                        i++;
                        if (i == input.length()) {
                            break;
                        }
                    }
                    stackNumb.push(Double.parseDouble(sb.toString()) * (-1));
                    minus = false;
                    sb.delete(0, sb.length());
                } else {
                    stackNumb.push(Double.parseDouble(sb.toString()));
                    /* очищаю S.Builder */
                    sb.delete(0, sb.length());
                }
                /* если следует кобинация /+-* и следом '-' */
                if (input.charAt(i + 1) == '-') {
                    minus = true;
                    I = 1;
                }
                /* если стек  пустой */
                if (stackChar.isEmpty()) {
                    /*кладу в стек знак*/
                    stackChar.push(input.charAt(i));
                    /*преобразую стрингбилдер в строку, ппарсим в дабл и кладем в стек с числами
                    если минус подтверждён-умножаю на -1,с целью сделать число отриц.
                    в противном случае сверяем знаки*/
                } else {
                    stackNumb.push(Double.parseDouble(sb.toString()));
                    sb.delete(0, sb.length());
                    /* достаём крайний знак и сверяем с приоритетом */
                    /* если приоритет знака из стека меньше */
                    char temp = stackChar.pop();
                    if (getPrior(input.charAt(i)) > getPrior(temp)) {
                        stackChar.push(temp);
                        /* проходим char  извлекаем следующее число */
                        int j;
                        for (j = i + 1; j < input.length(); j++) {
                            /* если char число и строка не закончилась */
                            if (input.charAt(j) > 47 && input.charAt(j) < 58 && j != input.length() - 1) {
                                /* кладем в стрингбилдер */
                                sb.append(input.charAt(j));
                            } else if (input.charAt(j) > 47 && input.charAt(j) < 58 && j == input.length() - 1) {
                                /* если char число и строка закончилась, то  мы парсим в дабл пpеобразованный в стринг стрингбилдер */
                                sb.append(input.charAt(j));
                                y = Double.parseDouble(sb.toString());
                                sb.delete(0, sb.length());
                            } else {
                                /* если уже char не число,то мы парсим в дабл пpеобразованный в стринг стрингбилдер */
                                y = Double.parseDouble(sb.toString());
                                sb.delete(0, sb.length());
                            }
                        }
                        result = getResult(stackNumb.pop(), y, input.charAt(i));
                        /*вместо извлеченного згачения устанавливаю результат операции*/
                        stackNumb.push(result);
                        i = I + j - 1;
                        I = 0;
                    }
                    /* еcли приоритет  равны */
                    else if (getPrior(input.charAt(i)) == getPrior(temp)) {
                        /* вернул в стек ранее извлеченный знак */
                        stackChar.push(temp);
                        /* добавил следующий знак равный по приоритету */
                        stackChar.push(input.charAt(i));
                    } /* если приоритет из стека выше */ else if (getPrior(input.charAt(i)) < getPrior(temp)) {
//                        System.out.println("In stack numb");
//                        for (Double el : stackNumb) {
//                            System.out.println(el);
//                        }
                        y = stackNumb.pop();
                        result = getResult(stackNumb.pop(), y, temp);
                        stackNumb.push(result);
                        stackChar.push(input.charAt(i));
                    }
                }
            }
            i = i + I;
            I = 0;
        }
//        System.out.println("In stack numb");
//        for (Double el : stackNumb) {
//            System.out.println(el);
//        }
//        System.out.println("In stack char ");
//        for (char el : stackChar) {
//            System.out.println(el);
//        }
        result = printResult(stackNumb, stackChar);
        String stringResult = result + "";
        return stringResult;
    }


    public static void letter() {
        char[][] letter = new char[7][6];
        letter[0][2] = '_';
        letter[0][3] = '_';
        letter[0][4] = ' ';
        letter[1][0] = ' ';
        letter[0][0] = ' ';
        letter[0][1] = ' ';
        letter[0][5] = ' ';
        letter[1][5] = ' ';
        letter[1][4] = ' ';
        letter[1][4] = '\\';
        letter[2][1] = ' ';
        letter[2][2] = ' ';
        letter[2][3] = ' ';
        letter[2][4] = ' ';
        letter[3][1] = ' ';
        letter[3][2] = ' ';
        letter[3][3] = ' ';
        letter[3][4] = ' ';
        letter[5][1] = ' ';
        letter[5][2] = ' ';
        letter[5][3] = ' ';
        letter[5][4] = ' ';
        letter[6][1] = ' ';
        letter[6][2] = ' ';
        letter[6][3] = ' ';
        letter[6][4] = ' ';
        letter[1][2] = ' ';
        letter[1][3] = ' ';
        letter[5][0] = '|';
        letter[1][1] = '/';
        letter[2][0] = '/';
        letter[2][5] = '\\';
        letter[3][0] = '|';
        letter[3][5] = '|';
        letter[4][1] = '=';
        letter[4][4] = '=';
        letter[4][2] = '=';
        letter[4][3] = '=';
        letter[4][0] = '|';
        letter[4][5] = '|';
        letter[4][0] = '|';
        letter[5][5] = '|';
        letter[6][0] = '|';
        letter[6][5] = '|';
        for (int i = 0; i < letter.length; i++) {
            for (int j = 0; j < letter[i].length; j++) {
                System.out.print(letter[i][j]);
            }
            System.out.println();
        }
    }

    public static double printResult(Stack stackNumb, Stack stackChar) {
        double result = 0;
        Stack<Double> newStackDigit = new Stack<>();
        Stack<Character> newStackChar = new Stack<>();
        //развернул стеки
        while (!stackNumb.empty()) {
            newStackDigit.push((Double) stackNumb.pop());
        }
        while (!stackChar.empty()) {
            newStackChar.push((Character) stackChar.pop());

        }//совершить все операции последовательно пока стек не опусташится
        while (!newStackChar.isEmpty()) {
            double x = (Double) newStackDigit.pop();
            double y = (Double) newStackDigit.pop();
            char ch = (Character) newStackChar.pop();
            result = getResult(x, y, ch);
            newStackDigit.push(result);
        }
        return result;
    }

    public static int getPrior(char el) {
        if (el == '+' || el == '-') {
            return 2;
        } else if (el == '*' || el == '/') {
            return 3;
        } else if (el == '(') {
            return 1;
        } else if (el == ')') {
            return -1;
        } else {
            return 0;
        }
    }

    public static double getResult(double x, double y, char ch) {
        double result = 0;
        if (ch == '+') {
            result = x + y;
        }
        if (ch == '-') {
            result = x - y;
        }
        if (ch == '*') {
            result = x * y;
        }
        if (ch == '/') {
            result = x / y;
        }
        return result;
    }
}

