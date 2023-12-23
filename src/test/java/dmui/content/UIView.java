package dmui.content;

import controller.form.*;
import utils.IEtc;
import dmui.toplevel.TopLevelFrame;
import mock.MockCartController;
import mock.MockPaginatorController;
import org.junit.Test;
import utils.ShippingConfig;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

//How to create test: add 'assert promt()' to end of test
public class UIView {

    private boolean promt() {
        var ret = JOptionPane.showConfirmDialog(null, "Test Passed ?", "Test result", JOptionPane.YES_NO_OPTION);
        return (ret == JOptionPane.YES_OPTION);
    }

    private void simplePanelDisplay(JPanel jPanel) {
        var jFrame = new JFrame();
        jFrame.add(jPanel);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Semaphore sem = new Semaphore(0);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                sem.release();
            }
        });
        try {
            sem.acquire();
        } catch (InterruptedException exception) {
            jFrame.dispose();
        }
    }
    @Test
    public void mainFrame() throws IOException {
        TopLevelFrame topLevelFrame = new TopLevelFrame(new ByteArrayInputStream(new byte[0]));
        topLevelFrame.run();
        assert promt();
    }

    @Test
    public void errorDialog() {
        BasePanel basePanel = new BasePanel();
        basePanel.defaultExceptionHandle(new Exception());
        assert promt();
    }

    @Test
    public void viewCart() throws Exception {
        TopLevelFrame topLevelFrame = new TopLevelFrame(new ByteArrayInputStream(new byte[0]));
        topLevelFrame.getContentNavigator().register(new ViewCartUI(new MockCartController(), new IEtc() {
            @Override
            public int getDefaultPageSize() {
                return 5;
            }

            @Override
            public double getTax() {
                return 0;
            }

            @Override
            public ShippingConfig getShippingConfig() {
                return null;
            }

            @Override
            public CurrencyConfig getCurrencyConfig() {
                return null;
            }
        }));
        topLevelFrame.getContentNavigator().changeTo(ViewCartUI.class);
        topLevelFrame.run();
        assert promt();
    }

    @Test
    public void viewCartItem() throws Exception {
        //simplePanelDisplay(new ViewCartItemUI(new MockCartITemController()));
        assert promt();

    }

    @Test
    public void paginator() throws Exception {
        simplePanelDisplay(new PaginatorUI(new MockPaginatorController(), List.of(3,5,7)));
        assert promt();
    }

    @Test
    public void leftRightPanel() throws Exception {
        var i = new SplitPane("tsk", new JButton("a"), new JButton("b"), new JButton("C"), new JButton("D")) {
            @Override
            public void reset() {

            }
        };
        simplePanelDisplay(i);
        assert promt();
    }

    @Test
    public void formTest() throws Exception {
        ISimpleForm form = new ISimpleForm() {

            @Override
            public boolean isValid() {
                return false;
            }

            @Override
            public String getTitle() {
                return "Title";
            }

            @Override
            public List<IFormField> getFields() {
                return List.of(new ITextFormField() {
                                   private String value = "";

                                   @Override
                                   public void setValue(String value) {
                                       this.value = value;
                                       System.out.println("setValue "+value);
                                   }

                                   @Override
                                   public boolean isParagraph() {
                                       return true;
                                   }

                                   @Override
                                   public String getName() {
                                       return "test";
                                   }

                                   @Override
                                   public String getCurrentValue() {
                                       return value;
                                   }

                                   @Override
                                   public Optional<String> getError() {
                                       return Optional.empty();
                                   }

                                   @Override
                                   public void setConsumer(Consumer<String> errorConsumer) {

                                   }

                                   @Override
                                   public FieldType getFieldType() {
                                       return FieldType.TEXT;
                                   }
                               },
                        new ISelectFormField() {
                            private SelectOption option;
                            @Override
                            public List<SelectOption> getOptionList() {
                                return List.of(new SelectOption(0, "Hanoi"), new SelectOption(1, "HCM"));
                            }

                            @Override
                            public void select(int id) throws ArrayIndexOutOfBoundsException {
                                option = new SelectOption(0, "Hanoi");
                                System.out.println("setValue ");
                            }

                            @Override
                            public SelectOption getCurrentSelection() {
                                return new SelectOption(0, "Hanoi");
                            }

                            @Override
                            public void backToDefault() {
                            }

                            @Override
                            public String getName() {
                                return "select test";
                            }

                            @Override
                            public String getCurrentValue() {
                                return new SelectOption(0, "Hanoi").getLabel();
                            }

                            @Override
                            public Optional<String> getError() {
                                return Optional.of("Error");
                            }

                            @Override
                            public void setConsumer(Consumer<String> errorConsumer) {

                            }

                            @Override
                            public FieldType getFieldType() {
                                return FieldType.SELECT;
                            }
                        });
            }

            @Override
            public <T> T toStringModel(Class<T> clazz) {
                return null;
            }

            @Override
            public int addChangeListener(Runnable runnable) {
                return 0;
            }

            @Override
            public void removeChangeListener(int runnable) {

            }
        };

        simplePanelDisplay(new SimpleFormUI(form));
        assert promt();
    }

    @Test
    public void placeOrderLayout() {
        simplePanelDisplay(new PlaceOrderUI());
        assert promt();
    }

    @Test
    public void bigasscheckTest() {
        simplePanelDisplay(new BigAssCheckbox(30, b->{}));
        assert promt();
    }
}

