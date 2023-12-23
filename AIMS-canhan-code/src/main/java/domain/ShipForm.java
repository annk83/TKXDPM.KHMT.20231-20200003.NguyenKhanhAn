package domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ShipForm {
    private String rushNote,rushAddress, name, phone, note, address;
    private IProvinceDomain rushProvince, province;
}
