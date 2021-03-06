package seedu.address.logic.commandhistory;

//@@author A0140042A
/**
 * Represents the command history of the task manager
 */
public interface CommandHistory {

    /**
     * Adds a command which has been executed previously
     * @param command - previously executed
     */
    void addCommand(String command);

    /**
     * Returns a previously executed command
     */
    String previous();

    /**
     * Returns the next command (if any) if the user has previously iterated through his commands before
     */
    String next();
}
