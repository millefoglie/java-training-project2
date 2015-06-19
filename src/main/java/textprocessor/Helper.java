package textprocessor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A helper class that contains some useful methods for working with text.
 */
public class Helper {
    
    /** The logger. */
    private static final Logger LOGGER = LogManager.getLogger();
    
    /** The helper instance. */
    private static Helper helper = new Helper();
    
    /** The list of predefined abbreviations. */
    private List<String> abbreviations = loadAbbreviations();
    
    /** The map of paired punctuation marks */
    private Map<String, String> pairedPunctMarks = loadPunctMarks();
    
    /**
     * Instantiates a new helper.
     */
    private Helper() {}

    /**
     * Gets the single instance of Helper.
     * @return single instance of Helper
     */
    public static Helper getInstance() {
        return helper;
    }
    
    /**
     * Load the abbreviations dictionary from the file.
     * Encoding should be UTF-8.
     * @return the list of predefined abbreviations
     */
    private List<String> loadAbbreviations() {
	List<String> result = new ArrayList<>();
	
	try (BufferedReader br = new BufferedReader(
		new InputStreamReader(
			new FileInputStream("config/abbreviations.txt"),
				"UTF-8"))) {
	    br.lines().forEach(s -> result.add(s));
	} catch (IOException e) {
	    LOGGER.error(e);
	}
	
	return result;
    }
    
    /**
     * Load paires for opening and closing punctuation marks.
     * E.g. parentheses, brackets and quotation marks.
     * @return the map
     */
    private Map<String, String> loadPunctMarks() {
	Map<String, String> result = new HashMap<>();
	
	result.put("(", ")");
	result.put(")", "(");
	result.put("[", "]");
	result.put("]", "[");
	result.put("“", "”");
	result.put("”", "“");
	result.put("‘", "’");
	result.put("’", "‘");
	result.put("«", "»");
	result.put("»", "«");
	
	return result;
    }

    /**
     * Gets the opening or closing pair of a punctuation mark (parentheses).
     * @param p a punctuation mark
     * @return punctuation mark's pair
     */
    public String getPunctuationPair(String p) {
        return pairedPunctMarks.get(p);
    }

    /**
     * Checks if a string is an abbreviation.
     * @param str a string
     * @return true, if the string is an abbreviation
     */
    public boolean isAbbreviation(String str) {
        return (str != null) && (abbreviations.contains(str.toLowerCase()));
    }
}
