import java.text.DecimalFormat;
import java.util.*;

class ReturnTypeConvert {
    String result = "";
    HashMap<Integer, Boolean> map = new HashMap<>();
}

class InfixToPostFix {

    String expression;
    ArrayList<String> operands;
    public InfixToPostFix(String expression)
    {
        this.expression = expression;
        this.operands = new ArrayList<String>();
        this.operands.add("+");
        this.operands.add("*");
        this.operands.add("/");
        this.operands.add("-");
    }

    public int precedence(char ch)
    {
        switch (ch)
        {
            case '+':
            case '-':
                return 1;

            case '*':
            case '/':
                return 2;
        }
        return -1;
    }

    public ReturnTypeConvert convert()
    {
        Stack<Character> s = new Stack<>();
        String result = "";
        HashMap<Integer, Boolean> map = new HashMap<>();
        HashMap<Integer, Boolean> map1 = new HashMap<>();
        int openBracketCount = 0;
        boolean previousCharOperand = false;
        boolean negativeNumber = false;
        boolean doNegativeWhenBrackClose = false;
        for(int i=0;i<this.expression.length();i++)
        {
            char c = this.expression.charAt(i);
            if(!previousCharOperand && c == '-')
            {
                negativeNumber = true;
                continue;
            }
            else if(Character.isDigit(c))
            {
                previousCharOperand = true;
                double num = 0;
                boolean ifDecimal = false;
                int cntAfterDecimal = 0;
                while(Character.isDigit(c) || c == '.')
                {
                    if(c == '.')
                    {
                        ifDecimal = true;
                        i++;
                    }
                    else{
                        if(ifDecimal)
                        {
                            cntAfterDecimal += 1;
                        }
                        num = num*10 + (int)(c-'0');
                        i++;
                    }
                    if(i == this.expression.length())
                        break;
                    c = this.expression.charAt(i);
                }
                if(ifDecimal)
                {
                    num = num / Math.pow(10, cntAfterDecimal);
                }
                DecimalFormat df = new DecimalFormat("#.##");
                int index = result.length() + df.format(num).length() - 1;
                map.put(index, negativeNumber);
                if(negativeNumber) negativeNumber = false;

                result = result + String.valueOf(df.format(num)) + " ";
                i--;
            }
            else if(c == '(')
            {
                previousCharOperand = false;
                openBracketCount++;
                map1.put(openBracketCount, negativeNumber);
                if(negativeNumber)
                {
                    negativeNumber = false;
                }
                s.push(c);
            }
            else if(c == ')')
            {
                previousCharOperand = true;
                while (!s.isEmpty() &&
                        s.peek() != '(')
                    result = result + s.pop() + " ";

                if(map1.containsKey(openBracketCount))
                {
                    boolean val = map1.get(openBracketCount);
                    if(val)
                    {
                        map.put(result.length(), true);
                        result = result + "1 * ";
                    }
                }
                openBracketCount--;
                s.pop();
            }
            else if(this.operands.contains(String.valueOf(c)))
            {
                previousCharOperand = false;
                while (!s.isEmpty() && precedence(c) <= precedence(s.peek())){
                    result = result + s.pop() + " ";
                }
                s.push(c);
            }
        }
        while (!s.isEmpty()){
            if(s.peek() == '(')
            {
                ReturnTypeConvert rt = new ReturnTypeConvert();
                rt.result = "";
                rt.map = map;
                return rt;
            }
            result = result + s.pop() + " ";
        }

        ReturnTypeConvert rt = new ReturnTypeConvert();
        rt.result = result;
        rt.map = map;
        return rt;
    }
}

public class ArithmeticExpression {

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String infixExpression = sc.nextLine();
        infixExpression.replaceAll(" ","");
        InfixToPostFix itp = new InfixToPostFix(infixExpression);
        ReturnTypeConvert rt = itp.convert();
        String postfixExp = rt.result;
        HashMap<Integer, Boolean> map = rt.map;
        System.out.println(postfixExp);
        if(postfixExp !=null && postfixExp.length() > 0)
        {
            Stack<Double> s = new Stack<>();
            for(int i =0;i<postfixExp.length();i++)
            {
                char c = postfixExp.charAt(i);

                if(c == ' ')
                {
                    continue;
                }
                else if(Character.isDigit(c) || c == '.')
                {
                    double num = 0;
                    boolean ifDecimal = false;
                    int cntAfterDecimal = 0;
                    while(Character.isDigit(c) || c == '.')
                    {
                        if(c == '.')
                        {
                            ifDecimal = true;
                            i++;
                        }
                        else{
                            if(ifDecimal)
                            {
                                cntAfterDecimal += 1;
                            }
                            num = num*10 + (int)(c-'0');
                            i++;
                        }
                        if(i == postfixExp.length())
                            break;
                        c = postfixExp.charAt(i);
                    }
                    if(ifDecimal)
                    {
                        num = num / Math.pow(10, cntAfterDecimal);
                    }
                    i--;
                    if(map.containsKey(i))
                    {
                        boolean negativeNumber = map.get(i);
                        if(negativeNumber)
                            num = -1*num;
                    }
                    s.push(num);
                }
                else
                {
                    double val1 = s.pop();
                    double val2 = s.pop();

                    switch(c)
                    {
                        case '+':
                            s.push(val2+val1);
                            break;

                        case '-':
                            s.push(val2-val1);
                            break;

                        case '/':
                            s.push(val2/val1);
                            break;

                        case '*':
                            s.push(val2*val1);
                            break;
                    }
                }
            }
            DecimalFormat df = new DecimalFormat("#.##");

            System.out.println(df.format(s.pop()));
        }
        else
        {
            System.out.println("Invalid Expression");
        }
    }
}
