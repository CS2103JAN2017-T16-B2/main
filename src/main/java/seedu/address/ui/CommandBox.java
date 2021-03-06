package seedu.address.ui;

import java.util.logging.Logger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.autocomplete.AutocompleteManager;
import seedu.address.logic.autocomplete.AutocompleteRequest;
import seedu.address.logic.autocomplete.AutocompleteResponse;
import seedu.address.logic.commandhistory.CommandHistory;
import seedu.address.logic.commandhistory.CommandHistoryManager;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

public class CommandBox extends UiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";
    private AutocompleteManager autocompleteManager;
    private CommandHistory commandHistory;

    private Logic logic;

    @FXML
    private TextField commandTextField;

    @FXML
    private HBox commandTextFieldContainer;

    @FXML
    private FontAwesomeIconView commandTextFieldBeforeLabel;

    public CommandBox(AnchorPane commandBoxPlaceholder, Logic logic) {
        super(FXML);
        this.autocompleteManager = new AutocompleteManager();
        this.commandHistory = CommandHistoryManager.getInstance();
        this.logic = logic;
        addToPlaceholder(commandBoxPlaceholder);
    }

    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextFieldContainer);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @FXML
    private void handleCommandInputChanged() {
        try {
            String command = commandTextField.getText();
            CommandResult commandResult = logic.execute(command);

            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
            commandHistory.addCommand(command);
        } catch (CommandException e) {
            // handle command failure
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    //@@author A0140042A
    /**
     * Handles the tab character for auto-completion, up/down for iterating through the command
     */
    @FXML
    private void handleOnKeyPressed(KeyEvent ke) {
        if (ke.getCode() == KeyCode.TAB) {
            handleAutocomplete();
            //Consume the event so the text field will not go to the next ui component
            ke.consume();
        } else if (ke.getCode() == KeyCode.UP) {
            getPreviousCommand();
            ke.consume();
        } else if (ke.getCode() == KeyCode.DOWN) {
            getNextCommand();
            ke.consume();
        }
    }

    /**
     * Handles the auto-completion when a request to auto complete is invoked
     */
    private void handleAutocomplete() {
        //Extract the command as well as the caret position
        String command = commandTextField.getText();
        int caretPosition = commandTextField.getCaretPosition();

        //Create a auto complete request
        AutocompleteRequest request = new AutocompleteRequest(command, caretPosition);
        //Get the response using the auto complete manager
        AutocompleteResponse response = autocompleteManager.getSuggestions(request);

        //Update fields with the response
        updateAutocompleteFeedback(response);
        commandTextField.setText(response.getPhrase());
        commandTextField.positionCaret(response.getCaretPosition());
    }

    /**
     * Gets the next executed command from the current command (if iterated through before)
     */
    public void getNextCommand() {
        String command = commandHistory.next();
        setCommandAndCursorToEnd(command);
    }

    /**
     * Gets the previously executed command from the current command
     */
    public void getPreviousCommand() {
        String command = commandHistory.previous();
        setCommandAndCursorToEnd(command);
    }

    /**
     * Sets the command to the string input given along with the cursor at the end
     */
    private void setCommandAndCursorToEnd(String command) {
        String commandToSet = command == null ? commandTextField.getText() : command;
        commandTextField.setText(commandToSet);
        moveCursorToEndOfField();
    }

    /**
     * Moves the cursor in commandTextField to the end of the string
     */
    private void moveCursorToEndOfField() {
        commandTextField.positionCaret(commandTextField.getLength());
    }

    /**
     * Updates the output window to either nothing or a list of suggestions
     */
    private void updateAutocompleteFeedback(AutocompleteResponse response) {
        if (response.getSuggestions().size() > 1) { //Show list of suggestions if more than 1
            raise(new NewResultAvailableEvent(response.getSuggestions().toString()));
        } else {
            raise(new NewResultAvailableEvent(""));
        }
    }

    /**
     * Sets the logic
     */
    public void setLogic(Logic logic) {
        this.logic = logic;
    }

    /**
     * Gets the text field component of commandBox
     */
    public TextField getCommandTextField() {
        return commandTextField;
    }
}
