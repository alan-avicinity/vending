package tesco.vendor;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import tesco.vendor.SelectionResult.ResultReason;
import tesco.vendor.coins.Coin;
import tesco.vendor.coins.Coins;
import tesco.vendor.coins.GreedyCoinSelector;
import tesco.vendor.items.VendingItemType;
import tesco.vendor.items.VendingItemTypes;


/**
 * Unit tests for {@link DefaultVendingMachine}
 */
public class VendingMachineTest {
	
	@Test
	public void defaultStateIsOff() {
		assertFalse(newVendingMachine().isOn());
	}
	
	@Test
	public void turnsOn() {
		VendingMachine machine = newVendingMachine();
		machine.setOn();
		assertTrue(machine.isOn());		
	}
	
	@Test
	public void turnsOff() {
		VendingMachine machine = newVendingMachine();
		machine.setOn();
		machine.setOff();
		assertFalse(machine.isOn());		
	}
	
	@Test
	public void insertAndReturnOneTen() {
		insertAndReturn(pair(Coin.TEN_PENCE,1));
	}
	
	@Test
	public void insertAndReturnOneTwenty() {
		insertAndReturn(pair(Coin.TWENTY_PENCE,1));
	}
	
	@Test
	public void insertAndReturnOneFifty() {
		insertAndReturn(pair(Coin.FIFTY_PENCE,1));
	}
	
	@Test
	public void insertAndReturnOnePound() {
		insertAndReturn(pair(Coin.POUND,1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void insertAndReturnOneOther() {
		insertAndReturn(pair(Coin.OTHER,1));
	}
	
	@Test
	public void insertAndReturnMultipleTen() {
		insertAndReturn(pair(Coin.TEN_PENCE,2));
	}
	
	@Test
	public void insertAndReturnMultipleTwenty() {
		insertAndReturn(pair(Coin.TWENTY_PENCE,3));
	}
	
	@Test
	public void insertAndReturnMultipleFifty() {
		insertAndReturn(pair(Coin.FIFTY_PENCE,4));
	}
	
	@Test
	public void insertAndReturnMultiplePound() {
		insertAndReturn(pair(Coin.POUND,5));
	}
	
	@Test
	public void insertAndReturnMixed() {
		insertAndReturn(pair(Coin.TEN_PENCE,1),pair(Coin.TWENTY_PENCE,3),pair(Coin.POUND,8));
	}
	
	@Test
	public void getA_NoCoins() {
		SelectionResult result = getA(newVendingMachine());	
		assertFalse(result.isSuccess());
		assertEquals(ResultReason.INSUFFICIENT_PAYMENT,result.getReason());
	}
	
	@Test
	public void getA_InsufficientCoins() {
		SelectionResult result = getA(newVendingMachine(), Coin.FIFTY_PENCE);	
		assertFalse(result.isSuccess());
		assertEquals(ResultReason.INSUFFICIENT_PAYMENT,result.getReason());
	}
	

	@Test
	public void getA_EmptyMachine() {
		SelectionResult result = getA(newVendingMachine(), Coin.TEN_PENCE, Coin.FIFTY_PENCE);	
		assertFalse(result.isSuccess());
		assertEquals(ResultReason.UNAVAILABLE, result.getReason());
	}
	
	@Test
	public void getA_UnavailableItem() {
		
		SelectionResult result = getA(newVendingMachine().fill(VendingItemTypes.ITEM_B, 1), 
										Coin.TWENTY_PENCE, Coin.TWENTY_PENCE, Coin.TWENTY_PENCE);	
		
		assertFalse(result.isSuccess());
		assertEquals(ResultReason.UNAVAILABLE, result.getReason());
	}
	
	@Test
	public void getA_ExactCoins1() {
		
		getA_ExactCoins(Coin.TEN_PENCE, Coin.FIFTY_PENCE);	
	}
	
	@Test
	public void getA_ExactCoins2() {
		
		getA_ExactCoins(Coin.TEN_PENCE, Coin.TEN_PENCE, Coin.TWENTY_PENCE, Coin.TWENTY_PENCE);	
	}
	
	@Test
	public void getB_Multiple() {
		
		// make changeCount < bCount so change runs out
		int bCount = 3;
		int changeCount = 2;
		VendingMachine machine = newVendingMachine().fill(VendingItemTypes.ITEM_A, 2)
													.fill(VendingItemTypes.ITEM_B, bCount)
													.fill(VendingItemTypes.ITEM_C, 4).
													addChange(Coin.TEN_PENCE, changeCount);

		for (int loop = 0; loop < bCount; loop++)
		{
			SelectionResult result = get(machine, VendingItemTypes.ITEM_B, Coin.FIFTY_PENCE,
										Coin.TWENTY_PENCE,Coin.TWENTY_PENCE,Coin.TWENTY_PENCE);
			assertTrue(result.isSuccess());
			int expectedChange = 10;
			ResultReason expectedReason = ResultReason.OK;
			if (loop >= changeCount)
			{
				expectedChange = 0;
				expectedReason = ResultReason.EXACT_CHANGE_UNAVAILABLE;
			}
			assertEquals(expectedChange, result.getChange().getValue());
			assertEquals(expectedReason, result.getReason());
		}
		
		SelectionResult result = get(machine, VendingItemTypes.ITEM_B, Coin.FIFTY_PENCE,
				Coin.TWENTY_PENCE,Coin.TWENTY_PENCE,Coin.TWENTY_PENCE);
		assertFalse(result.isSuccess());
		assertEquals(ResultReason.UNAVAILABLE, result.getReason());
		
	}
	
	@Test
	public void getA_thenUnavailable() {
		
		VendingMachine machine = newVendingMachine().fill(VendingItemTypes.ITEM_A, 1);
		getA(machine,Coin.TEN_PENCE,Coin.FIFTY_PENCE);
		SelectionResult result = getA(machine,Coin.TEN_PENCE,Coin.FIFTY_PENCE);
		
		assertFalse(result.isSuccess());
		assertEquals(ResultReason.UNAVAILABLE, result.getReason());
	}
	
	@Test
	public void getA_WithChange() {
		SelectionResult result = getA(newVendingMachine().fill(VendingItemTypes.ITEM_A, 1), 
										Coin.TEN_PENCE, Coin.TEN_PENCE, Coin.FIFTY_PENCE);
		assertTrue(result.isSuccess());
		assertEquals(ResultReason.OK, result.getReason());
		validate(result.getChange(),pair(Coin.TEN_PENCE,1));
	}
	
	
	@Test
	public void getC_WithoutAvailableChange() {
		SelectionResult result = getC(newVendingMachine().
											fill(VendingItemTypes.ITEM_A, 1).
											fill(VendingItemTypes.ITEM_B, 1).
											fill(VendingItemTypes.ITEM_C, 1),
											Coin.POUND, Coin.POUND, Coin.TEN_PENCE);
		assertTrue(result.isSuccess());
		assertEquals(ResultReason.EXACT_CHANGE_UNAVAILABLE, result.getReason());
		assertEquals(10, result.getChange().getValue());
	}
	
	@Test
	public void getC_WithAvailableChange() {
		SelectionResult result = getC(newVendingMachine().
											fill(VendingItemTypes.ITEM_A, 1).
											fill(VendingItemTypes.ITEM_C, 1).
											addChange(Coin.TWENTY_PENCE, 1).
											addChange(Coin.TEN_PENCE,2),		
											Coin.POUND, Coin.POUND);
		assertTrue(result.isSuccess());
		assertEquals(ResultReason.OK, result.getReason());
		validate(result.getChange(),pair(Coin.TEN_PENCE,1),pair(Coin.TWENTY_PENCE,1));
	}
	
	@Test
	public void getABC_WithAvailableChange() {
		SelectionResult result = getC(newVendingMachine().
											fill(VendingItemTypes.ITEM_A, 2).
											fill(VendingItemTypes.ITEM_A, 1).
											fill(VendingItemTypes.ITEM_C, 1).
											addChange(Coin.TWENTY_PENCE, 1).
											addChange(Coin.TEN_PENCE,2),		
											Coin.POUND, Coin.POUND);
		assertTrue(result.isSuccess());
		assertEquals(ResultReason.OK, result.getReason());
		validate(result.getChange(),pair(Coin.TEN_PENCE,1),pair(Coin.TWENTY_PENCE,1));
	}
	
	private VendingMachine newVendingMachine()
	{
		//Inject selector
		//Could mock selector using, say, mockito, as have separate test, but using actual for now. 
		return new DefaultVendingMachine(new GreedyCoinSelector());
	}
	
	private void insertAndReturn(CoinPair...coinPairs) {
		VendingMachine machine = newVendingMachine();
		for (CoinPair pair : coinPairs)
		{
			for (int loop = 0; loop < pair.count; loop++)
			{
				machine.insertCoin(pair.coin);
			}
		}
		Coins coins = machine.returnInsertedCoins();
		validate(coins, coinPairs);
	}
	
	private void validate(Coins coins, CoinPair...coinPairs)
	{
		List<Coin> validCoins = new ArrayList<>(Arrays.asList(Coin.getOrderedValidValues()));
		for (CoinPair pair : coinPairs)
		{
			assertEquals(pair.count,coins.getCount(pair.coin));
			validCoins.remove(pair.coin);
		}
		validCoins.forEach(coin -> assertEquals(0,coins.getCount(coin)));
	}
	
	private void getA_ExactCoins(Coin...coins) {
		
		SelectionResult result = getA(newVendingMachine().fill(VendingItemTypes.ITEM_A, 1), coins);	
		
		assertTrue(result.isSuccess());
		assertEquals(ResultReason.OK, result.getReason());
		assertEquals(0, result.getChange().getValue());
	}
	
	private SelectionResult getA(VendingMachine machine, Coin...coins)
	{
		return get(machine, VendingItemTypes.ITEM_A, coins);
	}
	
	private SelectionResult getC(VendingMachine machine, Coin...coins)
	{
		return get(machine, VendingItemTypes.ITEM_C, coins);
	}
	
	private SelectionResult get(VendingMachine machine, VendingItemType type, Coin...coins)
	{
		for (Coin coin : coins) {
			machine.insertCoin(coin);
		}
		SelectionResult result = machine.select(type);		
		return result;
	}
	
	private CoinPair pair(Coin coin, int count)
	{
		return new CoinPair(coin,count);
	}
	private class CoinPair
	{
		Coin coin;
		int count;
		CoinPair(Coin coin, int count) { this.coin = coin; this.count = count; }
	}
	
}

