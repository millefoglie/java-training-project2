package textprocessor;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import textelements.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;

/**
 * A class that provides functionality for reading, writing and parsing
 * an epub file.
 */
public class TextProcessor {
    
    /** The logger */
    private static final Logger LOGGER = LogManager.getLogger();

    /** An epub book */
    private Book book;

    /**
     * Instantiates a new text processor.
     */
    public TextProcessor() {}

    /**
     * Load the epub book form a path
     * @param path a path to the epub file
     */
    public void readBook(String path) {
        EpubReader er = new EpubReader();
        
        try {
            book = er.readEpub(new FileInputStream(path));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
    
    /**
     * Write the book into a file
     * @param path path to the output file
     */
    public void writeBook(String path) {
        EpubWriter ew = new EpubWriter();
        
        try {
            ew.write(book, new FileOutputStream(path));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Process the book: extract its paragraphs from all chapters, parse
     * them into {@code Text} objects, perform the words swapping operation,
     * replace the initial paragraphs with the resulted text and 
     * write the processed book back into a file.
     * @param pathIn path to the input epub file.
     * @param pathOut path to the output epub file.
     */
    public void processBook(String pathIn, String pathOut) {
        readBook(pathIn);

        Spine spine = book.getSpine();
        Resource resource;
        InputStream is;
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        
        String str;
	NodeList paragraphs;
	Node node;
        
        // traverse all Spine elements (chapter xml files in the epub)
        for (int i = 0, iend = spine.size(); i < iend; i++) {
            resource = spine.getResource(i);

            try {
        	is = resource.getInputStream();
        	builder = dbf.newDocumentBuilder();
        	doc = builder.parse(is);
        	
        	paragraphs = doc.getElementsByTagName("p");

        	// traverse all paragraphs in the xml file
        	for (int j = 0, jend = paragraphs.getLength(); j < jend; j++) {
        	    node = paragraphs.item(j);
        	    str = node.getTextContent();
        	    Text text = parse(str);
        	    
        	    text.apply(TextOperations.swapFirstLastWords);
        	    node.setTextContent(text.toString());
        	    System.out.println(text);
        	}

        	resource.setData(docToString(doc).getBytes());
            } catch (IOException e) {
        	LOGGER.error(e);
            } catch (SAXException e) {
        	LOGGER.error(e);
            } catch (ParserConfigurationException e) {
        	LOGGER.error(e);
            }
        }

        writeBook(pathOut);
    }
    
    /**
     * Parse a text string into a {@code Text} object, which has a tree 
     * structure and contains text elements like sentences, words and
     * punctuation marks. 
     * @param str a string to be parsed.
     * @return the {@code Text} object.
     */
    public Text parse(String str) {
        Text text = new Text();
        Sentence sentence = new Sentence();
        Helper h = Helper.getInstance();
        
        // previous word. For managing abbreviations
        String prev = null;
        
        // has quotation started?
        boolean quoteFlag = false;
        
        // stack of paired marks, like parentheses, etc.
        LinkedList<String> pairedPunctuationElements = new LinkedList<String>();
        
        // tokenise and parse
        for (String s : prepare(str).split(" +")) {
            if (s.matches(Patterns.WORD)) {
                sentence.add(new Word(s));
                prev = s;
                continue;
            }

            if (s.matches(Patterns.SIMPLE_MARK)) {
                sentence.add(new PunctuationMark(s, MergeOptions.MERGE_LEFT));
                continue;
            }

            if (s.matches(Patterns.HYPHEN)) {
                sentence.add(new PunctuationMark(s, MergeOptions.MERGE_BOTH));
                continue;
            }

            if (s.matches(Patterns.DASH)) {
                sentence.add(new PunctuationMark(s, MergeOptions.NO_OPERATION));
                continue;
            }

            if (s.matches(Patterns.PAIRED_OPEN)) {
                pairedPunctuationElements.add(s);
                sentence.add(new PunctuationMark(s, MergeOptions.MERGE_RIGHT));
                continue;
            }

            if (s.matches(Patterns.PAIRED_CLOSE)) {
        	
        	// if a paired mark is on stack, remove it
                if (h.getPunctuationPair(s)
                        .equals(pairedPunctuationElements.peek())) {
                    pairedPunctuationElements.poll();
                }
                
                sentence.add(new PunctuationMark(s, MergeOptions.MERGE_LEFT));
                continue;
            }

            if (s.matches(Patterns.STD_QUOTE)) {
                quoteFlag = !quoteFlag;
                MergeOptions opt = quoteFlag ?
                        MergeOptions.MERGE_RIGHT :
                        MergeOptions.MERGE_LEFT;
                sentence.add(new PunctuationMark(s, opt));
                continue;
            }

            if (s.matches(Patterns.SENTENCE_END)) {
        	
        	/*
        	 * if the last word is an abbreviation, add the dot to it
        	 * and continue
        	 */
        	if (h.isAbbreviation(prev)) {
        	    sentence.setLastElement(
        		    new Word(prev + s, MergeOptions.NO_OPERATION));
        	    continue;
        	}
                
        	// otherwise, just add it usually
        	sentence.add(
            	    new PunctuationMark(s, MergeOptions.MERGE_LEFT));
        	
                /*
                 * finish the sentence only if not in a quote and 
                 * all parentheses or other paired marks are closed
                 */
                if (pairedPunctuationElements.isEmpty() && !quoteFlag) {
                    text.add(sentence);
                    sentence = new Sentence();
                }
                
                continue;
            }
            
            // if couldn't establish element's type, just add it as is
            sentence.add(new TextElement(s));
            LOGGER.warn("Element " + s 
        	    + " could not be parsed correctly");
        }

        // add anything that was left
        if (!sentence.isEmpty()) {
            text.add(sentence);
        }
        
        return text;
    }

    /**
     * Add extra spaces around punctuation marks for proper tokenisation
     * @param str a string to be processed
     * @return the string {@code str} with extra white spaces
     */
    private String prepare(String str) {
        return str.replaceAll("(" + Patterns.LINE_BREAK + "|"
                + Patterns.SENTENCE_END + "|"
                + Patterns.PAIRED_OPEN + "|"
                + Patterns.PAIRED_CLOSE + "|"
                + Patterns.SIMPLE_MARK + "|"
                + Patterns.STD_QUOTE + ")", " $1 ");
    }
    
    /**
     * Convert {@code org.w3c.Document} to a String. 
     * @param doc {@code org.w3c.Document} to be converted.
     * @return the string that contains {@code doc}'s code.
     */
    private String docToString(Document doc) {
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transf = null;

        try {
            transf = tf.newTransformer();
            transf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transf.setOutputProperty(OutputKeys.METHOD, "xml");
            transf.setOutputProperty(OutputKeys.INDENT, "yes");
            transf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transf.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (TransformerConfigurationException e) {
            LOGGER.error(e);
        } catch (TransformerException e) {
            LOGGER.error(e);
        }

        return sw.toString();
    }
}
