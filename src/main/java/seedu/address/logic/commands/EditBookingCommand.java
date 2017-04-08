package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.Collections;
import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.undo.UndoManager;
import seedu.address.model.booking.Booking;
import seedu.address.model.booking.UniqueBookingList;
import seedu.address.model.booking.UniqueBookingList.DuplicateBookingException;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;

//@@author A0162877N
/**
 * Add, change or remove booking time slot in the Task Manager
 */
public class EditBookingCommand extends Command {

    public static final String COMMAND_WORD = "editbooking";
    public static final String MESSAGE_DUPLICATE_BOOKING = "This booking already exists in the task manager";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the booking of the task identified "
            + "by the index number used in the last task listing. "
            + "Parameters: INDEX (index of the task in the current task list and must be a positive integer)\n"
            + "Parameters: [add DATE, MORE DATES] | [remove BOOKING_INDEX] | [change BOOKING_INDEX DATE]\n"
            + "Example: " + COMMAND_WORD + " 1 add tuesday 2pm to 5pm\n" + "Example: " + COMMAND_WORD + " 1 remove 1\n"
            + "Example: " + COMMAND_WORD + " 1 change 1 tuesday 1pm to 4pm\n";
    public static final String MESSAGE_NO_SUCH_BOOKING = "Index provided is invalid.\n" + MESSAGE_USAGE;
    public static final String MESSAGE_TASK_NO_BOOKING = "This task does not have bookings to update.";
    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
    public static final int BOOKING_INDEX = 0;
    public static final int VALID_INDEX = 0;

    private final int filteredTaskListIndex;
    private final int bookingSlotIndex;
    private final Set<Booking> bookingSet;

    public EditBookingCommand(int filteredTaskListIndex, int bookingSlotIndex) {
        assert filteredTaskListIndex > VALID_INDEX;
        assert bookingSlotIndex > VALID_INDEX;
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
        this.bookingSlotIndex = bookingSlotIndex - 1;
        this.bookingSet = new HashSet<>();
    }

    public EditBookingCommand(int filteredTaskListIndex, String... dates) throws CommandException {
        assert filteredTaskListIndex > VALID_INDEX;
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
        bookingSlotIndex = -1;
        bookingSet = new HashSet<>();
        for (String bookingSlot : dates) {
            bookingSet.add(new Booking(bookingSlot));
        }
    }

    public EditBookingCommand(int filteredTaskListIndex, int bookingSlotIndex, String date)
            throws CommandException {
        assert filteredTaskListIndex > VALID_INDEX;
        assert bookingSlotIndex > VALID_INDEX;
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
        this.bookingSlotIndex = bookingSlotIndex - 1;
        bookingSet = new HashSet<>();
        bookingSet.add(new Booking(date));
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
        try {
            boolean bookingSuccess = false;
            if (bookingSet.isEmpty() && bookingSlotIndex >= VALID_INDEX) {
                bookingSuccess = removeBookingsInTasks(lastShownList);
            } else if (!bookingSet.isEmpty() && bookingSlotIndex >= VALID_INDEX) {
                bookingSuccess = changeBookingsInTasks(lastShownList);
            } else if (!bookingSet.isEmpty()) {
                bookingSuccess = addBookingsInTasks(lastShownList);
            } else {
                throw new CommandException(MESSAGE_TASK_NO_BOOKING);
            }

            if (!bookingSuccess) {
                throw new CommandException(MESSAGE_NO_SUCH_BOOKING);
            }

            return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, filteredTaskListIndex + 1));
        } catch (DuplicateBookingException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Removes a specific booking time slot in the booking
     *
     * @param lastShownList
     * @return true if the specified booking exists
     * @throws DuplicateBookingException
     */
    private boolean removeBookingsInTasks(List<ReadOnlyTask> lastShownList)
            throws CommandException, DuplicateBookingException {
        boolean bookingExist = false;
        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASKS_DISPLAYED_INDEX);
        } else {
            Task task = new Task(lastShownList.get(filteredTaskListIndex));
            UniqueBookingList bookings = task.getBookings();
            if (!bookings.isEmpty()) {
                List<Booking> bookingList = bookings.toList();
                if (bookingSlotIndex >= bookingList.size()) {
                    throw new CommandException(MESSAGE_NO_SUCH_BOOKING);
                }
                bookingList.remove(bookingSlotIndex);
                Collections.sort(bookingList);
                task.setBookings(new UniqueBookingList(bookingList));
                bookingExist = true;
                try {
                    saveCurrentState();
                    model.updateTask(filteredTaskListIndex, task);
                } catch (DuplicateTaskException dpe) {
                    throw new CommandException(MESSAGE_DUPLICATE_BOOKING);
                }
            } else {
                throw new CommandException(MESSAGE_NO_SUCH_BOOKING);
            }
        }
        return bookingExist;
    }

    /**
     * Adds time slots in booking
     *
     * @param lastShownList
     * @return true if the specified booking exists
     * @throws DuplicateBookingException
     */
    private boolean addBookingsInTasks(List<ReadOnlyTask> lastShownList)
            throws CommandException, DuplicateBookingException {
        boolean bookingSuccess = false;
        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASKS_DISPLAYED_INDEX);
        } else {
            Task task = new Task(lastShownList.get(filteredTaskListIndex));
            UniqueBookingList bookings = task.getBookings();
            if (!bookings.isEmpty()) {
                Set<Booking> taskBookingSet = bookings.toSet();
                List<Booking> bookingList = new ArrayList<Booking>(bookingSet);
                for (Booking booking : bookingList) {
                    taskBookingSet.add(booking);
                }
                task.setBookings(new UniqueBookingList(taskBookingSet));
                bookingSuccess = true;
                try {
                    saveCurrentState();
                    model.updateTask(filteredTaskListIndex, task);
                } catch (DuplicateTaskException dpe) {
                    throw new CommandException(MESSAGE_DUPLICATE_BOOKING);
                }
            } else {
                throw new CommandException(MESSAGE_NO_SUCH_BOOKING);
            }
        }
        return bookingSuccess;
    }

    /**
     * Replaces a specific booking time slot in bookings
     *
     * @param allTaskList
     * @return true if the specified booking exists
     * @throws DuplicateBookingException
     */
    private boolean changeBookingsInTasks(List<ReadOnlyTask> lastShownList)
            throws CommandException, DuplicateBookingException {
        boolean bookingExist = false;
        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASKS_DISPLAYED_INDEX);
        } else {
            Task task = new Task(lastShownList.get(filteredTaskListIndex));
            UniqueBookingList bookings = task.getBookings();
            if (!bookings.isEmpty()) {
                List<Booking> bookingList = bookings.toList();
                if (bookingSlotIndex >= bookingList.size() || bookingSlotIndex < VALID_INDEX) {
                    throw new CommandException(MESSAGE_NO_SUCH_BOOKING);
                }
                if (!bookings.contains(new ArrayList<Booking>(bookingSet).get(BOOKING_INDEX))) {
                    bookingList.remove(bookingSlotIndex);
                    bookingList.add(new ArrayList<Booking>(bookingSet).get(BOOKING_INDEX));
                }
                Collections.sort(bookingList);
                task.setBookings(new UniqueBookingList(bookingList));
                bookingExist = true;
                try {
                    saveCurrentState();
                    model.updateTask(filteredTaskListIndex, task);
                } catch (DuplicateTaskException dpe) {
                    throw new CommandException(MESSAGE_DUPLICATE_BOOKING);
                }
            } else {
                throw new CommandException(MESSAGE_NO_SUCH_BOOKING);
            }
        }
        return bookingExist;
    }

    /**
     * Save the data in task manager if command is mutating the data
     */
    public void saveCurrentState() {
        if (isMutating()) {
            try {
                UndoManager.getInstance().addStorageHistory(model.getTaskManager().getImmutableTaskList(),
                        model.getTaskManager().getImmutableLabelList());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isMutating() {
        return true;
    }

}
