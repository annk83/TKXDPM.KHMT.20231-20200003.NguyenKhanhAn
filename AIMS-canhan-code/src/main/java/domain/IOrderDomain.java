package domain;

import java.util.List;

public interface IOrderDomain {
    int getTotalWeight();
    long getRawMoney();
    long getTotalMoney();
    long getTaxMoney();
    boolean isRushing();
    void setRushing(boolean b);
    long getShipMoney();
    int getItemTypeCount();
    boolean hasEnough();
    int getItemCount();
    List<? extends IOrderItemDomain> getPage(int u, int v);
    ShipForm getShipForm();
    int getRushItemCount();
    boolean isRushable();
}
