package controller.impl;

import controller.form.ISimpleForm;
import controller.order.*;
import repo.IProvinceRepo;
import utils.IEtc;
import controller.utils.IPaginatorController;
import domain.IOrderDomain;
import java.util.List;
import java.util.Optional;

public class OrderController extends BaseController implements IRushOrderController, IRushOrderItemListController {
    private final BoundedPaginator boundedPaginator;
    private final IOrderDomain order;
    private final ShipForm shipForm;
    private final IdMapped<Runnable> runnableIdMapped = new IdMapped<>();
    public OrderController(IEtc iEtcController, IOrderDomain order, IProvinceRepo iProvinceRepo) {
        super(iEtcController);
        this.order = order;
        boundedPaginator = new BoundedPaginator(order::getItemTypeCount, config.getDefaultPageSize());
        shipForm = new ShipForm(iProvinceRepo, order);
        shipForm.addChangeListener(()->runnableIdMapped.foreach(Runnable::run));
    }
    @Override
    public boolean checkRemain() {
        return order.hasEnough();
    }
    @Override
    public int getItemCount() {
        return order.getItemCount();
    }

    @Override
    public int getItemTypeCount() {
        return order.getItemTypeCount();
    }

    @Override
    public String getTotalWeight() {
        return formatWeight(order.getTotalWeight());
    }

    @Override
    public String getTotalRawMoney() {
        return formatMoney(order.getRawMoney());
    }

    @Override
    public String getTaxMoney() {
        return formatMoney(order.getTaxMoney());
    }

    @Override
    public String getShipMoney() {
        return formatMoney(order.getShipMoney());
    }

    @Override
    public String getTotalMoney() {
        return formatMoney(order.getTotalMoney());
    }

    @Override
    public ISimpleForm getShipForm() {
        return shipForm;
    }

    @Override
    public IOrderController toNormalOrder() {
        order.setRushing(false);
        return this;
    }

    @Override
    public int getRushItemCount() {
        return order.getRushItemCount();
    }

    @Override
    public IRushOrderItemListController getItemListController() {
        return this;
    }

    @Override
    public Optional<IRushOrderController> toRushOrder() {
        if(order.isRushing()) return Optional.of(this);
        if(order.isRushable()) {
            order.setRushing(true);
            return Optional.of(this);
        }
        else return Optional.empty();
    }

    @Override
    public boolean isRushing() {
        return order.isRushing();
    }

    @Override
    public int addChangeListener(Runnable runnable) {
        return runnableIdMapped.addObj(runnable);
    }

    @Override
    public void removeChangeListener(int runnable) {
        runnableIdMapped.removeByKey(runnable);
    }

    @Override
    public IPaginatorController getPaginatorController() {
        return boundedPaginator;
    }

    @Override
    public List<? extends IRushOrderItemController> getPage(int u, int v) {
        return order.getPage(u, v).stream().map(i->new OrderItemController(this, i, ()->runnableIdMapped.foreach(Runnable::run), config)).toList();
    }
}
