package controller.cart;

import controller.order.IOrderController;
import controller.utils.IPageableController;

import java.util.Optional;

public interface ICartController extends IPageableController<ICartItemController> {
    String getTotalMoney();
    int getItemCount();
    int getItemTypeCount();
    String getSavedDate();
    Optional<IOrderController> payOrder();
}
