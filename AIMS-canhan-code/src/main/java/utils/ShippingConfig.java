package utils;

public record ShippingConfig(long freeshipThreashold, long lowest, long startPrice, long extendPrice, long rushCharge) {
    public long calculateNormal(long rawMoney, int weight) {
        if(rawMoney >= freeshipThreashold) return 0;
        long ship = startPrice;
        if(weight > lowest)
            ship += extendPrice * (weight - lowest());
        return ship;
    }

    public long calculateRush(int weight, int count) {
        long ship = startPrice;
        if(weight > lowest)
            ship += extendPrice * (weight - lowest());
        ship += (long) count * rushCharge;
        return ship;
    }
}
