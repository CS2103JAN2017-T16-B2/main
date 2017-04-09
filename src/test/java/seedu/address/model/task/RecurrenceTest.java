package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
//@@author A0105287E
public class RecurrenceTest {

    @Test
    public void isValidRecurrence() {
        // invalid
        assertFalse(Recurrence.isValidRecurrence("")); // empty string
        assertFalse(Recurrence.isValidRecurrence(" ")); // spaces only
        assertFalse(Recurrence.isValidRecurrence("^")); // only non-alphanumeric characters
        assertFalse(Recurrence.isValidRecurrence("peter peter*")); // contains only alphabets characters
        assertFalse(Recurrence.isValidRecurrence("year 1"));

        // valid
        assertTrue(Recurrence.isValidRecurrence("1 day")); //contains one number followed by one word
        assertTrue(Recurrence.isValidRecurrence("3 years")); //contains one number followed by one word
    }

}
