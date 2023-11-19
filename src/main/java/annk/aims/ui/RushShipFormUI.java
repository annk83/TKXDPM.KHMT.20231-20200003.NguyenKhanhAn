package annk.aims.ui;

import annk.aims.domain.Province;
import annk.aims.domain.RushForm;
import annk.aims.services.IProvince;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.TextEvent;
import java.util.*;
import java.util.function.Consumer;
public class RushShipFormUI {
    private final JPanel root;
    private final Consumer<RushForm> emitter;
    private final int maxHist = 10;
    private final ArrayDeque<RushForm> history =  new ArrayDeque<>(10);
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final HashMap<String, JLabel> errMap = new HashMap<>();
    private void addHistory(Object obj, String field) {
        try {
            RushForm n;
            if (history.isEmpty()) n = new RushForm(null, "", null);
            else n = history.peekLast().clone();
            if ("province".equals(field)) n.setProvinceId((Integer) obj);
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

    public RushShipFormUI(RushForm shipForm, Consumer<RushForm> emitter, IProvince provinceService) {
        this.emitter = emitter;
        root = new JPanel();
        var r = new JPanel();
        root.setLayout(new BorderLayout());
        root.add(r, BorderLayout.PAGE_START);
        root.add(makeWarning(), BorderLayout.CENTER);
        root.setBackground(new Color(236,255,255));
        r.setBackground(new Color(236,255,255));

        r.setLayout(new FormLayout("right:pref, 15dlu, default", "12dlu, p, 12dlu, p, 12dlu, p, 6dlu, 100dlu"));
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

        r.add(lb("Province"), cc.xy(1, 2));
        r.add(bx, cc.xy(3, 2));
        r.add(lb("Address"), cc.xy(1, 4));
        r.add(tx(i->{ addHistory(i, "address");}, shipForm.getAddress()), cc.xy(3, 4));
        r.add(lb("Note"), cc.xy(1, 6));
        r.add(noteArea(shipForm.getNote()), cc.xywh(3, 6, 1, 3));

        r.add(er("fullName"), cc.xy(3, 1));
        r.add(er("phone"), cc.xy(3, 3));
        r.add(er("provinceId"), cc.xy(3, 7));
        r.add(er("address"), cc.xy(3, 5));
        validate(shipForm);
    }

    private JPanel makeWarning() {
        JPanel j = new JPanel(new FlowLayout());
        j.setBorder(new EmptyBorder(50, 0, 0, 0));
        j.setBackground(new Color(236,255,255));
        JLabel jLabel = new JLabel();
        jLabel.setText("!!!RUSH!!!");
        jLabel.setFont(new Font("Times new roman", Font.BOLD, 72));
        jLabel.setForeground(Color.RED);
        j.add(jLabel);
        return j;
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

    private void validate(RushForm shipForm) {
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