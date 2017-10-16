package tesco.vendor.items;

public class VendingItemStock {

	private VendingItemType type;
	private int count;

	public VendingItemStock(VendingItemType type, int count) {
		this.type = type;

		this.count = count;
	}

	public VendingItemType getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public void incrementCount(int amount) {
		count += amount;
	}

	public void decrementCount() {
		count--;
	}
}
