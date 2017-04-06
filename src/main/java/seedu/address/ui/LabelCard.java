package seedu.address.ui;

import java.util.logging.Logger;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.LeftPanelSelectionChangedEvent;

//@@author A0162877N
/**
 * LabelCard controller for each individual label on the Left Panel
 */
public class LabelCard extends UiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    private static final String FXML = "LabelCard.fxml";

    @FXML
    private Label labelText;

    @FXML
    private FontAwesomeIconView icon;

    @FXML
    private Label taskCountText;

    public LabelCard(seedu.address.model.label.Label labels, int count) {
        super(FXML);
        initLabel(labels, count);
    }

    //@@author A0140042A
    private void initLabel(seedu.address.model.label.Label labelToSet, int count) {
        icon.setIcon(FontAwesomeIcon.TAG);
        labelText.setText(labelToSet.getLabelName());
        taskCountText.setText(Integer.toString(count));
    }

    //@@author A0162877N
    /**
     * Event handler for label list selection
     */
    @FXML
    private void handleLabelClicked() {
        logger.fine("Selection in label left list panel changed to : '" + labelText.getText() + "'");
        raise(new LeftPanelSelectionChangedEvent(labelText.getText()));
    }
}
