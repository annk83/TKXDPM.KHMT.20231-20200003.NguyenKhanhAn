package domain.impl;

import dmui.IMessageDisplayer;
import domain.ICartDomain;
import domain.ICartItemDomain;
import repo.IItemRepository;
import repo.ItemDeletedException;
import utils.IEtc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class LocalCart implements ICartDomain {
    private final Map<Long, ICartItemDomain> idMap;
    private final List<ICartItemDomain> items;
    private final Date savedDate;
    private final IItemRepository repository;
    private final IEtc config;
    private final IMessageDisplayer messager;
    public LocalCart(InputStream is, IItemRepository iItemProvider, IMessageDisplayer iMessageDisplay, IEtc etc) throws IOException {
        Properties properties = new Properties();
        messager = iMessageDisplay;
        this.config = etc;
        properties.load(is);
        idMap = new HashMap<>();
        this.repository = iItemProvider;
        try {
            List<Long> ids = Arrays.stream(properties.getProperty("itemIds").split(",")).map(Long::parseLong).toList();
            List<Integer> cnts = Arrays.stream(properties.getProperty("counts").split(",")).map(Integer::parseInt).toList();
            if(ids.size() != cnts.size()) throw new IOException("data corrupted");
            items = new ArrayList<>();
            for(int i=0;i<ids.size();++i) {
                if(idMap.containsKey(ids.get(i))) throw new IOException("data corrupted");
                addItem(ids.get(i), cnts.get(i));
            }
            savedDate = new Date(Long.parseLong(properties.getProperty("lastSaved")));
        } catch (NullPointerException | NumberFormatException ex) {
            throw new IOException("data corrupted", ex);
        }
    }

    @Override
    public List<ICartItemDomain> getPage(int u, int v) {
        return Collections.unmodifiableList(items.subList(u, Math.min(v, items.size())));
    }

    @Override
    public void addItem(long itemId, int count) {
        var k = idMap.get(itemId);
        if(k == null) {
            try {
                var item = new CartItemProxyDomain(repository.getItemById(itemId), count);
                idMap.put(itemId, item);
                items.add(item);
            } catch (ItemDeletedException exception) {
                messager.displayInformation("Item with id '%s' has deleted by admin".formatted(itemId), "It has been removed from cart");
            }
        }
        else k.setCount(k.getCount() + count);
    }

    @Override
    public void removeItem(long item) {
        items.remove(idMap.remove(item));
    }

    @Override
    public long getRawPrice() {
        return items.stream().map(ICartItemDomain::getTotalPrice).reduce(Long::sum).orElse(0L);
    }

    @Override
    public Date getSavedDate() {
        return savedDate;
    }

    @Override
    public int countItemType() {
        return items.size();
    }

    @Override
    public int countItem() {
        return items.stream().map(ICartItemDomain::getCount).reduce(Integer::sum).orElse(0);
    }

    @Override
    public boolean hasEnough() {
        return items.stream().allMatch(ICartItemDomain::hasEnough);
    }

    @Override
    public List<ICartItemDomain> getAll() {
        return new ArrayList<>(idMap.values());
    }

    public void saveToOutputStream(OutputStream os) throws IOException {
        StringBuilder ids = new StringBuilder(), cnts = new StringBuilder();
        for(int i=0;i<items.size();++i) {
            ids.append(items.get(i).getItemId());
            cnts.append(items.get(i).getCount());
            if(i+1 != items.size()) {
                ids.append(",");
                cnts.append(",");
            }
        }
        Properties properties = new Properties();
        properties.setProperty("itemIds", ids.toString());
        properties.setProperty("counts", cnts.toString());
        properties.setProperty("lastSaved", String.valueOf(new Date().getTime()));
        properties.store(os, null);
    }
}
