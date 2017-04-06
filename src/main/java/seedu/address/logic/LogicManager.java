package seedu.address.logic;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.ObservableList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.CheckBoxSelectionChangedEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.dateparser.DateTimeManager;
import seedu.address.logic.dateparser.DateTimeParser;
import seedu.address.logic.parser.Parser;
import seedu.address.model.Model;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;
    private final DateTimeParser dtParser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
        dtParser = new DateTimeManager();
    }

    //@@author A0162877N
    @Override
    public CommandResult execute(String commandText) throws CommandException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        command.setDateParser(dtParser);
        return command.execute();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }

    @Override
    public ObservableList<ReadOnlyTask> getFilteredIncompleteTaskList() {
        return model.getFilteredIncompleteTaskList();
    }

    @Subscribe
    private void handleCheckBoxSelectionChangedEvent(CheckBoxSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        String status = "completed";
        if (!event.getTaskStatus()) {
            status = "incomplete";
        }
        String commandText = "mark " + event.getTaskIndex() + " " + status;
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        command.setDateParser(dtParser);
        try {
            command.execute();
        } catch (CommandException e) {
            e.printStackTrace();
        }
    }
}
