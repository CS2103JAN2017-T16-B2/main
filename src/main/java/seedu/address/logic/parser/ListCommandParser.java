package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMEINTERVAL_END;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMEINTERVAL_START;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;

public class ListCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FindCommand and returns an FindCommand object for execution.
     */
    public Command parse(String args) {
        try {
            ArgumentTokenizer argsTokenizer = new ArgumentTokenizer(PREFIX_DEADLINE,
                    PREFIX_TIMEINTERVAL_START, PREFIX_TIMEINTERVAL_END);
            argsTokenizer.tokenize(args);
            if (args.trim().contains(PREFIX_TIMEINTERVAL_START.getPrefix())
                    && args.trim().contains(PREFIX_TIMEINTERVAL_END.getPrefix())) {
                return new ListCommand(argsTokenizer.getValue(PREFIX_TIMEINTERVAL_START).get(),
                        argsTokenizer.getValue(PREFIX_TIMEINTERVAL_END).get());
            } else if (args.trim().contains(PREFIX_DEADLINE.getPrefix())) {
                return new ListCommand(argsTokenizer.getValue(PREFIX_DEADLINE).get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
        }

        return new ListCommand();
    }

}
