package page.form;

import java.awt.GridLayout;

import javax.swing.JCheckBox;

/**
 * Check boxes that can be ticked independently.
 * columns is how many sit side by side. Unlike OptionGroup, more than one may be selected.
 */
public class CheckGroup extends Block {

    private static final long serialVersionUID = 1L;

    private final JCheckBox[] boxes;

    public CheckGroup(String[] labels, int columns) {
        super();
        int rows = 1;
        if (columns < 1) {
            columns = 1;
        }
        if (labels.length > 0) {
            rows = ((labels.length - 1) / columns) + 1;
        }
        setLayout(new GridLayout(rows, columns, 12, 4));
        boxes = new JCheckBox[labels.length];
        for (int i = 0; i < labels.length; i++) {
            boxes[i] = Mono.check(labels[i]);
            add(boxes[i]);
        }
    }

    /** The ticked labels, joined with a comma. Empty when none are ticked. */
    public String getSelected() {
        StringBuffer selected = new StringBuffer();
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i].isSelected()) {
                if (selected.length() > 0) {
                    selected.append(", ");
                }
                selected.append(boxes[i].getText());
            }
        }
        return selected.toString();
    }
}
