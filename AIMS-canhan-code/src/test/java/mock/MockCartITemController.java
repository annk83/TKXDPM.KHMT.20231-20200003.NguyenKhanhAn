package mock;

import controller.cart.ICartItemController;

import java.awt.*;
import java.io.IOException;

public class MockCartITemController implements ICartItemController {
    int count;

    @Override
    public void remove() {
    }

    @Override
    public boolean checkRemain() {
        return count < 10;
    }

    @Override
    public String getTotalPrice() {
        return "";
    }

    @Override
    public String getItemPrice() {
        return "";
    }

    @Override
    public void plus() {
        ++count;
    }

    @Override
    public void minus() {
        --count;
    }

    @Override
    public void setItemCount(int count) {
        this.count = count;
    }

    @Override
    public String getTitle() {
        return "Title";
    }

    @Override
    public String getType() {
        return "Type";
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Image getImage() throws IOException {
        return null;
    }
}
