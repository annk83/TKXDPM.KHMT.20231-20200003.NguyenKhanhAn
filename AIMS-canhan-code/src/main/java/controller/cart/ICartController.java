package controller.cart;

import controller.utils.IPageableController;

public interface ICartController extends IPageableController<ICartItemController> {
    String getTotalMoney();
    int getItemCount();
    int getItemTypeCount();
    String getSavedDate();
    boolean payOrder();
}
