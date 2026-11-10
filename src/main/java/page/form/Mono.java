package page.form;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Shared black-and-white look.
 * A new screen calls these methods so it matches this one
 * without copying colour and border lines into every class.
 *
 * JComboBox is not generic here. Generics on JComboBox arrived in Java 7.
 */
public final class Mono {

    public static final Color BACKGROUND = Color.BLACK;
    public static final Color FOREGROUND = Color.WHITE;
    public static final Font FONT = new Font("Dialog", Font.PLAIN, 13);
    public static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 16);

    private static final Border LINE = BorderFactory.createLineBorder(FOREGROUND);

    private Mono() {
    }

    public static void paint(JComponent component) {
        component.setOpaque(true);
        component.setBackground(BACKGROUND);
        component.setForeground(FOREGROUND);
        component.setFont(FONT);
    }

    public static JTextField text(int columns) {
        JTextField field = new JTextField(columns);
        paint(field);
        field.setCaretColor(FOREGROUND);
        field.setBorder(LINE);
        return field;
    }

    public static JTextArea area(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        paint(area);
        area.setCaretColor(FOREGROUND);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(LINE);
        return area;
    }

    /** Java 6: use the raw JComboBox, not JComboBox&lt;String&gt;. */
    public static JComboBox combo() {
        JComboBox box = new JComboBox();
        box.addItem("");
        paint(box);
        box.setBorder(LINE);
        box.setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean selected, boolean focus) {
                Component row = super.getListCellRendererComponent(list, value, index, selected, focus);
                if (row instanceof JComponent) {
                    Mono.paint((JComponent) row);
                }
                return row;
            }
        });
        return box;
    }

    public static JButton button(String text) {
        JButton button = new JButton(text);
        paint(button);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                LINE,
                BorderFactory.createEmptyBorder(4, 14, 4, 14)));
        return button;
    }

    public static JCheckBox check() {
        JCheckBox box = new JCheckBox();
        paint(box);
        box.setFocusPainted(false);
        box.setIcon(new MarkIcon(false, false));
        box.setSelectedIcon(new MarkIcon(false, true));
        return box;
    }

    public static JRadioButton radio(String text) {
        JRadioButton radio = new JRadioButton(text);
        paint(radio);
        radio.setFocusPainted(false);
        radio.setIcon(new MarkIcon(true, false));
        radio.setSelectedIcon(new MarkIcon(true, true));
        return radio;
    }

    /**
     * White circle for a radio, white square for a check box.
     * Drawn here so the mark stays white on Ubuntu's own look and feel.
     */
    public static final class MarkIcon implements Icon {

        private final boolean radio;
        private final boolean selected;
        private final int size = 14;

        public MarkIcon(boolean radio, boolean selected) {
            this.radio = radio;
            this.selected = selected;
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(FOREGROUND);
            if (radio) {
                g.drawOval(x, y, size - 1, size - 1);
                if (selected) {
                    g.fillOval(x + 3, y + 3, size - 7, size - 7);
                }
            } else {
                g.drawRect(x, y, size - 1, size - 1);
                if (selected) {
                    g.fillRect(x + 3, y + 3, size - 7, size - 7);
                }
            }
            g.dispose();
        }
    }

    /** Small white triangle used by a section header. Pointing down means open. */
    public static final class TriangleIcon implements Icon {

        private final boolean open;
        private final int size = 10;

        public TriangleIcon(boolean open) {
            this.open = open;
        }

        public int getIconWidth() {
            return size;
        }

        public int getIconHeight() {
            return size;
        }

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(FOREGROUND);
            int[] xs;
            int[] ys;
            if (open) {
                xs = new int[] {x, x + size, x + (size / 2)};
                ys = new int[] {y + 2, y + 2, y + size - 1};
            } else {
                xs = new int[] {x + 2, x + 2, x + size - 1};
                ys = new int[] {y, y + size, y + (size / 2)};
            }
            g.fillPolygon(xs, ys, 3);
            g.dispose();
        }
    }
}
