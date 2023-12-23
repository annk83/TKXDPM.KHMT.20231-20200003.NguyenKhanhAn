package payment;
import lombok.Data;
import java.util.Date;

@Data
public class PaymentResult {
    private boolean success;
    private String status;
    private String description;
    private Date createDate;
    private long amount;
    private String nativeData;
}
