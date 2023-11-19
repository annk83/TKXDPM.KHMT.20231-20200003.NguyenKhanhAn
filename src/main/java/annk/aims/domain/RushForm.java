package annk.aims.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RushForm {
    @NotNull(message = "*Required")
    private Integer provinceId;
    @NotNull(message = "*Required") @NotBlank(message = "*Required")
    private String address;
    private String note;

    @Override
    public RushForm clone() {
        RushForm shipForm = new RushForm();
        shipForm.setAddress(address);
        shipForm.setProvinceId(provinceId);
        shipForm.setNote(note);
        return shipForm;
    }
}
