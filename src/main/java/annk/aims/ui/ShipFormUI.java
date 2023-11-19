package annk.aims.ui;

import annk.aims.domain.Province;
import annk.aims.domain.ShipForm;
import annk.aims.services.IProvince;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.TextEvent;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Consumer;

class ShipFormUI {
    private final JPanel root;
    private final Consumer<ShipForm> emitter;
    private final int maxHist = 10;
    private final ArrayDeque<ShipForm> history =  new ArrayDeque<>(10);
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final HashMap<String, JLabel> errMap = new HashMap<>();
    private void addHistory(Object obj, String field) {
        try {
            ShipForm n;
            if (history.isEmpty()) n = new ShipForm("", "", null, "", null);
            else n = history.peekLast().clone();
            if ("name".equals(field)) n.setFullName((String) obj);
            else if ("phone".equals(field)) n.setPhone((String) obj);
            else if ("province".equals(field)) n.setProvinceId((Integer) obj);
            else if ("address".equals(field)) n.setAddress((String) obj);
            else if ("note".equals(field)) n.setNote((String)obj);
            else throw new IllegalArgumentException("field %s not found in shipform".formatted(field));
            while(history.size() > maxHist-1)
                history.removeFirst();
            history.addLast(n);
            emitter.accept(n);

            validate(n);
        } catch (ClassCastException classCastException) {
            throw new IllegalArgumentException("field %s is not class %s".formatted(field, classCastException.getMessage()), classCastException);
        }
    }

    public ShipFormUI(ShipForm shipForm, Consumer<ShipForm> emitter, IProvince provinceService) {
        this.emitter = emitter;
        root = new JPanel();
        root.setLayout(new FormLayout("right:pref, 15dlu, default", "12dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 6dlu, 100dlu"));
        root.setBackground(new Color(236,255,255));
        history.addLast(shipForm);

        var cc = new CellConstraints();

        Vector<Province> items = new Vector<>();
        items.add(new Province(null, "(None)", 0,0,0));
        items.addAll(provinceService.getAllProvince());
        var bx = new JComboBox<>(items);
        for(int i=0;i< items.size();++i)
            if(Objects.equals(items.get(i).getId(), shipForm.getProvinceId())) {
                bx.setSelectedIndex(i);
                break;
            }
        bx.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        bx.setPreferredSize(new Dimension(Short.MAX_VALUE, 30));
        bx.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                return super.getListCellRendererComponent(list, ((Province)value).getName(), index, isSelected, cellHasFocus);
            }
        });
        bx.addItemListener(itemEvent -> {
            if(itemEvent.getStateChange() == ItemEvent.SELECTED) {
                addHistory(((Province)itemEvent.getItem()).getId(), "province");
            }
        });

        root.add(lb("Name"), cc.xy(1,2));
        root.add(tx(i->{ addHistory(i, "name"); }, shipForm.getFullName()), cc.xy(3, 2));
        root.add(lb("Phone"), cc.xy(1, 4));
        root.add(tx(i->{ addHistory(i, "phone");}, shipForm.getPhone()), cc.xy(3, 4));
        root.add(lb("Province"), cc.xy(1, 6));
        root.add(bx, cc.xy(3, 6));
        root.add(lb("Address"), cc.xy(1, 8));
        root.add(tx(i->{ addHistory(i, "address");}, shipForm.getAddress()), cc.xy(3, 8));
        root.add(lb("Note"), cc.xy(1, 10));
        root.add(noteArea(shipForm.getNote()), cc.xywh(3, 10, 1, 3));

        root.add(er("fullName"), cc.xy(3, 1));
        root.add(er("phone"), cc.xy(3, 3));
        root.add(er("provinceId"), cc.xy(3, 7));
        root.add(er("address"), cc.xy(3, 5));
        validate(shipForm);
    }

    private JTextArea noteArea(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        textArea.getDocument().addDocumentListener(
                new DocumentListener() {
                    private void sync(Document document) {
                        try {
                            addHistory(document.getText(0, document.getLength()), "note");
                        } catch (BadLocationException exception) {
                            throw new IllegalStateException(exception);
                        }
                    }
                    @Override
                    public void insertUpdate(DocumentEvent documentEvent) {
                        sync(documentEvent.getDocument());
                    }
                    @Override
                    public void removeUpdate(DocumentEvent documentEvent) {
                        sync(documentEvent.getDocument());
                    }
                    @Override
                    public void changedUpdate(DocumentEvent documentEvent) {
                        sync(documentEvent.getDocument());
                    }
                }
        );

        return textArea;
    }

    private void validate(ShipForm shipForm) {
        errMap.values().forEach(i->i.setText(""));
        Validator validator = validatorFactory.getValidator();
        var st = validator.validate(shipForm);
        st.forEach(i->{
            JLabel er = errMap.get(i.getPropertyPath().toString());
            if(er != null) {
                er.setText(i.getMessage());
            }
        });
    }

    private JLabel er(String path) {
        JLabel ret = new JLabel();
        errMap.put(path, ret);
        ret.setFont(new Font("Times New Roman", Font.BOLD, 10));
        ret.setVerticalAlignment(SwingConstants.BOTTOM);
        ret.setForeground(Color.RED);
        return ret;
    }

    private JLabel lb(String tx) {
        var lb = new JLabel();
        lb.setFont(new Font("Times New Roman", Font.BOLD, 17));
        lb.setText(tx);
        return lb;
    }

    private TextField tx(Consumer<String> textListener, String init) {
        var tx = new TextField();
        tx.setText(init);
        tx.setPreferredSize(new Dimension(Short.MAX_VALUE, 30));
        tx.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        tx.addTextListener(i->{
            if(i.getID() == TextEvent.TEXT_VALUE_CHANGED) {
                textListener.accept(((TextField)i.getSource()).getText());
            }
        });
        return tx;
    }

    public JPanel getPanel() {
        return root;
    }


}
