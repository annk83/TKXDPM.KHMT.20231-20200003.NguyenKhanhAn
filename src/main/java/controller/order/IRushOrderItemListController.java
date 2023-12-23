package controller.order;

import controller.utils.ChangeListener;

import java.util.List;

public interface IRushOrderItemListController extends IOrderItemListController, ChangeListener {
    List<? extends IRushOrderItemController> getPage(int u, int v);
}
