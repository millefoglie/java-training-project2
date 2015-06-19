package textprocessor;

import textelements.Sentence;
import textelements.TextElement;
import textelements.Word;

import java.util.function.Consumer;

/**
 * A class that provides different operations which can be performed on texts.
 */
public class TextOperations {
    
    /** Swaps first and last words in all sentences */
    public static final Consumer<TextElement> swapFirstLastWords =
	    new SwapFirstLastWordsConsumer();
    
    private TextOperations() {}
    
    /**
     * A {@code Consumer} that swaps first and last words in 
     * each {@code Sentence} of some {@code Text} or other collection of
     * {@code TextElement} objects.
     */
    public static class SwapFirstLastWordsConsumer
            implements Consumer<TextElement> {
	
        @Override
        public void accept(TextElement element) {
            
            // check if working with a sentence
            if (!(element instanceof Sentence)) {
                return;
            }

            Sentence sentence = (Sentence) element;
            int firstIdx = -1;		// index of the first word
            int lastIdx = -1;		// index of the last word
            TextElement te;		// temp variable

            // find the index of the first word
            for (int i = 0, end = sentence.size(); i < end; i++) {
                if (sentence.getElement(i) instanceof Word) {
                    firstIdx = i;
                    break;
                }
            }
            
            // if no words found, stop
            if (firstIdx < 0) {
        	return;
            }

            // find the index of the last word
            for (int i = sentence.size() - 1; i > -1; i--) {
                if (sentence.getElement(i) instanceof Word) {
                    lastIdx = i;
                    break;
                }
            }

            // swap the first and the last words
            te = sentence.getElement(firstIdx);
            sentence.setElement(firstIdx, sentence.getElement(lastIdx));
            sentence.setElement(lastIdx, te);
        }
    }
}
