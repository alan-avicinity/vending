package tesco.vendor.items;

import java.util.HashMap;
import java.util.Map;

public class VendingItems {

	private Map<VendingItemType, VendingItemStock> stocks = new HashMap<>();

	public boolean isAvailable(String type) {
		VendingItemStock item = stocks.get(type);
		return item != null && item.getCount() > 0;
	}

	public void removeItem(VendingItemType type) {
		VendingItemStock item = stocks.get(type);
		item.decrementCount();
	}
}
