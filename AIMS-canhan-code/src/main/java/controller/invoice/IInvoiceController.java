package controller.invoice;

import controller.utils.IPageableController;

public interface IInvoiceController extends IPageableController<IInvoiceItemController> {
    String getOrderSummary();

    String getPaymentSummary();

    boolean isPayed();

    boolean isCancellable();
    void pay();
}
