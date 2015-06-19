package textprocessor;

/**
 * A demo class.
 */
public class TextProcessorDemo {
    
    private TextProcessorDemo() {}

    public static void main(String[] args) {
        TextProcessor tp = new TextProcessor();

        tp.processBook("data/Sabatini.epub", "data/out.epub");
    }
}
