package textprocessor;

/**
 * A class that contains predefined patterns for matching text elements.
 */
public class Patterns {
    
    // caution! some characters may appear to be the same. They're not!
    public static final String WORD = "[\\p{L}\\d']+";
    public static final String SIMPLE_MARK = "[,;:]";
    public static final String HYPHEN = "-";
    public static final String DASH = "[–—―]";
    public static final String PAIRED_OPEN = "[\\(\\[“‘«]";
    public static final String PAIRED_CLOSE = "[\\)\\]”’»]";
    public static final String STD_QUOTE = "\"";
    public static final String SENTENCE_END = "([\\..!?…]+)";
    public static final String DOT = "([\\..])";
    public static final String LINE_BREAK = "[\\n\\r]";
    
    private Patterns() {}
}
