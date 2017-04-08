package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOK_ADD_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOK_CHANGE_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BOOK_REMOVE_DATE;

import java.util.Optional;

import seedu.address.commons.util.DateTimeUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.logic.commands.EditBookingCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author A0162877N
/**
* Parses input arguments and creates a new EditBookingCommand object
*/
public class EditBookingCommandParser {

    private static final String DELIMITER = " ";
    private static final int ARGUMENT_LENGTH = 3;
    private static final int ACTION_INDEX = 1;
    private static final int BOOKING_SLOT_INDEX = 2;
    private static final int INVALID_INDEX = -1;
    private static final int TASK_INDEX = 0;
    private static final int VALID_INDEX = 0;
    private static final String REGEX_INDEX = "^[0-9]*";

    /**
     * Parses the given {@code String} of arguments in the context of the
     * EditLabelCommand and returns an EditLabelCommand object for execution.
     */
    public Command parse(String args) {
        ArgumentTokenizer argsTokenizer = new ArgumentTokenizer();
        argsTokenizer.tokenize(args);
        try {
            String[] arguments = argsTokenizer.getPreamble().get().trim().split(DELIMITER);
            if (arguments.length < ARGUMENT_LENGTH) {
                throw new CommandException(ConfirmCommand.MESSAGE_USAGE);
            }

            if (arguments[TASK_INDEX].matches(REGEX_INDEX)) {
                if (PREFIX_BOOK_ADD_DATE.getPrefix().equalsIgnoreCase(arguments[ACTION_INDEX])) {
                    argsTokenizer = new ArgumentTokenizer(PREFIX_BOOK_ADD_DATE);
                    argsTokenizer.tokenize(args);
                    String[] dates = argsTokenizer.getValue(PREFIX_BOOK_ADD_DATE)
                            .get().trim().split(DateTimeUtil.DATE_DELIMITER);
                    return new EditBookingCommand(tryParseAsIndex(arguments[TASK_INDEX]), dates);
                } else if (PREFIX_BOOK_REMOVE_DATE.getPrefix().equalsIgnoreCase(arguments[ACTION_INDEX])) {
                    if (arguments[BOOKING_SLOT_INDEX].matches(REGEX_INDEX)) {
                        int bookingSlotIndex = tryParseAsIndex(arguments[BOOKING_SLOT_INDEX]);
                        int listIndex = tryParseAsIndex(arguments[TASK_INDEX]);
                        return new EditBookingCommand(listIndex, bookingSlotIndex);
                    }
                } else if (PREFIX_BOOK_CHANGE_DATE.getPrefix().equalsIgnoreCase(arguments[ACTION_INDEX])) {
                    if (arguments[BOOKING_SLOT_INDEX].matches(REGEX_INDEX)) {
                        String[] input = args.split(arguments[ACTION_INDEX] + " " + arguments[BOOKING_SLOT_INDEX]);
                        int bookingSlotIndex = tryParseAsIndex(arguments[2]);
                        int listIndex = tryParseAsIndex(arguments[TASK_INDEX]);
                        if (bookingSlotIndex > VALID_INDEX && listIndex > VALID_INDEX) {
                            return new EditBookingCommand(listIndex,
                                    bookingSlotIndex,
                                    input[ACTION_INDEX]);
                        }
                    }
                } else {
                    return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            EditBookingCommand.MESSAGE_USAGE));
                }
            } else {
                throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EditBookingCommand.MESSAGE_USAGE));
            }
        } catch (Exception e) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditBookingCommand.MESSAGE_USAGE));
        }
        return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EditBookingCommand.MESSAGE_USAGE));
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
