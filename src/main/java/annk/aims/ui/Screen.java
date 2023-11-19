package annk.aims.ui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Screen {
    @Setter @Getter
    private Consumer<Screen> consumer = null;
    public abstract JPanel getPanel();
    protected abstract void reload();

    public void nextScreen(Screen screen) {
        if(consumer != null) consumer.accept(screen);
    }
    public void dispose() {
        consumer = null;
    }
}
