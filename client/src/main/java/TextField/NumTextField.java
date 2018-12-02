package TextField;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class NumTextField extends JFormattedTextField {
    public NumTextField(){
        super();
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    JOptionPane.showMessageDialog(null, "Некорректный ввод");
                    e.consume();
                }
            }
        });
    }

    public NumTextField(AbstractFormatter formatter) {
        super(formatter);
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    JOptionPane.showMessageDialog(null, "Некорректный ввод");
                    e.consume();
                }
            }
        });
    }
}
