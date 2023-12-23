package controller.order;

import java.awt.*;
import java.io.IOException;

public interface IOrderItemController {
    String getTotalPrice();
    String getItemPrice();
    String getTitle();
    String getType();
    int getCount();
    boolean checkRemain();
    boolean isRushable();
    Image getImage() throws IOException;
    int getWeight();
    boolean isRushing();
    boolean isOrderRushing();
}
