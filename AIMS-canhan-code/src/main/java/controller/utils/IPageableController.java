package controller.utils;

import java.util.List;

public interface IPageableController<CHILDCONTROLLER> {
    IPaginatorController getPaginatorController();
    List<? extends CHILDCONTROLLER> getPage(int u, int v);
}
