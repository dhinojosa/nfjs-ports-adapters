package com.xyzcorp.blackjack.domain;

import java.util.Objects;

import static org.fusesource.jansi.Ansi.ansi;

/**
 * Source: Ted Young
 * <a href="https://moretestable.com/">MoreTestable.com</a>
 */
public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Suit suit, Rank rank) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isTen() {
        return getRank().toString().equals("10");
    }

    @Override
    public String toString() {
        return "Card {" +
               "suit=" + suit.toString() +
               ", rank=" + rank.toString() +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(suit, card.getSuit()) && Objects.equals(rank, card.getRank());
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
