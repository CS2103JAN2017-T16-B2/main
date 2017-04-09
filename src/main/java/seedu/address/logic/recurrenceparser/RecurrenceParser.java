package seedu.address.logic.recurrenceparser;

import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Recurrence;

//@@author A0105287E
/**
 * The API of the RecurrenceParser component.
 */
public interface RecurrenceParser {

    /**
     * Creates and returns a {@code Deadline} a new instance of the updated deadline passed in
     * @throws IllegalDateTimeValueException when the DateParser is unable to parse given date
     * @throws IllegalValueException when the deadline string is invalid
     */
    Deadline getRecurringDate(Deadline oldDate, Recurrence recurrence)
            throws IllegalValueException, IllegalDateTimeValueException;

    /**
     * Returns java.util.Calendar constant integer value for the interval string entered
     */
    int getInterval(String input);
}
