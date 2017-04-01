package seedu.address.model;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.label.Label;

//@@author A0140042A
/**
 * Test class to test Label class
 */
public class LabelTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void label_TestToString_ReturnTrue() throws IllegalValueException {
        String tag = "label1";
        String tagToString = "[" + tag + "]";
        Label label = new Label(tag);
        assertTrue(label.toString().equals(tagToString));
    }

    @Test
    public void label_TestLabelNull_ThrowAssertionError() throws IllegalValueException {
        exception.expect(AssertionError.class);
        new Label(null);
    }

    @Test
    public void label_TestValidLabels_ReturnTrue() throws IllegalValueException {
        new Label("label1");
        new Label("aVeryLongLabel");
        new Label("abc123");
    }
}
