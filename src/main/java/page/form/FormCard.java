package page.form;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A bordered box that holds a vertical list of rows.
 * One card is one group on the form. Add another card for the next group.
 */
public class FormCard extends Block {

    private static final long serialVersionUID = 1L;

    private final JPanel body;

    public FormCard() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Mono.RULE),
                BorderFactory.createEmptyBorder(6, 8, 8, 8)));
        body = this;
    }

    public void addRow(JComponent row) {
        row.setAlignmentX(LEFT_ALIGNMENT);
        body.add(row);
    }
}
