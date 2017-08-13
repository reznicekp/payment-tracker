package com.bsc.reznicek.paymenttracker.model;

public class Payment {

	private String currency;
	private int amount;

	public Payment(String payment) {
		String[] paymentParts = payment.split(" ");
		if (paymentParts[0].length() != 3) {
			throw new IllegalArgumentException("Currency is invalid!");
		}
		currency = paymentParts[0].toUpperCase();
		amount = Integer.valueOf(paymentParts[1]);
	}

	public Payment(String currency, int amount) {
		this.currency = currency;
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Payment other = (Payment) obj;
		if (currency == null) {
			if (other.currency != null) {
				return false;
			}
		} else if (!currency.equals(other.currency)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Payment [currency=" + currency + ", amount=" + amount + "]";
	}
}
