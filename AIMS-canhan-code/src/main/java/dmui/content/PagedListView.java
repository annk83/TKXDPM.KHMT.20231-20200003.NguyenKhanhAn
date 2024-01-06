package dmui.content;

import controller.utils.IPageableController;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PagedListView<CHILD_CONTROLLER, CONTROLLER extends IPageableController<CHILD_CONTROLLER>> extends BasePanel {
    @Getter
    private final PaginatorUI paginatorUI = new PaginatorUI();
    private final JPanel panelList;
    private int callbackId = -1;
    private final List<AbstractUI<CHILD_CONTROLLER>> holders = new ArrayList<>();
    private final Supplier<AbstractUI<CHILD_CONTROLLER>> uiSupplier;
    public PagedListView(Supplier<AbstractUI<CHILD_CONTROLLER>> uiSupplier) {
        super(new BorderLayout());
        this.uiSupplier = uiSupplier;
        JPanel top = new JPanel(new BorderLayout());
        panelList = new JPanel();
        panelList.setLayout(new BoxLayout(panelList, BoxLayout.Y_AXIS));
        top.add(panelList, BorderLayout.PAGE_START);
        var j = new JScrollPane(top);
        paginatorUI.setPageSizeList(List.of(3,5,7));
        add(j, BorderLayout.CENTER);
        add(paginatorUI, BorderLayout.PAGE_END);
   }

    void setController(CONTROLLER controller) {
        var iPaginatorController = controller.getPaginatorController();
        iPaginatorController.removeCallback(callbackId);

        callbackId = iPaginatorController.addCallback((u,v,t)->{
            displayList(controller.getPage(u, v));
        });
        iPaginatorController.invokeAllCallback();
        paginatorUI.setController(iPaginatorController);
    }

    private void displayList(List<? extends CHILD_CONTROLLER> list) {
        int sz = list.size();
        int i=0;
        for(; i < Math.min(sz, holders.size()); ++i)
            holders.get(i).setController(list.get(i));

        for(;i<sz;++i) {
            var child = uiSupplier.get();
            child.setController(list.get(i));
            holders.add(child);
            panelList.add(child);
        }

        for(; i< holders.size(); ++i)
            holders.get(i).setVisible(false);

        panelList.invalidate();
    }

    public interface PageSupplier<T> {
        List<T> getPage(int u, int v);
    }

}
