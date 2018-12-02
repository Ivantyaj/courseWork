package TextField;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.Format;

public class StrTextField extends JFormattedTextField{
    public StrTextField(){
        super();
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= 'А') && (c <= 'я') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    JOptionPane.showMessageDialog(null, "Вводите только русские символы!");
                    e.consume();
                }
            }
        });
    }

    public StrTextField(AbstractFormatter formatter) {
        super(formatter);
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!((c >= '0') && (c <= '9') ||
                        (c == KeyEvent.VK_BACK_SPACE) ||
                        (c == KeyEvent.VK_DELETE))) {
                    JOptionPane.showMessageDialog(null, "Вводите только русские символы!");
                    e.consume();
                }
            }
        });
    }
}
