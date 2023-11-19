package annk.aims.services;

import java.util.List;

public interface ICatalog {
    List<Object> getItemInfo(int itemId, List<String> fields);
}
