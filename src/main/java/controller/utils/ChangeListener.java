package controller.utils;

public interface ChangeListener {
    int addChangeListener(Runnable runnable);
    void removeChangeListener(int runnable);
}

