package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0162877N
/**
* This class handles the show all selection change event
*/
public class ShowAllSelectionChangedEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
