package annk.aims.domain;

import java.io.IOException;
import java.util.List;

public interface ICart {
    void setItem(int item, int count);

    void addItem(int item, int count);

    void removeItem(int item);

    void emptyCart();

    CartItem getItem(int item) throws IOException;

    List<CartItem> getAllCartItem() ;
}
