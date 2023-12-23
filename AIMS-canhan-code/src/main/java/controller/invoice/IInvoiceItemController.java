package controller.invoice;

import java.awt.*;
import java.io.IOException;

public interface IInvoiceItemController {
    boolean isRushing();

    Image getImage() throws IOException;

    String getTitle();

    String getType();

    String getCount();

    String getTotalPrice();

    String getItemPrice();
}
