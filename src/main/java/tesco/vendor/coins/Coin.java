package tesco.vendor.coins;

public enum Coin {
	TEN_PENCE(10),
	TWENTY_PENCE(20),
	FIFTY_PENCE(50),
	POUND(100),
	OTHER(-1);

	private final int value;

	private Coin(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Coin[] getOrderedValidValues() {
		return new Coin[] { Coin.POUND, Coin.FIFTY_PENCE, Coin.TWENTY_PENCE, Coin.TEN_PENCE };
	}
}
