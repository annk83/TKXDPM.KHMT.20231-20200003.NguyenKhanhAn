package controller.order;

import controller.utils.IPageableController;
import controller.utils.IPaginatorController;

import java.util.List;

public interface IOrderItemListController extends IPageableController<IOrderItemController> {
    IPaginatorController getPaginatorController();
    List<? extends IOrderItemController> getPage(int u, int v);
}
