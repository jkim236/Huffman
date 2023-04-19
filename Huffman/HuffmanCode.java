/**
 * Joshua Kim
 * March 10th, 2023
 * TA: Chaos Gao
 * HuffmanCode
 */
import java.util.*;
import java.io.*;

// This class is a HuffmanCode class, it can reduce the amount of memory used to store a sequence
// of characters.
public class HuffmanCode {
    private Queue<HuffmanNode> queue;
    private HuffmanNode root;

    /**
    * Contructor for the HuffmanCode class
    * Parameters:
    *   - frequencies: Integer array containing frequencies for each character in a String.
    *                  each index represents an ascii code for a character and the value at
    *                  that index is the frequency of that character in the String
    * Exceptions:
    *   - None
    */
    public HuffmanCode(int[] frequencies) {
        queue = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i ++) {
            if (frequencies[i] > 0) {
                queue.add(new HuffmanNode(frequencies[i], (char)i));
            }
        }
        while (queue.size() > 1) {
            HuffmanNode one = queue.remove();
            HuffmanNode two = queue.remove();
            HuffmanNode toAdd = new HuffmanNode(one.freq + two.freq, one, two);
            queue.add(toAdd);
        }
        this.root = queue.remove();
    }

    /**
    * Contructor for the HuffmanCode class
    * Parameters:
    *   - input: Scanner that contains a .code file which will be read to initilize this instance
    * Exceptions:
    *   - None
    */
    public HuffmanCode(Scanner input) {
        this.root = new HuffmanNode('\0');
        while (input.hasNextLine()) {
            char letter = (char)Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            root = change(root, letter, code);
        }
    }

    /**
    * Helper method for constructor, uses the x = change(x) pattern to initialize the tree
    * Parameters:
    *   - node: HuffmanNode that is used to traverse the tree
    *   - letter: Character that represents letter to be added to the tree
    *   - code: String that represents the position of the letter to be added to the tree
    * Returns:
    *   - modified version of HuffmanNode passed in, updated with letter to be added
    * Exceptions:
    *   - None
    */
    private HuffmanNode change(HuffmanNode node, char letter, String code) {
        if (code.length() == 1) {
            if (code.charAt(0) == '0') {
                node.left = new HuffmanNode(letter);
            }
            else {
                node.right = new HuffmanNode(letter);
            }
            return node;
        }
        if (code.charAt(0) == '0') {
            if (node.left == null) {
                node.left = new HuffmanNode('\0');
            }
            node.left = change(node.left, letter, code.substring(1));
        }
        else {
            if (node.right == null) {
                node.right = new HuffmanNode('\0');
            }
            node.right = change(node.right, letter, code.substring(1));
        }
        return node;
    }

    /**
    * This method stores the current Huffman Code to the given output stream.
    * Parameters:
    *   - output: Printstream which this Huffman Code will be stored in
    * Returns:
    *   - Nothing
    * Exceptions:
    *   - None
    */
    public void save(PrintStream output) {
        helper(output, this.root, "");
    }

    /**
    * Helper method for save(), traverses the tree and adds all characters and
    * ascii code representations to the output stream
    * Parameters:
    *   - output: Printstream which this Huffman Code will be stored in
    *   - node: HuffmanNode used to traverse the tree
    *   - curr: String used to keep track of location while traversing the tree
    * Returns:
    *   - Nothing
    * Exceptions:
    *   - None
    */
    private void helper(PrintStream output, HuffmanNode node, String curr) {
        if (node.character != '\0') {
            if (curr.length() == 0) {
                curr += "0";
            }
            output.append((int)node.character + "\n" + curr + "\n");
        }
        else {
            if (node.left != null) helper(output, node.left, curr + "0");
            if (node.right != null) helper(output, node.right, curr + "1");
        }
    }

    /**
    * This method reads individual bits from the input stream and writes the corresponding 
    * characters to the output stream. Stops reading when the BitInputStream is empty.
    * Parameters:
    *   - input: BitInputStream that contains ascii codes to be translated and written to
    *            the output stream
    *   - output: Printstream which this Huffman Code will be stored in
    * Returns:
    *   - Nothing
    * Exceptions:
    *   - None
    */
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode node = root;
        while (input.hasNextBit()) {
            if (node.left == null && node.right == null) {
                output.write(node.character);
                node = root;
            }
            else {
                int num = input.nextBit();
                if (num == 0) {
                    node = node.left;
                }
                else if (num == 1) {
                    node = node.right;
                }
            }
        }
        output.write(node.character);
        node = root;
    }

    // private class representing HuffmanNode. Implements Comparable interface in order to 
    // compare frequencies of nodes
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public int freq;
        public char character;
        public HuffmanNode left;
        public HuffmanNode right;

        /**
        * Constructor for HuffmanNode class
        * Parameters: 
        *   - freq: Frequency a given character appears in a string
        *   - character: character this node will contain
        * Exceptions: 
        *   - None
        */
        public HuffmanNode(int freq, char character) {
            this.freq = freq;
            this.character = character;
            this.left = null;
            this.right = null;
        }

        /**
        * Constructor for HuffmanNode class, used for nodes without characters
        * Parameters: 
        *   - freq: Frequency a given character appears in a string
        *   - left: Left child of this HuffmanNode 
        *   - right: Right child of this HuffmanNode
        * Exceptions: 
        *   - None
        */
        public HuffmanNode(int freq, HuffmanNode left, HuffmanNode right) {
            this.freq = freq;
            this.character = '\0';
            this.left = left;
            this.right = right;
        }

        /**
        * Constructor for HuffmanNode class, used when HuffmanCode is initialized with Scanner
        * input
        * Parameters: 
        *   - character: character this node will contain
        * Exceptions: 
        *   - None
        */
        public HuffmanNode(char character) {
            this.freq = 0;
            this.character = character;
            this.left = null;
            this.right = null;
        }

        /**
        * Method that compares two HuffmanNodes
        * Parameters: 
        *   - node: HuffmanNode this instance will be compared to
        * Returns:
        *   - Integer representing the frequency of the HuffmanNode passed in subtracted
        *     from this instance of a HuffmanNode
        * Exceptions: 
        *   - None
        */
        public int compareTo(HuffmanNode node) {
            return this.freq - node.freq;
        }

    }
}
