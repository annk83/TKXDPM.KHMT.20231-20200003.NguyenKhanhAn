package domain;

import java.io.IOException;
import java.io.InputStream;

public interface ICartItemDomain {
    int getItemId();
    boolean hasEnough();
    void setCount(int count);
    long getTotalPrice();
    long getEachItemPrice();
    String getTitle();
    String getType();
    int getCount();
    InputStream getImage() throws IOException;
}
