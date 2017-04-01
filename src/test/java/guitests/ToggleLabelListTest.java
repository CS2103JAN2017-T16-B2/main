package guitests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

//@@author A0140042A
/**
 * Test class to check if label list is being toggled
 */
public class ToggleLabelListTest extends TaskManagerGuiTest {

    @Test
    public void gui_TestToggleLabelListView_ReturnFalseThenTrue() {
        leftPanel.clickOnLabelHeader();
        assertFalse(leftPanel.isVisible());
        leftPanel.clickOnLabelHeader();
        assertTrue(leftPanel.isVisible());
    }
}
