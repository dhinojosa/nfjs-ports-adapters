package com.evolutionnext.infrastruture.adapter.out;


import com.evolutionnext.application.port.out.ForGetttingTaxRates;

public class FixedTaxRateRepository implements ForGetttingTaxRates {
    @Override
    public double taxRate(double amount) {
        return 0.15;
    }
}
