package controller.order;

import controller.form.ISimpleForm;

public interface IRushOrderController extends IOrderController {
    IOrderController toNormalOrder();
    int getRushItemCount();
    IRushOrderItemListController getItemListController();
}
