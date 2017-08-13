package com.bsc.reznicek.paymenttracker.model;

public enum ExchangeRate {

	EUR(0.85),
	CZK(22.13),
	GBP(0.77),
	HUF(257.6),
	JPY(109.2),
	PLN(3.62),
	RUB(59.83),
	CHF(0.96);

	private double rateUSD;

	private ExchangeRate(double rateUSD) {
		this.rateUSD = rateUSD;
	}

	public static double equivalentInUSD(Payment payment) {
		return Long.valueOf(Math.round(payment.getAmount() * valueOf(payment.getCurrency()).rateUSD * 100)).doubleValue() / 100;
	}
}
