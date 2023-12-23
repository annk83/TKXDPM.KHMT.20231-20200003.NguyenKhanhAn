package repo;

import domain.IItemAdminDomain;
import domain.IItemDomain;

import java.util.Optional;

public interface IItemRepository {

    IItemDomain getItemById(int id) throws ItemDeletedException;
    IItemDomain save(IItemDomain item);
    void delete(int id);
}
