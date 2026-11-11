package page.form;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/** Cyan section title, such as "Section 1". */
public class Heading extends Block {

    private static final long serialVersionUID = 1L;

    public Heading(String text) {
        super();
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(12, 2, 6, 2));
        JLabel label = new JLabel(text);
        label.setForeground(Mono.ACCENT);
        label.setFont(Mono.HEADING_FONT);
        add(label, BorderLayout.WEST);
    }
}
