package tesco.vendor.coins;

import java.util.ArrayList;
import java.util.List;

public class GreedyCoinSelector implements CoinSelector {

	public GreedyCoinSelector() {
	}

	public Coins select(Coins coinStore, int amount) {
		Coins selected = new Coins();
		List<Coin> remaining = coinStore.asList();

		Remainder remainder = new Remainder();
		// target is exact change
		remainder.targetRemainder = 0;
		remainder.minRemainderSoFar = amount;

		// First pass try to get exact change. If not then go again to get the
		// best fit
		select(remaining, selected, amount, remainder);
		if (remainder.minRemainderSoFar != 0) {
			remainder.targetRemainder = remainder.minRemainderSoFar;
			remainder.minRemainderSoFar = amount;
			select(remaining, selected, amount, remainder);
		}
		return selected;
	}


	/*
	 * (Remaining coins must be ordered, highest value first)
	 * Go down the list and if coin value less than the remaining change subtract and
	 * then recurse on the remaining section of the list to try to match the new remaining change
	 * if that doesn't work (recursive select returns false) then go onto the next in the list.
	 * Track the best fit (smallest change left) so we can go for that as next target if needed.
	 */
	private boolean select(List<Coin> remaining, Coins selected, int amount, Remainder remainder) {
		int index = 0;
		int endIndex = remaining.size() - 1;

		for (Coin coin : remaining) {
			int remainingAmount = amount - coin.getValue();

			if (remainingAmount >= 0) {
				remainder.updateMinimum(remainingAmount);
				if (remainingAmount == remainder.targetRemainder) {
					selected.add(coin, 1);
					return true;
				}
				if (index < endIndex) {
					List<Coin> nremaining = new ArrayList<>(remaining.subList(index + 1, endIndex + 1));

					if (select(nremaining, selected, remainingAmount, remainder)) {
						selected.add(coin, 1);
						return true;
					}
				}
			}
			index++;
		}
		return false;
	}

	//Not using setters / getters as local and simple
	class Remainder
	{
		int targetRemainder;
		int minRemainderSoFar;

		void updateMinimum(int value) {
			if (value < minRemainderSoFar) minRemainderSoFar = value;
		}
	}
}
