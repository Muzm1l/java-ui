package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Footer of twelve function buttons.
 *
 * This class only draws the bar. It does not decide what the keys do.
 * Pass in the one listener from the screen that owns the functions.
 * setLabels replaces the twelve captions. The physical F1–F12 keys follow.
 * Change the package line when you paste this into the other project.
 */
public class FunctionKeyBar extends JPanel {

    private static final long serialVersionUID = 1L;

    public static final String[] KEYS = {
        "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"
    };

    private static final int[] KEY_CODES = {
        KeyEvent.VK_F1,
        KeyEvent.VK_F2,
        KeyEvent.VK_F3,
        KeyEvent.VK_F4,
        KeyEvent.VK_F5,
        KeyEvent.VK_F6,
        KeyEvent.VK_F7,
        KeyEvent.VK_F8,
        KeyEvent.VK_F9,
        KeyEvent.VK_F10,
        KeyEvent.VK_F11,
        KeyEvent.VK_F12
    };

    /** Face colour of the first set, F1 through F12. */
    public static final Color FIRST_FACE = new Color(186, 224, 248);

    /** Face colour of the second set, so the swap is visible. */
    public static final Color SECOND_FACE = new Color(255, 214, 153);

    private final int barHeight;
    private final ActionListener listener;
    private final JButton[] buttons = new JButton[KEYS.length];

    /**
     * @param barHeight    height of the whole footer, in pixels
     * @param buttonWidth  width of each button, in pixels
     * @param buttonHeight height of each button, in pixels
     * @param listener     the single listener shared by every button
     */
    public FunctionKeyBar(int barHeight, int buttonWidth, int buttonHeight, ActionListener listener) {
        this.barHeight = barHeight;
        this.listener = listener;
        setLayout(new BorderLayout());
        setBackground(new Color(168, 214, 245));
        setOpaque(true);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        row.setOpaque(false);

        for (int i = 0; i < KEYS.length; i++) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            button.addActionListener(listener);
            buttons[i] = button;
            row.add(button);
        }

        add(row, BorderLayout.SOUTH);
        setLabels(KEYS, FIRST_FACE);
    }

    /**
     * Replace the twelve captions and the command each button sends.
     * The physical F1–F12 keys follow the new commands.
     * labels must contain twelve strings, one per slot.
     */
    public void setLabels(String[] labels, Color face) {
        for (int i = 0; i < buttons.length; i++) {
            String label = labels[i];
            buttons[i].setText(label);
            buttons[i].setActionCommand(label);
            buttons[i].setBackground(face);
            bindKey(KEY_CODES[i], label);
        }
        revalidate();
        repaint();
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = barHeight;
        return size;
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        Dimension size = getPreferredSize();
        size.width = Integer.MAX_VALUE;
        return size;
    }

    /**
     * Pressing the real function key fires the same listener as a click.
     * The command is whatever caption is on that slot right now.
     * The map name is the key code, so a later setLabels call replaces the old action.
     */
    private void bindKey(int keyCode, final String command) {
        String mapKey = "function-" + keyCode;
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(keyCode, 0), mapKey);
        getActionMap().put(mapKey, new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent event) {
                listener.actionPerformed(new ActionEvent(
                        FunctionKeyBar.this,
                        ActionEvent.ACTION_PERFORMED,
                        command));
            }
        });
    }
}
