package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;


/**
 * The screen. One listener handles every footer button.
 * The window opens on Engagement Parameters. F1 calls create().
 * F4 swaps the footer to a second set of buttons.
 * A second F4 restores the first set. The other methods are empty
 * on purpose: fill those in with the functions from the real project.
 *
 * To drop this into an existing window, copy the listener and the
 * create() method, then add the bar with BorderLayout.SOUTH.
 */
public class FunctionPage extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Commands for the second set. Slot 4 stays "F4" so the same key can switch back. */
    private static final String[] SECOND_KEYS = {
        "B1", "B2", "B3", "F4", "B5", "B6", "B7", "B8", "B9", "B10", "B11", "B12"
    };

    /** Words beside the blue F1–F12 squares on the first set. */
    private static final String[] FIRST_TEXT = {
        "Create", "Op 2", "Op 3", "Swap", "Op 5", "Op 6",
        "Op 7", "Op 8", "Op 9", "Op 10", "Op 11", "Op 12"
    };

    /** Words beside the blue squares after F4. Slot 4 stays "Swap". */
    private static final String[] SECOND_TEXT = {
        "Alt 1", "Alt 2", "Alt 3", "Swap", "Alt 5", "Alt 6",
        "Alt 7", "Alt 8", "Alt 9", "Alt 10", "Alt 11", "Alt 12"
    };

    private final JLabel resultLabel = new JLabel(" ");
    private EngagementParametersPanel parameters;
    private FunctionKeyBar footer;
    private boolean secondSet;

    /**
     * One listener for every function button.
     * The button's action command says which key it was.
     */
    private final ActionListener functionListener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            String key = event.getActionCommand();

            if ("F4".equals(key)) {
                onF4();
            } else if ("F1".equals(key)) {
                create();
            } else if ("F2".equals(key)) {
                onF2();
            } else if ("F3".equals(key)) {
                onF3();
            } else if ("F5".equals(key)) {
                onF5();
            } else if ("F6".equals(key)) {
                onF6();
            } else if ("F7".equals(key)) {
                onF7();
            } else if ("F8".equals(key)) {
                onF8();
            } else if ("F9".equals(key)) {
                onF9();
            } else if ("F10".equals(key)) {
                onF10();
            } else if ("F11".equals(key)) {
                onF11();
            } else if ("F12".equals(key)) {
                onF12();
            } else if ("B1".equals(key)) {
                onB1();
            } else if ("B2".equals(key)) {
                onB2();
            } else if ("B3".equals(key)) {
                onB3();
            } else if ("B5".equals(key)) {
                onB5();
            } else if ("B6".equals(key)) {
                onB6();
            } else if ("B7".equals(key)) {
                onB7();
            } else if ("B8".equals(key)) {
                onB8();
            } else if ("B9".equals(key)) {
                onB9();
            } else if ("B10".equals(key)) {
                onB10();
            } else if ("B11".equals(key)) {
                onB11();
            } else if ("B12".equals(key)) {
                onB12();
            }
        }
    };

    public FunctionPage() {
        super("Java UI");

        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(168, 214, 245));

        JPanel blue = new JPanel(new BorderLayout());
        blue.setBackground(new Color(168, 214, 245));
        resultLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        blue.add(resultLabel, BorderLayout.NORTH);

        LookAndFeel previous = UIManager.getLookAndFeel();
        try {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception ex) {
            /* Keep the look that is already installed. */
        }
        parameters = new EngagementParametersPanel();
        try {
            UIManager.setLookAndFeel(previous);
        } catch (Exception ex) {
            /* Leave the look the form was built with. */
        }

        page.add(blue, BorderLayout.NORTH);
        page.add(parameters, BorderLayout.CENTER);

        /* bar height, button width, button height — change these to fit the other screen */
        footer = new FunctionKeyBar(64, 96, 40, functionListener);
        footer.setLabels(FunctionKeyBar.KEYS, FIRST_TEXT);
        page.add(footer, BorderLayout.SOUTH);

        setContentPane(page);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setLocationRelativeTo(null);
    }

    /** F1. The engagement screen is already open. This reports the four drop-downs. */
    private void create() {
        resultLabel.setText(parameters.getAcousticHeadMode()
                + " / " + parameters.getAttackMode()
                + " / " + parameters.getWakeSensor()
                + " / " + parameters.getGuidanceMode());
    }

    /** F2. */
    private void onF2() {
        resultLabel.setText("F2");
    }

    /** F3. */
    private void onF3() {
        resultLabel.setText("F3");
    }

    /**
     * F4 swaps the footer.
     * The blue squares stay F1–F12. The words beside them change.
     * The first press shows Alt 1 and the rest. The next press restores Create and the rest.
     */
    private void onF4() {
        secondSet = !secondSet;
        if (secondSet) {
            footer.setLabels(SECOND_KEYS, SECOND_TEXT);
            resultLabel.setText("F4 second set");
        } else {
            footer.setLabels(FunctionKeyBar.KEYS, FIRST_TEXT);
            resultLabel.setText("F4 first set");
        }
    }

    /** F5. */
    private void onF5() {
        resultLabel.setText("F5");
    }

    /** F6. */
    private void onF6() {
        resultLabel.setText("F6");
    }

    /** F7. */
    private void onF7() {
        resultLabel.setText("F7");
    }

    /** F8. */
    private void onF8() {
        resultLabel.setText("F8");
    }

    /** F9. */
    private void onF9() {
        resultLabel.setText("F9");
    }

    /** F10. */
    private void onF10() {
        resultLabel.setText("F10");
    }

    /** F11. */
    private void onF11() {
        resultLabel.setText("F11");
    }

    /** F12. */
    private void onF12() {
        resultLabel.setText("F12");
    }

    /** B1. Second-set operation for the first slot. */
    private void onB1() {
        resultLabel.setText("B1");
    }

    /** B2. */
    private void onB2() {
        resultLabel.setText("B2");
    }

    /** B3. */
    private void onB3() {
        resultLabel.setText("B3");
    }

    /** B5. Slot 4 is F4, the swap, in both sets. */
    private void onB5() {
        resultLabel.setText("B5");
    }

    /** B6. */
    private void onB6() {
        resultLabel.setText("B6");
    }

    /** B7. */
    private void onB7() {
        resultLabel.setText("B7");
    }

    /** B8. */
    private void onB8() {
        resultLabel.setText("B8");
    }

    /** B9. */
    private void onB9() {
        resultLabel.setText("B9");
    }

    /** B10. */
    private void onB10() {
        resultLabel.setText("B10");
    }

    /** B11. */
    private void onB11() {
        resultLabel.setText("B11");
    }

    /** B12. */
    private void onB12() {
        resultLabel.setText("B12");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FunctionPage().setVisible(true);
            }
        });
    }
}
