package com.bsc.reznicek.paymenttracker;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.bsc.reznicek.paymenttracker.components.PaymentTracker;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class PaymentTrackerTest {

	@InjectMocks
	private PaymentTracker paymentTracker;

	@Test
	public void testNewPaymentRoughInputInvalid() {
		paymentTracker.newPayment("invalid input");
		paymentTracker.newPayment("CZK invalid");
		paymentTracker.newPayment("invalid 1000");
		assertEquals(0, paymentTracker.getPayments().size());
	}

	@Test
	public void testNewPaymentRoughInputIsEmpty() {
		paymentTracker.newPayment(null);
		paymentTracker.newPayment("");
		assertEquals(0, paymentTracker.getPayments().size());
	}

	@Test
	public void testAddNewPaymentFirstOfGivenCurrency() {
		paymentTracker.newPayment("CZK 1000");
		assertEquals(1, paymentTracker.getPayments().size());
	}

	@Test
	public void testAddNewPaymentAnotherOfGivenCurrency() {
		paymentTracker.newPayment("CZK 1000");
		paymentTracker.newPayment("CZK 500");
		assertEquals(1, paymentTracker.getPayments().size());
	}

	@Test
	public void testAddNewPaymentAnotherOfGivenCurrencyTotalAmount0() {
		paymentTracker.newPayment("CZK 1000");
		paymentTracker.newPayment("CZK 500");
		paymentTracker.newPayment("CZK -1500");
		assertEquals(0, paymentTracker.getPayments().size());
	}
}
