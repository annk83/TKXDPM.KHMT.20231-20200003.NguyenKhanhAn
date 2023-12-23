package dmui.content;

import controller.order.IOrderItemController;
import controller.order.IRushableOrderItemController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PlaceOrderItemUI extends AbstractUI<IOrderItemController> {
    private final JLabel title = new JLabel(), type = new JLabel(), priceCalculate = new JLabel();
    private final ImageIcon icon = new ImageIcon();
    private final JPanel rushCheckboxPanel = new JPanel(new GridBagLayout());
    private IOrderItemController controller;
    public PlaceOrderItemUI() {
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

        rushCheckboxPanel.add(new BigAssCheckbox(30, b->{
            ((IRushableOrderItemController) controller).setRush(b);
        }));
        add(rushCheckboxPanel, BorderLayout.LINE_END);
    }
    @Override
    public void setController(IOrderItemController iOrderItemController) {
        this.controller = iOrderItemController;
        Image image = null;
        try {
            image = iOrderItemController.getImage();
        } catch (IOException ioException) {
            defaultExceptionHandle(ioException);
        }
        if(image == null) image = missingImage;
        icon.setImage(image);
        title.setText(iOrderItemController.getTitle());
        type.setText(iOrderItemController.getType());
        priceCalculate.setText("%s * %s = %s".formatted(iOrderItemController.getCount(), iOrderItemController.getItemPrice(), iOrderItemController.getTotalPrice()));
        if(iOrderItemController.isOrderRushing() && iOrderItemController.isRushable()) {
            rushCheckboxPanel.setVisible(true);
        }
        else rushCheckboxPanel.setVisible(false);
    }
}
