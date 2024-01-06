package repo;

import domain.IItemDomain;

public interface IItemRepository {

    IItemDomain getItemById(long id) throws ItemDeletedException;
}
