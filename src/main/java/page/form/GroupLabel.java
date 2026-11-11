package page.form;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSeparator;

/**
 * A centred title with a line on each side.
 * Used inside a card for "Group 4", "Group 3", and the same kind of label.
 */
public class GroupLabel extends Block {

    private static final long serialVersionUID = 1L;

    public GroupLabel(String text) {
        super();
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 4, 4, 4));

        GridBagConstraints line = new GridBagConstraints();
        line.fill = GridBagConstraints.HORIZONTAL;
        line.weightx = 1;
        line.insets = new Insets(0, 0, 0, 8);
        add(rule(), line);

        JLabel label = new JLabel(text);
        Mono.paint(label);
        GridBagConstraints middle = new GridBagConstraints();
        middle.insets = new Insets(0, 4, 0, 4);
        add(label, middle);

        line.insets = new Insets(0, 8, 0, 0);
        add(rule(), line);
    }

    private JSeparator rule() {
        JSeparator separator = new JSeparator();
        separator.setForeground(Mono.RULE);
        separator.setPreferredSize(new Dimension(40, 2));
        return separator;
    }
}
