package com.bsc.reznicek.paymenttracker.components;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bsc.reznicek.paymenttracker.model.ExchangeRate;
import com.bsc.reznicek.paymenttracker.model.Payment;

/**
 * Accepts payments and prints out the overview of net amounts.
 */
@Component
public class PaymentTracker implements IPaymentTracker {

	private Set<Payment> payments = new HashSet<>();

	/**
	 * Accept and manage new payment. E.g. if the currency is already present, only the amount is added to current net amount,
	 * if the currency is not present yet, it's added to the collection.
	 */
	@Override
	public void newPayment(String newPaymentRoughInput) {
		if (StringUtils.isEmpty(newPaymentRoughInput)) {
			return;
		}

		try {
			Payment newPayment = new Payment(newPaymentRoughInput);
			Optional<Payment> existingPayment = payments.stream().filter(payment -> newPayment.equals(payment)).findFirst();
			if (existingPayment.isPresent()) {
				int newAmount = existingPayment.get().getAmount() + newPayment.getAmount();
				if (newAmount == 0) {
					payments.remove(existingPayment.get());
				} else {
					existingPayment.get().setAmount(newAmount);
				}
			} else {
				payments.add(newPayment);
			}
		} catch (RuntimeException e) {
			System.out.println("Sorry, your payment wasn't accepted. Check the format and try again.");
		}
	}

	/**
	 * Once per minute print out an overview about realized payments - currencies and their net amounts.
	 * In case of known currency also the equivalent of USD is printed out.
	 */
	@Scheduled(initialDelay = 60 * 1000, fixedDelay = 60 * 1000)
	@Override
	public void overview() {
		System.out.println("==SUMMARY==");

		if (payments.isEmpty()) {
			System.out.println("no payments");
		} else {
			payments.forEach(payment -> {
				if (payment.getAmount() != 0) {
					String basicPaymentPrintout = payment.getCurrency() + " " + payment.getAmount();
					try {
						System.out.println(basicPaymentPrintout + " (USD " + ExchangeRate.equivalentInUSD(payment) + ")");
					} catch (RuntimeException e) {
						System.out.println(basicPaymentPrintout);
					}
				}
			});
		}

		System.out.println("===========");
	}

	public Set<Payment> getPayments() {
		return payments;
	}
}
