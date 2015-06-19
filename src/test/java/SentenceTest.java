import org.junit.Before;
import org.junit.Test;

import textelements.*;
import textprocessor.Helper;
import textprocessor.TextOperations;
import textprocessor.TextProcessor;
import static org.junit.Assert.assertEquals;

public class SentenceTest {

    private Sentence tester;

    @Before
    public void setUp() {
        tester = new Sentence();
    }

    @Test
    public void testToString() throws Exception {
        tester.add(new Word("Hello", MergeOptions.NO_OPERATION));
        tester.add(new PunctuationMark(",", MergeOptions.MERGE_LEFT));
        tester.add(new Word("world", MergeOptions.NO_OPERATION));
        tester.add(new PunctuationMark("!", MergeOptions.MERGE_LEFT));

        assertEquals(tester.toString(), "Hello, world!");
    }

    @Test
    public void testParser() throws Exception {
        TextProcessor tp = new TextProcessor();
        String str = "Как бы то ни было, Чезаре продолжал занятия,"
        	+ " покинув Пизу лишь через месяц — отец назначил его "
        	+ "комендантом замка Сполето, города на полдороге между ит."
        	+ " Римом и Перуджей.";
        String exp1 = "ит бы то ни было, Чезаре продолжал занятия,"
        	+ " покинув Пизу лишь через месяц — отец назначил его "
        	+ "комендантом замка Сполето, города на полдороге между Как."
        	+ " Перуджей и Римом.";
        String exp2 = "Перуджей бы то ни было, Чезаре продолжал занятия,"
        	+ " покинув Пизу лишь через месяц — отец назначил его "
        	+ "комендантом замка Сполето, города на полдороге между ит."
        	+ " Римом и Как.";
        Text text = tp.parse(str);
        Helper h = Helper.getInstance();
        
        text.apply(TextOperations.swapFirstLastWords);
        
        if (h.isAbbreviation("ит")) {
            assertEquals(exp2, text.toString());
        } else {
            assertEquals(exp1, text.toString());
        }
    }
}
