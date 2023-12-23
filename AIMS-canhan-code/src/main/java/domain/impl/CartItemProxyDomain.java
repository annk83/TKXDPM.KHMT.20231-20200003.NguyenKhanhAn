package domain.impl;

import domain.ICartItemDomain;
import domain.IItemDomain;

import java.io.IOException;
import java.io.InputStream;

public class CartItemProxyDomain extends ItemProxy implements ICartItemDomain{
    private int count;

    CartItemProxyDomain(IItemDomain proxy, int count) {
        super(proxy);
        this.count = count;
    }

    @Override
    public boolean hasEnough() {
        return super.hasEnough(count);
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public long getTotalPrice() {
        return getEachItemPrice() * count;
    }

    @Override
    public long getEachItemPrice() {
        return super.getPrice();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public InputStream getImage() throws IOException {
        return proxy.getImage();
    }
}
