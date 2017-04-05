package guitests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;

//@@author A0140042A
/**
 * A test class to iterate through the commands using the GUI itself
 */
public class CommandBoxIterateCommandTest extends TaskManagerGuiTest {
    @Test
    public void commandBox_IterateCommandTest_Success() {
        commandBox.runCommand("add task1");
        commandBox.runCommand("add task2");
        commandBox.pressUp();
        assertEquals(AddCommand.COMMAND_WORD + " task2", commandBox.getCommandInput());
        commandBox.pressUp();
        assertEquals(AddCommand.COMMAND_WORD + " task1", commandBox.getCommandInput());
        commandBox.pressDown();
        assertEquals(AddCommand.COMMAND_WORD + " task2", commandBox.getCommandInput());
    }
}
