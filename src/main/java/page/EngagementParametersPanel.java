package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import page.form.Block;
import page.form.FormRow;
import page.form.FormStack;
import page.form.GroupLabel;
import page.form.Mono;
import page.form.OptionGroup;

/**
 * Engagement Parameters. This is the screen shown when the window opens.
 * Acoustic head mode, Attack mode, Wake Sensor, and Guidance Mode are
 * drop-downs. Their lists are the choices from the option popup.
 */
public class EngagementParametersPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JComboBox engm;
    private final JTextField tube;
    private final JTextField engmType;
    private final OptionGroup weapon;
    private final JTextField configName;
    private final JComboBox acousticHeadMode;
    private final JComboBox attackMode;
    private final JComboBox wakeSensor;
    private final JComboBox guidanceMode;
    private final JComboBox type;

    public EngagementParametersPanel() {
        super(new BorderLayout());
        Mono.paint(this);

        engm = Mono.combo(new String[] {""});
        tube = Mono.field(8);
        engmType = Mono.field(16);
        weapon = new OptionGroup(new String[] {"EHWT WAR", "EHWT EXE"}, 0, 2);
        configName = Mono.field(16);
        acousticHeadMode = Mono.combo(new String[] {"Passive", "Simultaneous", "Wake"});
        attackMode = Mono.combo(new String[] {"Sub Surface", "Surface"});
        wakeSensor = Mono.combo(new String[] {"OFF", "ON"});
        guidanceMode = Mono.combo(new String[] {"Bg Rider Out", "CCM", "TCCM"});
        type = Mono.combo(new String[] {""});

        add(scroller(form()), BorderLayout.CENTER);
    }

    private FormStack form() {
        FormStack form = new FormStack();
        form.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        form.add(banner("ENGAGEMENT PARAMETERS"));
        form.add(pad(pair(labeled("ENGM", engm), labeled("Tube", tube))));
        form.add(pad(new FormRow("ENGM Type", engmType)));

        form.add(banner("WEAPON CONFIGURATION"));
        form.add(pad(weapon));
        form.add(pad(new FormRow("CONFIG NAME", configName)));

        form.add(banner("PARAMETER CONFIGURATION"));
        form.add(new GroupLabel("TORPEDO TRAJECTORY"));
        form.add(pad(new FormRow("Initial Speed", Mono.field(12))));
        form.add(pad(new FormRow("Ordered Speed", Mono.field(12))));
        form.add(pad(new FormRow("Ordered Depth", Mono.field(12))));
        form.add(pad(new FormRow("Search Speed", Mono.field(12))));

        form.add(new GroupLabel("SAFETY PARAMETERS"));
        form.add(pad(pair(labeled("Enab dist (m)", Mono.field(8)), labeled("Saf dist (m)", Mono.field(8)))));
        form.add(pad(pair(labeled("UDL (M)", Mono.field(8)), labeled("LDL (m)", Mono.field(8)))));
        form.add(pad(new FormRow("MAX DIST", Mono.field(12))));

        form.add(new GroupLabel("CAL ENGM PARAMETERS"));
        form.add(pad(pair(labeled("WCse", Mono.field(8)), labeled("Prog CSe", Mono.field(8)))));
        form.add(pad(pair(labeled("ITA", Mono.field(8)), labeled("Sonar Range", Mono.field(8)))));
        form.add(pad(pair(labeled("Wake EAl", Mono.field(8)), labeled("WAKE EAL", Mono.field(8)))));
        form.add(pad(pair(labeled("RT", Mono.field(8)), labeled("MAX. RT", Mono.field(8)))));

        form.add(new GroupLabel("ATTACK PARAMETERS"));
        form.add(pad(new FormRow("Acoustic head mode", acousticHeadMode)));
        form.add(pad(new FormRow("Attack mode", attackMode)));
        form.add(pad(new FormRow("Wake Sensor", wakeSensor)));
        form.add(pad(new FormRow("Guidance Mode", guidanceMode)));

        form.add(banner("TARGET PARAMETERS"));
        form.add(pad(pair(labeled("TRACK", Mono.field(8)), labeled("RB", Mono.field(8)))));
        form.add(pad(pair(labeled("TB", Mono.field(8)), labeled("Dp", Mono.field(8)))));
        form.add(pad(pair(labeled("TR", Mono.field(8)), labeled("TCo", Mono.field(8)))));
        form.add(pad(pair(labeled("TSp", Mono.field(8)), labeled("Type", type))));
        form.add(pad(pair(labeled("ATB", Mono.field(8)), labeled("Lat", Mono.field(8)))));
        form.add(pad(pair(labeled("Lng", Mono.field(8)), empty())));
        return form;
    }

    public String getAcousticHeadMode() {
        return selected(acousticHeadMode);
    }

    public String getAttackMode() {
        return selected(attackMode);
    }

    public String getWakeSensor() {
        return selected(wakeSensor);
    }

    public String getGuidanceMode() {
        return selected(guidanceMode);
    }

    private static String selected(JComboBox box) {
        Object item = box.getSelectedItem();
        if (item == null) {
            return "";
        }
        return item.toString();
    }

    private static JScrollPane scroller(FormStack form) {
        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Mono.BACKGROUND);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    /** Cyan bar with the section name centred in black. */
    private static Block banner(String title) {
        Block bar = new Block();
        bar.setLayout(new BorderLayout());
        bar.setBackground(Mono.ACCENT);
        bar.setOpaque(true);
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Dialog", Font.BOLD, 14));
        label.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        bar.add(label, BorderLayout.CENTER);
        return bar;
    }

    private static Block pad(JComponent child) {
        Block line = new Block();
        line.setLayout(new BorderLayout());
        line.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        line.add(child, BorderLayout.CENTER);
        return line;
    }

    private static Block pair(JPanel left, JPanel right) {
        Block line = new Block();
        line.setLayout(new GridLayout(1, 2, 28, 0));
        line.add(left);
        line.add(right);
        return line;
    }

    private static JPanel labeled(String name, JComponent field) {
        JPanel line = new JPanel(new BorderLayout(8, 0));
        Mono.paint(line);
        JLabel label = new JLabel(name);
        Mono.paint(label);
        label.setPreferredSize(new Dimension(110, 24));
        line.add(label, BorderLayout.WEST);
        line.add(field, BorderLayout.CENTER);
        return line;
    }

    private static JPanel empty() {
        JPanel blank = new JPanel();
        Mono.paint(blank);
        return blank;
    }
}
