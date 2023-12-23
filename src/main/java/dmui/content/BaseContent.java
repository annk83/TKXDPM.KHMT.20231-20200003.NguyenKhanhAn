package dmui.content;

import dmui.toplevel.ContentNavigator;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public abstract class BaseContent extends BasePanel {
    @Getter
    protected final String title;
    @Setter
    protected ContentNavigator contentNavigator;

    public BaseContent(String title) {
        super();
        this.title = title;
    }

    public BaseContent(String title, LayoutManager layoutManager) {
        super(layoutManager);
        this.title = title;
    }

    public abstract void reset();
}
