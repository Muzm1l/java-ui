package page.form;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * Vertical pile of form rows.
 * Tracks the width of a scroll pane, and scrolls up and down when the
 * pile is taller than the window. Use one of these as the body of a screen.
 */
public class FormStack extends JPanel implements Scrollable {

    private static final long serialVersionUID = 1L;

    public FormStack() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Mono.paint(this);
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
        return 16;
    }

    public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
        return Math.max(16, visible.height - 16);
    }

    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
