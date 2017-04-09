package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.NoSuchElementException;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author A0162877N
/**
* Parses input arguments and creates a new ConfirmCommand object
*/
public class ConfirmCommandParser extends Parser {

    private static final String DELIMITER = " ";
    private static final int ARGUMENT_LENGTH = 2;
    private static final int BOOKING_SLOT_INDEX = 1;
    private static final int INVALID_INDEX = -1;
    private static final int TASK_INDEX = 0;
    private static final int VALID_INDEX = 0;
    private static final String REGEX_INDEX = "^[0-9]*";

    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer();
        argsTokenizer.tokenize(args);
        try {
            String[] arguments = argsTokenizer.getPreamble().get().trim().split(DELIMITER);
            if (arguments.length != ARGUMENT_LENGTH) {
                throw new CommandException(ConfirmCommand.MESSAGE_USAGE);
            }

            if (arguments[TASK_INDEX].matches(REGEX_INDEX)
                    && arguments[BOOKING_SLOT_INDEX].matches(REGEX_INDEX)) {
                int filteredTaskListIndex = tryParseAsIndex(arguments[TASK_INDEX]);
                int bookingSlotIndex = tryParseAsIndex(arguments[BOOKING_SLOT_INDEX]);
                if (filteredTaskListIndex > VALID_INDEX && bookingSlotIndex > VALID_INDEX) {
                    return new ConfirmCommand(filteredTaskListIndex, bookingSlotIndex);
                } else {
                    return new IncorrectCommand(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
                }
            } else {
                return new IncorrectCommand(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
            }
        } catch (NoSuchElementException nsee) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ConfirmCommand.MESSAGE_USAGE));
        } catch (Exception e) {
            return new IncorrectCommand(Messages.MESSAGE_INVALID_TASKS_DISPLAYED_INDEX);
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
}
