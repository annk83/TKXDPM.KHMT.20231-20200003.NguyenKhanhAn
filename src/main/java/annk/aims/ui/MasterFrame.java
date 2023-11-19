package annk.aims.ui;

import annk.aims.domain.ICart;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Semaphore;

public class MasterFrame implements Runnable {
    private final JFrame jFrame = new JFrame();
    private Screen current;

    public MasterFrame(Screen init) {
        jFrame.setSize(1000, 1000);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
    }

    private void initScreen(Screen s) {
        s.setConsumer(i->{
            i.dispose();
            initScreen(i);
        });
        jFrame.setContentPane(s.getPanel());
        jFrame.invalidate();
        current = s;
    }

    @Override
    public void run() {
        Semaphore semaphore = new Semaphore(0);
        jFrame.add(current.getPanel());

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                semaphore.release();
            }
        });
        try {
            semaphore.acquire();
        } catch (InterruptedException exception) {
            jFrame.dispose();
            Thread.currentThread().interrupt();
        }
    }


}
