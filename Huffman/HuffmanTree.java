import java.util.*;
import java.io.*;

public class HuffmanTree {

    HuffmanNode overallRoot; 

    // Constructs a Huffman Tree using the frequencies of letters from the passed in array
    public HuffmanTree(int[] counts) {
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
    public HuffmanTree(Scanner input) {
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
                        node.isLeaf = true; 
                    } else {
                        node.isLeaf = false; 
                    }
                } else {
                    if (node.right == null) {
                        node.right = new HuffmanNode(0, 0); 
                    }
                    node = node.right;
                    if (i == code.length() - 1) {
                        node.character = n; 
                        node.isLeaf = true;
                    } else {
                        node.isLeaf = false; 
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
            if (node.isLeaf) {
                if (node.character == eof) {
                    return; 
                }
                output.write(node.character);
                node = overallRoot; 
            }
        } 
    }

    // Class for a HuffmanNode that implements Comparable so that it can be used in a priority queue 
    private class HuffmanNode implements Comparable<HuffmanNode> {
    
        public int character; 
        public int frequency; 
        public HuffmanNode left;
        public HuffmanNode right; 
        public boolean isLeaf; 
    
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
    
        // Method for comparing the frequencies of two nodes, used in the priority queue 
        public int compareTo(HuffmanNode node) {
            return this.frequency - node.frequency;         
        }
    
    }
  
}