package textelements;

/**
 * A class that represents punctuation marks.
 */
public class PunctuationMark extends TextElement {

    /**
     * Instantiates a new punctuation mark.
     * @param mark the punctuation mark
     */
    public PunctuationMark(String mark) {
        super(mark);
    }

    /**
     * Instantiates a new punctuation mark.
     * @param mark the punctuation mark
     * @param mergeOption the merge option
     */
    public PunctuationMark(String mark, MergeOptions mergeOption) {
        super(mark, mergeOption);
    }
}
