package seedu.address.model.booking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.DuplicateDataException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.label.UniqueLabelList;
import seedu.address.model.label.UniqueLabelList.DuplicateLabelException;

//@@author A0162877N
public class UniqueBookingList implements Iterable<Booking> {
    private final ObservableList<Booking> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty BookingList.
     */
    public UniqueBookingList() {}

    /**
     * Creates a UniqueBookingList using given String bookings. Enforces no nulls or
     * duplicates.
     * @throws CommandException
     */
    public UniqueBookingList(String... bookings)
            throws DuplicateLabelException, IllegalValueException, CommandException {
        final List<Booking> bookingList = new ArrayList<Booking>();
        for (String label : bookings) {
            bookingList.add(new Booking(label));
        }
        setBookings(bookingList);
    }

    /**
     * Creates a UniqueBookingList using given bookings. Enforces no nulls or
     * duplicates.
     */
    public UniqueBookingList(Booking... booking) throws DuplicateBookingException {
        assert !CollectionUtil.isAnyNull((Object[]) booking);
        final List<Booking> initialBookings = Arrays.asList(booking);
        if (!CollectionUtil.elementsAreUnique(initialBookings)) {
            throw new DuplicateBookingException();
        }
        internalList.addAll(initialBookings);
    }

    /**
     * Creates a UniqueBookingList using given bookings. Enforces no null or
     * duplicate elements.
     * @throws DuplicateBookingException
     */
    public UniqueBookingList(Collection<Booking> bookings) throws DuplicateBookingException {
        this();
        setBookings(bookings);
    }

    /**
     * Creates a UniqueBookingList using given bookings. Enforces no nulls.
     */
    public UniqueBookingList(Set<Booking> labels) {
        assert !CollectionUtil.isAnyNull(labels);
        internalList.addAll(labels);
    }

    /**
     * Creates a copy of the given list. Insulates from changes in source.
     */
    public UniqueBookingList(UniqueBookingList source) {
        internalList.addAll(source.internalList); // insulate internal list from
                                                  // changes in argument
    }

    /**
     * Returns all bookings in this list as a Set. This set is mutable and
     * change-insulated against the internal list.
     */
    public Set<Booking> toSet() {
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the Tags in this list with those in the argument booking list.
     */
    public void setBookings(UniqueBookingList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setBookings(Collection<Booking> bookings) throws DuplicateBookingException {
        assert !CollectionUtil.isAnyNull(bookings);
        if (!CollectionUtil.elementsAreUnique(bookings)) {
            throw new DuplicateBookingException();
        }
        internalList.setAll(bookings);
    }

    /**
     * Removes all bookings from the list. Warning, this cannot be undone!
     */
    public void removeAll() {
        internalList.clear();
    }

    @Override
    public UniqueBookingList clone() {
        UniqueBookingList bookingList = new UniqueBookingList();
        try {

            for (Booking booking : internalList) {
                bookingList.add(new Booking(booking.getBookingStartDate(), booking.getBookingEndDate()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    /**
     * Ensures every booking in the argument list exists in this object.
     */
    public void mergeFrom(UniqueBookingList from) {
        final Set<Booking> alreadyInside = this.toSet();
        from.internalList.stream().filter(label -> !alreadyInside.contains(label)).forEach(internalList::add);
    }

    /**
     * Returns true if the list contains an equivalent Label as the given
     * argument.
     */
    public boolean contains(Booking toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Returns true if the booking list contains an equivalent booking as the given
     * argument.
     */
    public boolean containsStringBooking(Booking bookingToCheck) {
        assert bookingToCheck != null;
        for (Booking booking : internalList) {
            if (booking.getBookingStartDate().equals(bookingToCheck.getBookingStartDate())
                    && booking.getBookingEndDate().equals(bookingToCheck.getBookingEndDate())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a booking to the list.
     *
     * @throws DuplicateLabelException
     *             if the booking to add is a duplicate of an existing booking in
     *             the list.
     */
    public void add(Booking toAdd) throws DuplicateBookingException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateBookingException();
        }
        internalList.add(toAdd);
    }

    @Override
    public Iterator<Booking> iterator() {
        return internalList.iterator();
    }

    public UnmodifiableObservableList<Booking> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueLabelList // instanceof handles nulls
                        && this.internalList.equals(((UniqueBookingList) other).internalList));
    }

    public boolean equalsOrderInsensitive(UniqueBookingList other) {
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates'
     * property of the list.
     */
    public static class DuplicateBookingException extends DuplicateDataException {
        protected DuplicateBookingException() {
            super("Operation would result in duplicate bookings");
        }
    }
}