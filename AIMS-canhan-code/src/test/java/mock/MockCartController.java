package mock;

import controller.cart.ICartController;
import controller.cart.ICartItemController;
import controller.utils.IPaginatorController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MockCartController implements ICartController {
    @Override
    public String getTotalMoney() {
        return "";
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemTypeCount() {
        return 0;
    }

    @Override
    public String getSavedDate() {
        return "";
    }

    @Override
    public IPaginatorController getPaginatorController() {
        return new MockPaginatorController();
    }

    @Override
    public List<ICartItemController> getPage(int u, int v) {
        ArrayList<ICartItemController> arrayList = new ArrayList<>();
        for(int i=u;i<v;++i)
            arrayList.add(new MockCartITemController());
        return arrayList;
    }

    @Override
    public boolean payOrder() {
        return false;
    }
}
