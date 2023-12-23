package controller.utils;

import java.util.List;
import java.util.function.Supplier;

public interface IPaginatorController {
    interface PageControlCallback {
        void render(int start, int end, int count);
    }
    int addCallback(PageControlCallback callback);
    void removeCallback(int id);
    void setPageSize(int size);
    void invokeAllCallback();
    void nextPage();
    void prevPage();
    void lastPage();
    void firstPage();
}
