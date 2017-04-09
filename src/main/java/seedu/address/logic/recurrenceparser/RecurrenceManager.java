package seedu.address.logic.recurrenceparser;

import java.util.Calendar;
import java.util.Date;

import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Recurrence;

//@@author A0105287E
/**
 *
 * Parses string into interval and returns the next date for a task
 *
 */
public class RecurrenceManager implements RecurrenceParser {


    /**
     * Creates and returns a {@code Deadline} a new instance of the updated deadline passed in
     * @throws IllegalDateTimeValueException when the DateParser is unable to parse given date
     * @throws IllegalValueException when the deadline string is invalid
     */
    @Override
    public Deadline getRecurringDate(Deadline date, Recurrence recurrence)
            throws IllegalValueException, IllegalDateTimeValueException {
        Date oldDate = date.getDateTime();
        return new Deadline (getNextDate(oldDate, recurrence).toString());
    }

    /**
     * Returns the a date one one recurrence interval after the given date
     */
    public Date getNextDate(Date oldDate, Recurrence recurrence) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(oldDate);
        cal.add(recurrence.interval, recurrence.value);
        return cal.getTime();
    }

    /**
     * Returns java.util.Calendar constant integer value for the interval string entered
     */
    @Override
    public int getInterval(String input) {
        int interval;
        switch (input) {
        case "year": case "years":
            interval = Calendar.YEAR;
            break;
        case "month": case "months":
            interval = Calendar.MONTH;
            break;
        case "day": case "days":
            interval = Calendar.DATE;
            break;
        case "hour": case "hours":
            interval = Calendar.HOUR;
            break;
        case "minutes": case "minute":
            interval = Calendar.MINUTE;
            break;
        case "seconds": case "second":
            interval = Calendar.SECOND;
            break;
        default:
            throw new IllegalArgumentException("Invalid interval");
        }
        return interval;
    }


}
