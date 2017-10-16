package tesco.vendor.coins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Coins {

	private Map<Coin, CoinCount> coins = new LinkedHashMap<>();

	public Coins() {
		clear();
	}

	public Coins(Coins initial) {
		clear();
		add(initial);
	}

	public void clear() {
		for (Coin coin : Coin.getOrderedValidValues()) {
			coins.put(coin, new CoinCount(0));
		}
	}

	public int size() {
		int count = coins.values().stream().mapToInt(coinCount -> coinCount.getCount()).sum();
		return count;
	}

	public Coins add(Coin coin, int amount) {
		get(coin).increment(amount);
		return this;
	}

	public Coins remove(Coin coin, int amount) {
		get(coin).decrement(amount);
		return this;
	}

	public Set<Coin> getCoinTypes() {
		return coins.keySet();
	}

	public int getCount(Coin coin) {
		return get(coin).getCount();
	}

	public void add(Coins coins) {
		coins.getCoinTypes().forEach(coin -> add(coin, coins.getCount(coin)));
	}

	public void remove(Coins coins) {
		coins.getCoinTypes().forEach(coin -> remove(coin, coins.getCount(coin)));
	}
	
	public int getValue() {
		int value = 0;
		for (Map.Entry<Coin, CoinCount> entry : coins.entrySet()) {
			value += entry.getKey().getValue() * entry.getValue().getCount();
		}
		return value;
	}

	private CoinCount get(Coin coin) {
		CoinCount count = coins.get(coin);
		if (count == null) {
			throw new IllegalArgumentException("Unrecognized coin " + coin);
		}
		return count;
	}

	public List<Coin> asList() {
		List<Coin> coinList = new ArrayList<>();
		for (Map.Entry<Coin, CoinCount> entry : coins.entrySet()) {
			coinList.addAll(Collections.nCopies(entry.getValue().getCount(), entry.getKey()));
		}
		return coinList;
	}

	public static class CoinCount {

		private int count;

		public CoinCount(int count) {

			this.count = count;
		}

		public int getCount() {
			return count;
		}

		public void increment(int amount) {
			count += amount;
		}

		public void decrement(int amount) {
			count -= amount;
		}
	}
}
