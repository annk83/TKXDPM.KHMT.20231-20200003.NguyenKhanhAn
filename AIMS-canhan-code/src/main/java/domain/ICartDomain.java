package domain;

import java.util.Date;
import java.util.List;

public interface ICartDomain {
    List<ICartItemDomain> getPage(int u, int v);
    void addItem(long itemId, int count);
    void removeItem(long itemId);
    long getRawPrice();
    Date getSavedDate();
    int countItemType();
    int countItem();
    boolean hasEnough();
    List<ICartItemDomain> getAll();
}

