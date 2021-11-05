package p2;

import java.util.*;
 /**
  * Main class for creation and control the expression tree
  * @author Andrés & Adrián
  */
class GFG{
   
    static double result = 0.0;

    // Tree Structure
    /**
     * Declares the expression nodes
     */
    static class nptr{
        char data;
        nptr left, right;
    }
 
    // Function to create new node
    /**
     * It works to create a new node
     * @param c Char 
     * @return n node
     */
    static nptr newNode(char c){
        nptr n = new nptr();
        n.data = c;
        n.left = n.right = null;
        return n;
    }
    /**
     * It Converts from String to Integer
     * @param s String
     * @return num String converted to Integer
     */
    static int toInt(String s){
        int num = 0;

        // Check if the integral value is
        // negative or not
        // If it is not negative, generate 
        // the number normally
        if (s.charAt(0) != '-')
            for(int i = 0; i < s.length(); i++)
                num = num * 10 + ((int)s.charAt(i) - 48);

        // If it is negative, calculate the +ve number
        // first ignoring the sign and invert the
        // sign at the end
        else
            for(int i = 1; i < s.length(); i++) 
            {
                num = num * 10 + ((int)(s.charAt(i)) - 48);
                num = num * -1;
            }
        return num;
    }
/**
 * It detects the operation sign and does the operation
 * @param Node position of the String
 * @return result Result of the operation
 */
    static double evaluation(nptr Node){    
        //double result = 0.0;
        
        if(Node == null){
            return 0;
        }
        if(Node.left == null && Node.right == null){
            String dato = String.valueOf(Node.data);
            return toInt(dato);
        }
        else{
            //double result = 0.0;
            double left = evaluation(Node.left);
            double right = evaluation(Node.right);
            char operator = Node.data;
            
            if(Node.data == '+'){
                result = left + right;
            }
            if(Node.data == '-'){
                result = left - right;
            }
            if(Node.data == '*'){
                result = left * right;
            }
            if(Node.data == '/'){
                result = left / right;
            }
            if(Node.data == '%'){
                result = left % right;
            }

            /*switch(operator){
                case '(':
                    result += 0;
                case ')':
                    result += 0;
                case '+':
                    result = left + right;
                    break;
                case '-':
                    result = left - right;
                    break;
                case '*':
                    result = left * right;
                    break;
                case '/':
                    result = left / right;
                    break;
                case '%':
                    result = left % right;
                    break;

            }*/
        }
        System.out.println(result);
        return result;
    }
 
    // Function to build Expression Tree
    /**
     * It works to build the Expression Tree
     * @param s Operation Sgin
     * @return t
     */
    static nptr build(String s){
        System.out.println(s);

        // Stack to hold nodes
        Stack<nptr> stN = new Stack<>();

        // Stack to hold chars
        Stack<Character> stC = new Stack<>();
        nptr t, t1, t2;

        // Prioritising the operators
        int p[] = new int[123];
        p[')'] = 0;
        p['+'] = p['-'] = 1;
        p['*'] = p['/'] = 2;
        p['%'] = 3;

        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == '(') {

                // Push '(' in char stack
                stC.add(s.charAt(i));
            }

            // Push the operands in node stack
            else if (Character.isDigit(s.charAt(i))){
                t = newNode(s.charAt(i));
                System.out.println(s.charAt(i));
                stN.add(t);
            }
            else if (p[s.charAt(i)] > 0){
                // If an operator with lower or
                // same associativity appears
                while (!stC.isEmpty() && stC.peek() != '(' && ((s.charAt(i) != '^' && p[stC.peek()] >= p[s.charAt(i)])
                        || (s.charAt(i) == '^'&& p[stC.peek()] > p[s.charAt(i)]))){

                    // Get and remove the top element
                    // from the character stack
                    t = newNode(stC.peek());
                    stC.pop();

                    // Get and remove the top element
                    // from the node stack
                    t1 = stN.peek();
                    stN.pop();

                    // Get and remove the currently top
                    // element from the node stack
                    t2 = stN.peek();
                    stN.pop();

                    // Update the tree
                    t.left = t2;
                    t.right = t1;

                    // Push the node to the node stack
                    stN.add(t);
                }

                // Push s[i] to char stack
                stC.push(s.charAt(i));
            }
            else if (s.charAt(i) == ')') {
                while (!stC.isEmpty() && stC.peek() != '('){
                    t = newNode(stC.peek());
                    stC.pop();

                    t1 = stN.peek();
                    stN.pop();

                    t2 = stN.peek();
                    stN.pop();

                    t.left = t2;
                    t.right = t1;
                    stN.add(t);
                }
                stC.pop();
            }
        }
        t = stN.peek();
        evaluation(t);
        return t;
    }
 
    // Function to print the post order
    // traversal of the tree
    /**
     * It works to print the post order
     * @param root 
     */
    static void postorder(nptr root){
        if (root != null){
            postorder(root.left);
            postorder(root.right);
            System.out.print(root.data);
        }
    }

    // Driver code
    /*public static void main(String[] args){

        Scanner in = new Scanner(System.in);

        System.out.println("Digite la operación a realizar");
        String s = in.nextLine();
        s = "(" + s;
        s += ")";
        nptr root = build(s);

        // Function call
        postorder(root);
        System.out.println("\n");
    }*/

}
