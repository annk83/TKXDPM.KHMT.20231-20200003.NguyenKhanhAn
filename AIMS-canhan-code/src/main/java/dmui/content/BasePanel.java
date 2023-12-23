package dmui.content;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;

public class BasePanel extends JPanel {
    protected final Image missingImage;
    protected final Color foreground1;
    protected final Color background1;
    protected final Color foreground2;
    protected final Color background2;
    protected final Color foreground3;
    protected final Color background3;
    protected final Font normalFont;

    protected BasePanel() {
        this(new FlowLayout());
    }
    protected BasePanel(LayoutManager layoutManager) {
        super(layoutManager);
        missingImage = new BufferedImage(100, 100, ColorSpace.TYPE_RGB);
        foreground1 = Color.black;
        background1 = new Color(246, 255, 255);

        foreground2 = foreground1.brighter();
        background2 = background1.darker();

        foreground3 = foreground2.brighter();
        background3 = background2.darker();

        normalFont = new Font("Times New Roman", Font.PLAIN, 13);
        //setBackground(background3);
        //setForeground(foreground3);
    }

    protected void defaultExceptionHandle(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        displayError("Exception !!!",exception.getMessage(), stringWriter.toString());
    }

    protected boolean confirmDialog(String title, String message) {
        return JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }
    protected void displayError(String title, String message, String full) {
        JDialog jDialog = new JDialog(SwingUtilities.getWindowAncestor(this));
        //TODO: Code dep hon
        jDialog.setLayout(new BorderLayout());
        jDialog.setTitle(title);
        var messageLabel = new JLabel(message);
        messageLabel.setFont(normalFont.deriveFont(Font.BOLD, 15));
        jDialog.add(messageLabel, BorderLayout.PAGE_START);
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setText(full);
        jDialog.add(jTextArea, BorderLayout.CENTER);

        JPanel btns = new JPanel();
        var closeBtn = new JButton("Close");
        closeBtn.addActionListener((e)->{ jDialog.dispose(); });
        var showMore = new JButton("Show more");
        showMore.addActionListener((e)->{
           jTextArea.setVisible(jTextArea.isVisible());
           showMore.setText(jTextArea.isVisible()?"Show less":"Show more");
        });
        jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        btns.add(closeBtn);
        btns.add(showMore);

        jDialog.add(btns, BorderLayout.LINE_END);
        jDialog.pack();
        jDialog.setVisible(true);
    }

    protected Image scaleImage(Image input, int width, int height) {
        return input.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }


}
