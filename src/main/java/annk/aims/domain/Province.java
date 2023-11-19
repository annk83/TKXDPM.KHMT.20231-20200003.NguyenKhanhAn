package annk.aims.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {
    private Integer id;
    private String name;
    private double shipStart;
    private double startPrice;
    private double extendedPrice;

    public Integer getId() {
        return id;
    }
}
