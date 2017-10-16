package tesco.vendor;

import tesco.vendor.SelectionResult.ResultReason;
import tesco.vendor.coins.Coin;
import tesco.vendor.coins.CoinSelector;
import tesco.vendor.coins.Coins;
import tesco.vendor.coins.GreedyCoinSelector;
import tesco.vendor.items.AvailableItems;
import tesco.vendor.items.VendingItemType;
import tesco.vendor.items.VendingItemTypes;

/**
 * Encapsulates the state of a vending machine and the operations that can be performed on it
 */
public class DefaultVendingMachine implements VendingMachine {

	private boolean on = false; // just for clarity - default is false

	private final Coins insertedCoins = new Coins();
	private final Coins storedCoins = new Coins();

	private final AvailableItems availableItems = new AvailableItems();

	private final CoinSelector changeSelector = new GreedyCoinSelector();

	public DefaultVendingMachine(CoinSelector changeSelector) {
		changeSelector = this.changeSelector;
	}

	@Override
	public VendingMachine fill(VendingItemType type, int count) {
		availableItems.add(type, count);
		return this;
	}

	@Override
	public VendingMachine addChange(Coin coin, int count) {
		storedCoins.add(coin, count);
		return this;
	}

	@Override
	public boolean isOn() {
		return on;
	}

	// unsure if its intended there should be a check against on/off for select
	// operations etc -
	// assumed that higher level check against isOn blocks any being issued

	@Override
	public void setOn() {
		on = true;
	}

	@Override
	public void setOff() {
		on = false;
	}

	@Override
	public void insertCoin(Coin coin) {
		insertedCoins.add(coin, 1);
	}

	@Override
	public Coins returnInsertedCoins() {
		Coins coins = new Coins();
		coins.add(insertedCoins);
		insertedCoins.clear();
		return coins;
	}

	@Override
	public SelectionResult selectA() {
		return select(VendingItemTypes.ITEM_A);
	}

	@Override
	public SelectionResult selectB() {
		return select(VendingItemTypes.ITEM_B);
	}

	@Override
	public SelectionResult selectC() {
		return select(VendingItemTypes.ITEM_C);
	}

	@Override
	public SelectionResult select(VendingItemType type) {
		if (!hasSufficientPayment(type)) {
			return new SelectionResult(ResultReason.INSUFFICIENT_PAYMENT);
		}
		if (!itemIsAvailable(type)) {
			return new SelectionResult(ResultReason.UNAVAILABLE);
		}
		vendItem(type);

		int changeAmount = transferPayment(type);

		SelectionResult result = getChange(changeAmount);
		return result;
	}

	private void vendItem(VendingItemType type) {
		availableItems.remove(type);
	}

	private boolean itemIsAvailable(VendingItemType type) {
		return availableItems.isAvailable(type);
	}

	private int transferPayment(VendingItemType type) {
		int currentValue = insertedCoins.getValue();
		int change = currentValue - type.getPrice();
		storedCoins.add(insertedCoins);
		insertedCoins.clear();
		return change;
	}

	private boolean hasSufficientPayment(VendingItemType type) {
		return this.insertedCoins.getValue() >= type.getPrice();
	}

	private SelectionResult getChange(int changeAmount) {
		if (changeAmount == 0) {
			return new SelectionResult();
		}
		Coins change = changeSelector.select(storedCoins, changeAmount);
		storedCoins.remove(change);
		if (change.getValue() != changeAmount) {
			return new SelectionResult(true, change, ResultReason.EXACT_CHANGE_UNAVAILABLE);
		}
		return new SelectionResult(change);
	}
}
