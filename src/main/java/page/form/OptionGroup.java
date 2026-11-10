package page.form;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * Radio buttons that belong to one choice.
 * columns is how many choices sit side by side.
 * selectedIndex is the choice that starts selected. Pass -1 for none.
 */
public class OptionGroup extends Block {

    private static final long serialVersionUID = 1L;

    private final JRadioButton[] buttons;

    public OptionGroup(String[] choices, int selectedIndex, int columns) {
        super();
        int rows = 1;
        if (columns < 1) {
            columns = 1;
        }
        if (choices.length > 0) {
            rows = ((choices.length - 1) / columns) + 1;
        }
        setLayout(new GridLayout(rows, columns, 12, 4));

        ButtonGroup group = new ButtonGroup();
        buttons = new JRadioButton[choices.length];
        for (int i = 0; i < choices.length; i++) {
            JRadioButton button = Mono.radio(choices[i]);
            group.add(button);
            if (i == selectedIndex) {
                button.setSelected(true);
            }
            buttons[i] = button;
            add(button);
        }
    }

    /** The text of the selected choice, or an empty string when none is selected. */
    public String getSelected() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isSelected()) {
                return buttons[i].getText();
            }
        }
        return "";
    }
}
