package seedu.address.model.label;

import seedu.address.commons.exceptions.IllegalValueException;

//@@author A0140042A
/**
 * Represents a Label in DoOrDie Task Manager Application
 * Guarantees: immutable; name is valid as declared in {@link #isValidLabelName(String)}
 */
public class Label implements Comparable<Label> {

    public static final String MESSAGE_LABEL_CONSTRAINTS =
            "Label name should not be empty";

    public final String labelName;

    /**
     * Validates given label name.
     *
     * @throws IllegalValueException if the given label name string is invalid.
     */
    public Label(String name) throws IllegalValueException {
        assert name != null;
        String trimmedName = name.trim();
        if (!isValidLabelName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_LABEL_CONSTRAINTS);
        }
        this.labelName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid label name.
     */
    public static boolean isValidLabelName(String test) {
        return test != null && !test.trim().isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Label // instanceof handles nulls
                        && this.labelName.equals(((Label) other).labelName)); // state check
    }

    @Override
    public int hashCode() {
        return labelName.hashCode();
    }

    /**
     * Returns labelName in String
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + labelName + ']';
    }

    @Override
    public int compareTo(Label o) {
        return this.labelName.compareTo(o.labelName);
    }

}
