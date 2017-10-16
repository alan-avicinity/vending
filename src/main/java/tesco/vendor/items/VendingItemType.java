package tesco.vendor.items;

// could enumerate type, but vending items should probably be extensible so using String,

public class VendingItemType
{
	private final String type;
	private final int price;

	public VendingItemType(String type, int price) {
		this.type = type;
		this.price = price;
	}

	public int getPrice() {
		return price;
	}

	// eclipse generated
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VendingItemType other = (VendingItemType) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}