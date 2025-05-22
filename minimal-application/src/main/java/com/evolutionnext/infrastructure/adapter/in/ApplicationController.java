package com.evolutionnext.infrastructure.adapter.in;

import com.evolutionnext.application.port.in.ForCalculatingTaxes;

/**
 * I included the controller here. This will represent what your
 * web application or cli classes would do on your behalf.
 */
public class ApplicationController {
    private final ForCalculatingTaxes forCalculatingTaxes;

    /**
     * Inject the driving port (interface), it is up to the application
     * developer to wire whatever driving implementation that they wish
     *
     * @param forCalculatingTaxes the driving port
     */
    public ApplicationController(ForCalculatingTaxes forCalculatingTaxes) {
        this.forCalculatingTaxes = forCalculatingTaxes;
    }

    /**
     * I decided here to mimic create a DTO by returning a formatted string
     *
     * @param amount the amount that is to be taxed
     * @return the string representation of the tax applied
     */
    public String getTaxCalculationStringFor(double amount) {
        double result = forCalculatingTaxes.taxOn(amount);
        return String.format("Tax calculated for the amount $%.2f will be $%.2f", amount, result);
    }
}
