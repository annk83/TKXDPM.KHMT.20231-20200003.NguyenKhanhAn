package dmui.content;

import controller.form.FieldType;
import controller.form.ISelectFormField;
import controller.form.ISimpleForm;
import controller.form.ITextFormField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
public class SimpleFormUI extends BasePanel {
    private ISimpleForm iSimpleForm;
    private final JLabel title = new JLabel();
    private final JPanel formPanel = new JPanel(new GridBagLayout());
    public SimpleFormUI() {
        super(new BorderLayout());
        add(title, BorderLayout.PAGE_START);
        add(formPanel, BorderLayout.CENTER);
    }

    public SimpleFormUI(ISimpleForm simpleForm) {
        this();
        setCurrentForm(simpleForm);
    }

    void setCurrentForm(ISimpleForm simpleForm) {
        if(simpleForm == null) throw new IllegalArgumentException("not null");
        this.iSimpleForm = simpleForm;
        init();
        reload();

    }

    void reload() {

    }

    void init() {
        formPanel.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 2;
        c.insets = new Insets(10, 20, 20, 10);
        c.fill = GridBagConstraints.BOTH;


        GridBagConstraints cc = new GridBagConstraints();
        cc.gridx = 1;
        cc.gridy = 1;
        cc.fill = GridBagConstraints.BOTH;
        cc.weightx = 1.0;
        cc.insets = new Insets(5, 20, 15, 10);

        GridBagConstraints ccc = new GridBagConstraints();
        ccc.gridx = 1;
        ccc.gridy = 0;
        ccc.fill = GridBagConstraints.BOTH;
        ccc.insets = new Insets(0, 20, 0, 0);
        ccc.weightx = 1.0;

        for(var formField : iSimpleForm.getFields()) {
            JLabel label = new JLabel(formField.getName());
            JComponent field;
            if(formField.getFieldType() == FieldType.TEXT) {
                ITextFormField iTextFormField = (ITextFormField) formField;
                JTextComponent jTextComponent;
                if (iTextFormField.isParagraph()) {
                    JTextPane jTextPane = new JTextPane();
                    jTextPane.setText(iTextFormField.getCurrentValue());
                    jTextComponent = jTextPane;
                }
                else jTextComponent = new JTextField();
                jTextComponent.setText(formField.getCurrentValue());
                jTextComponent.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            iTextFormField.setValue(e.getDocument().getText(0, e.getDocument().getLength()));
                        } catch (Exception ex) {
                            throw new IllegalStateException(ex);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            iTextFormField.setValue(e.getDocument().getText(0, e.getDocument().getLength()));
                        } catch (Exception ex) {
                            throw new IllegalStateException(ex);
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        try {
                            iTextFormField.setValue(e.getDocument().getText(0, e.getDocument().getLength()));
                        } catch (Exception ex) {
                            throw new IllegalStateException(ex);
                        }
                    }
                });
                field = jTextComponent;
            }
            else if(formField.getFieldType() == FieldType.SELECT) {
                JComboBox<ISelectFormField.SelectOption> jComboBox = new JComboBox<>();
                jComboBox.setEditable(false);
                jComboBox.setModel(new ISimpleFormComboBoxModel((ISelectFormField)formField));
                field = jComboBox;
            }
            else throw new IllegalStateException("Cant reach");
            label.setLabelFor(field);

            formPanel.add(label, c);
            formPanel.add(field, cc);

            var err = new JLabel(formField.getError().orElse(""));
            formField.setConsumer(error->{
                err.setText(error == null ? "" :error);
            });
            formPanel.add(err, ccc);

            c.gridy += 2;
            cc.gridy += 2;
            ccc.gridy += 2;
        }
        invalidate();
        formPanel.invalidate();
        formPanel.repaint();
        var win = (Window)getTopLevelAncestor();
        if(win != null)
            win.pack();
    }

    private static class ISimpleFormComboBoxModel extends AbstractListModel<ISelectFormField.SelectOption> implements ComboBoxModel<ISelectFormField.SelectOption>{
        private final ISelectFormField iSelectFormField;

        private ISimpleFormComboBoxModel(ISelectFormField iSelectFormField) {
            this.iSelectFormField = iSelectFormField;
        }

        @Override
        public int getSize() {
            return iSelectFormField.getOptionList().size();
        }

        @Override
        public ISelectFormField.SelectOption getElementAt(int index) {
            return iSelectFormField.getOptionList().get(index);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            if(anItem instanceof ISelectFormField.SelectOption)
                iSelectFormField.select(((ISelectFormField.SelectOption) anItem).getId());
        }

        @Override
        public Object getSelectedItem() {
            return iSelectFormField.getCurrentSelection();
        }
    }
}
