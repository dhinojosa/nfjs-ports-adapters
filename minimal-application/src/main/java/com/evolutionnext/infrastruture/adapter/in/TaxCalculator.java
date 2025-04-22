package com.evolutionnext.infrastruture.adapter.in;


import com.evolutionnext.application.port.in.ForCalculatingTaxes;
import com.evolutionnext.application.port.out.ForGetttingTaxRates;

public class TaxCalculator implements ForCalculatingTaxes {
    private final ForGetttingTaxRates taxRateRepository;

    public TaxCalculator(ForGetttingTaxRates taxRateRepository) {
        this.taxRateRepository = taxRateRepository;
    }

    @Override
    public double taxOn(double amount) {
        return amount * taxRateRepository.taxRate(amount);
    }
}
