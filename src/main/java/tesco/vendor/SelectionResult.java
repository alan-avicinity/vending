package tesco.vendor;

import tesco.vendor.coins.Coins;

public class SelectionResult {
	public static enum ResultReason {
		INSUFFICIENT_PAYMENT,
		UNAVAILABLE,
		EXACT_CHANGE_UNAVAILABLE,
		OK;
	}

	private final boolean success;
	private final ResultReason reason;
	private final Coins change;

	public SelectionResult() {
		this(true, new Coins(), ResultReason.OK);
	}

	public SelectionResult(Coins change) {
		this(true, change, ResultReason.OK);
	}

	public SelectionResult(ResultReason reason) {
		this(false, new Coins(), reason);
	}

	public SelectionResult(boolean success, Coins change, ResultReason reason) {
		this.success = success;
		this.reason = reason;
		this.change = change;
	}

	public boolean isSuccess() {
		return success;
	}

	public ResultReason getReason() {
		return reason;
	}

	public Coins getChange() {
		return change;
	}
}
