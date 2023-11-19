package annk.aims.ui;

import annk.aims.domain.CartItem;
import annk.aims.domain.ICart;

import javax.swing.*;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Vector;

@org.springframework.stereotype.Component
public class ViewCartUI extends Screen {
    private JPanel root;
    private final ICart cart;
    private JList<CartItem> uiList;
    private Vector<CartItem> items;

    public ViewCartUI(ICart cart) throws IOException {
        this.cart = cart;
        this.items = new Vector<>(cart.getAllCartItem());
        root = new JPanel();
        root.setLayout(new BorderLayout());
        root.add(topLabel(), BorderLayout.PAGE_START);
        root.add(center(), BorderLayout.CENTER);
    }

    public void reload() {
        this.items = new Vector<>(cart.getAllCartItem());
        uiList.setListData(items);
    }

    public JPanel getPanel() {
        return root;
    }

    private JSplitPane center() {
        var ret = new JSplitPane();
        ret.setLeftComponent(new JScrollPane(createList()));
        ret.setRightComponent(createButtons());
        ret.setDividerSize(0);
        ret.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ((JSplitPane)e.getComponent()).setDividerLocation(e.getComponent().getWidth() - 120);
            }
        });
        return ret;
    }

    private JPanel createButtons() {
        return new JPanel();
    }

    private JList<CartItem> createList() {
        JList<CartItem> ret = new JList<>(new Vector<>(items));
        uiList = ret;
        ret.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ret.setCellRenderer(new MyCellRenderer());
        ret.setSelectionModel(new DefaultListSelectionModel() {
            private int count;
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if(super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                    --count;
                }
                else {
                    super.addSelectionInterval(index0, index1);
                    ++count;
                }
            }
        });
        return ret;
    }

    private JPanel topLabel() {
        JPanel p = new JPanel();
        p.setBorder(new StrokeBorder(new BasicStroke(0)));
        p.setLayout(new BorderLayout());


        JLabel ret = new JLabel();
        ret.setText("My Cart");
        ret.setFont(new Font("Times New Roman", Font.PLAIN, 36));
        ret.setForeground(new Color(10, 10, 10));
        ret.setBackground(new Color(0, 0, 0, 0));

        p.add(ret, BorderLayout.CENTER);
        var btn = new JButton();
        btn.add(new JLabel("Place order"));
        btn.addActionListener((ac)->{
        });
        p.add(btn, BorderLayout.LINE_END);
        return p;
    }

    private static class MyCellRenderer implements ListCellRenderer<CartItem> {
        private static final Color bg = new Color(158, 208, 222);
        @Override
        public Component getListCellRendererComponent(JList<? extends CartItem> jList, CartItem cartItem, int i, boolean b, boolean b1) {
            JPanel ret = new JPanel();
            ret.setLayout(new FlowLayout(FlowLayout.LEFT));
            ret.add(new JLabel(new ImageIcon(cartItem.getImage().getScaledInstance(-1, 150, Image.SCALE_DEFAULT))));
            ret.add(new JLabel("<html>%s. %s<br>Number of item: %s<br>Total cost: %s x %s = %s</html>".formatted(i+1, cartItem.getName(), cartItem.getCount(), cartItem.getCount(), cartItem.getPrice(), cartItem.getPrice() * cartItem.getCount())));
            if(b) ret.setBackground(bg);
            return ret;
        }
    }
}
