package com.evolutionnext.infrastructure.adapter.out;


import com.evolutionnext.application.port.out.ForGettingTaxRates;

/**
 * This is a fixed repository,
 * but it can be made complex, for example a database, or a webservice,
 * although I wouldn't name it a repository if we were making a
 * synchronous call to a web service, I'd call it HttpClient, or something
 * to that effect.
 */
public class FixedTaxRateRepository implements ForGettingTaxRates {
    @Override
    public double taxRate(double amount) {
        return 0.15;
    }
}
