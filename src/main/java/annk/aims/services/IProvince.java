package annk.aims.services;

import annk.aims.domain.Province;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.TypeVariable;
import java.util.List;

public interface IProvince {
    List<Integer> getAllProvinceId();
    List<Province> getAllProvince();
    default Province getById(Integer provinceId) {
        if(provinceId == null) throw new IllegalArgumentException();
        return getAllProvince().stream().filter(i->i.getId().equals(provinceId)).findAny().get();
    }
}
