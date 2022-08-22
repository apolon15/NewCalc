import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введи строку");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        System.out.println("Итог " + getNum(input));
    }

    public static Double getNum(String input) {
        Stack<Double> stackNumb = new Stack<>();
        Stack<Character> stackChar = new Stack<>();
        StringBuilder sb = new StringBuilder();
        double result = 0;
        double y = 0;
        boolean minus = false;
        int i = 0;
        //проверка на отрицательный знак в начале выражение.Если да-начинаем цикл с первого индекса
        if (input.charAt(0) == '-') {
            minus = true;
            i = 1;
        }
        for (; i < input.length(); i++) {
            //если найдено число
            if (input.charAt(i) > 47 && input.charAt(i) < 58) {//кладем в буферридер
                sb.append(input.charAt(i));
                //если закончилась строка
                if (i == input.length() - 1) {
                    stackNumb.push(Double.parseDouble(sb.toString()));
                }
            } else {
                //если стек  пустой
                if (stackChar.isEmpty()) {
                    //кладу в стек знак
                    stackChar.push(input.charAt(i));
                    //преобразую стрингбилдер в строку, ппарсим в дабл и кладем в стек с числами
                    //если минус подтверждён-умножаю на -1,с целью сделать число отриц.
                    if (minus == true) {
                        stackNumb.push(Double.parseDouble(sb.toString()) * (-1));
                        minus = false;
                        sb.delete(0, sb.length());
                    } else {
                        stackNumb.push(Double.parseDouble(sb.toString()));
                        //очищаю S.Builder
                        sb.delete(0, sb.length());
                    }

                    //в противном случае сверяем знаки
                } else {
                    stackNumb.push(Double.parseDouble(sb.toString()));
                    sb.delete(0, sb.length());
                    //достаём крайний знак и сверяем с приоритетом
                    //если приоритет знака из стека меньше
                    char temp = stackChar.pop();
                    if (getPrior(input.charAt(i)) > getPrior(temp)) {
                        stackChar.push(temp);
                        //проходим char  извлекаем следующее число
                        int j;
                        for (j = i + 1; j < input.length(); j++) {
                            //если char число и строка не закончилась
                            if (input.charAt(j) > 47 && input.charAt(j) < 58 && j != input.length() - 1) {
                                //кладем в стрингбилдер
                                sb.append(input.charAt(j));
                            } else if (input.charAt(j) > 47 && input.charAt(j) < 58 && j == input.length() - 1) {
                                //если char число и строка закончилась, то  мы парсим в дабл пpеобразованный в стринг стрингбилдер
                                sb.append(input.charAt(j));
                                y = Double.parseDouble(sb.toString());
                                sb.delete(0, sb.length());
                            } else {
                                //если уже char не число,то мы парсим в дабл пpеобразованный в стринг стрингбилдер
                                y = Double.parseDouble(sb.toString());
                                sb.delete(0, sb.length());
                            }
                        }
                        result = getResult(stackNumb.pop(), y, input.charAt(i));
                        //вместо извлеченного згачения устанавливаю результат операции
                        stackNumb.push(result);
                        i = j - 1;
                    }
                    //еcли приоритет  равны
                    else if (getPrior(input.charAt(i)) == getPrior(temp)) {
                        //вернул в стек ранее извлеченный знак
                        stackChar.push(temp);
                        //добавил следующий знак равный по приоритету
                        stackChar.push(input.charAt(i));
                    }//если приоритет из стека выше
                    else if (getPrior(input.charAt(i)) < getPrior(temp)) {
                        System.out.println("In stack numb");
                        for (Double el : stackNumb) {
                            System.out.println(el);
                        }
                        y = stackNumb.pop();
                        result = getResult(stackNumb.pop(), y, temp);
                        stackNumb.push(result);
                        stackChar.push(input.charAt(i));
                    }
                }
            }
        }
        System.out.println("In stack numb");
        for (Double el : stackNumb) {
            System.out.println(el);
        }
        System.out.println("In stack char ");
        for (char el : stackChar) {
            System.out.println(el);
        }
        result = printResult(stackNumb, stackChar);
        return result;
    }

    public static double printResult(Stack stackNumb, Stack stackChar) {
        double result = 0;
        Stack<Double> newStackDigit = new Stack<>();
        Stack<Character> newStackChar = new Stack<>();
        while (!stackNumb.empty()) {
            newStackDigit.push((Double) stackNumb.pop());
        }
        while (!stackChar.empty()) {
            newStackChar.push((Character) stackChar.pop());

        }
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

