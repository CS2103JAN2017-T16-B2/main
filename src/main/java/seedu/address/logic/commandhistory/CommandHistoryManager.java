package seedu.address.logic.commandhistory;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

//@@author A0140042A
/**
 * Class to keep track of previous command executions
 * Singleton pattern is used as creating multiple instances of CommandHistory would mess up the history of the execution
 */
public class CommandHistoryManager implements CommandHistory {
    private final Logger logger = LogsCenter.getLogger(CommandHistoryManager.class);
    private static CommandHistoryManager instance;
    private LinkedList<String> history;
    private ListIterator<String> cursor;
    private boolean hasDirection = false;
    private boolean isTraversingBack = true;

    /**
     * Gets the instance of the CommandHistory
     */
    public static CommandHistoryManager getInstance() {
        if (instance == null) {
            instance = new CommandHistoryManager();
        }
        return instance;
    }

    private CommandHistoryManager() {
        history = new LinkedList<String>();
        resetIterator();
    }

    @Override
    public void addCommand(String command) {
        logger.info("Added " + command);
        history.addFirst(command);
        resetIterator();
    }

    @Override
    public String previous() {
        if (cursor.hasNext()) {
            if (hasDirection && !isTraversingBack) {
                cursor.next();
            }

            if (cursor.hasNext()) {
                hasDirection = true;
                isTraversingBack = true;
                String previousCommand = cursor.next();
                logger.info("Previous Command: " + previousCommand);
                return previousCommand;
            }
        }
        logger.info("No previous command");
        return null;
    }

    @Override
    public String next() {
        if (cursor.hasPrevious()) {
            if (hasDirection && isTraversingBack) {
                cursor.previous();
            }
            if (cursor.hasPrevious()) {
                hasDirection = true;
                isTraversingBack = false;
                String nextCommand = cursor.previous();
                logger.info("Next Command: " + nextCommand);
                return nextCommand;
            } else if (hasDirection && isTraversingBack) {
                //Reset the cursor to where it was before
                cursor.next();
            }
        }
        resetIterator();
        logger.info("No next command");
        return null;
    }

    /**
     * Brings cursor back to the front
     */
    public void resetIterator() {
        logger.info("CommandHistoryManager iterator resetted");
        cursor = history.listIterator(0);
        hasDirection = false;
    }
}
