package dmui.content;

import controller.order.IOrderController;
import controller.order.IOrderItemController;
import controller.order.IOrderItemListController;
import controller.order.IRushOrderController;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class PlaceOrderUI extends SplitPane {
    private IOrderController controller;
    private final SimpleFormUI shipForm;
    private final JButton cancel=new JButton("Cancel"), submit=new JButton("Submit"), rush=new JButton("Rush");
    private int formSubcription = -1;
    private final JLabel summaryText = new JLabel();
    private JLabel getFormShorted() {
        return (JLabel)getLeftShrink();
    }
    private JLabel getItemListShorted() {
        return (JLabel)getRightShrink();
    }
    private PagedListView<IOrderItemController, IOrderItemListController> listView;
    public PlaceOrderUI() {
        super("Place Order", new JPanel(new BorderLayout()), new JLabel(), new JPanel(new BorderLayout()), new JLabel());
        listView = new PagedListView<> (PlaceOrderItemUI::new);
        getRightExpand().add(listView, BorderLayout.CENTER);
        getRightExpand().add(summaryText, BorderLayout.PAGE_END);
        JPanel leftExpand = (JPanel)getLeftExpand();
        shipForm =  new SimpleFormUI();
        leftExpand.add(shipForm, BorderLayout.CENTER);
        JPanel btns = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets =new Insets(20, 10, 30, 10);
        c.weightx = 1.0;
        btns.add(cancel, c);
        c.gridx++;
        btns.add(submit, c);
        c.gridx++;
        btns.add(rush, c);
        leftExpand.add(btns, BorderLayout.PAGE_END);

        cancel.addActionListener(e->contentNavigator.changeTo(ViewCartUI.class));
        submit.addActionListener(e->{
            //TODO:
        });
        rush.addActionListener(e->{
            if(controller.isRushing()) {
                setController(((IRushOrderController)controller).toNormalOrder());
                rush.setText("Rush");
            }
            else {
                var opt = controller.toRushOrder();
                if (opt.isPresent()) {
                    setController(opt.get());
                    rush.setText("No rush");
                }
            }
        });
    }

    public void setController(IOrderController iOrderController) {
        if(iOrderController == null) throw new IllegalArgumentException("not null");
        if(formSubcription != -1)
            controller.getShipForm().removeChangeListener(formSubcription);
        controller = iOrderController;
        var formController = controller.getShipForm();
        formSubcription = controller.addChangeListener(this::updateForm);
        shipForm.setCurrentForm(formController);
        listView.setController(iOrderController.getItemListController());
        reset();
    }

    private void updateForm() {
        var c = controller;
        shipForm.reload();
        submit.setEnabled(c.getShipForm().isValid());
        getItemListShorted().setText((
            "<html>%s:<br>" +
            "%s types,&nbsp;" + "%s items<br>" +
            "<br>" +
            "<br>" + "<table style='display:none'>" +
            "<tr><td>Cost:</td><td>%s</td></tr>"+
            "<tr><td>Taxed:</td><td>%s</td></tr>"+
            "<tr><td>Ship:</td><td>%s</td></tr>"+
            "<tr><td>Total:</td><td>%s</td></tr>"+
            "</table></html>").formatted(controller.isRushing() ? "Rushing":"Normal Ship", c.getItemTypeCount(), c.getItemCount(), c.getTotalRawMoney(), c.getTaxMoney(), c.getShipMoney(), c.getTotalMoney()));
        var cc = controller.getShipForm();
        var form = cc.toStringModel(ShipForm.class);
        getFormShorted().setText(("<html>" +
                "%s<br>" +
                "%s<br>" +
                "%s %s<br>" +
                "<b>Note</b>:<br>" +
                "<em>%s</em>"+
                "<br>" + "<table style='display:none'>" +
                "<tr><td>Cost:</td><td>%s</td></tr>"+
                "<tr><td>Taxed:</td><td>%s</td></tr>"+
                "<tr><td>Ship:</td><td>%s</td></tr>"+
                "<tr><td>Total:</td><td>%s</td></tr>"+
                "</table></html>"
        ).formatted(form.name, form.phone, form.address, form.province, form.note, c.getTotalRawMoney(), c.getTaxMoney(), c.getShipMoney(), c.getTotalMoney()));
        summaryText.setText(("<html>" +
                "<table style='display:none'>"+
                "<tr><td>Order Weight:</td><td>%s</td></tr>"+
                "<tr><td>Order Raw Money:</td><td>%s</td></tr>"+
                "<tr><td>Order Tax:</td><td>%s</td></tr>"+
                "<tr><td>Order Ship Fee:</td><td>%s</td></tr>"+
                "<tr><td>Total:</td><td>%s</td></tr>"+
                "</table></html>").formatted(c.getTotalWeight(), c.getTotalRawMoney(), c.getTaxMoney(), c.getShipMoney(), c.getTotalMoney()));
    }

    @Override
    public void reset() {
        if(controller != null) {
            shipForm.reload();
            updateForm();
        }
    }

    @Data
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ShipForm {
        private String name, phone, province, note, address;
    }
}
