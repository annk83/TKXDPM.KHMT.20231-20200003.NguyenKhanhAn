package dmui.content;

import java.awt.*;

public abstract class AbstractUI<CONTROLLER> extends BasePanel {
    protected AbstractUI() {
        this(new FlowLayout());
    }
    protected AbstractUI(LayoutManager layoutManager) {
        super(layoutManager);
    }
    public abstract void setController(CONTROLLER controller);
}
