package utils;

public interface IEtc {
    int getDefaultPageSize();
    double getTax();
    ShippingConfig getShippingConfig();
    CurrencyConfig getCurrencyConfig();

    record CurrencyConfig(String currency, int precision, int displayedPrecision) {
        public String formatMoney(long input) {
            input /= precision;
            //TODO:
            return input + " " + currency;
        }
    }
}
