package seedu.address.logic.commands;

import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.InvalidUndoCommandException;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.undo.UndoManager;
import seedu.address.model.datastructure.UndoPair;
import seedu.address.model.label.Label;
import seedu.address.model.task.ReadOnlyTask;

//@@author A0162877N
/**
 * Undo the most recent mutating command from the task manager,
 * returning the data to the previous state
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo-ed previous command successfully!\n"
            + "To Redo past command, hit the up arrow key.";
    public static final String MESSAGE_USAGE = COMMAND_WORD;
    public static final String MESSAGE_UNSUCCESSFUL_UNDO = "No previous command to undo.";

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            if (!UndoManager.getInstance().isEmpty()) {
                UndoPair<ObservableList<ReadOnlyTask>, ObservableList<Label>> pair =
                        UndoManager.getInstance().getUndoData();
                model.undoPrevious(pair.getLeft(), pair.getRight());
                return new CommandResult(String.format(MESSAGE_SUCCESS));
            } else {
                throw new InvalidUndoCommandException(MESSAGE_UNSUCCESSFUL_UNDO);
            }
        } catch (InvalidUndoCommandException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean isMutating() {
        return true;
    }

}
