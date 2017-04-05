package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0162877N
/**
 * This class handles the check box selection change event
 */
public class CheckBoxSelectionChangedEvent extends BaseEvent {
    private final int taskIndex;
    private final boolean status;

    public CheckBoxSelectionChangedEvent(int taskIndex, boolean status) {
        this.taskIndex = taskIndex;
        this.status = status;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public int getTaskIndex() {
        return taskIndex;
    }

    public boolean getTaskStatus() {
        return status;
    }
}
