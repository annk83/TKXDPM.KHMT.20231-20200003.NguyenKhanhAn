package dmui.content;

import controller.utils.IPaginatorController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;

public class PaginatorUI extends BasePanel {
    private final JLabel description;
    private final JButton next;
    private final JButton prev;
    private final JButton last;
    private final JButton first;
    private final DefaultComboBoxModel<Integer> pageSizeModel;
    private final JComboBox<Integer> chooser;
    private IPaginatorController controller;

    private ItemListener itemListener;

    public PaginatorUI() {
        super(new BorderLayout());
        JPanel left = new JPanel();
        this.add(left, BorderLayout.LINE_END);
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        description = new JLabel();
        next = new JButton(">");
        prev = new JButton("<");
        last = new JButton(">>");
        first = new JButton("<<");
        pageSizeModel = new DefaultComboBoxModel<>();
        chooser = new JComboBox<>(pageSizeModel);
        left.add(description);
        left.add(first);
        left.add(prev);
        left.add(next);
        left.add(last);
        left.add(new JLabel("Item per page"));
        left.add(chooser);
    }
    public PaginatorUI(IPaginatorController controller, java.util.List<Integer> sizes) {
        this();
        setController(controller);
        setPageSizeList(sizes);
    }

    public void setController(IPaginatorController controller) {
        this.controller = controller;

        if(itemListener != null) chooser.removeItemListener(itemListener);
        chooser.addItemListener(itemListener = e -> controller.setPageSize((Integer)e.getItem()));
        controller.addCallback(this::render);
        controller.invokeAllCallback();

        next.addActionListener(ev->controller.nextPage());
        prev.addActionListener(ev->controller.prevPage());
        last.addActionListener(ev->controller.lastPage());
        first.addActionListener(ev->controller.firstPage());

    }
    public void setPageSizeList(java.util.List<Integer> sizes) {
        if(sizes == null || sizes.isEmpty()) throw new IllegalArgumentException("Not null or empty");
        pageSizeModel.removeAllElements();
        pageSizeModel.addAll(sizes);
        pageSizeModel.setSelectedItem(sizes.get(0));
    }
    private void render(int st, int ed, int ic) {
        description.setText("%s - %s of %s".formatted(st+1, ed, ic));
        if(ed == ic) {
            next.setEnabled(false);
            last.setEnabled(false);
        }
        if(st == 0) {
            prev.setEnabled(false);
            first.setEnabled(false);
        }
    }
}
