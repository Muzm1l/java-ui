package page.form;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * A row in a vertical form.
 * BoxLayout stretches a child to its maximum height. These blocks refuse
 * that, so each row stays only as tall as its text field or buttons.
 * Extend this when you add a new kind of row.
 */
public class Block extends JPanel {

    private static final long serialVersionUID = 1L;

    public Block() {
        Mono.paint(this);
    }

    public Dimension getMaximumSize() {
        Dimension size = getPreferredSize();
        size.width = Integer.MAX_VALUE;
        return size;
    }

    public Dimension getMinimumSize() {
        Dimension size = getPreferredSize();
        size.width = 0;
        return size;
    }
}
