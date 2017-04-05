package guitests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import seedu.address.commons.core.Messages;
import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddCommand;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TestTask;
import seedu.address.testutil.TestUtil;

public class AddCommandTest extends TaskManagerGuiTest {

    @Test
    public void add() throws IllegalValueException, IllegalDateTimeValueException {
        //add one task
        TestTask[] currentList = td.getTypicalTasks();
        TestTask taskToAdd = td.task8;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add another task
        taskToAdd = td.task9;
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add another task
        taskToAdd = (new TaskBuilder())
                .withTitle("Test Task")
                .withDeadline("today")
                .withLabels("label1")
                .build();
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add another task
        taskToAdd = (new TaskBuilder())
                .withTitle("Test Task 2")
                .withLabels("label1", "label2")
                .build();
        assertAddSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate task
        commandBox.runCommand(td.task8.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
        assertTrue(taskListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.task1);

        //invalid command
        commandBox.runCommand("addso do something");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
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

    //@@author A0105287E
    @Test
    public void add_swapDatesWhenStartIsAfterEnd_success() throws Exception {
        TestTask[] currentList = td.getTypicalTasks();

        //create expected Task
        TestTask expectedTask = new TaskBuilder().withTitle("Complete task 10").withStartTime("today")
                .withDeadline("tomorrow")
                .withStatus(false).build();

        //command with wrong dates
        String command = "add Complete task 10 from tomorrow to today";

        commandBox.runCommand(command);

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToTask(expectedTask.getTitle().title);
        assertMatching(expectedTask, addedCard);

        //confirm the list now contains all previous tasks plus the new tasks
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, expectedTask);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }

    //@@author A0105287E
    @Test
    public void add_recurringTask_success() throws Exception {
        TestTask[] currentList = td.getTypicalTasks();

        TestTask taskToAdd = new TaskBuilder().withTitle("Complete task 11").withStartTime("today")
                .withDeadline("tomorrow")
                .withRecurrenceStatus(true).withRecurrence("2 days")
                .withStatus(false).build();

        assertAddSuccess(taskToAdd, currentList);
        System.out.println();
    }

}
