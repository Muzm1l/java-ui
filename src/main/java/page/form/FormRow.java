package page.form;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * One line of a form: a label on the left, any component on the right.
 * Pass a text field, a combo box, or a small panel of your own.
 */
public class FormRow extends Block {

    private static final long serialVersionUID = 1L;

    private static final int LABEL_WIDTH = 168;

    public FormRow(String labelText, JComponent field) {
        super();
        setLayout(new BorderLayout(8, 0));

        JLabel label = new JLabel(labelText);
        Mono.paint(label);
        label.setPreferredSize(new Dimension(LABEL_WIDTH, 24));
        add(label, BorderLayout.WEST);
        add(field, BorderLayout.CENTER);
    }
}
