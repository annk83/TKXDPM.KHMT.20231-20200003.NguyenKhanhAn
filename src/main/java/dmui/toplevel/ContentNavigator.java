package dmui.toplevel;

import dmui.content.BaseContent;
import java.util.HashMap;
import java.util.Map;

public class ContentNavigator {
    private Map<Class<? extends BaseContent>, BaseContent> mp = new HashMap<>();
    private final Screen screen;

    ContentNavigator(Screen screen) {
        if(screen == null) throw new IllegalArgumentException("not null");
        this.screen = screen;
    }
    public <T extends BaseContent>void register(T contentContent) {
        if(contentContent == null) throw new IllegalArgumentException("not null");
        mp.put(contentContent.getClass(), contentContent);
    }

    public BaseContent get(Class<? extends BaseContent> panelClass) {
        return mp.get(panelClass);
    }

    public void changeTo(Class<? extends BaseContent> panelClass) {
        if(panelClass == null) throw new IllegalArgumentException("not null");
        var c = get(panelClass);
        if(c == null)
            throw new IllegalStateException("Khong co man hinh '%s'".formatted(panelClass.getSimpleName()));
        c.setContentNavigator(this);
        screen.setContent(c);
    }
}
