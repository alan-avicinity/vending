package tesco.vendor;

import tesco.vendor.coins.Coin;
import tesco.vendor.coins.Coins;
import tesco.vendor.items.VendingItemType;

public interface VendingMachine {

	VendingMachine fill(VendingItemType type, int count);

	VendingMachine addChange(Coin coin, int count);

	boolean isOn();

	void setOn();

	void setOff();

	void insertCoin(Coin coin);

	Coins returnInsertedCoins();

	SelectionResult selectA();

	SelectionResult selectB();

	SelectionResult selectC();

	SelectionResult select(VendingItemType type);

}