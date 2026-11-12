package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

/**
 * Footer of twelve function buttons.
 *
 * Each button is black. A small blue square on the left shows F1–F12.
 * The words to the right of that square are the operation. This class
 * only draws the bar. It does not decide what the keys do.
 * Pass in the one listener from the screen that owns the functions.
 * setLabels replaces the twelve operation words and the commands.
 * The blue squares stay F1–F12. The physical keys follow the new commands.
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

    /** Blue chip behind the key name. */
    public static final Color KEY_BLUE = new Color(47, 128, 237);

    private final int barHeight;
    private final ActionListener listener;
    private final KeyButton[] buttons = new KeyButton[KEYS.length];

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

        JPanel row = new JPanel(new GridLayout(1, KEYS.length, 4, 0));
        row.setOpaque(false);

        for (int i = 0; i < KEYS.length; i++) {
            KeyButton button = new KeyButton(KEYS[i], buttonWidth, buttonHeight);
            buttons[i] = button;
            row.add(button);
        }

        add(row, BorderLayout.SOUTH);
        setLabels(KEYS, KEYS);
    }

    /**
     * Replace the operation words and the command each button sends.
     * The blue square on the left stays F1–F12.
     * commands and texts must each contain twelve strings, one per slot.
     * commands[i] is what the listener reads. texts[i] is the words beside the square.
     */
    public void setLabels(String[] commands, String[] texts) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setCaption(texts[i]);
            buttons[i].setCommand(commands[i]);
            bindKey(KEY_CODES[i], commands[i]);
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
     * The command is whatever setLabels stored for that slot.
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

    /**
     * One black key. The blue square shows the key name. The label beside it
     * is the operation. A click anywhere on the button sends the command.
     */
    private final class KeyButton extends JPanel {

        private static final long serialVersionUID = 1L;

        private final JLabel caption = new JLabel(" ");
        private String command = "";

        KeyButton(String key, int width, int height) {
            setLayout(new BorderLayout(6, 0));
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

            JLabel badge = new JLabel(key, SwingConstants.CENTER);
            badge.setOpaque(true);
            badge.setBackground(KEY_BLUE);
            badge.setForeground(Color.WHITE);
            badge.setFont(new Font("Dialog", Font.BOLD, 10));
            badge.setPreferredSize(new Dimension(28, 22));

            JPanel badgeSlot = new JPanel(new GridBagLayout());
            badgeSlot.setOpaque(false);
            badgeSlot.add(badge);

            caption.setForeground(Color.WHITE);
            caption.setFont(new Font("Dialog", Font.PLAIN, 13));
            caption.setHorizontalAlignment(SwingConstants.LEFT);

            add(badgeSlot, BorderLayout.WEST);
            add(caption, BorderLayout.CENTER);

            MouseAdapter click = new MouseAdapter() {
                public void mousePressed(MouseEvent event) {
                    listener.actionPerformed(new ActionEvent(
                            FunctionKeyBar.this,
                            ActionEvent.ACTION_PERFORMED,
                            command));
                }
            };
            addMouseListener(click);
            badge.addMouseListener(click);
            badgeSlot.addMouseListener(click);
            caption.addMouseListener(click);
        }

        void setCaption(String text) {
            caption.setText(text);
        }

        void setCommand(String command) {
            this.command = command;
        }
    }
}
