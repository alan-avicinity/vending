package tesco.vendor.items;

import java.util.HashMap;
import java.util.Map;

public class AvailableItems {

	private Map<VendingItemType, VendingItemStock> stocks = new HashMap<>();

	public boolean isAvailable(VendingItemType type) {
		VendingItemStock stock = stocks.get(type);
		return stock != null && stock.getCount() > 0;
	}

	public void add(VendingItemType type, int count) {
		VendingItemStock stock = stocks.get(type);
		if (stock == null) {
			stock = new VendingItemStock(type, count);
			stocks.put(type, stock);
		} else {
			stock.incrementCount(count);
		}
	}

	public void remove(VendingItemType type) {
		VendingItemStock stock = stocks.get(type);
		if (stock != null) {
			stock.decrementCount();
		}
	}
}
