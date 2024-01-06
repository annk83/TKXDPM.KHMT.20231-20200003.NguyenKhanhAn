package dmui.content;

import controller.cart.ICartController;
import controller.cart.ICartItemController;
import utils.IEtc;

import javax.swing.*;
import java.awt.*;

import static utils.Utils.countUnitStr;

public class ViewCartUI extends BaseContent {
    private ICartController cart;
    private final IEtc config;
    private final JLabel totalLabel;
    private final PagedListView<ICartItemController, ICartController> pageListView;

    public ViewCartUI(ICartController cart, IEtc config) {
        this(config);
        setController(cart);
    }

    public ViewCartUI(IEtc config) {
        super("Cart manager");
        this.config = config;

        var total = new BasePanel();
        totalLabel = new JLabel();
        totalLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        total.setLayout(new GridBagLayout());
        total.add(totalLabel);

        var btns = new BasePanel();
        btns.setLayout(new GridBagLayout());
        var c = new GridBagConstraints();
        btns.add(payOrderButton(), c);
        c.gridy = 1;

        var leftPanel = new BasePanel(new BorderLayout());
        leftPanel.add(btns, BorderLayout.PAGE_START);
        leftPanel.add(total, BorderLayout.CENTER);

        pageListView = new PagedListView<>(()->new ViewCartItemUI(this));
        setLayout(new BorderLayout());
        add(pageListView, BorderLayout.CENTER);
        add(leftPanel, BorderLayout.LINE_END);

    }

    public void setController(ICartController cart) {
        if(cart == null) throw new IllegalArgumentException("not null");
        this.cart = cart;
        pageListView.setController(cart);
    }

    private void updateTotalLabel() {
        totalLabel.setText(
            "<html>" +
            countUnitStr(cart.getItemCount(), "item") + ", " +
            countUnitStr(cart.getItemTypeCount(), "item type") + "<br>" +
            "Total without tax: " + cart.getTotalMoney() + " " + config.getCurrencyConfig().currency() + "<br>" +
            "Last update: " + cart.getSavedDate() +
            "</html>");
    }

    private JButton payOrderButton() {
        var ret = new JButton("Pay Order");
        ret.addActionListener(e->{
            cart.payOrder();
        });
        return ret;
    }

    @Override
    public void reset() {
        updateTotalLabel();
    }
}
