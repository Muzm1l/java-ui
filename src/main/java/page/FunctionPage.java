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
import javax.swing.SwingUtilities;

/**
 * The screen. One listener handles all ten footer buttons.
 * F1 calls create(). The other methods are empty on purpose:
 * fill those in with the functions from the real project.
 *
 * To drop this into an existing window, copy the listener and the
 * create() method, then add the bar with BorderLayout.SOUTH.
 */
public class FunctionPage extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JLabel resultLabel = new JLabel(" ");

    /**
     * One listener for every function button.
     * The button's action command says which key it was.
     */
    private final ActionListener functionListener = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            String key = event.getActionCommand();

            if ("F1".equals(key)) {
                create();
            } else if ("F2".equals(key)) {
                onF2();
            } else if ("F3".equals(key)) {
                onF3();
            } else if ("F4".equals(key)) {
                onF4();
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
            } else if ("F0".equals(key)) {
                onF0();
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

        final WeaponConfigurationPanel weapon = new WeaponConfigurationPanel();
        weapon.getOkButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                resultLabel.setText("OK  " + weapon.getEngagement() + " / " + weapon.getWeapon());
            }
        });
        weapon.getCancelButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                resultLabel.setText("Cancel");
            }
        });

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(blue, BorderLayout.CENTER);
        body.add(weapon, BorderLayout.EAST);

        page.add(body, BorderLayout.CENTER);

        /* bar height, button width, button height — change these to fit the other screen */
        FunctionKeyBar footer = new FunctionKeyBar(64, 56, 40, functionListener);
        page.add(footer, BorderLayout.SOUTH);

        setContentPane(page);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 760);
        setLocationRelativeTo(null);
    }

    /** F1. */
    private void create() {
        resultLabel.setText("F1 called create()");
    }

    /** F2. */
    private void onF2() {
        resultLabel.setText("F2");
    }

    /** F3. */
    private void onF3() {
        resultLabel.setText("F3");
    }

    /** F4. */
    private void onF4() {
        resultLabel.setText("F4");
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

    /** F0. On the keyboard this is the F10 key. */
    private void onF0() {
        resultLabel.setText("F0");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FunctionPage().setVisible(true);
            }
        });
    }
}
