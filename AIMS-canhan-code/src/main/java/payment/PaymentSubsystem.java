package payment;

import utils.Pair;

import java.util.List;

public interface PaymentSubsystem {
    List<Pair<IPayment, String>> getPaymentList();
}
