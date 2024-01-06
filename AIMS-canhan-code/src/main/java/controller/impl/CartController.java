package controller.impl;

import controller.IOrderStarter;
import controller.cart.ICartController;
import controller.cart.ICartItemController;
import domain.ICartItemDomain;
import utils.IEtc;
import controller.utils.IPaginatorController;
import domain.ICartDomain;

import java.util.ArrayList;
import java.util.List;

public class CartController extends BaseController implements ICartController {
    final ICartDomain cart;
    final BoundedPaginator paginator;
    final IOrderStarter iOrderStarter;

    public CartController(ICartDomain cart, IEtc etc, IOrderStarter iOrderStarter) {
        super(etc);
        this.cart = cart;
        paginator = new BoundedPaginator(this::getItemTypeCount, etc.getDefaultPageSize());
        this.iOrderStarter = iOrderStarter;
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
    public boolean payOrder() {
        if(cart.hasEnough()) {
            var lst = cart.getAll();
            List<Integer> countList = new ArrayList<>();
            List<Long> itemIds = new ArrayList<>();
            for(ICartItemDomain item : lst) {
                countList.add(item.getCount());
                itemIds.add(item.getItemId());
            }
            iOrderStarter.payOrder(itemIds, countList);
            return true;
        }
        return false;
    }
}
