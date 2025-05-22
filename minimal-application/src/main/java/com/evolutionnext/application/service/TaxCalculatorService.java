package com.evolutionnext.application.service;


import com.evolutionnext.application.port.in.ForCalculatingTaxes;
import com.evolutionnext.application.port.out.ForGettingTaxRates;

/**
 * This is an application service. Note the repository injected here. The book
 * Hexagonal Architecture Explained, just calls this `TaxCalculator`, but I
 * added the suffix `Service` since I believe this is a service, because:
 * <p/>
 * 1. It is implementing a port in
 * 2. It is injecting in a repository
 */
public class TaxCalculatorService implements ForCalculatingTaxes {
    private final ForGettingTaxRates taxRateRepository;

    public TaxCalculatorService(ForGettingTaxRates taxRateRepository) {
        this.taxRateRepository = taxRateRepository;
    }

    @Override
    public double taxOn(double amount) {
        return amount * taxRateRepository.taxRate(amount);
    }
}
