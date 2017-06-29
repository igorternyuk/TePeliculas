package tepeliculas.gui.validators;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/**
 *
 * @author igor
 */
public class TextFieldInputValidator implements KeyListener {

    private final int keyListenerType;

    public TextFieldInputValidator(int keyListenerType) {
        this.keyListenerType = keyListenerType;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        switch (keyListenerType) {
            case KeyListenerType.NO_SPACES:
                if (c == KeyEvent.VK_SPACE) {
                    e.consume();
                }
                break;
            case KeyListenerType.INTEGER_VALUES_ONLY:
                if (!((c >= '0' && c <= '9')
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_MINUS)) {
                    Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
                break;
           case KeyListenerType.FLOAT_VALUES_ONLY:
                if (c == KeyEvent.VK_PERIOD) {
                    JTextField text = (JTextField) e.getSource();
                    if (text.getText().contains(String.valueOf(c))) {
                        Toolkit.getDefaultToolkit().beep();
                        e.consume();
                    }
                }
                if (!((c >= '0' && c <= '9')
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE
                        || c == KeyEvent.VK_MINUS)) {
                    Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
                break;
            case KeyListenerType.LETTERS_ONLY:
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                        || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE)) {
                    Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
                break;
            case KeyListenerType.ANY_SYMBOLS:
                break;
            case KeyListenerType.NO_SYMBOLS :
                e.consume();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
