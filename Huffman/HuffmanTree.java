import java.io.PrintStream;
import java.util.PriorityQueue;

public class HuffmanTree {
    
    PriorityQueue<HuffmanNode> frequencies;

    public HuffmanTree(int[] count) {
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                char c = (char) i;
                frequencies.add(new HuffmanNode(count[i], c));
            }
        }
    }

    public void write(PrintStream output) {
        
    }

}
