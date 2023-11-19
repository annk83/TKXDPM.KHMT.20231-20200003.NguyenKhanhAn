package annk.aims.controller;

import annk.aims.domain.PlaceOrderItem;

public interface PlaceOrderController {
    PlaceOrderItem get(int itemId);
    double getTax();

    int getFreeShipThreshold();

    double getRushShipCharge();
}
