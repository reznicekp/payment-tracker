package com.bsc.reznicek.paymenttracker.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Class responsible for collecting all existing and incoming payments and sending them to the payment tracker.
 */
@Component
public class PaymentGate implements CommandLineRunner {

	@Autowired
	private IPaymentTracker paymentTracker;

	@Override
	public void run(String... args) throws Exception {
		registerExistingPayments(args);
		registerIncomingPayments();
	}

	/**
	 * Collect all existing payments provided in external files. Payments should be in format <code>CCC 12345</code>, where:
	 * <ul>
	 * <li><code>CCC</code> is 3 letter currency code (case insensitive)</li>
	 * <li><code>12345</code> is amount (any positive or negative whole number)</li>
	 * </ul>
	 *
	 * @param externalFilePaths
	 *            paths relative to the jar file
	 * @throws IOException
	 */
	private void registerExistingPayments(String[] externalFilePaths) throws IOException {
		for (String externalFilePath : externalFilePaths) {
			Stream<String> existingPayments = Files.lines(Paths.get(externalFilePath));
			existingPayments.forEach(payment -> {
				paymentTracker.newPayment(payment);
			});
			existingPayments.close();
		}
		paymentTracker.overview();
	}

	/**
	 * Listen to the user input. If a valid payment comes, it's processed by the tracker.
	 */
	private void registerIncomingPayments() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.lines().forEach(inputLine -> {
			if ("quit".equals(inputLine)) {
				System.exit(0);
			}
			paymentTracker.newPayment(inputLine);
		});
	}
}
