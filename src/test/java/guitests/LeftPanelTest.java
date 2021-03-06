package guitests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import seedu.address.commons.util.DateTimeUtil;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.testutil.TestTask;

//@@author A0140042A
/**
 * Test class to ensure LeftPanel is functional
 */
public class LeftPanelTest extends TaskManagerGuiTest {

    @Test
    public void labelListView_TestToggleLabelListView_ReturnFalseThenTrue() {
        leftPanel.clickOnLabelHeader();
        assertFalse(leftPanel.isVisible());
        leftPanel.clickOnLabelHeader();
        assertTrue(leftPanel.isVisible());
    }

    //@@author A0162877N
    @Test
    public void today_TestTodayPanel_ReturnTrue() {
        TestTask[] testTask = td.getTypicalTasks();
        ArrayList<TestTask> filteredTestTask = new ArrayList<TestTask>();

        for (TestTask task : testTask) {
            if (isTaskInToday(task)) {
                filteredTestTask.add(task);
            }
        }

        testTask = new TestTask[filteredTestTask.size()];
        for (int i = 0; i < filteredTestTask.size(); i++) {
            testTask[i] = filteredTestTask.get(i);
        }

        leftPanel.clickOnToday();
        assertTrue(taskListPanel.isListMatching(testTask));
    }

    @Test
    public void label_TestFirstLabel_ReturnTrue() {
        TestTask[] testTask = td.getTasksByIndex(0);
        commandBox.runCommand("delete 2");
        leftPanel.clickOnFirstLabel();
        assertTrue(taskListPanel.isListMatching(testTask));
    }

    public boolean isTaskInToday(ReadOnlyTask task) {
        Date endDate = DateTimeUtil.getEndDate();
        Date startDate = DateTimeUtil.getStartDate();

        if (!task.isCompleted() && task.getDeadline().isPresent() && task.getStartTime().isPresent()) {
            return (task.getDeadline().get().getDateTime().before(endDate)
                    && task.getDeadline().get().getDateTime().after(startDate))
                    || task.getDeadline().get().getDateTime().equals(endDate);
        } else if (!task.isCompleted() && task.getDeadline().isPresent()) {
            return task.getDeadline().get().getDateTime().before(startDate)
                    || task.getDeadline().get().getDateTime().equals(startDate);
        }
        return false;
    }
}
