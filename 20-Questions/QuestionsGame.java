// This is a starter file for QuestionsGame.
//
// You should delete this comment and replace it with your class
// header comment.

import java.io.PrintStream;
import java.util.Scanner;

public class QuestionsGame {
    // Your code here

    private QuestionNode overallRoot; 
    private Scanner console; 

    // Initializes the root of the question tree as an answer "computer" and initializes the input
    public QuestionsGame() {
        overallRoot = new QuestionNode("computer"); 
        console = new Scanner(System.in); 
    }

    // Writes the question tree to the output file in pre-order 
    public void write(PrintStream output) {
        writeNode(output, overallRoot); 
    }

    // Recursive helper method for the write method that prints the node's
    // type and data, and if it's a question node, calls the method on its children 
    private void writeNode(PrintStream output, QuestionNode node) {
        if (node.data.indexOf('?') > -1) {
            output.println("Q:");
            output.println(node.data);
            writeNode(output, node.yes);
            writeNode(output, node.no); 
        } else {
            output.println("A:");
            output.println(node.data); 
        }
    }

    // Reads a text file that contains nodes and their data in pre-order and 
    // creates the question tree based off of that  
    public void read(Scanner input) {
        overallRoot = readTreeHelper(input);
    }

    // Recursive helper method for the read method, either returns the node
    // if it's an answer node, or sets the left and right nodes to the next
    // line nodes and returns itself
    private QuestionNode readTreeHelper(Scanner input) {
        char type = input.nextLine().charAt(0);
        String data = input.nextLine();
        QuestionNode node = new QuestionNode(data);
        if (type == 'A') {
            return node; 
        } else {
            node.yes = readTreeHelper(input);
            node.no = readTreeHelper(input);
            return node; 
        }
    }

    // Asks questions until it gets to an answer, when it will
    // ask the user if the answer is right; if not, it will add
    // the user's answer to the question tree 
    public void askQuestions() {
        askQuestions(overallRoot);
    }

    // Recursive helper method for the ask questions method which
    // asks the question of the input node or checks its answer 
    // if it's an answer node 
    private void askQuestions(QuestionNode node) {
        if (node.data.contains("?")) {
            if (yesTo(node.data)) {
                askQuestions(node.yes);
            } else {
                askQuestions(node.no);
            }
        } else {
            if (yesTo("Would your object happen to be " + node.data)) {
                System.out.println("I win");
            } else {
                System.out.print("What is the name of your object? ");
                String answer = console.nextLine(); 
                System.out.print("Please give me a yes/no question to distinguish between the two objects: "); 
                String question = console.nextLine(); 
                while (!question.contains("?")) {
                    System.out.print("Please enter a valid question: ");
                    question = console.nextLine(); 
                }
                if (yesTo("And what is the answer for your object:")) {
                    String noAnswer = node.data; 
                    node.data = question;
                    node.yes = new QuestionNode(answer);
                    node.no = new QuestionNode(noAnswer);
                } else {
                    String yesAnswer = node.data;
                    node.data = question;
                    node.yes = new QuestionNode(yesAnswer);
                    node.no = new QuestionNode(answer); 
                }
            }
        }
    }

    // post: asks the user a question, forcing an answer of "y" or "n";
    //       returns true if the answer was yes, returns false otherwise
    public boolean yesTo(String prompt) {
        System.out.print(prompt + " (y/n)? ");
        String response = console.nextLine().trim().toLowerCase();
        while (!response.equals("y") && !response.equals("n")) {
            System.out.println("Please answer y or n.");
            System.out.print(prompt + " (y/n)? ");
            response = console.nextLine().trim().toLowerCase();
        }
        return response.equals("y");
     }   

    // Class for a question node 
    private static class QuestionNode {
        // Your code here
        public String data;
        public QuestionNode yes;
        public QuestionNode no;

        private QuestionNode(String info) {
            data = info; 
            yes = null;
            no = null;             
        }

    }
}
