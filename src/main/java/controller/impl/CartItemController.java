package controller.impl;

import controller.cart.ICartItemController;
import domain.ICartItemDomain;
import utils.IEtc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class CartItemController extends BaseController implements ICartItemController {
    private final ICartItemDomain cartItem;
    private final CartController parrent;
    CartItemController(ICartItemDomain ICartItemDomain, CartController parrent, IEtc iEtc) {
        super(iEtc);
        this.cartItem = ICartItemDomain;
        this.parrent = parrent;
    }
    @Override
    public void remove() {
        parrent.cart.removeItem(cartItem.getItemId());
        parrent.paginator.invokeAllCallback();
    }

    @Override
    public boolean checkRemain() {
        return cartItem.hasEnough();
    }

    @Override
    public String getTotalPrice() {
        return formatMoney(cartItem.getTotalPrice());
    }

    @Override
    public String getItemPrice() {
        return formatMoney(cartItem.getEachItemPrice());
    }

    @Override
    public void plus() {
        cartItem.setCount(cartItem.getCount()+1);
    }

    @Override
    public void minus() {
        cartItem.setCount(cartItem.getCount()-1);
    }

    @Override
    public void setItemCount(int count) {
        cartItem.setCount(count);
    }

    @Override
    public String getTitle() {
        return cartItem.getTitle();
    }

    @Override
    public String getType() {
        return cartItem.getType();
    }

    @Override
    public int getCount() {
        return cartItem.getCount();
    }

    @Override
    public Image getImage() throws IOException {
        return ImageIO.read(cartItem.getImage());
    }
}
