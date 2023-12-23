package payment;

import controller.form.ISimpleForm;

public interface IPayment {
    ISimpleForm paymentForm();
    PaymentResult doPayment();
}
