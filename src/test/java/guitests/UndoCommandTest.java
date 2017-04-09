package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.undo.UndoManager;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;

//@@author A0162877N
/**
 * Undo command jUnit GUI test
 */
public class UndoCommandTest extends TaskManagerGuiTest {

    @Test
    public void undo_IsMutating_ReturnTrue() {
        UndoCommand undo = new UndoCommand();
        assertTrue(undo.isMutating());
    }

    @Test
    public void undo_NothingToUndo_ReturnTrue() {
        UndoManager.getInstance().clear();
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand("undo");
        //No change should occur and show error message
        assertResultMessage(UndoCommand.MESSAGE_UNSUCCESSFUL_UNDO);
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    @Test
    public void undo_InvalidCommand() {
        commandBox.runCommand("un");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        commandBox.runCommand("undosomething");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
        commandBox.runCommand("und");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void undo_NoMutatingCommand_ReturnTrue() {
        UndoManager.getInstance().clear();
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand("find");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_UNSUCCESSFUL_UNDO);
        assertTrue(taskListPanel.isListMatching(currentList));

        commandBox.runCommand("list");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_UNSUCCESSFUL_UNDO);
        assertTrue(taskListPanel.isListMatching(currentList));

        commandBox.runCommand("help");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_UNSUCCESSFUL_UNDO);
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    @Test
    public void undo_EditLabelValid_ReturnTrue() throws IllegalValueException {
        UndoManager.getInstance().clear();
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand("edit #friends #allies");
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");

        assertTrue(td.getTypicalTasks().length == taskListPanel.getNumberOfTasks());
        assertTrue(taskListPanel.isListMatching(currentList));
    }

    @Test
    public void undo_AddTask_ReturnTrue() {
        UndoManager.getInstance().clear();
        //add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.task8;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(currentList));

        //undo command
        commandBox.runCommand("undo");
        taskToAdd = td.task8;
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));

        //undo command
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));

        //empty list and undo
        commandBox.runCommand("clear");
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }

    @Test
    public void undo_BookTask_ReturnTrue()
            throws IllegalValueException, CommandException, IllegalDateTimeValueException {
        UndoManager.getInstance().clear();
        //add one booking
        TestTask taskToAdd = (new TaskBuilder())
                .withTitle("Complete booking")
                .withDeadline("")
                .withLabels("friends")
                .withBookings("10-10-2017 2pm to 5pm",
                        "11-10-2017 2pm to 5pm",
                        "12-10-2017 2pm to 5pm")
                .build();
        commandBox.runCommand("book Complete booking #friends on 10-10-2017 2pm to 5pm,"
                + " 11-10-2017 2pm to 5pm, 12-10-2017 2pm to 5pm");
        TaskCardHandle addedCard = taskListPanel.navigateToTask("Complete booking");
        assertTrue(addedCard.getTitle().equals("Complete booking"));
        assertTrue(addedCard.isSameTask(taskToAdd));

        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }

    @Test
    public void undo_DeleteTask_ReturnTrue() {
        UndoManager.getInstance().clear();
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand("delete 1");
        assertTrue(taskListPanel.getNumberOfTasks() == (currentList.length - 1));

        commandBox.runCommand("undo");
        assertTrue(taskListPanel.getNumberOfTasks() == currentList.length);
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));

        commandBox.runCommand("delete 2");
        assertTrue(taskListPanel.getNumberOfTasks() == (currentList.length - 1));

        commandBox.runCommand("undo");
        assertTrue(taskListPanel.getNumberOfTasks() == currentList.length);
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }

    @Test
    public void undo_MarkTaskCompleted_ReturnTrue() {
        UndoManager.getInstance().clear();
        TestTask[] currentList = td.getTypicalTasks();
        commandBox.runCommand("mark 1 completed");
        assertTrue(taskListPanel.getNumberOfTasks() == (currentList.length - 1));

        commandBox.runCommand("undo");
        assertTrue(taskListPanel.getNumberOfTasks() == currentList.length);
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));

        commandBox.runCommand("mark 2 completed");
        assertTrue(taskListPanel.getNumberOfTasks() == (currentList.length - 1));

        commandBox.runCommand("undo");
        assertTrue(taskListPanel.getNumberOfTasks() == currentList.length);
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
    }

    private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
        commandBox.runCommand(taskToAdd.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(taskToAdd.getTitle().title);
        assertMatching(taskToAdd, addedCard);

        //confirm the list now contains all previous tasks plus the new tasks
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
}
