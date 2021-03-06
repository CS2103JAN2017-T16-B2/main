package seedu.address.ui;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Region> {

    private static final String ICON = "/images/app_icon.png";
    private static final String FXML = "MainWindow.fxml";
    private static final int MIN_HEIGHT = 500;
    private static final int MIN_WIDTH = 800;

    private Stage primaryStage;
    private Logic logic;
    private Model model;
    private CommandBox commandBox;
    private ResultDisplay resultDisplay;
    private StatusBarFooter statusBarFooter;

    // Independent Ui parts residing in this Ui container
    private TaskListPanel taskListPanel;
    private LeftPanel leftPanel;
    private Config config;

    @FXML
    private AnchorPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private AnchorPane taskListPanelPlaceholder;

    @FXML
    private AnchorPane resultDisplayPlaceholder;

    @FXML
    private AnchorPane statusbarPlaceholder;

    @FXML
    private AnchorPane leftPanelPlaceholder;

    public MainWindow(Stage primaryStage, Config config, UserPrefs prefs, Logic logic, Model model) {
        super(FXML);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.config = config;
        this.model = model;

        // Configure the UI
        setTitle(config.getAppTitle());
        setIcon(ICON);
        setWindowMinSize();
        setWindowDefaultSize(prefs);
        Scene scene = new Scene(getRoot());
        primaryStage.setScene(scene);

        setAccelerators();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
                //@@author A0140042A
            } else if (!commandBox.getCommandTextField().isFocused()) {
                commandBox.getCommandTextField().requestFocus();
                commandBox.getCommandTextField().positionCaret(
                        commandBox.getCommandTextField().lengthProperty().intValue());

                //Manually override commandHistory here since the arrow keys will be consumed by the task/label card
                if (event.getCode() == KeyCode.UP) {
                    event.consume();
                    commandBox.getPreviousCommand();
                } else if (event.getCode() == KeyCode.DOWN) {
                    event.consume();
                    commandBox.getNextCommand();
                }
            }
        });
    }

    //@@author A0140042A
    /**
     * Fill up components in the main window,
     * but only update appropriate components if already initialized
     */
    public void fillInnerParts() {
        setTaskListPanel();
        setLeftPanel();
        setResultDisplay();
        setStatusBarFooter();
        setCommandBox();
    }

    private void setTaskListPanel() {
        if (taskListPanel == null) {
            taskListPanel = new TaskListPanel(getTaskListPlaceholder(), logic.getFilteredIncompleteTaskList());
        } else {
            taskListPanel.setConnections(logic.getFilteredIncompleteTaskList());
        }
    }

    private void setLeftPanel() {
        if (leftPanel == null) {
            leftPanel = new LeftPanel(getleftPanelPlaceholder(), model.getTaskManager().getTaskList());
        } else {
            leftPanel.setTaskList(model.getTaskManager().getTaskList());
            leftPanel.updateLabelCount();
            leftPanel.setTodayListView(model.getTaskManager().getTaskList());
        }
    }

    private void setResultDisplay() {
        if (resultDisplay == null) {
            resultDisplay = new ResultDisplay(getResultDisplayPlaceholder());
        }
    }

    private void setStatusBarFooter() {
        if (statusBarFooter == null) {
            statusBarFooter = new StatusBarFooter(getStatusbarPlaceholder(), config.getTaskManagerFilePath());
        } else {
            statusBarFooter.setSaveLocation(config.getTaskManagerFilePath());
        }
    }

    private void setCommandBox() {
        if (commandBox == null) {
            commandBox = new CommandBox(getCommandBoxPlaceholder(), logic);
        } else {
            commandBox.setLogic(logic);
        }
    }

    private AnchorPane getCommandBoxPlaceholder() {
        return commandBoxPlaceholder;
    }

    private AnchorPane getStatusbarPlaceholder() {
        return statusbarPlaceholder;
    }

    private AnchorPane getResultDisplayPlaceholder() {
        return resultDisplayPlaceholder;
    }

    private AnchorPane getTaskListPlaceholder() {
        return taskListPanelPlaceholder;
    }

    private AnchorPane getleftPanelPlaceholder() {
        return leftPanelPlaceholder;
    }

    public void hide() {
        primaryStage.hide();
    }

    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    /**
     * Sets the given image as the icon of the main window.
     * @param iconSource e.g. {@code "/images/help_icon.png"}
     */
    private void setIcon(String iconSource) {
        FxViewUtil.setStageIcon(primaryStage, iconSource);
    }

    /**
     * Sets the default size based on user preferences.
     */
    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    protected GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

    @FXML
    public void handleHelp() {
        HelpWindow helpWindow = new HelpWindow();
        helpWindow.show();
    }

    public void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    //@@author A0162877N
    /**
     * Display task with the label selected
     */
    public void loadLabelSelection(String label) {
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(label));
        model.updateFilteredTaskList(keywordSet);
    }

    /**
     * Updates task list with tasks that starts after today
     */
    public void loadTodaySelection() {
        Date endDate = DateTimeUtil.getEndDate();
        Date startDate = DateTimeUtil.getStartDate();
        model.updateFilteredTaskList(startDate, endDate, false);
    }

    /**
     * Display all incomplete task
     */
    public void showAllTask() {
        model.updateFilteredTaskList(false);
    }

    public TaskListPanel getTaskListPanel() {
        return this.taskListPanel;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
