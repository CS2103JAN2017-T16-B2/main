package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0162877N
/**
* This class handles the left panel selection change event
*/
public class LeftPanelSelectionChangedEvent extends BaseEvent {

    private final String newSelection;

    public LeftPanelSelectionChangedEvent(String newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public String getNewSelection() {
        return newSelection;
    }

}
