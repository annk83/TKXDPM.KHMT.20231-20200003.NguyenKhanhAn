package dmui.content;

import lombok.AccessLevel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

abstract class SplitPane extends BaseContent {
    @Getter(AccessLevel.PROTECTED)
    protected final JComponent leftExpand;
    @Getter(AccessLevel.PROTECTED)
    protected final JComponent leftShrink;
    @Getter(AccessLevel.PROTECTED)
    protected final JComponent rightExpand;
    @Getter(AccessLevel.PROTECTED)
    protected final JComponent rightShrink;
    protected final JPanel expandPanel = new JPanel(new BorderLayout());
    protected final JPanel btnPanel = new JPanel();
    protected final JButton switchButton = new JButton();
    protected boolean isLeft;

    protected SplitPane(String title, JComponent leftExpand, JComponent leftShrink, JComponent rightExpand, JComponent rightShrink) {
        super(title, new BorderLayout());
        this.leftShrink = leftShrink;
        this.leftExpand = leftExpand;
        this.rightExpand = rightExpand;
        this.rightShrink = rightShrink;
        leftShrink.setMinimumSize(new Dimension(300, 0));
        leftShrink.setPreferredSize(new Dimension(300, 0));
        rightShrink.setMinimumSize(new Dimension(300, 0));
        rightShrink.setPreferredSize(new Dimension(300, 0));
        btnPanel.setLayout(new GridBagLayout());
        btnPanel.add(switchButton);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftExpandMode();
        switchButton.addActionListener(ev->{
            if(isLeft) rightExpandMode();
            else leftExpandMode();
        });
    }

    private void leftExpandMode() {
        isLeft = true;
        removeAll();
        expandPanel.removeAll();
        switchButton.setText("<");
        add(expandPanel, BorderLayout.CENTER);
        add(rightShrink, BorderLayout.LINE_END);
        expandPanel.add(leftExpand, BorderLayout.CENTER);
        expandPanel.add(btnPanel, BorderLayout.LINE_END);
        expandPanel.invalidate();
        invalidate();
        var top = ((Window)getTopLevelAncestor());
        if(top != null)top.pack();
    }

    private void rightExpandMode() {
        isLeft = false;
        removeAll();
        expandPanel.removeAll();
        switchButton.setText(">");
        add(expandPanel, BorderLayout.CENTER);
        add(leftShrink, BorderLayout.LINE_START);
        expandPanel.add(rightExpand,BorderLayout.CENTER);
        expandPanel.add(btnPanel, BorderLayout.LINE_START);
        expandPanel.invalidate();
        invalidate();
        var top = ((Window)getTopLevelAncestor());
        if(top != null)top.pack();
    }
}
