package com.evolutionnext;

import com.evolutionnext.application.port.out.ForGetttingTaxRates;
import com.evolutionnext.infrastruture.adapter.in.TaxCalculator;
import com.evolutionnext.infrastruture.adapter.out.FixedTaxRateRepository;

public class Runner {
    public static void main(String[] args) {
        ForGetttingTaxRates taxRateRepository = new FixedTaxRateRepository();
        TaxCalculator myCalculator = new TaxCalculator(taxRateRepository);
        System.out.println(myCalculator.taxOn(10000));
    }
}
