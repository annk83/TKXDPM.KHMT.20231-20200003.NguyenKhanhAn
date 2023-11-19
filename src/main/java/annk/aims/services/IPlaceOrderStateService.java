package annk.aims.services;

import annk.aims.domain.CartItem;
import annk.aims.domain.PlaceOrderState;

import java.util.List;

public interface IPlaceOrderStateService {
   PlaceOrderState get();
   void save(PlaceOrderState placeOrderState);
   void complete();
   void startNew(List<Integer> items, List<Integer> counts);
}
