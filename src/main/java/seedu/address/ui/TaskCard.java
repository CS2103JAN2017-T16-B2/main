package seedu.address.ui;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.events.ui.CheckBoxSelectionChangedEvent;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.task.ReadOnlyTask;

//@@author A0162877N
/**
 * This class handles the initialization of the task card in task list panel
 */
public class TaskCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";
    private static final int INVALID_INDEX = -1;
    private static final int VALID_INDEX = 0;

    @FXML
    private HBox cardPane;
    @FXML
    private VBox vBoxMain;
    @FXML
    private VBox leftVBox;
    @FXML
    private CheckBox completedCB;
    @FXML
    private Label title;
    @FXML
    private Label id;
    @FXML
    private VBox dateVBox;
    @FXML
    private Label startTime;
    @FXML
    private Label dashLabel;
    @FXML
    private Label deadline;
    @FXML
    private Label status;
    @FXML
    private javafx.scene.control.Label reserveSlot;
    @FXML
    private FlowPane labels;
    @FXML
    private FlowPane bookings;
    @FXML
    private Label recurrence;

    /**
     * Task Card initialization
     */
    public TaskCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);
        title.setText(task.getTitle().title);
        id.setText(displayedIndex + ". ");
        setStartTime(task);
        setDeadline(task);
        setLabel(task);
        setBookings(task);
        removeStatus();
        setCheckBox(task, displayedIndex);
        setDashLabel(task);
        setIsRecurringLabel(task);
    }

    /**
     * Set the recurring label text
     */
    private void setIsRecurringLabel(ReadOnlyTask task) {
        if (task.isRecurring()) {
            recurrence.setText("Repeats every " + task.getRecurrence().get().toString());
        } else {
            dateVBox.getChildren().remove(recurrence);
        }
    }

    /**
     * Set dash label to display only when both start date and deadline is present
     */
    private void setDashLabel(ReadOnlyTask task) {
        if (!(task.getStartTime().isPresent() && task.getDeadline().isPresent())) {
            dateVBox.getChildren().remove(dashLabel);
        }
    }

    /**
     * Initialize the checkbox and stores the display index in the style of checkbox
     */
    private void setCheckBox(ReadOnlyTask task, int displayedIndex) {
        completedCB.setStyle(displayedIndex + "");
        completedCB.setSelected(task.isCompleted());
        if (task.isRecurring() && task.isCompleted()) {
            completedCB.setDisable(true);
        }
    }

    /**
     * Initialize the start time. If start time is present, display,
     * else dynamically delete the start time label from parent
     */
    private void setStartTime(ReadOnlyTask task) {
        if (task.getStartTime().isPresent()) {
            startTime.setText(task.getStartTime().get().toString());
        } else {
            dateVBox.getChildren().remove(startTime);
        }
    }

    /**
     * Initialize the deadline. If deadline is present, display,
     * else dynamically delete the deadline label from parent
     */
    private void setDeadline(ReadOnlyTask task) {
        if (task.getDeadline().isPresent()) {
            deadline.setText(task.getDeadline().get().toString());
        } else {
            dateVBox.getChildren().remove(deadline);
        }
    }

    /**
     * Initialize the labels panel. If there exist label, display,
     * else dynamically delete the label flow pane from parent
     */
    private void setLabel(ReadOnlyTask task) {
        if (task.getLabels().isEmpty()) {
            leftVBox.getChildren().remove(labels);
        } else {
            initLabels(task);
        }
    }

    /**
     * Initialize the deadline. If deadline is present, display,
     * else dynamically delete the deadline label from parent
     */
    private void setBookings(ReadOnlyTask task) {
        if (task.getBookings().isEmpty()) {
            dateVBox.getChildren().remove(reserveSlot);
            dateVBox.getChildren().remove(bookings);
        } else {
            reserveSlot.setText("Reserved Slots");
            completedCB.setDisable(true);
            initBookings(task);
        }
    }

    /**
     * Remove the status label from task card
     */
    private void removeStatus() {
        vBoxMain.getChildren().remove(status);
    }

    /**
     * Event handler for check box selection
     */
    @FXML
    private void handleCheckBoxChanged(ActionEvent event) {
        int id = tryParseAsIndex(completedCB.getStyle());
        if (id > VALID_INDEX) {
            raise(new CheckBoxSelectionChangedEvent(id, completedCB.isSelected()));
        }
    }

    /**
     * Try parsing arguments as index
     */
    private int tryParseAsIndex(String args) {
        Optional<Integer> index = ParserUtil.parseIndex(args);
        if (index.isPresent()) {
            return index.get();
        } else {
            return INVALID_INDEX;
        }
    }

    /**
     * Add the label flowpane with the labels in the task
     */
    private void initLabels(ReadOnlyTask task) {
        task.getLabels().forEach(label -> labels.getChildren().add(new Label(label.labelName)));
    }

    /**
     * Add the booking flowpane with the booking time slots in a task
     */
    private void initBookings(ReadOnlyTask task) {
        task.getBookings().forEach(booking -> bookings.getChildren().add(new Label(booking.toString() + "\n")));
    }
}
