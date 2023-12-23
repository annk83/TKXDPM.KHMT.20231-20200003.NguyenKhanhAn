package domain;

import utils.ShippingConfig;

import java.util.Optional;

public interface IProvinceDomain {
    int getProvinceId();
    String getName();
    Optional<ShippingConfig> getShippingPolicy();
}
