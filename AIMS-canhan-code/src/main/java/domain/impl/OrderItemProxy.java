package domain.impl;

import com.sun.jna.platform.win32.COM.IRunningObjectTable;
import domain.IItemDomain;
import domain.IOrderItemDomain;

public class OrderItemProxy extends ItemProxy implements IOrderItemDomain {
    private int count;
    private boolean rushing;

    public OrderItemProxy(IItemDomain iItemDomain, int count, boolean rushing) {
        super(iItemDomain);
        setCount(count);
        setRush(rushing);
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
        return this.count * this.getEachItemPrice();
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
    public boolean isRushing() {
        return rushing;
    }

    @Override
    public void setRush(boolean b) {
        if(isRushable())
            rushing = b;
        else rushing = false;
    }
}
