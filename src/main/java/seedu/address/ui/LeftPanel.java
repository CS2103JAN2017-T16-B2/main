package seedu.address.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.TaskManagerChangedEvent;
import seedu.address.commons.events.ui.LeftPanelTodaySelectionChangedEvent;
import seedu.address.commons.events.ui.ShowAllSelectionChangedEvent;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.model.label.Label;
import seedu.address.model.task.ReadOnlyTask;

//@@author @A0140042A
/**
 *  Controller class for the LeftPanel of Ui
 */
public class LeftPanel extends UiPart<Region> {

    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private static final String FXML = "LeftPanel.fxml";
    private HashMap<Label, Integer> labelCount;
    private ObservableList<ReadOnlyTask> taskList;
    private ObservableList<Label> labels;

    @FXML
    private javafx.scene.control.Label appTitleLabel;

    @FXML
    private javafx.scene.control.Label todayLabel;

    @FXML
    private javafx.scene.control.Label todayCounterLabel;

    @FXML
    private javafx.scene.control.Label showAllLabel;

    @FXML
    private javafx.scene.control.Label labelCounterLabel;

    @FXML
    private javafx.scene.control.Label labelTitleLabel;

    @FXML
    private FontAwesomeIconView todayIconLabel;

    @FXML
    private FontAwesomeIconView showAllIconLabel;

    @FXML
    private FontAwesomeIconView labelArrow;

    @FXML
    private FontAwesomeIconView labelIconLabel;

    @FXML
    private ListView<Label> labelListView;

    @FXML
    private HBox todayHeader;

    @FXML
    private HBox showAllHeader;

    public LeftPanel(AnchorPane leftListPlaceholder,
            ObservableList<ReadOnlyTask> taskList) {
        super(FXML);
        this.taskList = taskList;
        initIcons();
        updateLabelCount();
        setTodayListView(taskList);
        initializeShowAllLabel(taskList);
        addToPlaceholder(leftListPlaceholder);
        registerAsAnEventHandler(this);
    }

    /**
     * Updates the count in the LeftPanel labelList
     */
    public void updateLabelCount() {
        labelCount = new HashMap<Label, Integer>();

        for (ReadOnlyTask task : taskList) {
            for (Label label : task.getLabels()) {
                int currentCount = labelCount.get(label) == null ? 0 : labelCount.get(label);
                labelCount.put(label, currentCount + 1);
            }
        }

        setConnections(labelCount);
    }

    /**
     * Initializes the icons on the LeftPanel
     */
    private void initIcons() {
        todayIconLabel.setIcon(FontAwesomeIcon.CALENDAR_ALT);
        showAllIconLabel.setIcon(FontAwesomeIcon.LIST);
        labelIconLabel.setIcon(FontAwesomeIcon.HASHTAG);
        labelArrow.setIcon(FontAwesomeIcon.ANGLE_UP);
    }

    /**
     * Sets the counter beside the today button with tasks that start after today midnight
     */
    public void setTodayListView(ObservableList<ReadOnlyTask> taskList) {
        todayLabel.setText("Today");
        int count = 0;
        Date endTime = DateTimeUtil.getEndDate();
        Date startDate = DateTimeUtil.getStartDate();

        // Add all tasks that is not completed and deadline is after today
        for (ReadOnlyTask task : taskList) {
            if (!task.isCompleted() && task.getDeadline().isPresent() && task.getStartTime().isPresent()) {
                if ((task.getDeadline().get().getDateTime().before(endTime)
                        && task.getDeadline().get().getDateTime().after(startDate))
                        || task.getDeadline().get().getDateTime().equals(endTime)) {
                    count++;
                }
            } else if (!task.isCompleted() && task.getDeadline().isPresent() &&
                        (task.getDeadline().get().getDateTime().before(endTime)
                                || task.getDeadline().get().getDateTime().equals(endTime))) {
                count++;
            }
        }
        todayCounterLabel.setText(Integer.toString(count));
        setEventHandlerForTodaySelectionChangeEvent();
    }

    /**
     * Sets the showAll label as well as the event handler for it
     */
    public void initializeShowAllLabel(ObservableList<ReadOnlyTask> taskList) {
        showAllLabel.setText("Show All");
        setEventHandlerForCalendarSelectionChangeEvent();
    }

    /**
     * Sets the connections for the label list and the counter
     */
    public void setConnections(HashMap<Label, Integer> labelList) {
        labels = getLabelsWithCount(labelList);
        labelCounterLabel.setText(Integer.toString(labelList.size()));
        labelListView.setItems(labels);
        labelListView.setCellFactory(listView -> new LabelListViewCell());
    }

    /**
     * Returns labels with count more than 0, ignoring all empty labels
     */
    private ObservableList<Label> getLabelsWithCount(HashMap<Label, Integer> labelList) {
        labels = FXCollections.observableArrayList();
        for (Entry<Label, Integer> entry : labelList.entrySet()) {
            if (entry.getValue() > 0) {
                labels.add(entry.getKey());
            }
        }
        FXCollections.sort(labels);
        return labels;
    }

    /**
     * Sets the task list to the given task list
     */
    public void setTaskList(ObservableList<ReadOnlyTask> taskList) {
        this.taskList = taskList;
    }

    /**
     * Toggles the visibility of the labelList
     */
    @FXML
    private void toggleLabelList() {
        labelListView.setVisible(!labelListView.isVisible());
        if (labelListView.isVisible()) {
            labelArrow.setIcon(FontAwesomeIcon.ANGLE_UP);
        } else {
            labelArrow.setIcon(FontAwesomeIcon.ANGLE_DOWN);
        }
    }

    /**
     * Adds this item to the placeHolderPane
     */
    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        placeHolderPane.getChildren().add(getRoot());
    }

    /**
     * Creates an event handler to show all task that
     */
    private void setEventHandlerForTodaySelectionChangeEvent() {
        todayHeader.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.fine("Selection in label left list panel changed to : 'Today'");
                raise(new LeftPanelTodaySelectionChangedEvent());
            }
        });
    }

    /**
     * Creates an event handler to show all the incomplete task on the task panel
     */
    private void setEventHandlerForCalendarSelectionChangeEvent() {
        showAllHeader.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                logger.fine("Clicked on show all menu");
                raise(new ShowAllSelectionChangedEvent());
            }
        });
    }

    /**
     * Scrolls to the index of the task
     */
    public void scrollTo(int index) {
        Platform.runLater(() -> {
            labelListView.scrollTo(index);
            labelListView.getSelectionModel().clearAndSelect(index);
        });
    }

    /**
     *  Updates the label counts and today counts when the task manager has been changed
     */
    @Subscribe
    public void handleTaskManagerChangedEvent(TaskManagerChangedEvent tmce) {
        updateLabelCount();
        setTodayListView(taskList);
        logger.info(LogsCenter.getEventHandlingLogMessage(tmce,
                "Updating label list count and total number of tasks for today"));
    }

    class LabelListViewCell extends ListCell<Label> {

        @Override
        protected void updateItem(Label label, boolean empty) {
            super.updateItem(label, empty);
            if (empty || label == null) {
                setGraphic(null);
                setText(null);
            } else {
                Integer newCount = labelCount.get(label);
                if (newCount == null) {
                    newCount = 1;
                }
                setGraphic(new LabelCard(label, newCount).getRoot());
            }
        }
    }
}
