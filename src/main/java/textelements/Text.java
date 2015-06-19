package textelements;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A class that represents text.
 */
public class Text {
    
    /** The list of text elements that constitute the text. */
    private List<TextElement> textElements;

    /**
     * Instantiates a new text.
     */
    public Text() {
        textElements = new LinkedList<>();
    }

    /**
     * Adds a text element to the text.
     * @param te a text element
     */
    public void add(TextElement te) {
        textElements.add(te);
    }

    /**
     * Gets the text element.
     * @param i an index of a text element
     * @return the i-th text element
     */
    public TextElement getElement(int i) {
        if ((i < 0) || (i >= textElements.size())) {
            return null;
        }
        
        return textElements.get(i);
    }

    /**
     * Get the size of text.
     *
     * @return the size of text
     */
    public int size() {
        return textElements.size();
    }

    /**
     * Checks if the text is empty.
     * @return true, if the text is empty
     */
    public boolean isEmpty() {
        return textElements.isEmpty();
    }

    /**
     * Apply a {@code Consumer} object to text. This method is used to
     * perform various operations on text's child elements.
     * @param consumer the consumer
     */
    public void apply(Consumer<? super TextElement> consumer) {
        textElements.forEach(consumer);
    }

    @Override
    public String toString() {
        return textElements.stream()
        	.map(Object::toString)
        	.collect(Collectors.joining(" "));
    }
}
