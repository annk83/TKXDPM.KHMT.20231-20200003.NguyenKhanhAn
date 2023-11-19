package annk.aims;

import java.math.BigDecimal;

public class Utils {
    static public String formatMoney(int price) {
        if(price == -1) return "Invalid!!!";
        return Integer.toString(price) + " VND";
    }
}
