package controller.impl;

import controller.cart.ICartController;
import controller.cart.ICartItemController;
import controller.order.IOrderController;
import repo.IProvinceRepo;
import utils.IEtc;
import controller.utils.IPaginatorController;
import domain.ICartDomain;

import java.util.List;
import java.util.Optional;

public class CartController extends BaseController implements ICartController {
    final ICartDomain cart;
    final BoundedPaginator paginator;
    final IProvinceRepo iProvinceRepo;

    public CartController(ICartDomain cart, IEtc etc, IProvinceRepo iProvinceRepo) {
        super(etc);
        this.cart = cart;
        paginator = new BoundedPaginator(this::getItemTypeCount, etc.getDefaultPageSize());
        this.iProvinceRepo = iProvinceRepo;
    }

    @Override
    public String getTotalMoney() {
        return formatMoney(cart.getRawPrice());
    }

    @Override
    public int getItemCount() {
        return cart.countItem();
    }

    @Override
    public int getItemTypeCount() {
        return cart.countItemType();
    }

    @Override
    public String getSavedDate() {
        return formatDate(cart.getSavedDate());
    }

    @Override
    public IPaginatorController getPaginatorController() {
        return paginator;
    }

    @Override
    public List<ICartItemController> getPage(int u, int v) {
        return cart.getPage(u, v).stream().map(i->(ICartItemController)new CartItemController(i, this, config)).toList();
    }

    @Override
    public Optional<IOrderController> payOrder() {
        if(cart.hasEnough()) return Optional.of(new OrderController(config, cart.startOrder(), iProvinceRepo));
        else return Optional.empty();
    }
}
