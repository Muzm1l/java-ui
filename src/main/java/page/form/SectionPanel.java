package page.form;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A titled block with a triangle. Click the title to open or close it.
 * Call addContent for each row that belongs inside the section.
 */
public class SectionPanel extends Block {

    private static final long serialVersionUID = 1L;

    private final JLabel triangle;
    private final JPanel body;
    private boolean open;

    public SectionPanel(String title) {
        super();
        open = true;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Mono.FOREGROUND));

        triangle = new JLabel(new Mono.TriangleIcon(true));
        JLabel titleLabel = new JLabel(title);
        Mono.paint(titleLabel);

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        Mono.paint(header);
        header.add(triangle);
        header.add(titleLabel);
        header.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                setOpen(!open);
            }
        });

        body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        Mono.paint(body);
        body.setBorder(BorderFactory.createEmptyBorder(2, 22, 8, 4));

        add(header, BorderLayout.NORTH);
        add(body, BorderLayout.CENTER);
    }

    public void addContent(JComponent content) {
        content.setAlignmentX(LEFT_ALIGNMENT);
        body.add(content);
    }

    public void setOpen(boolean open) {
        this.open = open;
        triangle.setIcon(new Mono.TriangleIcon(open));
        body.setVisible(open);
        revalidate();
        repaint();
    }

    public boolean isOpen() {
        return open;
    }
}
