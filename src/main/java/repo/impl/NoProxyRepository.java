package repo.impl;

import domain.IItemDomain;
import lombok.RequiredArgsConstructor;
import repo.IItemRepository;
import repo.ItemDeletedException;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class NoProxyRepository implements IItemRepository {
    private final SimpleConnection simpleConnection;

    @Override public IItemDomain getItemById(int id) throws ItemDeletedException {
        var i = new SimpleItem();
        try {
            return simpleConnection.doStuff(conn -> {
                try (var stmt = conn.prepareStatement("SELECT type, title, price, remain, image, deleted FROM items WHERE item_id = ?")) {
                    stmt.setInt(1, id);
                    var rs = stmt.executeQuery();
                    if (rs.next()) {
                        i.setItemId(id);
                        i.setType(rs.getString(1));
                        i.setTitle(rs.getString(2));
                        i.setPrice(rs.getLong(3));
                        i.setRemain(rs.getLong(4));
                        var k = rs.getAsciiStream(5);
                        i.setImage(k != null? k.readAllBytes() : null);
                        i.setDeleted(rs.getBoolean(6));
                        if (i.isDeleted()) throw new ItemDeletedException();
                        return i;
                    } else throw new NoSuchElementException();
                }
            });
        } catch (ItemDeletedException exception) {
            throw exception;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public IItemDomain save(IItemDomain item) {
        return item;
    }

    @Override
    public void delete(int id) {
    }
}
