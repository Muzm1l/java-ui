package page;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import page.form.Block;
import page.form.CheckGroup;
import page.form.FormCard;
import page.form.FormRow;
import page.form.FormStack;
import page.form.GroupLabel;
import page.form.Heading;
import page.form.HintArea;
import page.form.HintField;
import page.form.Mono;
import page.form.OptionGroup;
import page.model.Engagement;

/**
 * Create Engagement form, built from the pieces in page.form.
 * Copy this class and change build() when you need a similar screen.
 */
public class WeaponConfigurationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int PANEL_WIDTH = 620;

    private final HintField field1;
    private final HintField field2;
    private final OptionGroup group1;
    private final CheckGroup group2;
    private final OptionGroup group3;
    private final JTextField field3;
    private final OptionGroup group4;
    private final JComboBox group5;
    private final JComboBox field4;
    private final JComboBox field5;
    private final HintArea field6;
    private final JButton cancelButton;
    private final JButton okButton;
    private final JButton closeButton;
    private final Map<String, JTextField> parameters = new LinkedHashMap<String, JTextField>();

    public WeaponConfigurationPanel() {
        super(new BorderLayout());
        LookAndFeel previous = UIManager.getLookAndFeel();
        try {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception ex) {
            /* Keep the look that is already installed. */
        }
        field1 = new HintField("Enter field 1", 18);
        field2 = new HintField("Enter field 2", 18);
        group1 = new OptionGroup(new String[] {"Choice A", "Choice B"}, 0, 2);
        group2 = new CheckGroup(new String[] {"Choice C", "Choice D"}, 2);
        group3 = new OptionGroup(new String[] {"Choice E", "Choice F"}, 1, 2);
        field3 = Mono.field(10);
        field3.setText("0");
        group4 = new OptionGroup(
                new String[] {"Choice 6", "Choice 5", "Choice 4", "Choice 3", "Choice 2", "Choice 1"},
                5,
                2);
        group5 = Mono.combo(new String[] {"Choice G"});
        field4 = Mono.combo(new String[] {"Choice H"});
        field5 = Mono.combo(new String[] {"Choice I"});
        field6 = new HintArea("Enter field 6", 4, 20);
        cancelButton = Mono.button("Cancel");
        okButton = Mono.accentButton("OK");
        closeButton = closeControl();
        try {
            build();
        } finally {
            try {
                UIManager.setLookAndFeel(previous);
            } catch (Exception ex) {
                /* Leave the look that build() just used. */
            }
        }
    }

    private void build() {
        Mono.paint(this);
        add(titleBar(), BorderLayout.NORTH);
        add(scroller(form()), BorderLayout.CENTER);
        add(buttonBar(), BorderLayout.SOUTH);
    }

    private JPanel titleBar() {
        JPanel bar = new JPanel(new BorderLayout());
        Mono.paint(bar);
        bar.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 8));

        JLabel title = new JLabel("Create Engagement");
        title.setForeground(Mono.FOREGROUND);
        title.setFont(Mono.TITLE_FONT);
        bar.add(title, BorderLayout.WEST);
        bar.add(closeButton, BorderLayout.EAST);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        return bar;
    }

    private FormStack form() {
        FormStack form = new FormStack();
        form.setBorder(BorderFactory.createEmptyBorder(4, 12, 12, 12));

        form.add(new FormRow("Field 1 *", field1));
        form.add(Box.createVerticalStrut(8));
        form.add(new FormRow("Field 2 *", field2));
        form.add(new Heading("Section 1"));
        form.add(section1Card());
        form.add(new Heading("Section 2"));
        form.add(section2Card());
        form.add(new Heading("Section 3"));
        form.add(section3Card());
        form.add(new Heading("Section 4"));
        form.add(section4Card());
        return form;
    }

    private FormCard section1Card() {
        FormCard card = new FormCard();
        card.addRow(new GroupLabel("Group 1"));
        card.addRow(group1);
        card.addRow(new GroupLabel("Group 2"));
        card.addRow(group2);
        card.addRow(new GroupLabel("Group 3"));
        card.addRow(group3);
        card.addRow(field3Row());
        card.addRow(new GroupLabel("Group 4"));
        card.addRow(group4);
        card.addRow(new GroupLabel("Group 5"));
        card.addRow(row(group5));
        return card;
    }

    private Block field3Row() {
        JPanel line = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
        Mono.paint(line);
        JLabel label = new JLabel("Field 3");
        Mono.paint(label);
        JButton smallOk = Mono.button("OK");
        line.add(label);
        line.add(field3);
        line.add(smallOk);
        return row(line);
    }

    private FormCard section2Card() {
        FormCard card = new FormCard();
        card.addRow(parameterPair("P1", "305.00", "P2", "305.00"));
        card.addRow(parameterPair("P3", "2.87", "P4", "0.00"));
        card.addRow(parameterPair("P5", "5.00", "P6", "125.00"));
        card.addRow(parameterPair("P7", "0.00", field4Row()));
        card.addRow(parameterPair("P8", "0.00", "P9", "0.00"));
        return card;
    }

    private FormCard section3Card() {
        FormCard card = new FormCard();
        card.addRow(new FormRow("Field 5 *", field5));
        return card;
    }

    private FormCard section4Card() {
        FormCard card = new FormCard();
        card.addRow(row(field6));
        return card;
    }

    private JScrollPane scroller(FormStack form) {
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Mono.BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel buttonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        Mono.paint(bar);
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Mono.RULE));
        bar.add(cancelButton);
        bar.add(okButton);
        return bar;
    }

    private JButton closeControl() {
        JButton close = new JButton("\u00D7");
        Mono.paint(close);
        close.setFont(Mono.TITLE_FONT);
        close.setFocusPainted(false);
        close.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        close.setContentAreaFilled(false);
        return close;
    }

    private Block row(java.awt.Component child) {
        Block line = new Block();
        line.setLayout(new BorderLayout());
        line.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        line.add(child, BorderLayout.CENTER);
        return line;
    }

    private Block parameterPair(String leftName, String leftValue, String rightName, String rightValue) {
        return pair(namedField(leftName, leftValue), namedField(rightName, rightValue));
    }

    private Block parameterPair(String leftName, String leftValue, JPanel right) {
        return pair(namedField(leftName, leftValue), right);
    }

    private Block pair(JPanel left, JPanel right) {
        Block line = new Block();
        line.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        line.setLayout(new java.awt.GridLayout(1, 2, 16, 0));
        line.add(left);
        line.add(right);
        return line;
    }

    private JPanel namedField(String name, String value) {
        JPanel line = new JPanel(new BorderLayout(8, 0));
        Mono.paint(line);
        JLabel label = new JLabel(name);
        Mono.paint(label);
        label.setPreferredSize(new Dimension(44, 24));
        JTextField field = Mono.field(8);
        field.setText(value);
        parameters.put(name, field);
        line.add(label, BorderLayout.WEST);
        line.add(field, BorderLayout.CENTER);
        return line;
    }

    private JPanel field4Row() {
        JPanel line = new JPanel(new BorderLayout(8, 0));
        Mono.paint(line);
        JLabel label = new JLabel("Field 4");
        Mono.paint(label);
        label.setPreferredSize(new Dimension(44, 24));
        line.add(label, BorderLayout.WEST);
        line.add(field4, BorderLayout.CENTER);
        return line;
    }

    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, 700);
    }

    public Dimension getMinimumSize() {
        return new Dimension(PANEL_WIDTH, 240);
    }

    public Dimension getMaximumSize() {
        Dimension size = getPreferredSize();
        size.height = Integer.MAX_VALUE;
        return size;
    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public String getField1() {
        return field1.getValue();
    }

    public String getField2() {
        return field2.getValue();
    }

    public String getGroup1() {
        return group1.getSelected();
    }

    public String getGroup2() {
        return group2.getSelected();
    }

    public String getGroup3() {
        return group3.getSelected();
    }

    public String getGroup4() {
        return group4.getSelected();
    }

    public String getField6() {
        return field6.getValue();
    }

    public String getParameter(String name) {
        JTextField field = parameters.get(name);
        if (field == null) {
            return "";
        }
        return field.getText();
    }

    /**
     * Copy every control into one plain object.
     * Call this from the OK listener, then pass the object to the backend.
     */
    public Engagement readForm() {
        Engagement engagement = new Engagement();
        engagement.setField1(field1.getValue());
        engagement.setField2(field2.getValue());
        engagement.setGroup1(group1.getSelected());
        engagement.setGroup2(group2.getSelected());
        engagement.setGroup3(group3.getSelected());
        engagement.setField3(field3.getText());
        engagement.setGroup4(group4.getSelected());
        engagement.setGroup5(selected(group5));
        engagement.setP1(getParameter("P1"));
        engagement.setP2(getParameter("P2"));
        engagement.setP3(getParameter("P3"));
        engagement.setP4(getParameter("P4"));
        engagement.setP5(getParameter("P5"));
        engagement.setP6(getParameter("P6"));
        engagement.setP7(getParameter("P7"));
        engagement.setField4(selected(field4));
        engagement.setP8(getParameter("P8"));
        engagement.setP9(getParameter("P9"));
        engagement.setField5(selected(field5));
        engagement.setField6(field6.getValue());
        return engagement;
    }

    private String selected(JComboBox box) {
        Object item = box.getSelectedItem();
        if (item == null) {
            return "";
        }
        return item.toString();
    }
}
