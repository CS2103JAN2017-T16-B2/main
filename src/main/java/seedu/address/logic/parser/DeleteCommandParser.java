package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Optional;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteLabelCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author A0140042A
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     */
    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(CliSyntax.PREFIX_LABEL);
        argsTokenizer.tokenize(args);

        try {
            if (!argsTokenizer.getPreamble().isPresent() &&
                    !argsTokenizer.getAllValues(CliSyntax.PREFIX_LABEL).isPresent()) {
                throw new CommandException(DeleteCommand.MESSAGE_USAGE);
            }

            Optional<String> parameter = argsTokenizer.getValue(CliSyntax.PREFIX_LABEL);
            if (parameter.isPresent()) {
                return new DeleteLabelCommand(parameter.get());
            } else {
                return tryParseAsIndex(args);
            }
        } catch (Exception e) {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Try parsing arguments as index
     */
    private Command tryParseAsIndex(String args) {
        Optional<Integer> index = ParserUtil.parseIndex(args);
        if (index.isPresent()) {
            return new DeleteCommand(index.get());
        } else {
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

}
