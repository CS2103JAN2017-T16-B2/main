package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.address.TestApp;

//@@author A0140042A
/**
 * Handle for LeftPanel logic
 */
public class LeftPanelHandle extends GuiHandle {

    private static final String LABEL_HEADER = "#labelListHeader";
    private static final String LABEL_LIST_VIEW = "#labelListView";
    private static final String LABEL_LIST_CARD = "#labelListCard";
    private static final String TODAY_HEADER = "#todayHeader";

    public LeftPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public void clickOnLabelHeader() {
        guiRobot.clickOn(LABEL_HEADER);
    }

    public void clickOnToday() {
        guiRobot.clickOn(TODAY_HEADER);
    }

    public void clickOnFirstLabel() {
        guiRobot.clickOn(LABEL_LIST_CARD);
    }

    public boolean isVisible() {
        return getNode(LABEL_LIST_VIEW).isVisible();
    }
}
