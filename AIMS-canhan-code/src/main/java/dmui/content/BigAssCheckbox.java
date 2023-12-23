package dmui.content;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class BigAssCheckbox extends JPanel {
    private JPanel inner = new JPanel();
    private final Consumer<Boolean> stateChange;
    public BigAssCheckbox(int size, Consumer<Boolean> stateChange) {
        super(new BorderLayout());
        this.stateChange = stateChange;
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 2), BorderFactory.createEmptyBorder(2,2,2,2)));
        add(inner);
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(check) {
                    check = false;
                    inner.setBackground(Color.WHITE);
                }
                else {
                    check = true;
                    inner.setBackground(Color.RED);
                }
                stateChange.accept(check);
            }
        });
    }
    private boolean check;
}
