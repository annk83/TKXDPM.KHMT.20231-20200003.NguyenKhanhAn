package dmui.content;

import mock.MockCartITemController;
import utils.IEtc;
import dmui.toplevel.TopLevelFrame;
import mock.MockCartController;
import mock.MockPaginatorController;
import org.junit.Test;
import utils.ShippingConfig;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

//How to create test: add 'assert promt()' to end of test
public class UIView {

    private boolean promt() {
        var ret = JOptionPane.showConfirmDialog(null, "Test Passed ?", "Test result", JOptionPane.YES_NO_OPTION);
        return (ret == JOptionPane.YES_OPTION);
    }

    private void simplePanelDisplay(JPanel jPanel) {
        var jFrame = new JFrame();
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Semaphore sem = new Semaphore(0);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                sem.release();
            }
        });
        try {
            sem.acquire();
        } catch (InterruptedException exception) {
            jFrame.dispose();
        }
    }
    @Test
    public void mainFrame() throws IOException {
        TopLevelFrame topLevelFrame = new TopLevelFrame(new ByteArrayInputStream(new byte[0]));
        topLevelFrame.run();
        assert promt();
    }

    @Test
    public void errorDialog() {
        BasePanel basePanel = new BasePanel();
        basePanel.defaultExceptionHandle(new Exception());
        assert promt();
    }

    @Test
    public void viewCart() throws Exception {
        TopLevelFrame topLevelFrame = new TopLevelFrame(new ByteArrayInputStream(new byte[0]));
        topLevelFrame.getContentNavigator().register(new ViewCartUI(new MockCartController(), new IEtc() {
            @Override
            public int getDefaultPageSize() {
                return 5;
            }

            @Override
            public double getTax() {
                return 0;
            }

            @Override
            public ShippingConfig getShippingConfig() {
                return null;
            }

            @Override
            public CurrencyConfig getCurrencyConfig() {
                return null;
            }
        }));
        topLevelFrame.getContentNavigator().changeTo(ViewCartUI.class);
        topLevelFrame.run();
        assert promt();
    }

    @Test
    public void paginator() throws Exception {
        simplePanelDisplay(new PaginatorUI(new MockPaginatorController(), List.of(3,5,7)));
        assert promt();
    }
}

