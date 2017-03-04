package seedu.address.model.person;

import seedu.address.model.label.UniqueLabelList;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyPerson {

    Name getName();
    Phone getPhone();
    Email getEmail();
    Address getAddress();

    /**
     * The returned LabelList is a deep copy of the internal LabelList,
     * changes on the returned list will not affect the person's internal labels.
     */
    UniqueLabelList getLabels();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyPerson other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getPhone().equals(this.getPhone())
                && other.getEmail().equals(this.getEmail())
                && other.getAddress().equals(this.getAddress()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
        .append(" Phone: ")
        .append(getPhone())
        .append(" Email: ")
        .append(getEmail())
        .append(" Address: ")
        .append(getAddress())
        .append(" Label: ");
        getLabels().forEach(builder::append);
        return builder.toString();
    }

}
