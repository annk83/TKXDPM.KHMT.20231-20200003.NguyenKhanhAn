package dmui;

public interface IMessageDisplayer {
    void displayInformation(String title, String message);

    void displayCriticalError(String s);
}
