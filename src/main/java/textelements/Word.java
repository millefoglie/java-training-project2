package textelements;

/**
 * A class that represents words.
 */
public class Word extends TextElement {

    /**
     * Instantiates a new word.
     * @param word the word
     */
    public Word(String word) {
        super(word);
    }

    /**
     * Instantiates a new word.
     * @param word the word
     * @param mergeOption the merge option
     */
    public Word(String word, MergeOptions mergeOption) {
        super(word, mergeOption);
    }
}
