package com.evolutionnext;

import com.evolutionnext.application.port.out.ForGettingTaxRates;
import com.evolutionnext.application.service.TaxCalculatorService;
import com.evolutionnext.infrastructure.adapter.in.ApplicationController;
import com.evolutionnext.infrastructure.adapter.out.FixedTaxRateRepository;

public class Runner {
    public static void main(String[] args) {
        //Wiring of the layers
        ForGettingTaxRates taxRateRepository = new FixedTaxRateRepository();
        TaxCalculatorService taxCalculatorService = new TaxCalculatorService(taxRateRepository);
        ApplicationController applicationController = new ApplicationController(taxCalculatorService);

        //Invoking the application
        System.out.println(applicationController.getTaxCalculationStringFor(301.00));
    }
}
