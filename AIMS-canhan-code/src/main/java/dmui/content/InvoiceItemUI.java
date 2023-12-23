package dmui.content;

import controller.invoice.IInvoiceItemController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class InvoiceItemUI extends AbstractUI<IInvoiceItemController> {
    private final JLabel title = new JLabel(), type = new JLabel(), priceCalculate = new JLabel();
    private IInvoiceItemController controller;
    private final ImageIcon icon = new ImageIcon();
    private final JLabel rushNotify = new JLabel("Rushing");
    public InvoiceItemUI() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel imagePanel = new BasePanel();
        imagePanel.add(new JLabel(icon));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
        add(imagePanel, BorderLayout.LINE_START);
        JPanel contentPanel = new BasePanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(title);
        contentPanel.add(type);
        contentPanel.add(priceCalculate);
        add(contentPanel, BorderLayout.CENTER);
        add(rushNotify, BorderLayout.LINE_END);
    }

    @Override
    public void setController(IInvoiceItemController iInvoiceItemController) {
        this.controller = iInvoiceItemController;
        Image image = null;
        try {
            image = controller.getImage();
        } catch (IOException ioException) {
            defaultExceptionHandle(ioException);
        }
        if(image == null) image = missingImage;
        icon.setImage(image);
        title.setText(controller.getTitle());
        type.setText(controller.getType());
        priceCalculate.setText("%s * %s = %s".formatted(controller.getCount(), controller.getItemPrice(), controller.getTotalPrice()));
        rushNotify.setVisible(controller.isRushing());
    }
}
