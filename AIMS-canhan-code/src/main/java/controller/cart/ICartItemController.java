package controller.cart;

import java.awt.*;
import java.io.IOException;

public interface ICartItemController {
    void remove();

    boolean checkRemain();

    String getTotalPrice();

    String getItemPrice();

    void plus();

    void minus();

    void setItemCount(int count);

    String getTitle();

    String getType();

    int getCount();

    Image getImage() throws IOException;
}
