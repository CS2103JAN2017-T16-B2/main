package guitests.guihandles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.model.booking.UniqueBookingList;
import seedu.address.model.label.UniqueLabelList;
import seedu.address.model.task.ReadOnlyTask;

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String TITLE_FIELD_ID = "#title";
    private static final String STARTTIME_FIELD_ID = "#startTime";
    private static final String DEADLINE_FIELD_ID = "#deadline";
    private static final String LABELS_FIELD_ID = "#labels";
    private static final String STATUS_FIELD_ID = "#status";
    private static final String BOOKING_FIELD_ID = "#bookings";
    private static final String RECURRENCE_FIELD_ID = "#recurrence";
    private static final String CHECKBOX_ID = "#completedCB";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node) {
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    protected String getTextFromCheckbox(String fieldId) {
        return getStringFromCheckbox(fieldId, node);
    }

    public String getTitle() {
        return getTextFromLabel(TITLE_FIELD_ID);
    }

    public String getStartTime() {
        return getTextFromLabel(STARTTIME_FIELD_ID);
    }

    public String getDeadline() {
        return getTextFromLabel(DEADLINE_FIELD_ID);
    }

    public List<String> getBooking() {
        return getBookings(getBookingsContainer());
    }

    public List<String> getLabels() {
        return getLabels(getLabelsContainer());
    }

    private List<String> getLabels(Region labelsContainer) {
        if (labelsContainer == null) {
            return new ArrayList<String>();
        }
        return labelsContainer.getChildrenUnmodifiable().stream().map(node -> ((Labeled) node).getText())
                .collect(Collectors.toList());
    }

    private List<String> getLabels(UniqueLabelList labels) {
        return labels.asObservableList().stream().map(label -> label.labelName).collect(Collectors.toList());
    }

    //@@author A0162877N
    private List<String> getBookings(Region bookingsContainer) {
        if (bookingsContainer == null) {
            return new ArrayList<String>();
        }
        return bookingsContainer.getChildrenUnmodifiable().stream().map(node -> ((Labeled) node).getText())
                .collect(Collectors.toList());
    }

    private List<String> getBookings(UniqueBookingList bookings) {
        return bookings.asObservableList().stream().map(booking -> booking.toString()).collect(Collectors.toList());
    }

    private Region getLabelsContainer() {
        return guiRobot.from(node).lookup(LABELS_FIELD_ID).query();
    }

    private Region getBookingsContainer() {
        return guiRobot.from(node).lookup(BOOKING_FIELD_ID).query();
    }

    //@@author A0105287E
    public boolean isSameTask(ReadOnlyTask task) {
        assert (task != null);
        boolean result;

        if (task.getDeadline().isPresent() && task.getStartTime().isPresent() && !(this.getDeadline() == null)
                && !(this.getStartTime() == null)) {
            result = getTitle().equals(task.getTitle().title)
                    && getDeadline().equals(task.getDeadline().get().toString())
                    && getLabels().equals(getLabels(task.getLabels()))
                    && getStartTime().equals(task.getStartTime().get().toString())
                    && isCompleted().equals(task.isCompleted())
                    && matchRecurrence(task);
        } else if (task.getDeadline().isPresent() && !(this.getDeadline() == null)) {
            result = getTitle().equals(task.getTitle().title)
                    && getDeadline().equals(task.getDeadline().get().toString())
                    && getLabels().equals(getLabels(task.getLabels()))
                    && isCompleted().equals(task.isCompleted())
                    && (getStartTime() == null || getStartTime().equals(""))
                    && !task.getStartTime().isPresent()
                    && matchRecurrence(task);
        } else {
            result = getTitle().equals(task.getTitle().title)
                    && getLabels().equals(getLabels(task.getLabels()))
                    && isCompleted().equals(task.isCompleted()
                    && (getDeadline() == null || getDeadline().equals(""))
                    && !task.getDeadline().isPresent()
                    && (getStartTime() == null || getStartTime().equals(""))
                    && !task.getStartTime().isPresent()
                    && isGuiBookingMatch(getBookings(task.getBookings())))
                    && matchRecurrence(task);
        }
        return result;
    }

    //@@author A0162877N
    private boolean isGuiBookingMatch(List<String> taskBookings) {
        boolean isEqual = true;
        for (int i = 0; i < getBooking().size(); i++) {
            String guiBooking = getBooking().get(i).trim();
            String taskBooking = taskBookings.get(i).trim();
            if (!guiBooking.equals(taskBooking)) {
                isEqual = false;
            }
        }
        isEqual = isEqual && (getBooking().size() == taskBookings.size());
        return isEqual;
    }

    //@@author A0105287E
    private boolean matchRecurrence(ReadOnlyTask task) {
        boolean result;
        if (task.isRecurring() &&  this.isRecurring() &&
                this.getRecurrence().equals(task.getRecurrence().get().toString())) {
            result = true;
        } else if (!this.isRecurring() && !task.isRecurring()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    //@@author A0105287E
    private Boolean isCompleted() {
        String text = getTextFromCheckbox(CHECKBOX_ID);
        if ("Completed".equals(text)) {
            return true;
        } else {
            return false;
        }
    }

    //@@author A0105287E
    private Boolean isRecurring() {
        return isFieldPresent(RECURRENCE_FIELD_ID, node);
    }

    //@@author A0105287E
    private String getRecurrence() {
        return getTextFromLabel(RECURRENCE_FIELD_ID).replaceFirst("Repeats every ", "");

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getTitle().equals(handle.getTitle()) && getStartTime().equals(handle.getStartTime())
                    && getDeadline().equals(handle.getDeadline()) && isCompleted().equals(handle.isCompleted())
                    && getLabels().equals(handle.getLabels());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        String status = "Incomplete";
        if (isCompleted()) {
            status = "Completed";
        }
        return getTitle() + " Start: " + getStartTime() + " Deadline: " + getDeadline() + " Status: " + status
                + " Label: " + getLabels();
    }
}
