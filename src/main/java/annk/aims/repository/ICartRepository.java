package annk.aims.repository;

import annk.aims.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartRepository extends JpaRepository<CartEntity, CartEntity.Key> {
    void deleteByCompositeKeyUserId(long userId);
}
