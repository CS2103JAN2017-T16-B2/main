package seedu.address.model.booking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.booking.UniqueBookingList.DuplicateBookingException;
import seedu.address.model.label.UniqueLabelList.DuplicateLabelException;

//@@author A0162877N
/**
 * Unique booking list test case
 */
public class UniqueBookingListTest {

    public static final String ERROR_DUPLICATE_BOOKING = "Operation would result in duplicate bookings";

    private UniqueBookingList normalList;
    private UniqueBookingList unorderedList;
    private UniqueBookingList disruptiveList;
    private UniqueBookingList emptyList1;
    private UniqueBookingList emptyList2;

    @Before
    public void setUp() throws DuplicateBookingException, IllegalValueException, CommandException {
        disruptiveList = new UniqueBookingList("today 12pm to 1pm");
        normalList = new UniqueBookingList(new Booking("today 1pm to 2pm"), new Booking("today 2pm to 3pm"));
        unorderedList = new UniqueBookingList(new Booking("today 3pm to 4pm"), new Booking("today 1pm to 2pm"));
        emptyList1 = new UniqueBookingList();
        emptyList2 = new UniqueBookingList();

        disruptiveList.add(new Booking("tmr 6pm to 7pm"));
    }

    @Test
    public void uniqueBookingList_TestInitializationBookingNull_ThrowAssertionException() {
        try {
            new UniqueBookingList(new Booking("wed 12pm to 1pm"), null, null);
        } catch (AssertionError e) {
            assertTrue(null == e.getMessage());
        } catch (Exception e) {
            fail("Unexpected Error!");
        }
    }

    @Test
    public void uniqueBookingList_TestInitializationDuplicateString_ThrowDuplicateLabelException() {
        try {
            Booking booking = new Booking("wed 12pm to 1pm");
            new UniqueBookingList(booking, booking);
        } catch (DuplicateBookingException e) {
            assertTrue(ERROR_DUPLICATE_BOOKING.equals(e.getMessage()));
        } catch (Exception e) {
            fail("Unexpected Error!");
        }
    }

    @Test
    public void uniqueBookingList_TestInitializationDuplicateList_ThrowDuplicateBookingException() {
        try {
            List<Booking> bookings = new LinkedList<Booking>();
            bookings.add(new Booking("wed 12pm to 1pm"));
            bookings.add(new Booking("wed 2pm to 3pm"));
            bookings.add(new Booking("wed 12pm to 1pm"));
            new UniqueBookingList(bookings);
        } catch (DuplicateBookingException e) {
            assertTrue(ERROR_DUPLICATE_BOOKING.equals(e.getMessage()));
        } catch (Exception e) {
            fail("Unexpected Error!");
        }

    }

    @Test
    public void uniqueBookingList_TestInitializationList()
            throws DuplicateBookingException, IllegalValueException, CommandException {
        UniqueBookingList list1 = new UniqueBookingList("today 3pm to 4pm", "today 5pm to 6pm");
        List<Booking> bookings = new LinkedList<Booking>();
        bookings.add(new Booking("today 3pm to 4pm"));
        bookings.add(new Booking("today 5pm to 6pm"));
        UniqueBookingList list2 = new UniqueBookingList(bookings);
        assertTrue(list1.equals(list2));

        list2 = new UniqueBookingList(list1.toSet());
        assertTrue(list1.equals(list2));

        list2 = new UniqueBookingList(list1);
        assertTrue(list1.equals(list2));

        list2 = new UniqueBookingList();
        list2.mergeFrom(list1);
        assertTrue(list1.equals(list2));

        list2.setBookings(list1);
        assertTrue(list1.equals(list2));
    }

    @Test
    public void uniqueBookingList_TestEmptyLists() {
        //Test equals
        assertTrue(emptyList1.equals(emptyList1));

        //Test both empty list
        assertTrue(emptyList1.equals(emptyList2));

        //Test null
        assertFalse(emptyList1 == null);
    }

    @Test
    public void uniqueBookingList_TestEqualOrderCaseSensitive_ReturnTrue()
            throws DuplicateLabelException, IllegalValueException {
        //Test same list
        assertTrue(normalList.equalsOrderInsensitive(normalList));

        //Test different order
        assertFalse(normalList.equalsOrderInsensitive(unorderedList));

        //Test null
        assertFalse(normalList.equalsOrderInsensitive(disruptiveList));
    }

    @Test
    public void uniqueBookingList_TestHashCode() {
        assertTrue(emptyList1.hashCode() == emptyList1.hashCode());
        assertTrue(emptyList1.hashCode() == emptyList2.hashCode());
        assertTrue(normalList.hashCode() != disruptiveList.hashCode());
    }

    @Test
    public void uniqueBookingList_TestDuplicateBookings() {
        try {
            UniqueBookingList duplicateTagList = new UniqueBookingList();
            duplicateTagList.add(new Booking("today 1am to 2am"));
            duplicateTagList.add(new Booking("today 1am to 2am"));
        } catch (DuplicateBookingException e) {
            assertTrue(ERROR_DUPLICATE_BOOKING.equals(e.getMessage()));
        } catch (Exception e) {
            fail("Unexpected Error!");
        }

    }

    @Test
    public void uniqueBookingList_ContainLabelString() throws CommandException {
        assertTrue(normalList.containsStringBooking(new Booking("today 1pm to 2pm")));
        assertFalse(normalList.containsStringBooking(new Booking("thur 1am to 2am")));
    }

    @Test
    public void uniqueBookingList_TestIteratorAndUnmodifiableString() {
        Iterator<Booking> iterator = normalList.iterator();
        UnmodifiableObservableList<Booking> list = normalList.asObservableList();
        for (int i = 0; i < list.size() && iterator.hasNext(); i++) {
            assertTrue(iterator.next().equals(list.get(i)));
        }
    }
}
