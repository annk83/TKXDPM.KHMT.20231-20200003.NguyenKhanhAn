package annk.aims.ui;

import annk.aims.Utils;
import annk.aims.controller.PlaceOrderController;
import annk.aims.domain.*;
import annk.aims.services.IPlaceOrderStateService;
import annk.aims.services.IProvince;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Component
public class PlaceOrderUI extends Screen {
    private JPanel root;
    private PlaceOrderState state;
    private final IPlaceOrderStateService placeOrderStateService;
    private final IProvince iProvince;
    private final PlaceOrderController placeOrderController;
    private final ShipFormUI shipFormUI;
    private final List<Runnable> stateUpdate = new ArrayList<>();
    private final RushShipFormUI rushShipFormUI;
    private JList<PlaceOrderItem> list;
    private Vector<PlaceOrderItem> listData;

    public PlaceOrderUI(IPlaceOrderStateService placeOrderStateService, IProvince iProvince, PlaceOrderController placeOrderController) throws IOException {
        this.iProvince = iProvince;
        this.placeOrderStateService = placeOrderStateService;
        state = placeOrderStateService.get();
        shipFormUI = new ShipFormUI(state.getShipForm(), this::updateShipForm, iProvince);
        rushShipFormUI = new RushShipFormUI(state.getRushForm(), this::updateRushForm, iProvince);
        this.placeOrderController = placeOrderController;
        initUI();
    }

    public void reload() {
        state = placeOrderStateService.get();
        stateUpdate.forEach(Runnable::run);
        listData = new Vector<>(getItemList());
        list.setListData(listData);
    }

    private void updateRushForm(RushForm rushForm) {
        state.setRushForm(rushForm);
        placeOrderStateService.save(state);
        stateUpdate.forEach(Runnable::run);
    }

    private void initUI() throws IOException {
        root = new JPanel();
        root.setLayout(new BorderLayout());
        root.add(createHeaderBar("Place Order"), BorderLayout.PAGE_START);
        root.add(contentPanel(), BorderLayout.CENTER);
    }

    private List<PlaceOrderItem> getItemList() {
        return state.getItemList().stream().map(placeOrderController::get).peek(i->{
            if(state.isRush() && state.getRushList().contains(i.getItemId())) {
                i.setRush(true);
            }
        }).toList();
    }

    private JSplitPane contentPanel() {
        JSplitPane jSplitPane = new JSplitPane();
        jSplitPane.setBorder(new StrokeBorder(new BasicStroke(30), new Color(241,255,255)));
        jSplitPane.setDividerSize(0);
        AtomicBoolean leftExpand = new AtomicBoolean(true);
        jSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = e.getComponent().getWidth();
                int w = (int)(width * (leftExpand.get() ? 0.8 : 0.2));
                jSplitPane.setDividerLocation(w);
            }
        });

        JPanel leftShorted = new JPanel(new BorderLayout());
        JPanel rightShorted = new JPanel(new BorderLayout());

        JLabel shortedShipLeft = new JLabel();
        JLabel shortedContentRight = new JLabel();
        JLabel savedDate1= new JLabel();
        JLabel savedDate2= new JLabel();
        rightShorted.setBorder(new EmptyBorder(10,10,10,10));
        rightShorted.add(shortedContentRight, BorderLayout.PAGE_START);
        rightShorted.add(savedDate2, BorderLayout.PAGE_END);
        leftShorted.setBorder(new EmptyBorder(10,10,10,10));
        leftShorted.add(shortedShipLeft, BorderLayout.PAGE_START);
        leftShorted.add(savedDate1, BorderLayout.PAGE_END);
        JLabel formName = new JLabel();
        JPanel formHolder = new JPanel(new BorderLayout());
        formHolder.add(shipFormUI.getPanel(), BorderLayout.PAGE_START,0);
        formHolder.add(createButtonPanel(), BorderLayout.CENTER,1);

        Runnable updateTask = ()->{
            if(state.isRush()) {
                if(formHolder.getComponent(0) == shipFormUI.getPanel()) {
                    formHolder.remove(0);
                    formHolder.add(rushShipFormUI.getPanel(), BorderLayout.PAGE_START, 0);
                    formHolder.invalidate();
                }
            }
            else {
                if(formHolder.getComponent(0) == rushShipFormUI.getPanel()) {
                    formHolder.remove(0);
                    formHolder.add(shipFormUI.getPanel(), BorderLayout.PAGE_START, 0);
                    formHolder.invalidate();
                }
            }
            formName.setText(state.isRush() ? "Rush information" : "Shipping information");
            var placeOrderItems = getItemList();
            Prices prices = calculatePrice(placeOrderItems);
            savedDate1.setText("Saved at "+new SimpleDateFormat("dd/MM/yyyy HH:mm").format(state.getSaved()));
            savedDate2.setText(savedDate1.getText());
            shortedShipLeft.setText(("<html>" +
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
                    ).formatted(state.getShipForm().getFullName(),
                    state.getShipForm().getPhone(),
                    state.getShipForm().getAddress(),
                    state.getShipForm().getProvinceId() == null ? "" : iProvince.getById(state.getShipForm().getProvinceId()).getName(),
                    state.getShipForm().getNote() == null ? "" : state.getShipForm().getNote(),
                            Utils.formatMoney(prices.raw),
                            Utils.formatMoney(prices.taxed),
                            Utils.formatMoney(prices.ship),
                            Utils.formatMoney(prices.total))

            );
            shortedContentRight.setText((
                    "<html>Normal Ship:<br>" +
                    "%s types,&nbsp;" + "%s items<br>" +
                    "<br>" +
                    "Rush:<br>" +
                    "%s types,&nbsp; " + "%s items<br>" +
                    "<br>" + "<table style='display:none'>" +
                    "<tr><td>Cost:</td><td>%s</td></tr>"+
                    "<tr><td>Taxed:</td><td>%s</td></tr>"+
                    "<tr><td>Ship:</td><td>%s</td></tr>"+
                    "<tr><td>Total:</td><td>%s</td></tr>"+
                            "</table></html>").formatted(
                    placeOrderItems.stream().filter(i->!i.isRush()).count(),
                    placeOrderItems.stream().filter(i->!i.isRush()).map(PlaceOrderItem::getCount).reduce(Integer::sum).orElse(0),
                    placeOrderItems.stream().filter(PlaceOrderItem::isRush).count(),
                    placeOrderItems.stream().filter(PlaceOrderItem::isRush).map(PlaceOrderItem::getCount).reduce(Integer::sum).orElse(0),
                    Utils.formatMoney(prices.raw),
                    Utils.formatMoney(prices.taxed),
                    Utils.formatMoney(prices.ship),
                    Utils.formatMoney(prices.total)));
        };
        stateUpdate.add(updateTask);

        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new JPanel(new BorderLayout());
        JButton lb = new JButton(">");
        JButton rb = new JButton("<");
        left.add(formHolder, BorderLayout.CENTER);

        formName.setFont(new Font("Times new roman", Font.BOLD, 26));
        left.add(formName, BorderLayout.PAGE_START);
        left.setBorder(new EmptyBorder(10, 10, 10, 10));

        Consumer<Boolean> runnable = new Consumer<>() {
            private Boolean last = null;
            @Override
            public void accept(Boolean aBoolean) {
                if(last == null || !Objects.equals(aBoolean, last)) {
                    leftExpand.set(aBoolean);
                    int width = jSplitPane.getWidth();
                    jSplitPane.setDividerLocation((int)(width * (leftExpand.get() ? 0.8 : 0.2)));
                    if(aBoolean) {
                        jSplitPane.setLeftComponent(left);
                        jSplitPane.setRightComponent(rightShorted);
                    }
                    else {
                        jSplitPane.setLeftComponent(leftShorted);
                        jSplitPane.setRightComponent(right);
                    }
                    last = aBoolean;
                }
            }
        };

        lb.addActionListener(actionEvent -> {
            runnable.accept(true);
        });
        rb.addActionListener(actionEvent -> {
            runnable.accept(false);
        });

        runnable.accept(leftExpand.get());
        updateTask.run();
        JPanel p  =  new JPanel();
        p.setLayout(new GridBagLayout());
        p.setBackground(new Color(0,0,0,0));
        p.add(rb);
        p.setPreferredSize(new Dimension(100, Short.MAX_VALUE));
        left.add(p, BorderLayout.LINE_END);

        JPanel p1 = new JPanel();
        p1.setLayout(new GridBagLayout());
        p1.setBackground(new Color(0,0,0,0));
        p1.add(rb);
        p1.setPreferredSize(new Dimension(100, Short.MAX_VALUE));
        right.add(p1, BorderLayout.LINE_START);
        right.add(createList(), BorderLayout.CENTER);

        left.setBackground(new Color(236,255,255));
        right.setBackground(new Color(236,255,255));
        rb.setPreferredSize(new Dimension(40, 200));
        rb.setBorder(new EmptyBorder(0, 0, 0,0));
        rb.setBackground(new Color(175,175,175 ));
        rb.setOpaque(true);

        lb.setPreferredSize(new Dimension(40, 200));
        lb.setBorder(new EmptyBorder(0, 0, 0,0));
        lb.setBackground(new Color(175,175,175 ));
        lb.setOpaque(true);
        p.add(rb);
        p1.add(lb);

        rightShorted.setBackground(new Color(215,232,232));
        leftShorted.setBackground(new Color(215,232,232));
        return jSplitPane;
    }

    private JScrollPane createList() {
        listData = new Vector<>(getItemList());
        list = new JList<>();
        list.setBackground(new Color(236,255,255));
        list.setListData(listData);

        list.setCellRenderer((jList, placeOrderItem, i, b, b1) -> {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            JLabel jLabel = new JLabel(new ImageIcon(placeOrderItem.getImage().getScaledInstance(-1, 150, Image.SCALE_DEFAULT)));
            jPanel.add(jLabel, BorderLayout.LINE_START);
            jLabel.setBorder(new EmptyBorder(25, 25, 25, 25));
            jPanel.setBackground(Color.WHITE);
            return jPanel;
        });

        JScrollPane jScrollPane = new JScrollPane(list);
        jScrollPane.setBorder(new EmptyBorder(0,0,0,0));
        return jScrollPane;
    }

    private JButton createButton(String text) {
        JButton j = new JButton(text);
        j.setFont(new Font("Times New Roman", Font.PLAIN, 30));
        j.setOpaque(true);
        j.setBackground(new Color(175,175,175 ));
        j.setBorder(new EmptyBorder(15,25,15,25));
        return j;
    }

    private JPanel createButtonPanel() {
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(30);
        JPanel jPanel = new JPanel(flowLayout);
        jPanel.setBorder(new EmptyBorder(50, 0, 0,0));
        jPanel.setBackground(new Color(236,255,255));
        jPanel.add(createButton("Cancel"));
        jPanel.add(createButton("Place"));
        JButton sw = createButton("");
        Runnable runnable = () -> {
            if(state.isRush()) sw.setText("No Rush");
            else sw.setText("Rush!!!");
        };

        stateUpdate.add(runnable);
        runnable.run();
        sw.addActionListener(ac->{
            state.setRush(!state.isRush());
            placeOrderStateService.save(state);
            stateUpdate.forEach(Runnable::run);
        });
        jPanel.add(sw);
        return jPanel;
    }

    Prices calculatePrice(List<PlaceOrderItem> items) {
        double price = items.stream().map(i->i.getPrice() * i.getCount()).reduce(Double::sum).orElse(0.0);
        double taxed = price * placeOrderController.getTax();
        double ship;
        if(price > placeOrderController.getFreeShipThreshold())
            ship = 0;
        else {
            if(state.getShipForm().getProvinceId() == null) return new Prices((int)price,(int)taxed,-1,-1);
            Province p = iProvince.getById(state.getShipForm().getProvinceId());
            double weight = items.stream().map(i->i.getWeight()*i.getCount()).reduce(Double::sum).orElse(0.0);
            if(weight < p.getShipStart()) ship = p.getStartPrice();
            else ship = p.getShipStart() + p.getExtendedPrice() * (weight - p.getShipStart());
            ship += items.stream().filter(PlaceOrderItem::isRush).count() * placeOrderController.getRushShipCharge();
        }
        return new Prices((int)price, (int)taxed, (int)ship, (int)(price + taxed+ship));
    }


    private void updateShipForm(ShipForm shipForm) {
        state.setShipForm(shipForm);
        placeOrderStateService.save(state);
        stateUpdate.forEach(Runnable::run);
    }

    public static JPanel createHeaderBar(String menuName) throws IOException {
        if(menuName == null) throw new IllegalArgumentException("menuName != null");
        URL url = ClassLoader.getSystemClassLoader().getResource("background/aims.png");
        assert url != null;
        BackgroundPanel ret = new BackgroundPanel(ImageIO.read(url));
        ret.setBorder(new StrokeBorder(new BasicStroke(0)));
        ret.setLayout(new BorderLayout());

        JLabel menu = new JLabel(menuName);
        menu.setBackground(new Color(0,0,0,0));
        menu.setForeground(new Color(214,211,47));
        menu.setFont(new Font("Times New Roman", Font.BOLD, 40));
        menu.setVerticalAlignment(SwingConstants.TOP);
        menu.setPreferredSize(new Dimension(Short.MAX_VALUE, 80));
        ret.add(menu, BorderLayout.LINE_START);

        JLabel end = new JLabel("AIMS");
        end.setBackground(new Color(0,0,0,0));
        end.setForeground(Color.black);
        end.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        end.setVerticalAlignment(SwingConstants.BOTTOM);
        ret.add(end, BorderLayout.LINE_END);
        return ret;
    }

    public JPanel getPanel() {
        return root;
    }

    private record Prices(int raw, int taxed, int ship, int total) {}
}
