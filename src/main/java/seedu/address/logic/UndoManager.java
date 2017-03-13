package seedu.address.logic;

import java.util.LinkedList;

import javafx.collections.ObservableList;
import seedu.address.model.datastructure.UndoPair;
import seedu.address.model.label.Label;
import seedu.address.model.task.ReadOnlyTask;

public class UndoManager implements Changeable {

    private LinkedList<UndoPair<ObservableList<ReadOnlyTask>, ObservableList<Label>>> storageHistory;

    public UndoManager() {
        storageHistory = new LinkedList<UndoPair<ObservableList<ReadOnlyTask>, ObservableList<Label>>>();
    }

    public void addStorageHistory(ObservableList<ReadOnlyTask> oldTaskState, ObservableList<Label> oldLabelState) {
        this.storageHistory.addLast(
                new UndoPair<ObservableList<ReadOnlyTask>,
                ObservableList<Label>>(oldTaskState, oldLabelState));
    }

    public boolean isEmpty() {
        return storageHistory.isEmpty();
    }

    public void clear() {
        storageHistory.clear();
    }

    @Override
    public UndoPair<ObservableList<ReadOnlyTask>, ObservableList<Label>> getUndoData() {
        return storageHistory.removeLast();
    }

}