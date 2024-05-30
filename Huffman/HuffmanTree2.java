// This is a starter file that includes the read9/write9 methods described in
// the bonus assignment writeup.

import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class HuffmanTree2 {

    HuffmanNode overallRoot; 

    public HuffmanTree2(int[] counts) {
        Queue<HuffmanNode> frequencies = new PriorityQueue<HuffmanNode>();
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0) {
                frequencies.add(new HuffmanNode(i, counts[i]));
            }
        }
        frequencies.add(new HuffmanNode(counts.length, 1));
        while (frequencies.size() > 1) {
            HuffmanNode left = frequencies.remove();
            HuffmanNode right = frequencies.remove(); 
            HuffmanNode root = new HuffmanNode(0, left.frequency + right.frequency, left, right); 
            frequencies.add(root);
        }
        overallRoot = frequencies.remove(); 
    }

     // Writes each leaf Huffman Node's data and path to the output file 
    public void write(PrintStream output) {
        String path = "";
        writeRecurse(output, path, overallRoot);
    }

    // Recursive helper method for the write method to keep track of the path of nodes
    private void writeRecurse(PrintStream output, String path, HuffmanNode node) {
        if (node == null) {
            return; 
        }
        if (node.left != null) {
            writeRecurse(output, path + "0", node.left);
            writeRecurse(output, path + "1", node.right);
        } else {
            output.println(node.character);
            output.println(path); 
        }
    }

    // Constructs a Huffman Tree based off of a file of nodes with their data and path
    public HuffmanTree2(Scanner input) {
        overallRoot = new HuffmanNode(256, 0);
        while (input.hasNext()) {
            int n = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            HuffmanNode node = overallRoot;
            for (int i = 0; i < code.length(); i++) {
                if (code.charAt(i) == '0') {
                    if (node.left == null) {
                        node.left = new HuffmanNode(0, 0); 
                    } 
                    node = node.left; 
                    if (i == code.length() - 1) {
                        node.character = n; 
                    } 
                } else {
                    if (node.right == null) {
                        node.right = new HuffmanNode(0, 0); 
                    }
                    node = node.right;
                    if (i == code.length() - 1) {
                        node.character = n; 
                    }
                }
            }
        }
    }

    // Deciphers the input file using the code and prints it to the output, stopping after reaching the end of file value
    public void decode(BitInputStream input, PrintStream output, int eof) {
        HuffmanNode node = overallRoot; 
        while (true) {
            int bit = input.readBit(); 
            if (bit == 0) {
                node = node.left;
            } else if (bit == 1) {
                node = node.right; 
            } else {
                return; 
            }
            if (node.left == null) {
                if (node.character == eof) {
                    return; 
                }
                output.write(node.character);
                node = overallRoot; 
            }
        } 
    }

    // Reads in an input file containing a huffman tree structure and sets the root to that encoded tree
    public HuffmanTree2(BitInputStream input) {
        overallRoot = readTree(input); 
    }

    // Recursive helper method to read in the tree from an encoded file 
    private HuffmanNode readTree(BitInputStream input) {
        int type = input.readBit();
        if (type == 0) {
            HuffmanNode left = readTree(input); 
            HuffmanNode right = readTree(input); 
            return new HuffmanNode(left, right); 
        } else {
            return new HuffmanNode(read9(input), 0); 
        }
    }

    // Sets each ascii value in the array the code that represents it 
    public void assign(String[] codes) {
        String path = ""; 
        assignRecurse(codes, path, overallRoot);
    }

    // Recursive helper method to assign the codes with their path
    public void assignRecurse(String[] codes, String path, HuffmanNode node) {
        if (node == null) {
            return; 
        }
        if (node.left != null) {
            assignRecurse(codes, path + "0", node.left);
            assignRecurse(codes, path + "1", node.right);
        } else {
            codes[node.character] = path; 
        }
    }

    // Writes the tree to the compressed file 
    public void writeHeader(BitOutputStream output) {
        writeHelper(output, overallRoot);
    }

    // Recursive helper method to write the tree to the output file 
    private void writeHelper(BitOutputStream output, HuffmanNode node) {
        if (node.left == null) {
            output.writeBit(1);
            write9(output, node.character); 
        } else {
            output.writeBit(0);
            writeHelper(output, node.left);
            writeHelper(output, node.right); 
        }
    }

    // pre : an integer n has been encoded using write9 or its equivalent
    // post: reads 9 bits to reconstruct the original integer
    private int read9(BitInputStream input) {
        int multiplier = 1;
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += multiplier * input.readBit();
            multiplier = multiplier * 2;
        }
        return sum;
    }

    // pre : 0 <= n < 512
    // post: writes a 9-bit representation of n to the given output stream
    private void write9(BitOutputStream output, int n) {
        for (int i = 0; i < 9; i++) {
            output.writeBit(n % 2);
            n = n / 2;
        }
    }

    // Class for a HuffmanNode that implements Comparable so that it can be used in a priority queue 
    private class HuffmanNode implements Comparable<HuffmanNode> {
    
        public int character; 
        public int frequency; 
        public HuffmanNode left;
        public HuffmanNode right; 
    
        // Constructs HuffmanNode with a given character and frequency, used for leaf nodes
        public HuffmanNode(int character, int frequency) {
            this.character = character;
            this.frequency = frequency; 
            left = null;
            right = null;
        }
    
        // Constructs HuffmanNode with a given character, frequency, left, and right, used for internal nodes
        public HuffmanNode(int character, int frequency, HuffmanNode left, HuffmanNode right) {
            this.character = character;
            this.frequency = frequency;
            this.left = left;
            this.right = right; 
        }

        // Constructs HuffmanNode with left and right nodes defined, used for internal nodes 
        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            this.left = left;
            this.right = right; 
        }
    
        // Method for comparing the frequencies of two nodes, used in the priority queue 
        public int compareTo(HuffmanNode node) {
            return this.frequency - node.frequency;         
        }
    
    }
}
