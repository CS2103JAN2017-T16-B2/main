package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.logic.Autocomplete;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

public class CommandBox extends UiPart<Region> {
    private static final int ONLY_SUGGESTION_INDEX = 0;
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private static final String FXML = "CommandBox.fxml";
    public static final String ERROR_STYLE_CLASS = "error";
    private Autocomplete autocomplete;
    private CommandHistory commandHistory;

    private final Logic logic;

    @FXML
    private TextField commandTextField;

    public CommandBox(AnchorPane commandBoxPlaceholder, Logic logic) {
        super(FXML);
        this.autocomplete = new Autocomplete();
        this.commandHistory = new CommandHistory();
        this.logic = logic;
        addToPlaceholder(commandBoxPlaceholder);
    }

    private void addToPlaceholder(AnchorPane placeHolderPane) {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @FXML
    private void handleCommandInputChanged() {
        try {
            String command = commandTextField.getText();
            CommandResult commandResult = logic.execute(command);

            // process result of the command
            setStyleToIndicateCommandSuccess();
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));
            commandHistory.addCommand(command);
        } catch (CommandException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Hijacks the tab character for auto-completion, up/down for iterating through the command
     * @param ke
     */
    @FXML
    private void handleOnKeyPressed(KeyEvent ke) {
        if (ke.getCode() == KeyCode.TAB) {
            commandTextField.setText(commandTextField.getText().replaceAll("\\s+$", ""));
            String lastToken = extractLastKey();
            List<String> suggestions = autocomplete.getSuggestions(lastToken);
            handleSuggestions(suggestions);
            moveCursorToEnd();
            //Consume the event so the textfield will not go to the next ui component
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
     * Gets the next executed command from the current command (if iterated through before)
     */
    private void getNextCommand() {
        String text = commandHistory.next();
        text = text == null ? commandTextField.getText() : text;
        commandTextField.setText(text);
        moveCursorToEnd();
    }

    /**
     * Gets the previously executed command from the current command
     */
    private void getPreviousCommand() {
        String text = commandHistory.previous();
        text = text == null ? commandTextField.getText() : text;
        commandTextField.setText(text);
        moveCursorToEnd();
    }

    /**
     * Moves the cursor in commandTextField to the end of the string
     */
    private void moveCursorToEnd() {
        commandTextField.positionCaret(commandTextField.getLength());
    }

    /**
     * Handles suggestions to replace/suggest
     * @param suggestions - list of suggestions
     */
    private void handleSuggestions(List<String> suggestions) {
        if (suggestions.isEmpty()) {
            return;
        } else if (suggestions.size() == 1) { //exactly 1 suggestion
            replaceLastTokenWithSuggestion(suggestions.get(ONLY_SUGGESTION_INDEX));
        } else { //show suggestions in the output box
            showSuggestions(suggestions);
        }
    }

    /**
     * Shows suggestions in the output box
     * @param suggestions - list of suggestions
     */
    private void showSuggestions(List<String> suggestions) {
        logger.info("Suggestions: " + suggestions);

        String longestString = getLongestString(suggestions);
        int commonSubstringIndex = 0;
        char currentChar;
        commonSubstring:
        for (int charIndex = 0; charIndex < longestString.length(); charIndex++) {
            currentChar = longestString.charAt(charIndex);
            for (String suggestion : suggestions) {
                if (suggestion.length() <= charIndex || suggestion.charAt(charIndex) != currentChar) {
                    break commonSubstring;
                }
            }
            commonSubstringIndex++;
        }

        String commonSubstring = longestString.substring(0, commonSubstringIndex);
        replaceLastTokenWithSuggestion(commonSubstring, "");
        raise(new NewResultAvailableEvent(suggestions.toString()));
    }

    private String getLongestString(List<String> suggestions) {
        String longest = "";
        for (String suggestion : suggestions) {
            if (suggestion.length() >= longest.length()) {
                longest = suggestion;
            }
        }
        return longest;
    }

    private void replaceLastTokenWithSuggestion(String suggestion) {
        replaceLastTokenWithSuggestion(suggestion, " ");
    }

    private void replaceLastTokenWithSuggestion(String suggestion, String appendSpace) {
        String commandBoxText = commandTextField.getText();
        String afterReplace = commandBoxText.replace(extractLastKey(), (suggestion + appendSpace));
        commandTextField.setText(afterReplace);
    }

    /**
     * Extracts the last word from the last space
     * @return String from the last space
     */
    private String extractLastKey() {
        String commandBoxText = commandTextField.getText();
        int lastSpaceIndex = getIndexAfterLastSpace(commandBoxText);
        return commandBoxText.substring(lastSpaceIndex);
    }

    /**
     * Returns the last index of a space character of a given input string
     * @return index of the last space, 0 otherwise
     */
    private int getIndexAfterLastSpace(String input) {
        return input.replaceAll("\\s+$", "").lastIndexOf(" ") > 0 ? input.replaceAll("\\s+$", "").lastIndexOf(" ") + 1
                                                                  : 0;
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        commandTextField.getStyleClass().add(ERROR_STYLE_CLASS);
    }

}
