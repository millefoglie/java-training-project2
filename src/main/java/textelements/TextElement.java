package textelements;

/**
 * A class for a generic text element.
 */
public class TextElement {

    /** The element. */
    protected String element;
    
    /** The merge option. */
    protected MergeOptions mergeOption;
    
    /**
     * Instantiates a new text element.
     */
    public TextElement() {}
    
    /**
     * Instantiates a new text element.
     * @param element a string that represents the element
     */
    public TextElement(String element) {
        this(element, MergeOptions.NO_OPERATION);
    }

    /**
     * Instantiates a new text element.
     * @param element a string that represents the element
     * @param mergeOption the merge option
     */
    public TextElement(String element, MergeOptions mergeOption) {
        this.element = element;
        this.mergeOption = mergeOption;
    }

    /**
     * Gets the element string.
     * @return the element string
     */
    public String getElement() {
        return element;
    }

    /**
     * Gets the merge option.
     * @return the merge option
     */
    public MergeOptions getMergeOption() {
        return mergeOption;
    }

    @Override
    public String toString() {
        return element;
    }
}
