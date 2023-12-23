package dmui.toplevel;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image img;
    private Image scaled;

    BackgroundPanel(Image image) {
        img = image;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if ((scaled == null || getWidth() > scaled.getWidth(this) || getHeight() > scaled.getHeight(this)) && getWidth() > 0 && getHeight() > 0) {
            scaled = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        } else {
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scaled != null) {
            int x = (getWidth() - scaled.getWidth(this)) / 2;
            int y = (getHeight() - scaled.getHeight(this)) / 2;
            g.drawImage(scaled, x, y, this);
        }
    }
}
