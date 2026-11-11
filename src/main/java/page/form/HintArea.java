package page.form;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;

/**
 * A white comment box with grey hint text. Same idea as HintField.
 */
public class HintArea extends JTextArea {

    private static final long serialVersionUID = 1L;

    private final String hint;
    private boolean showingHint;

    public HintArea(final String hint, int rows, int columns) {
        super(rows, columns);
        this.hint = hint;
        Mono.styleField(this);
        setLineWrap(true);
        setWrapStyleWord(true);
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
