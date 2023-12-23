package dmui.toplevel;

import dmui.content.BaseContent;
import dmui.content.BasePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class Screen extends JPanel{
    private JPanel currentContent;
    private JLabel title;
    protected Screen() {
        super(new BorderLayout());
        add(createHeaderBar(""), BorderLayout.PAGE_START);
        add(getFooter(), BorderLayout.PAGE_END);
        currentContent = null;
    }

    public void setContent(BaseContent jPanel) {
        if(jPanel == null) throw new IllegalArgumentException("Not null");
        if(currentContent != null) {
            remove(currentContent);
        }
        add(jPanel, BorderLayout.CENTER);
        currentContent = jPanel;
        title.setText(jPanel.getTitle());
        invalidate();
        jPanel.reset();
    }

    protected JComponent getFooter() {
        //TODO: Tao footer
        return new JLabel("Nguyen Khanh An - 20200003");
    }

    private JComponent createHeaderBar(String menuName) {
        if(menuName == null) throw new IllegalArgumentException("menuName != null");
        URL url = ClassLoader.getSystemClassLoader().getResource("background/aims.png");
        assert url != null;
        BackgroundPanel ret;
        try {
            ret = new BackgroundPanel(ImageIO.read(url));
        } catch (IOException ioException) {
            throw new RuntimeException("Resource File Missing");
        }

        ret.setBorder(new StrokeBorder(new BasicStroke(0)));
        ret.setLayout(new BorderLayout());

        JLabel menu = new JLabel(menuName);
        this.title = menu;
        menu.setBackground(new Color(0,0,0,0));
        menu.setForeground(new Color(214,211,47));
        menu.setFont(new Font("Times New Roman", Font.BOLD, 40));
        menu.setVerticalAlignment(SwingConstants.TOP);
        menu.setPreferredSize(new Dimension(Short.MAX_VALUE, 80));
        ret.add(menu, BorderLayout.LINE_START);

        JLabel end = new JLabel("AIMS");
        end.setBackground(new Color(0,0,0,0));
        end.setForeground(Color.black);
        end.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        end.setVerticalAlignment(SwingConstants.BOTTOM);
        ret.add(end, BorderLayout.LINE_END);
        return ret;
    }

}
