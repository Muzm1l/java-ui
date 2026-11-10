package page;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

import page.form.Block;
import page.form.FormRow;
import page.form.FormStack;
import page.form.Mono;
import page.form.OptionGroup;
import page.form.SectionPanel;

/**
 * One screen built only from the pieces in page.form.
 * Copy this class, change the title, and add your own rows and sections
 * to make the next screen. Do not put new layout code in FunctionPage.
 */
public class WeaponConfigurationPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int PANEL_WIDTH = 560;

    private final JComboBox targetSelection;
    private final JTextField trackField;
    private final JCheckBox trackMark;
    private final JTextArea comments;
    private final JButton cancelButton;
    private final JButton okButton;
    private final OptionGroup engagement;
    private final OptionGroup weapon;
    private final OptionGroup linkedLaunch;
    private final OptionGroup tube;
    private final JComboBox configuration;
    private final Map<String, JTextField> parameters = new LinkedHashMap<String, JTextField>();

    public WeaponConfigurationPanel() {
        super(new BorderLayout());
        LookAndFeel previous = UIManager.getLookAndFeel();
        try {
            /* Classic Metal, so black backgrounds are actually painted black. */
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception ex) {
            /* Keep the look that is already installed. */
        }
        targetSelection = Mono.combo();
        trackField = Mono.text(10);
        trackMark = Mono.check();
        comments = Mono.area(2, 20);
        cancelButton = Mono.button("Cancel");
        okButton = Mono.button("OK");
        engagement = new OptionGroup(new String[] {"Offset Engm", "Real Engm"}, 0, 2);
        weapon = new OptionGroup(new String[] {"ETH/INWAR", "ETH/URE"}, 0, 2);
        linkedLaunch = new OptionGroup(new String[] {"YES", "NO"}, -1, 2);
        tube = new OptionGroup(
                new String[] {"Tube 6", "Tube 5", "Tube 4", "Tube 3", "Tube 2", "Tube 1"},
                -1,
                2);
        configuration = Mono.combo();
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

        FormStack form = new FormStack();
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Mono.FOREGROUND),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JLabel title = new JLabel("Weapon Configuration");
        Mono.paint(title);
        title.setFont(Mono.TITLE_FONT);
        form.add(row(title));
        form.add(Box.createVerticalStrut(10));

        form.add(new FormRow("Target Selection", targetSelection));
        form.add(Box.createVerticalStrut(8));
        form.add(new FormRow("Track", trackLine()));
        form.add(Box.createVerticalStrut(8));
        form.add(new FormRow("Comments", comments));
        form.add(Box.createVerticalStrut(8));
        form.add(row(buttonRow()));
        form.add(Box.createVerticalStrut(8));

        form.add(section("Engagement Configuration", engagement));
        form.add(section("Weapon Selection", weapon));
        form.add(section("Linked Launch", linkedLaunch));
        form.add(section("Tube Select", tube));
        form.add(configurationSection());

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Mono.BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel trackLine() {
        JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        Mono.paint(line);
        line.add(trackField);
        line.add(trackMark);
        return line;
    }

    private JPanel buttonRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        Mono.paint(row);
        row.add(cancelButton);
        row.add(okButton);
        return row;
    }

    private SectionPanel section(String title, JPanel content) {
        SectionPanel section = new SectionPanel(title);
        section.addContent(content);
        return section;
    }

    private SectionPanel configurationSection() {
        SectionPanel section = new SectionPanel("Weapon Configuration Selection");
        section.addContent(row(configuration));

        JLabel parametersTitle = new JLabel("Target Parameters");
        Mono.paint(parametersTitle);
        section.addContent(row(parametersTitle));

        String[] left = new String[] {"Track", "TB", "TR", "TSp", "Lng"};
        String[] right = new String[] {"RB", "DP", "TCo", "Type", "Cat"};
        for (int i = 0; i < left.length; i++) {
            section.addContent(parameterPair(left[i], right[i]));
        }
        return section;
    }

    /** Keeps a single control from being stretched tall by the vertical stack. */
    private Block row(java.awt.Component child) {
        Block line = new Block();
        line.setLayout(new BorderLayout());
        line.add(child, BorderLayout.CENTER);
        return line;
    }

    /** Two short fields on one line: left name, right name. */
    private Block parameterPair(String leftName, String rightName) {
        Block line = new Block();
        line.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        line.setLayout(new java.awt.GridLayout(1, 2, 16, 0));
        line.add(namedField(leftName));
        line.add(namedField(rightName));
        return line;
    }

    private JPanel namedField(String name) {
        JPanel line = new JPanel(new BorderLayout(8, 0));
        Mono.paint(line);
        JLabel label = new JLabel(name);
        Mono.paint(label);
        label.setPreferredSize(new Dimension(48, 22));
        JTextField field = Mono.text(8);
        parameters.put(name, field);
        line.add(label, BorderLayout.WEST);
        line.add(field, BorderLayout.CENTER);
        return line;
    }

    public Dimension getPreferredSize() {
        return new Dimension(PANEL_WIDTH, 640);
    }

    public Dimension getMinimumSize() {
        return new Dimension(PANEL_WIDTH, 200);
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

    public String getEngagement() {
        return engagement.getSelected();
    }

    public String getWeapon() {
        return weapon.getSelected();
    }

    public String getLinkedLaunch() {
        return linkedLaunch.getSelected();
    }

    public String getTube() {
        return tube.getSelected();
    }

    public String getParameter(String name) {
        JTextField field = parameters.get(name);
        if (field == null) {
            return "";
        }
        return field.getText();
    }
}
