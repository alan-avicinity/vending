package tesco.vendor.coins;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import tesco.vendor.DefaultVendingMachine;
import tesco.vendor.coins.Coin;
import tesco.vendor.coins.CoinSelector;
import tesco.vendor.coins.Coins;
import tesco.vendor.coins.GreedyCoinSelector;


/**
 * Unit tests for {@link DefaultVendingMachine}
 */
public class GreedyCoinSelectorTest {
	
	@Test
	public void testNoPossibleSelection1() {
		CoinSelector selector = new GreedyCoinSelector();
		Coins selection = selector.select(new Coins(), 10);
		assertEquals(0, selection.getValue());
	}
	
	@Test
	public void testNoPossibleSelection2() {
		CoinSelector selector = new GreedyCoinSelector();
		Coins selection = selector.select(new Coins().add(Coin.TWENTY_PENCE, 2), 10);
		assertEquals(0, selection.getValue());
	}

	@Test
	public void testPartialSelection() {
		CoinSelector selector = new GreedyCoinSelector();
		Coins coins = new Coins().add(Coin.TEN_PENCE,2).add(Coin.FIFTY_PENCE,1);
		Coins selection = selector.select(coins, 30);
		assertEquals(20, selection.getValue());
	}
	
	@Test
	public void testExactSelection() {
		CoinSelector selector = new GreedyCoinSelector();
		Coins coins = new Coins();
		coins.add(Coin.TEN_PENCE,2).add(Coin.FIFTY_PENCE,2);
		Coins selection = selector.select(coins, 60);
		assertEquals(60, selection.getValue());
	}
	
	//Will select 50 initially but must then back track to get 3 20s
	@Test
	public void testExactBacktrackSelection() {
		CoinSelector selector = new GreedyCoinSelector();
		Coins coins = new Coins().add(Coin.FIFTY_PENCE,2).add(Coin.TWENTY_PENCE,4);
		Coins selection = selector.select(coins, 60);
		assertEquals(60, selection.getValue());
	}
	
	@Test
	public void testBestFitSelection2() {
		CoinSelector selector = new GreedyCoinSelector();
		Coins coins = new Coins().add(Coin.FIFTY_PENCE, 1).add(Coin.TWENTY_PENCE, 1);
		Coins selection = selector.select(coins, 60);
		assertEquals(50, selection.getValue());
	}
	
}

