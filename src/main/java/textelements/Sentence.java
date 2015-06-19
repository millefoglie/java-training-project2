package textelements;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A class that represents sentences.
 */
public class Sentence extends TextElement{
    
    /** 
     * The list of text elements (e.g. words or punctuation marks), which
     * makes the sentence 
     */
    private List<TextElement> textElements;

    /**
     * Instantiates a new sentence.
     */
    public Sentence() {
        textElements = new LinkedList<>();
    }

    /**
     * Adds a text element to the sentence.
     * @param te a text element
     */
    public void add(TextElement te) {
        textElements.add(te);
    }

    /**
     * Gets the element.
     * @param i an index of a text element
     * @return the i-th text element of the sentence
     */
    public TextElement getElement(int i) {
        if ((i < 0) || (i >= textElements.size())) {
            return null;
        } else {
            return textElements.get(i);
        }
    }

    /**
     * Sets the element.
     * @param i an index of a text element
     * @param te a new text element
     * @return true, if successful
     */
    public boolean setElement(int i, TextElement te) {
        if ((i < 0) || (i >= textElements.size())) {
            return false;
        }

        textElements.set(i, te);
        return true;
    }
    
    /**
     * Sets the last element.
     * @param te a new last text element
     * @return true, if successful
     */
    public boolean setLastElement(TextElement te) {
        textElements.set(textElements.size() - 1, te);
        return true;
    }

    /**
     * Get sentence's size.
     * @return the size of the sentence
     */
    public int size() {
        return textElements.size();
    }

    /**
     * Checks if the sentence is empty.
     * @return true, if the sentence is empty
     */
    public boolean isEmpty() {
        return textElements.isEmpty();
    }

    @Override
    public String toString() {
        if (textElements.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        ListIterator<TextElement> it = textElements.listIterator();
        TextElement prev = it.next();
        TextElement curr = prev;
        String delim = "";

        MergeOptions prevOpt;
        MergeOptions currOpt;

        sb.append(prev);

        while (it.hasNext()) {
            curr = it.next();
            prevOpt = prev.getMergeOption();
            currOpt = curr.getMergeOption();

            if ((prevOpt == MergeOptions.MERGE_RIGHT)
                    || (prevOpt == MergeOptions.MERGE_BOTH)
                    || (currOpt == MergeOptions.MERGE_LEFT)
                    || (currOpt == MergeOptions.MERGE_BOTH)) {
                delim = "";
            } else {
                delim = " ";
            }

            sb.append(delim).append(curr.getElement());

            prev = curr;
        }
        
        return sb.toString();
    }
}
