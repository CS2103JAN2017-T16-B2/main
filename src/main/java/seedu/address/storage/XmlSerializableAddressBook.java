package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.label.Label;
import seedu.address.model.person.Task;
import seedu.address.model.person.ReadOnlyTask;

/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableAddressBook implements ReadOnlyAddressBook {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    @XmlElement
    private List<XmlAdaptedLabel> labels;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAddressBook() {
        tasks = new ArrayList<>();
        labels = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableAddressBook(ReadOnlyAddressBook src) {
        this();
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
        labels.addAll(src.getLabelList().stream().map(XmlAdaptedLabel::new).collect(Collectors.toList()));
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
        final ObservableList<Task> persons = this.tasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(persons);
    }

    @Override
    public ObservableList<Label> getLabelList() {
        final ObservableList<Label> labels = this.labels.stream().map(t -> {
            try {
                return t.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return new UnmodifiableObservableList<>(labels);
    }

}
