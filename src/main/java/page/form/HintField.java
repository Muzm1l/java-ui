package page.form;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

/**
 * A white text field that shows grey hint text until the user types.
 * Java 6 has no built-in placeholder. getValue() returns "" while the hint is showing.
 */
public class HintField extends JTextField {

    private static final long serialVersionUID = 1L;

    private final String hint;
    private boolean showingHint;

    public HintField(final String hint, int columns) {
        super(columns);
        this.hint = hint;
        Mono.styleField(this);
        showHint();
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent event) {
                if (showingHint) {
                    setText("");
                    setForeground(Mono.FIELD_TEXT);
                    showingHint = false;
                }
            }

            public void focusLost(FocusEvent event) {
                if (getText().trim().length() == 0) {
                    showHint();
                }
            }
        });
    }

    public String getValue() {
        if (showingHint) {
            return "";
        }
        return getText();
    }

    private void showHint() {
        showingHint = true;
        setText(hint);
        setForeground(Mono.MUTED);
    }
}
