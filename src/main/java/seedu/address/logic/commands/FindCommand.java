package seedu.address.logic.commands;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Date;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalDateTimeValueException;
import seedu.address.commons.util.DateTimeUtil;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author A0162877N
/**
 * Finds and lists all tasks in task manager whose name contains any of the
 * argument keywords. Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks whose names contain any of "
            + "the specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]... | [(by DEADLINE) | (from STARTDATE to ENDDATE)]\n"
            + "Example: " + COMMAND_WORD + " meet alice";

    private final Set<String> keywords;
    private final String startDate;
    private final String endDate;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
        this.startDate = null;
        this.endDate = null;
    }

    public FindCommand(String endDate) throws IllegalDateTimeValueException {
        this.startDate = "";
        this.endDate = endDate;
        this.keywords = null;
    }

    public FindCommand(String startDate, String endDate) throws IllegalDateTimeValueException {
        this.startDate = startDate;
        this.endDate = endDate;
        this.keywords = null;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public boolean isParsableDate(String dateTime) {
        dtParser.parse(dateTime);
        return !dtParser.parse(dateTime).isEmpty();
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            executeFindCommandLogic();
            return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
        } catch (IllegalDateTimeValueException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
    }

    public void executeFindCommandLogic() throws IllegalDateTimeValueException {
        if (keywords == null) {
            if ("".equals(startDate) && isParsableDate(endDate)) { // by prefix is used by user
                executeFindWithEndDate();
            } else if (isParsableDate(startDate) && isParsableDate(endDate)) { // from and to prefix is used by user
                executeFindWithDateRange();
            } else {
                throw new IllegalDateTimeValueException();
            }
        } else { // no dates specified by user only search for keywords
            model.updateFilteredTaskList(keywords);
        }
    }

    public void executeFindWithDateRange() {
        Date start = null;
        Date end = null;
        if (dtParser.parse(endDate).get(DateTimeUtil.DATE_INDEX).isTimeInferred()) {
            end = dtParser.parse(endDate + " " + DateTimeUtil.DEFAULT_ENDTIME)
                    .get(DateTimeUtil.DATE_INDEX)
                    .getDates()
                    .get(DateTimeUtil.DATE_INDEX);
        } else {
            end = dtParser.parse(endDate).get(DateTimeUtil.DATE_INDEX)
                    .getDates()
                    .get(DateTimeUtil.DATE_INDEX);
        }

        if (dtParser.parse(startDate).get(DateTimeUtil.DATE_INDEX).isTimeInferred()) {
            start = dtParser.parse(startDate + " " + DateTimeUtil.DEFAULT_STARTTIME)
                    .get(DateTimeUtil.DATE_INDEX)
                    .getDates()
                    .get(DateTimeUtil.DATE_INDEX);
        } else {
            start = dtParser.parse(startDate).get(DateTimeUtil.DATE_INDEX)
                    .getDates()
                    .get(DateTimeUtil.DATE_INDEX);
        }

        executeFindCommand(start, end);
    }

    public void executeFindWithEndDate() {
        Date start = null;
        Date end = null;
        if (dtParser.parse(endDate).get(DateTimeUtil.DATE_INDEX).isTimeInferred()) {
            end = dtParser.parse(endDate + " " + DateTimeUtil.DEFAULT_ENDTIME)
                    .get(DateTimeUtil.DATE_INDEX)
                    .getDates()
                    .get(DateTimeUtil.DATE_INDEX);
        } else {
            end = dtParser.parse(endDate).get(DateTimeUtil.DATE_INDEX)
                    .getDates()
                    .get(DateTimeUtil.DATE_INDEX);
        }

        start = dtParser.parse("today " + DateTimeUtil.DEFAULT_STARTTIME)
                .get(DateTimeUtil.DATE_INDEX)
                .getDates()
                .get(DateTimeUtil.DATE_INDEX);

        executeFindCommand(start, end);
    }

    /**
     * Compare the start and end date and execute the filter method
     */
    private void executeFindCommand(Date start, Date end) {
        if (end.before(start)) {
            model.updateFilteredTaskList(end, start);
        } else {
            model.updateFilteredTaskList(start, end);
        }
    }

    @Override
    public boolean isMutating() {
        return false;
    }

}
