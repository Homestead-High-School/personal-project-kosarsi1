public class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    char data; 

    public HuffmanNode(int f, char c) {
        frequency = f;
        data = c; 
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return this.frequency - o.frequency;
    }

}
