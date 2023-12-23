package controller.order;
import controller.form.ISimpleForm;
import controller.utils.ChangeListener;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface IOrderController extends ChangeListener {
    boolean checkRemain();
    int getItemCount();
    int getItemTypeCount();
    String getTotalWeight();
    String getTotalRawMoney();
    String getTaxMoney();
    String getShipMoney();
    String getTotalMoney();
    IOrderItemListController getItemListController();
    ISimpleForm getShipForm();
    Optional<IRushOrderController> toRushOrder();

    boolean isRushing();
}
