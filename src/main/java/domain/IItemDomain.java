package domain;

import java.io.IOException;
import java.io.InputStream;

public interface IItemDomain {
    int getItemId();
    String getType();
    String getTitle();
    long getPrice();
    boolean hasEnough(int count);
    long getRemain();
    InputStream getImage() throws IOException;
    int getWeight();

    boolean isRushable();
}
