package mock;

import controller.utils.IPaginatorController;

import java.util.ArrayList;
import java.util.List;

public class MockPaginatorController implements IPaginatorController {
    List<PageControlCallback> pageControlCallbacks = new ArrayList<>();

    @Override
    public int addCallback(PageControlCallback callback) {
        return 0;
    }

    @Override
    public void removeCallback(int id) {

    }

    @Override
    public void setPageSize(int size) {
    }

    @Override
    public void invokeAllCallback() {
        pageControlCallbacks.forEach(i->i.render(0, 10, 12));
    }

    @Override
    public void nextPage() {
    }

    @Override
    public void prevPage() {
    }

    @Override
    public void lastPage() {
    }

    @Override
    public void firstPage() {
    }
}
