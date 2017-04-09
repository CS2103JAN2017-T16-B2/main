package seedu.address.logic.commands;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.recurrenceparser.RecurrenceManager;
import seedu.address.logic.recurrenceparser.RecurrenceParser;
import seedu.address.logic.undo.UndoManager;
import seedu.address.model.booking.UniqueBookingList;
import seedu.address.model.label.UniqueLabelList;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.Recurrence;
import seedu.address.model.task.Task;
import seedu.address.model.task.Title;
import seedu.address.model.task.UniqueTaskList;

//@@author A0105287E
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the task as completed or incomplete, task is "
            + "identified by the index number used in the last task listing.\n "
            + "Parameters: INDEX [completed|incomplete]\n"
            + "Example: mark 1 completed\n";

    public static final String MESSAGE_MARK_TASK_SUCCESS = "Marked Task: %1$s";
    public static final String MESSAGE_NOT_MARKED = "Status must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task manager.";
    public static final String MESSAGE_TYPE_BOOKING = "Booking type of tasks cannot be marked as completed.";
    public static final String MESSAGE_RECURRING_INCOMPLETE_DISABLE = "Status of completed recurring task "
            + "cannot be changed.";
    private static final RecurrenceParser recurrenceParser = new RecurrenceManager();

    private final int filteredTaskListIndex;
    private final Boolean isCompleted;

    /**
     * @param filteredTaskListIndex the index of the task in the filtered task list to edit
     *
     */
    public MarkCommand(int filteredTaskListIndex, Boolean isCompleted) {
        assert filteredTaskListIndex > 0;
        assert isCompleted != null;
        this.isCompleted = isCompleted;

        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
    }


    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASKS_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToEdit = lastShownList.get(filteredTaskListIndex);

        if (!taskToEdit.getBookings().isEmpty()) {
            throw new CommandException(MESSAGE_TYPE_BOOKING);
        }

        if (taskToEdit.isCompleted() && taskToEdit.isRecurring()) {
            throw new CommandException(MESSAGE_RECURRING_INCOMPLETE_DISABLE);
        }

        try {
            Task editedTask = createEditedTask(taskToEdit, isCompleted);
            saveCurrentState();
            model.updateTask(filteredTaskListIndex, editedTask);

            if (taskToEdit.isRecurring()) {
                Task newTask = createRecurringTask(taskToEdit);
                model.addTask(newTask);
            }
        } catch (UniqueTaskList.DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
        return new CommandResult(String.format(MESSAGE_MARK_TASK_SUCCESS, taskToEdit));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit, Boolean isCompleted)
                             throws IllegalValueException, IllegalDateTimeValueException, CommandException {
        assert taskToEdit != null;
        Optional<Deadline> updatedStartTime;
        Optional<Deadline> updatedDeadline;


        Title updatedTitle = new Title(taskToEdit.getTitle().toString());
        updatedStartTime = taskToEdit.getStartTime();
        updatedDeadline = taskToEdit.getDeadline();
        UniqueLabelList updatedLabels = taskToEdit.getLabels().clone();
        UniqueBookingList bookingList = taskToEdit.getBookings().clone();
        Boolean isRecurring = taskToEdit.isRecurring();
        Optional<Recurrence> updatedRecurrence = taskToEdit.getRecurrence();

        return new Task(updatedTitle, updatedStartTime, updatedDeadline, isCompleted,
                updatedLabels, bookingList, isRecurring, updatedRecurrence);
    }

    /**
     * Creates and returns a {@code Task} a new instance of the recurring task
     */
    private static Task createRecurringTask(ReadOnlyTask task) throws IllegalValueException,
                            IllegalDateTimeValueException {
        Optional<Deadline> updatedStartTime;
        Optional<Deadline> updatedDeadline;
        if (task.getStartTime().isPresent()) {
            updatedStartTime = Optional.ofNullable(recurrenceParser.getRecurringDate(task.getStartTime().get(),
                task.getRecurrence().get()));
            updatedDeadline = Optional.ofNullable(recurrenceParser.getRecurringDate(task.getDeadline().get(),
                task.getRecurrence().get()));
        } else {
            updatedStartTime = task.getStartTime();
            updatedDeadline = Optional.ofNullable(recurrenceParser.getRecurringDate(task.getDeadline().get(),
                    task.getRecurrence().get()));
        }
        Boolean isCompleted = AddCommand.DEFAULT_TASK_STATE;

        return new Task(task.getTitle(), updatedStartTime, updatedDeadline, isCompleted,
                task.getLabels(), task.getBookings(), task.isRecurring(), task.getRecurrence());
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
