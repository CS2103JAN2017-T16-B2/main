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
        if (!(task.getStartTime().isPresent() && task.getDeadline().isPresent())) {
            dateVBox.getChildren().remove(dashLabel);
        }
        if (task.isRecurring() && task.isCompleted()) {
            completedCB.setDisable(true);
        }

        if (task.isRecurring()) {
            recurrence.setText("Repeats every " + task.getRecurrence().get().toString());
        } else {
            dateVBox.getChildren().remove(recurrence);
        }
    }

    private void setCheckBox(ReadOnlyTask task, int displayedIndex) {
        completedCB.setStyle(displayedIndex + "");
        completedCB.setSelected(task.isCompleted());
    }

    private void setStartTime(ReadOnlyTask task) {
        if (task.getStartTime().isPresent()) {
            startTime.setText(task.getStartTime().get().toString());
        } else {
            dateVBox.getChildren().remove(startTime);
        }
    }

    private void setDeadline(ReadOnlyTask task) {
        if (task.getDeadline().isPresent()) {
            deadline.setText(task.getDeadline().get().toString());
        } else {
            dateVBox.getChildren().remove(deadline);
        }
    }

    private void setLabel(ReadOnlyTask task) {
        if (task.getLabels().isEmpty()) {
            leftVBox.getChildren().remove(labels);
        } else {
            initLabels(task);
        }
    }

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

    private void removeStatus() {
        vBoxMain.getChildren().remove(status);
    }

    /**
     * Event handler for check box selection
     */
    @FXML
    private void handleCheckBoxChanged(ActionEvent event) {
        int id = tryParseAsIndex(completedCB.getStyle());
        if (id > 0) {
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
            return -1;
        }
    }

    private void initLabels(ReadOnlyTask task) {
        task.getLabels().forEach(label -> labels.getChildren().add(new Label(label.labelName)));
    }

    private void initBookings(ReadOnlyTask task) {
        task.getBookings().forEach(booking -> bookings.getChildren().add(new Label(booking.toString() + "\n")));
    }
}
