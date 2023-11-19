package annk.aims.ui;

import annk.aims.controller.PlaceOrderController;
import annk.aims.domain.*;
import annk.aims.services.IPlaceOrderStateService;
import annk.aims.services.IProvince;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

public class PlaceOrderUITest {
    @Test
    public void uiTest() throws IOException, InterruptedException {
        var ui = new PlaceOrderUI(new IPlaceOrderStateService() {
            @Override
            public PlaceOrderState get() {
                return new PlaceOrderState(0, new ShipForm("","",null,"", null), false, List.of(0), List.of(0), new RushForm(), new Date());
            }

            @Override
            public void save(PlaceOrderState placeOrderState) {

            }

            @Override
            public void complete() {
            }

            @Override
            public void startNew(List<Integer> items, List<Integer> counts) {

            }
        }, new IProvince() {
            @Override
            public List<Integer> getAllProvinceId() {
                return List.of(0, 1);
            }

            @Override
            public List<Province> getAllProvince() {
                return List.of(new Province(0, "Hanoi", 1, 10000, 5000), new Province(1, "HCM", 0, 0, 0));
            }
        }, new PlaceOrderController() {
            @Override
            public PlaceOrderItem get(int itemId) {
                            PlaceOrderItem r = new PlaceOrderItem();
            r.setName("Test1");
            r.setCount(100);
            r.setPrice(123.3);
            r.setItemId(0);
            r.setWeight(1);

            try {
                r.setImage(ImageIO.read(new File("E:\\NestList\\app\\src\\main\\res\\drawable\\app1.png")));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            return r;

            }

            @Override
            public double getTax() {
                return 0.1;
            }

            @Override
            public int getFreeShipThreshold() {
                return 100000;
            }

            @Override
            public double getRushShipCharge() {
                return 10000.0;
            }
        });

        JFrame jFrame = new JFrame();
        jFrame.setSize(1000, 1000);
        jFrame.add(ui.getPanel());
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
