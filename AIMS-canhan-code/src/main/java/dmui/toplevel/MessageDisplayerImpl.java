package dmui.toplevel;

import dmui.IMessageDisplayer;

import javax.swing.*;

public class MessageDisplayerImpl implements IMessageDisplayer {
    private final TopLevelFrame topLevelFrame;

    MessageDisplayerImpl(TopLevelFrame topLevelFrame) {
        this.topLevelFrame = topLevelFrame;
    }

    @Override
    public void displayInformation(String title, String message) {
        JOptionPane.showMessageDialog(topLevelFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void displayCriticalError(String s) {
        JOptionPane.showMessageDialog(topLevelFrame, s, "Critical Failure", JOptionPane.ERROR_MESSAGE);
    }
}
