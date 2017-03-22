package seedu.address.ui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class LabelCard extends UiPart<Region> {

    private static final String FXML = "LabelListCard.fxml";

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

    private void initLabel(seedu.address.model.label.Label labelToSet, int count) {
        icon.setIcon(FontAwesomeIcon.TAG);
        labelText.setText(labelToSet.getLabelName());
        taskCountText.setText(Integer.toString(count));
    }
}