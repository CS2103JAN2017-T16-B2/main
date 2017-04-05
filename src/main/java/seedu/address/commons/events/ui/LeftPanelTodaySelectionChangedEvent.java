package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0162877N
/**
* This class handles the left panel today selection event
*/
public class LeftPanelTodaySelectionChangedEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
