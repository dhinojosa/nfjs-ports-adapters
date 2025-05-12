package com.xyzcorp.blackjack.domain;


public enum Rank {
    ACE("A"), TWO("2"), THREE("3"), FOUR("4"),
    FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
    NINE("9"), TEN("10"),
    JACK("J"), QUEEN("Q"), KING("K");

    private final String value;

    Rank(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public int rankValue() {
      if ("JQK".contains(toString())) {
        return 10;
      } else if (toString().equals("A")) {
        return 1;
      } else {
        return Integer.parseInt(toString());
      }
    }
}
