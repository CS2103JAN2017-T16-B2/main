package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;

public class DeadlineTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isValidDate_ReturnTrue() throws IllegalValueException, IllegalDateTimeValueException {
        // invalid deadlines
        exception.expect(AssertionError.class);
        new Deadline(null);

        // test equals
        Deadline deadline1 = new Deadline("Tomorrow 1000");
        Deadline deadline2 = new Deadline("Tomorrow 1000");
        Deadline deadline3 = new Deadline("Today"); // a different deadline

        assertTrue(deadline1.equals(deadline2));
        assertTrue(deadline1.equals(deadline1));
        assertFalse(deadline1 == null);
        assertFalse(deadline1.equals(deadline3));


        //hashCode
        assertTrue(deadline1.hashCode() == deadline2.hashCode());

        // valid deadlines
        assertTrue(Deadline.isValidDeadline("christmas"));
        assertTrue(Deadline.isValidDeadline("next wed"));
        assertTrue(Deadline.isValidDeadline("11-12-2106")); // date
        assertTrue(Deadline.isValidDeadline("Tomorrow"));
        assertTrue(Deadline.isValidDeadline("9pm")); // two characters only
        assertTrue(Deadline.isValidDeadline("")); // empty string
        assertTrue(Deadline.isValidDeadline(" ")); // spaces only
    }

    @Test
    public void deadline_ParseSameDate_ReturnTrue() throws IllegalValueException, IllegalDateTimeValueException {
        // test parse same date
        Deadline deadline = new Deadline("01-01-2017 2359");
        Deadline deadline1 = new Deadline("2359 01-01-2017");
        Deadline deadline2 = new Deadline("2359 tmr");
        Deadline deadline3 = new Deadline("tmr 2359");
        assertTrue(deadline.equals(deadline1));
        assertTrue(deadline2.equals(deadline3));
    }
    @Test
    public void deadline_ParseDifferentDate_ReturnFalse() throws IllegalValueException, IllegalDateTimeValueException {
        // test parse different date
        Deadline deadline4 = new Deadline("new year");
        Deadline deadline5 = new Deadline("christmas");
        Deadline deadline6 = new Deadline("now");
        Deadline deadline7 = new Deadline("tmr");
        assertFalse(deadline4.equals(deadline5));
        assertFalse(deadline6.equals(deadline7));
    }

    @Test
    public void deadline_ParseInvalidDateString_ReturnFalse()
            throws IllegalValueException, IllegalDateTimeValueException {
        // invalid deadlines
        exception.expect(IllegalDateTimeValueException.class);
        new Deadline("test");
        fail("Should not reach here");
    }

    @Test
    public void deadline_ParseInvalidDateSymbols_ReturnFalse()
            throws IllegalValueException, IllegalDateTimeValueException {
        // invalid deadlines
        exception.expect(IllegalDateTimeValueException.class);
        new Deadline("--");
        fail("Should not reach here");
    }

    @Test
    public void deadline_ParseInvalidDateSymbol_ReturnFalse()
            throws IllegalValueException, IllegalDateTimeValueException {
        // invalid deadlines
        exception.expect(IllegalDateTimeValueException.class);
        new Deadline("-");
        fail("Should not reach here");
    }

    @Test
    public void deadline_ParseInvalidDateCharacter_ReturnFalse()
            throws IllegalValueException, IllegalDateTimeValueException {
        // invalid deadlines
        exception.expect(IllegalDateTimeValueException.class);
        new Deadline("a");
        fail("Should not reach here");
    }

    @Test
    public void deadline_ParseInvalidDateLongString_ReturnFalse()
            throws IllegalValueException, IllegalDateTimeValueException {
        // invalid deadlines
        exception.expect(IllegalDateTimeValueException.class);
        new Deadline(" SAC AKBC");
        fail("Should not reach here");
    }

    @Test
    public void deadline_CheckIsParsable_ReturnTrue() throws IllegalValueException, IllegalDateTimeValueException {
        assertTrue(Deadline.isParsableDate("Today"));
        assertTrue(Deadline.isParsableDate("tmr"));
        assertTrue(Deadline.isParsableDate("next wed"));
        assertTrue(Deadline.isParsableDate("Today 2359"));
    }

    @Test
    public void deadline_CheckIsParsable_ReturnFalse() throws IllegalValueException, IllegalDateTimeValueException {
        assertFalse(Deadline.isParsableDate("invalidDate"));
        assertFalse(Deadline.isParsableDate("tooooo"));
        assertFalse(Deadline.isParsableDate("nulll"));
        assertFalse(Deadline.isParsableDate("wrong input"));
    }
}
