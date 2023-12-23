package dmui.toplevel;

import lombok.Data;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Semaphore;

public class TopLevelFrame extends JFrame {
    private final Screen screen = new Screen();
    @Getter
    private final ContentNavigator contentNavigator = new ContentNavigator(screen);
    private final JFrame jFrame;
    @Getter
    private final MessageDisplayerImpl messageDisplayer = new MessageDisplayerImpl(this);
    public TopLevelFrame() throws IOException {
        this(ClassLoader.getSystemClassLoader().getResourceAsStream("defaultConfig.properties"));
    }
    public TopLevelFrame(InputStream configStream) throws IOException {
        jFrame = new JFrame();
        FrameConfig frameConfig = new FrameConfig(configStream);

        jFrame.setLayout(new BorderLayout());
        jFrame.add(screen);
        jFrame.setTitle(frameConfig.title);
        jFrame.setLocationRelativeTo(null);
    }

    public void run() {
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

    @Data
    private static class FrameConfig {
        private String title;
        private String width;
        private String height;

        public FrameConfig(InputStream inputStream) throws IOException {
            Properties config = new Properties();
            config.load(inputStream);
            title = config.getProperty("title", "AIMS");
            width = config.getProperty("width", "1000");
            height = config.getProperty("height", "1000");
        }

        public long getWidthPx() {
            return Long.parseLong(width);
        }

        public long getHeightPx() {
            return Long.parseLong(height);
        }
    }
}
