package annk.aims.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderState {
    private long userId;
    private ShipForm shipForm;
    private boolean isRush;
    private Collection<Integer> itemList;
    private Collection<Integer> rushList;
    private RushForm rushForm;
    private Date saved;
}
