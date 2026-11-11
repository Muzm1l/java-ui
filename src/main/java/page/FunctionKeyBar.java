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
 * Footer of twelve function buttons: F1 through F12.
 *
 * This class only draws the bar. It does not decide what the keys do.
 * Pass in the one listener from the screen that owns the functions.
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

    private final int barHeight;

    /**
     * @param barHeight    height of the whole footer, in pixels
     * @param buttonWidth  width of each button, in pixels
     * @param buttonHeight height of each button, in pixels
     * @param listener     the single listener shared by every button
     */
    public FunctionKeyBar(int barHeight, int buttonWidth, int buttonHeight, ActionListener listener) {
        this.barHeight = barHeight;
        setLayout(new BorderLayout());
        setBackground(new Color(168, 214, 245));
        setOpaque(true);

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        row.setOpaque(false);

        for (int i = 0; i < KEYS.length; i++) {
            String key = KEYS[i];
            JButton button = new JButton(key);
            button.setActionCommand(key);
            button.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            button.setBackground(new Color(186, 224, 248));
            button.setOpaque(true);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            button.addActionListener(listener);
            row.add(button);
            bindKey(KEY_CODES[i], key, listener);
        }

        add(row, BorderLayout.SOUTH);
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
     * The action command is still "F1", "F2", ... so the screen has one path.
     */
    private void bindKey(int keyCode, final String command, final ActionListener listener) {
        String mapKey = "function-" + command;
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
