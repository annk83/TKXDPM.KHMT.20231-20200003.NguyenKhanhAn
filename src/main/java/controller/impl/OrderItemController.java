package controller.impl;

import controller.order.*;
import domain.IOrderDomain;
import domain.IOrderItemDomain;
import utils.IEtc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class OrderItemController extends BaseController implements IRushableOrderItemController {
    private final IOrderItemDomain item;
    private final Runnable change;
    private final IOrderController parrent;
    public OrderItemController(IOrderController orderDomain, IOrderItemDomain i, Runnable changeListener, IEtc ietc) {
        super(ietc);
        parrent = orderDomain;
        item = i;
        change = changeListener;
    }

    @Override
    public String getTotalPrice() {
        return formatMoney(item.getTotalPrice());
    }

    @Override
    public String getItemPrice() {
        return formatMoney(item.getEachItemPrice());
    }

    @Override
    public String getTitle() {
        return item.getTitle();
    }

    @Override
    public String getType() {
        return item.getType();
    }

    @Override
    public int getCount() {
        return item.getCount();
    }

    @Override
    public boolean checkRemain() {
        return item.hasEnough();
    }

    public Image getImage() throws IOException {
        return ImageIO.read(item.getImage());
    }

    @Override
    public int getWeight() {
        return item.getWeight();
    }

    @Override
    public boolean isRushing() {
        return item.isRushing();
    }

    @Override
    public boolean isOrderRushing() {
        return parrent.isRushing();
    }

    @Override
    public boolean isRushable() {
        return item.isRushable();
    }

    @Override
    public void setRush(boolean b) {
        item.setRush(b);
        change.run();
    }
}
