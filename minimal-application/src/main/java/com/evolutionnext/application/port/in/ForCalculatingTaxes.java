package com.evolutionnext.application.port.in;

//From the book Hexagonal Architecture Explained

public interface ForCalculatingTaxes {
    double taxOn(double amount);
}
