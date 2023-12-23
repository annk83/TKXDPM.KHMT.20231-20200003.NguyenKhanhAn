package dmui.content;

import controller.invoice.IInvoiceController;
import controller.invoice.IInvoiceItemController;

import javax.swing.*;
import java.awt.*;

public class InvoiceUI extends BaseContent {
    private final IInvoiceController invoiceController;
    private final PagedListView<IInvoiceItemController, IInvoiceController> pagedListView;
    private final JButton pay = new JButton("pay"), cancel = new JButton("cancel"), close = new JButton("close");
    private final JLabel paymentSummary = new JLabel();
    private final JLabel deliverySummary = new JLabel();
    public InvoiceUI(IInvoiceController invoiceController) {
        super("Invoice Viewing",new BorderLayout());
        pagedListView = new PagedListView<>(InvoiceItemUI::new);
        this.invoiceController = invoiceController;
        JPanel left = new JPanel(new BorderLayout());
        add(left, BorderLayout.CENTER);
        left.add(pagedListView, BorderLayout.PAGE_START);

        var btns = new JPanel();
        btns.setLayout(new GridBagLayout());
        var c = new GridBagConstraints();
        btns.add(pay, c);
        c.gridy = 1;
        pay.addActionListener(e->{
            invoiceController.pay();
            reset();
        });
        btns.add(cancel, c);
        c.gridy = 2;
        btns.add(close, c);
        add(btns, BorderLayout.LINE_END);

        JLabel orderSummary = new JLabel(invoiceController.getOrderSummary());
        reset();
    }

    @Override
    public void reset() {
        pay.setEnabled(!invoiceController.isPayed());
        cancel.setEnabled(invoiceController.isCancellable());
        paymentSummary.setText(invoiceController.getPaymentSummary());
    }
}
