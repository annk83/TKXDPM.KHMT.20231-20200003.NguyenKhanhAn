package domain.impl;

import dmui.IMessageDisplayer;
import domain.*;
import lombok.Getter;
import repo.IItemRepository;
import repo.ItemDeletedException;
import utils.IEtc;

import java.util.ArrayList;
import java.util.List;

public class LocalOrder implements IOrderDomain {
    private final List<IOrderItemDomain> items;
    private boolean rushing = false;
    @Getter
    private final ShipForm shipForm;
    private final IEtc config;

    public LocalOrder(IEtc config, List<Integer> itemIds, List<Integer> count, ShipForm shipForm, IItemRepository repo, IMessageDisplayer iMessageDisplayer) {
        this.config = config;
        this.shipForm = shipForm == null ? new ShipForm() : shipForm;
        if(itemIds == null || count == null)
            throw new IllegalArgumentException("Not null");
        if(itemIds.size() != count.size())
            throw new IllegalArgumentException("Not match size");
        ArrayList<IOrderItemDomain> arr = new ArrayList<>();
        for(int i=0;i<itemIds.size();++i) {
            try {
                arr.add(new OrderItemProxy(repo.getItemById(itemIds.get(i)), count.get(i), false));
            } catch (ItemDeletedException itemDeletedException) {
                iMessageDisplayer.displayInformation("Item with id '%s' has deleted by admin".formatted(itemIds.get(i)), "It has been removed from cart");
            }
        }
        items = arr;
    }

    @Override
    public int getTotalWeight() {
        return items.stream().map(IOrderItemDomain::getWeight).reduce(Integer::sum).orElse(0);
    }

    @Override
    public long getRawMoney() {
        return items.stream().map(IOrderItemDomain::getTotalPrice).reduce(Long::sum).orElse(0L);
    }

    @Override
    public long getTotalMoney() {
        return getRawMoney() + getTaxMoney() + getShipMoney();
    }

    @Override
    public long getTaxMoney() {
        return (long)(getRawMoney() * config.getTax());
    }

    @Override
    public boolean isRushing() {
        return rushing;
    }

    @Override
    public void setRushing(boolean b) {
        rushing = b;
    }

    @Override
    public long getShipMoney() {
        int weight = getTotalWeight();
        long ship = 0;
        if(!items.stream().allMatch(IOrderItemDomain::isRushing)) {
            if(shipForm.getProvince() != null) {
                var shipPolicy = shipForm.getProvince().getShippingPolicy().orElse(config.getShippingConfig());
                long money = items.stream().filter(i -> !i.isRushing()).map(IOrderItemDomain::getTotalPrice).reduce(Long::sum).orElse(0L);
                ship += shipPolicy.calculateNormal(money, weight);
            }
        }
        if(rushing && items.stream().anyMatch(IOrderItemDomain::isRushing)) {
            if(shipForm.getRushProvince() != null) {
                var shipPolicy = shipForm.getRushProvince().getShippingPolicy().orElse(config.getShippingConfig());
                int count = (int) items.stream().filter(IOrderItemDomain::isRushing).count();
                ship += shipPolicy.calculateRush(weight, count);
            }
        }
        return ship;
    }

    @Override
    public int getItemTypeCount() {
        return items.size();
    }

    @Override
    public boolean hasEnough() {
        return items.stream().allMatch(ICartItemDomain::hasEnough);
    }

    @Override
    public int getItemCount() {
        return items.stream().map(ICartItemDomain::getCount).reduce(Integer::sum).orElse(0);
    }

    @Override
    public List<? extends IOrderItemDomain> getPage(int u, int v) {
        return items.subList(Math.min(items.size()-1, u), Math.min(items.size(), v));
    }

    @Override
    public int getRushItemCount() {
        return (int)items.stream().filter(IOrderItemDomain::isRushing).count();
    }

    @Override
    public boolean isRushable() {
        return items.stream().anyMatch(IOrderItemDomain::isRushable);
    }
}
