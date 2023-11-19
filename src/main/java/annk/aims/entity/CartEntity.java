package annk.aims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
public class CartEntity {
    @Embeddable @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class Key {
        @Column(name = "user_id")
        private Long userId;
        @Column(name = "item_id")
        private long item_id;
    }
    @Id
    private Key compositeKey;
    private int count;
}
