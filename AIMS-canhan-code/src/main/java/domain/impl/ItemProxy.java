package domain.impl;

import domain.IItemDomain;

import java.io.IOException;
import java.io.InputStream;

public class ItemProxy implements IItemDomain {

    protected final IItemDomain proxy;
    public ItemProxy(IItemDomain iItemDomain) {
        this.proxy  = iItemDomain;
    }

    @Override
    public long getItemId() {
        return proxy.getItemId();
    }

    @Override
    public String getType() {
        return proxy.getType();
    }

    @Override
    public String getTitle() {
        return proxy.getTitle();
    }

    public long getPrice() {
        return proxy.getPrice();
    }

    @Override
    public boolean hasEnough(int count) {
        return proxy.hasEnough(count);
    }

    @Override
    public long getRemain() {
        return proxy.getRemain();
    }

    @Override
    public InputStream getImage() throws IOException {
        return proxy.getImage();
    }

    @Override
    public int getWeight() {
        return proxy.getWeight();
    }

    @Override
    public boolean isRushable() {
        return proxy.isRushable();
    }

}
