package domain;

import java.io.IOException;
import java.io.InputStream;

public interface IItemAdminDomain extends IItemDomain {
    void setType(String type);
    void setTitle(String title);
    void setPrice(long price);
    void setRemain(long remain);
    void setImage(InputStream image) throws IOException;
}
