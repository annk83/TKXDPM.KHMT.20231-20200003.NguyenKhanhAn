package domain;

public interface IOrderItemDomain extends ICartItemDomain {
    int getWeight();
    boolean isRushable();
    boolean isRushing();
    void setRush(boolean b);
}
