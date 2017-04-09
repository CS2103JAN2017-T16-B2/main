package seedu.address.model.task;

import java.util.Optional;

import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.booking.UniqueBookingList;
import seedu.address.model.label.UniqueLabelList;

/**
 * A read-only immutable interface for a Task in the task manager.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask extends Comparable<ReadOnlyTask> {

    Title getTitle();
    Optional<Deadline> getDeadline();
    Optional<Deadline> getStartTime();
    Boolean isCompleted();
    Boolean isRecurring();
    Optional<Recurrence> getRecurrence();

    /**
     * The returned LabelList is a deep copy of the internal LabelList,
     * changes on the returned list will not affect the task's internal labels.
     */
    UniqueLabelList getLabels();

    /**
     * The returned BookingList is a deep copy of the internal BookingList,
     * changes on the returned list will not affect the task's internal bookings.
     */
    UniqueBookingList getBookings();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(this.getTitle()) // state checks here onwards
                && other.getDeadline().equals(this.getDeadline())
                && other.getStartTime().equals(this.getStartTime())
                && other.getLabels().equals(this.getLabels()))
                && other.isCompleted().equals(this.isCompleted())
                && other.isRecurring().equals(this.isRecurring())
                && other.getRecurrence().equals(this.getRecurrence());
    }

    /**
     * Formats the task as text, showing all details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle());
        if (getStartTime().isPresent()) {
            builder.append(" Start: ")
                .append(getStartTime().get().toString());
        }
        if (getDeadline().isPresent()) {
            builder.append(" Deadline: ")
                .append(getDeadline().get().toString());
        }
        if (isCompleted()) {
            builder.append(" Status: Completed");
        } else {
            builder.append(" Status: Incomplete");
        }
        builder.append(" Label: ");
        getLabels().forEach(builder::append);
        builder.append(" Booking: ");
        getBookings().forEach(booking -> builder.append("[" + booking + "]\n"));
        return builder.toString();
    }

    //@@author A0105287E
    default int compareTo(ReadOnlyTask other) {
        int value = 0;
        try {
            value = compareCompletionStatus(other);
        } catch (IllegalValueException | IllegalDateTimeValueException e) {
            e.printStackTrace();
        }
        return value;
    }

    default int compareCompletionStatus(ReadOnlyTask other) throws IllegalValueException,
                        IllegalDateTimeValueException {
        if (this.isCompleted() && !other.isCompleted()) {
            return 1;
        } else if (!this.isCompleted() && other.isCompleted()) {
            return -1;
        } else {
            return compareDates(other);
        }
    }

    default int compareDates(ReadOnlyTask other) throws IllegalValueException, IllegalDateTimeValueException {
        Deadline dateToCompareForOther = getEarliestDateForTask(other);
        Deadline dateToCompareForThis = getEarliestDateForTask(this);
        if (dateToCompareForThis != null && dateToCompareForOther != null) {
            return dateToCompareForThis.getDateTime().compareTo(dateToCompareForOther.getDateTime());
        } else if (dateToCompareForThis == null && dateToCompareForOther == null) {
            return 0;
        } else if (dateToCompareForThis == null) {
            return 1;
        } else {
            return -1;
        }
    }

    default Deadline getEarliestDateForTask(ReadOnlyTask task)
            throws IllegalValueException, IllegalDateTimeValueException {
        Deadline date;
        if (task.getStartTime().isPresent()) {
            date = task.getStartTime().get();
        } else if (task.getDeadline().isPresent()) {
            date = task.getDeadline().get();
        } else if (!task.getBookings().isEmpty()) {
            date = new Deadline(task.getBookings().getEarliestStartTime().toString());
        } else {
            date = null;
        }
        return date;
    }
    //@@author

    default String getAsSearchText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle());
        if (getStartTime().isPresent()) {
            builder.append(" " + getStartTime().get().toString() + " ");
        }
        if (getDeadline().isPresent()) {
            builder.append(" " + getDeadline().get().toString() + " ");
        }
        if (isCompleted()) {
            builder.append(" Completed ");
        } else {
            builder.append(" Incomplete ");
        }
        getLabels().forEach(label -> builder.append(" " + label + " "));
        getBookings().forEach(booking -> builder.append(" " + booking + " "));
        System.out.println(builder.toString());
        return builder.toString();
    }



}
