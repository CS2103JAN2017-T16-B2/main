package seedu.address.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;

import seedu.address.logic.autocomplete.AutocompleteManager;
import seedu.address.logic.autocomplete.AutocompleteRequest;
import seedu.address.logic.autocomplete.AutocompleteResponse;
import seedu.address.logic.commands.EditBookingCommand;
import seedu.address.logic.commands.EditCommand;

//@@author A0140042A
/**
 * Test class to test AutocompleteManager Class
 */
public class AutocompleteManagerTest {

    @Test
    public void setUp() {
        AutocompleteManager ac = new AutocompleteManager();
        AutocompleteRequest request = new AutocompleteRequest("", 0);
        assertTrue(ac.getSuggestions(request).getSuggestions().
                    containsAll(Arrays.asList(AutocompleteManager.AUTOCOMPLETE_DATA)));
    }

    @Test
    public void autocomplete_TestEquals_ReturnTrue() {
        AutocompleteManager ac1 = new AutocompleteManager("phrase1");
        ac1.addData("phrase2");
        AutocompleteManager ac2 = new AutocompleteManager("phrase1", "phrase2");
        assertTrue(ac1.equals(ac2));
    }

    @Test
    public void autocomplete_TestEqualsNull_ReturnFalse() {
        AutocompleteManager ac1 = new AutocompleteManager("phrase1", "phrase2");
        assertFalse(ac1 == null);
    }

    @Test
    public void autocomplete_TestAutocompleteSuggestion_ReturnTrue() {
        AutocompleteManager ac = new AutocompleteManager();
        AutocompleteRequest request = new AutocompleteRequest("edi", 0);
        LinkedList<String> suggestions = new LinkedList<String>();
        suggestions.add(EditCommand.COMMAND_WORD);
        suggestions.add(EditBookingCommand.COMMAND_WORD);
        AutocompleteResponse response = new AutocompleteResponse("edit", 4, suggestions);
        assertTrue(ac.getSuggestions(request).equals(response));
    }
}
