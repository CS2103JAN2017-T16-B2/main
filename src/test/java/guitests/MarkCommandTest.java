package guitests;

import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.MarkCommand.MESSAGE_RECURRING_INCOMPLETE_DISABLE;
import static seedu.address.logic.commands.MarkCommand.MESSAGE_TYPE_BOOKING;

import java.util.ArrayList;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import guitests.guihandles.TaskCardHandle;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.testutil.TaskBuilder;
import seedu.address.testutil.TestTask;

//@@author A0105287E
public class MarkCommandTest extends TaskManagerGuiTest {

    @Test
    public void mark_statusSpecified_success() throws Exception {

        String detailsToEdit = "completed";
        int taskManagerIndex = 2;

        TestTask editedTask = new TaskBuilder().withTitle("Complete task 2").withStartTime("10-10-2017 0100")
                .withDeadline("11-11-2017 2300").withLabels("owesMoney", "friends")
                .withStatus(true).build();

        assertMarkSuccess(taskManagerIndex, taskManagerIndex, detailsToEdit, editedTask);
    }

    @Test
    public void mark_missingTaskIndex_failure() {
        commandBox.runCommand("mark completed");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void mark_missingStatus_failure() {
        commandBox.runCommand("mark 1");
        assertResultMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE));
    }

    @Test
    public void mark_completeBooking_failure() {
        //add a booking
        commandBox.runCommand("book Complete booking #friends on 10-10-2017 2pm to 5pm,"
                + " 11-10-2017 2pm to 5pm, 12-10-2017 2pm to 5pm");
        commandBox.runCommand("mark 8 completed");
        assertResultMessage(MESSAGE_TYPE_BOOKING);
    }

    @Test
    public void mark_completedRecurringTaskToIncomplete_failure() throws Exception {
        //add a recurring task
        TestTask taskToAdd = new TaskBuilder().withTitle("Complete task 12").withStartTime("today")
                .withDeadline("tomorrow")
                .withRecurrenceStatus(true).withRecurrence("2 days").build();
        commandBox.runCommand(taskToAdd.getAddCommand());

        //mark task complete because default status is incomplete
        commandBox.runCommand("mark 1 completed");

        //mark the completed recurring task incomplete
        commandBox.runCommand("list completed");
        commandBox.runCommand("mark 1 incomplete");

        assertResultMessage(MESSAGE_RECURRING_INCOMPLETE_DISABLE);
    }

    @Test
    public void mark_incompleteRecurringTask_success() throws Exception {
        //add a completed recurring task
        TestTask taskToAdd = new TaskBuilder().withTitle("Complete task 12").withStartTime("21st May")
                .withDeadline("22nd May")
                .withRecurrenceStatus(true).withRecurrence("2 days").build();
        commandBox.runCommand(taskToAdd.getAddCommand());

        //mark task complete because default status is incomplete
        commandBox.runCommand("mark 1 completed");

        //check for existence of new incomplete instance
        TestTask newTask = new TaskBuilder().withTitle("Complete task 12").withStartTime("23rd May")
                .withDeadline("24th May")
                .withRecurrenceStatus(true).withRecurrence("2 days").build();
        TaskCardHandle editedCard = taskListPanel.navigateToTask(newTask.getTitle().title);
        assertMatching(newTask, editedCard);

        //check for existence of old completed instance
        TestTask oldTask = new TaskBuilder().withTitle("Complete task 12").withStartTime("21st May")
                .withDeadline("22nd May")
                .withStatus(true)
                .withRecurrenceStatus(true).withRecurrence("2 days").build();
        commandBox.runCommand("list completed");
        editedCard = taskListPanel.navigateToTask(newTask.getTitle().title);
        assertMatching(oldTask, editedCard);
    }


    /**
     * Checks whether the edited task has the correct updated details.
     *
     * @param (int filteredTaskListIndex index of task to edit in filtered list
     * @param taskManagerIndex index of task to edit in the task manager.
     *      Must refer to the same task as {@code filteredTaskListIndex}
     * @param status the status to edit the task with as input to the mark command
     * @param editedTask the expected task after editing the task's details
     */
    private void assertMarkSuccess(int filteredTaskListIndex, int taskManagerIndex,
                                    String status, TestTask editedTask) {
        TestTask[] expectedTasksList = td.getTypicalTasks();
        if ("completed".equalsIgnoreCase(status)) {
            ArrayList<Integer> intArrayList = new ArrayList<Integer>();
            for (int i = 0; i < td.getTypicalTasks().length; i++) {
                if (!(i == taskManagerIndex - 1)) {
                    intArrayList.add(i);
                }
            }

            int[] arr = new int[intArrayList.size()];

            for (int i = 0; i < intArrayList.size(); i++) {
                arr[i] = intArrayList.get(i);
            }
            expectedTasksList = td.getTasksByIndex(arr);
        }

        commandBox.runCommand("mark " + filteredTaskListIndex + " " + status);
        System.out.println("details to edit: " + status);
        System.out.println("edited task: " + editedTask);
        Arrays.sort(expectedTasksList);
        assertTrue(taskListPanel.isListMatching(expectedTasksList));
        assertResultMessage(String.format(MarkCommand.MESSAGE_MARK_TASK_SUCCESS, editedTask));
    }

}
