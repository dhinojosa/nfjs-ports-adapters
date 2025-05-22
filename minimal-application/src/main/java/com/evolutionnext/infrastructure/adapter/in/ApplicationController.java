package com.evolutionnext.infrastructure.adapter.in;

import com.evolutionnext.application.port.in.ForCalculatingTaxes;

/**
 * I included the controller here. This will represent what your
 * web application or cli classes would do on your behalf.
 */
public class ApplicationController {
    private final ForCalculatingTaxes forCalculatingTaxes;

    public ApplicationController(ForCalculatingTaxes forCalculatingTaxes) {
        this.forCalculatingTaxes = forCalculatingTaxes;
    }

    public String getTaxCalculationStringFor(double amount) {
        double result = forCalculatingTaxes.taxOn(amount);
        return String.format("Tax calculated for the amount $%.2f will be $%.2f", amount, result);
    }
}
