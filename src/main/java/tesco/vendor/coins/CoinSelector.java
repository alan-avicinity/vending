package tesco.vendor.coins;

public interface CoinSelector {

	Coins select(Coins coins, int cost);
}
