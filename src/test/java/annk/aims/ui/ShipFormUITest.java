package annk.aims.ui;

import annk.aims.domain.Province;
import annk.aims.domain.ShipForm;
import annk.aims.services.IProvince;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ShipFormUITest {
    @Test
    public void tc1() throws Exception {
        uiTest(new ShipForm("annk", "sdfv", 0, "dsfsd", null));
    }

    @Test
    public void tc2() throws Exception {
        uiTest(new ShipForm());
    }

    private void uiTest(ShipForm shipForm) throws InterruptedException {
        var shipformUI = new ShipFormUI(shipForm, System.out::println, new IProvince() {
            @Override
            public List<Integer> getAllProvinceId() {
                return List.of(0,1);
            }

            @Override
            public List<Province> getAllProvince() {
                return List.of(new Province(0, "Hanoi",0,0,0), new Province(1, "HCM",0,0,0));
            }
        });
        JFrame jFrame = new JFrame();
        jFrame.setSize(1000, 1000);
        jFrame.add(shipformUI.getPanel());
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
