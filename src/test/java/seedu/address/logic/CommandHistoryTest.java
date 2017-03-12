package seedu.address.logic;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CommandHistoryTest {
    private CommandHistory history;

    @Before
    public void setup() {
        history = new CommandHistory();
    }

    @Test
    public void test() {
        history.addCommand("command1");
        history.addCommand("command2");
        history.addCommand("command3");
        history.addCommand("command4");

        //Test standard iteration
        assertTrue(history.previous().equals("command4"));
        assertTrue(history.previous().equals("command3"));
        assertTrue(history.previous().equals("command2"));
        assertTrue(history.previous().equals("command1"));
        assertTrue(history.next().equals("command2"));
        assertTrue(history.next().equals("command3"));
        assertTrue(history.previous().equals("command2"));

        //Test edge case of going all the way to the back
        history.addCommand("command5");
        assertTrue(history.previous().equals("command5"));
        assertTrue(history.previous().equals("command4"));
        assertTrue(history.previous().equals("command3"));
        assertTrue(history.previous().equals("command2"));
        assertTrue(history.previous().equals("command1"));
        assertTrue(history.previous() == null); //No more commands, stop at command1
        assertTrue(history.previous() == null); //No more commands, stop at command1
        //Next will be command2 since "current command" should be at command1
        assertTrue(history.next().equals("command2"));

        //Test edge case of going all the way to the front
        history.addCommand("command6");
        assertTrue(history.next() == null);
        assertTrue(history.next() == null);
        assertTrue(history.next() == null);

        //Test edge case of going back once, then going all the way to the front
        history.addCommand("command7");
        assertTrue(history.previous().equals("command7"));
        assertTrue(history.next() == null);
        assertTrue(history.next() == null);
        assertTrue(history.next() == null);
        assertTrue(history.previous().equals("command7"));

        //Iterator all the way with overlaps
        history.resetIterator();
        assertTrue(history.previous().equals("command7"));
        assertTrue(history.previous().equals("command6"));
        assertTrue(history.previous().equals("command5"));
        assertTrue(history.previous().equals("command4"));
        assertTrue(history.previous().equals("command3"));
        assertTrue(history.previous().equals("command2"));
        assertTrue(history.previous().equals("command1"));
        assertTrue(history.previous() == null);
        assertTrue(history.previous() == null);
        assertTrue(history.previous() == null);
        assertTrue(history.next().equals("command2"));
        assertTrue(history.next().equals("command3"));
        assertTrue(history.next().equals("command4"));
        assertTrue(history.next().equals("command5"));
        assertTrue(history.next().equals("command6"));
        assertTrue(history.next().equals("command7"));
        assertTrue(history.next() == null);
        assertTrue(history.next() == null);
        assertTrue(history.next() == null);
        assertTrue(history.previous().equals("command7"));
        assertTrue(history.previous().equals("command6"));
        assertTrue(history.previous().equals("command5"));
        assertTrue(history.previous().equals("command4"));
        assertTrue(history.previous().equals("command3"));
        assertTrue(history.previous().equals("command2"));
        assertTrue(history.previous().equals("command1"));

    }
}