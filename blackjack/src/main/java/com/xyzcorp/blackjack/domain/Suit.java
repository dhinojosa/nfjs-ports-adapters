package com.xyzcorp.blackjack.domain;


public enum Suit {
    HEARTS("♥"), SPADES("♠"), CLUBS("♣"), DIAMONDS("♦");

    private final String value;

    Suit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
