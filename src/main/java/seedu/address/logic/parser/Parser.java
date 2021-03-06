package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.BookCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ConfirmCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditBookingCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IncorrectCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LoadCommand;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.SaveAsCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.dateparser.DateTimeManager;
import seedu.address.logic.dateparser.DateTimeParser;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    protected DateTimeParser dtParser;
    public static final String DEFAULT_STARTTIME = "00:00:00";
    public static final String DEFAULT_ENDTIME = "23:59:59";

    public Parser() {
        initialiseDateParser();
    }

    public void initialiseDateParser() {
        dtParser = new DateTimeManager();
    }

    //@@author A0162877N
    public boolean isDateParseable(String input) {
        return !dtParser.parse(input).isEmpty();
    }

    //@@author A0105287E
    /**
     * Returns whether the date contained in the first String (@code startDate) occurs before
     * the date contained in the second String {@code endDate}
     *
     */
    public boolean isBefore(String startDate, String endDate) {
        Date start = null;
        Date end = null;
        if (dtParser.parse(endDate).get(0).isTimeInferred()) {
            end = dtParser.parse(endDate + " " + DEFAULT_ENDTIME).get(0).getDates().get(0);
        } else {
            end = dtParser.parse(endDate).get(0).getDates().get(0);
        }

        if (dtParser.parse(startDate).get(0).isTimeInferred()) {
            start = dtParser.parse(startDate + " " + DEFAULT_STARTTIME).get(0).getDates().get(0);
        } else {
            start = dtParser.parse(startDate).get(0).getDates().get(0);
        }

        if (end.before(start)) {
            return false;
        } else {
            return true;
        }
    }
    //@@author

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {
        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case BookCommand.COMMAND_WORD:
            return new BookCommandParser().parse(arguments);

        case EditBookingCommand.COMMAND_WORD:
            return new EditBookingCommandParser().parse(arguments);

        case ConfirmCommand.COMMAND_WORD:
            return new ConfirmCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case MarkCommand.COMMAND_WORD:
            return new MarkCommandParser().parse(arguments);

        case LoadCommand.COMMAND_WORD:
            return new LoadCommand(arguments);

        case SaveAsCommand.COMMAND_WORD:
            return new SaveAsCommand(arguments);


        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
