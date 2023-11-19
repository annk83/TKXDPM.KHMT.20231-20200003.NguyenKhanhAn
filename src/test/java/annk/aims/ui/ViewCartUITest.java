package annk.aims.ui;

import annk.aims.domain.CartItem;
import annk.aims.domain.ICart;
import annk.aims.services.ICatalog;
import annk.aims.ui.ViewCartUI;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ViewCartUITest {
    public static class TestCart implements ICart {

        @Override
        public void setItem(int item, int count) {
        }

        @Override
        public void addItem(int item, int count) {
        }

        @Override
        public void removeItem(int item) {
        }

        @Override
        public void emptyCart() {
        }

        @Override
        public CartItem getItem(int item) {
            var r = new CartItem();
            r.setName("Test1");
            r.setCount(100);
            r.setPrice(123.3);
            r.setItemId(0);
            try {
                r.setImage(ImageIO.read(new File("E:\\NestList\\app\\src\\main\\res\\drawable\\app1.png")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return r;
        }

        @Override
        public List<CartItem> getAllCartItem() {
            return List.of(getItem(0), getItem(0), getItem(0), getItem(0), getItem(0), getItem(0));
        }
    }
    @Test
    public void uiTest() throws IOException, InterruptedException {
        ViewCartUI viewCartUI = new ViewCartUI(new TestCart());
        JFrame jFrame = new JFrame();
        jFrame.setSize(1000, 1000);
        jFrame.add(viewCartUI.getPanel());
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

        Semaphore semaphore = new Semaphore(0);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                semaphore.release();
            }
        });
        semaphore.acquire();
    }
}
