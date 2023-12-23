package utils;

import utils.IEtc;

public class Constant implements IEtc {

    @Override
    public int getDefaultPageSize() {
        return 5;
    }

    @Override
    public double getTax() {
        return 0.1;
    }

    @Override
    public ShippingConfig getShippingConfig() {
        return new ShippingConfig(100000,  1000, 20000, 1, 10000);
    }

    @Override
    public CurrencyConfig getCurrencyConfig() {
        return new CurrencyConfig("VND", 1, 0);
    }
}
