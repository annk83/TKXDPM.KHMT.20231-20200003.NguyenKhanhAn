package annk.aims.domain;

import annk.aims.validator.VnPhone;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipForm {
    @NotNull(message = "*Required") @NotBlank(message = "*Required")
    private String fullName;
    @NotNull @VnPhone(message = "*Vietnamese phone number")
    private String phone;
    @NotNull(message = "*Required")
    private Integer provinceId;
    @NotNull(message = "*Required") @NotBlank(message = "*Required")
    private String address;
    private String note;

    @Override
    public ShipForm clone() {
        ShipForm shipForm = new ShipForm();
        shipForm.setAddress(address);
        shipForm.setPhone(phone);
        shipForm.setFullName(fullName);
        shipForm.setProvinceId(provinceId);
        return shipForm;
    }
}
