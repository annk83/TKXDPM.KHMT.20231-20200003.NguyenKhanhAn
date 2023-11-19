package annk.aims.domain;
import annk.aims.entity.CartEntity;
import annk.aims.repository.ICartRepository;
import annk.aims.services.ICatalog;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class Cart implements ICart {
    private final Map<Integer, Integer> data = new HashMap<>();
    private final ICartRepository repository;
    private final ICatalog catalog;
    private final long userId;

    @Override
    public void setItem(int item, int count) {
        data.put(item, count);
        repository.getReferenceById(new CartEntity.Key(userId, item)).setCount(count);
    }
    @Override
    public void addItem(int item, int count) {
        if(data.containsKey(item)) {
            count += data.get(item);
        }
        setItem(item, count);
    }
    @Override
    public void removeItem(int item) {
        data.remove(item);
        repository.deleteById(new CartEntity.Key(userId, item));
    }
    @Override
    public void emptyCart() {
        data.clear();
        repository.deleteByCompositeKeyUserId(userId);
    }
    @Override
    public CartItem getItem(int item) {
        if(data.containsKey(item)) {
            CartItem cartItem = new CartItem();
            List<?> field = catalog.getItemInfo(item, List.of("name", "price", "image"));
            cartItem.setItemId(item);
            cartItem.setName((String) field.get(0));
            cartItem.setCount((Integer) field.get(1));
            try {
                cartItem.setImage(ImageIO.read(new ByteArrayInputStream((byte[]) field.get(2))));
            } catch (Exception exception) {
                cartItem.setImage(new BufferedImage(0,0,BufferedImage.TYPE_CUSTOM));
            }
            cartItem.setCount(data.get(item));
            return cartItem;
        }
        else throw new IllegalArgumentException("Item not in cart");
    }
    @Override
    public List<CartItem> getAllCartItem() {
        ArrayList<CartItem> ret = new ArrayList<>();
        for(int i : data.keySet())
            ret.add(getItem(i));
        return ret;
    }
}
