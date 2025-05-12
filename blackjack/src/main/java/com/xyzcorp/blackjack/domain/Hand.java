package com.xyzcorp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class Hand {
    private final List<Card> cardList = new ArrayList<Card>();

    public Stream<Card> stream() {
        return cardList.stream();
    }

    public Hand(Card... cards) {
        this.cardList.addAll(List.of(cards));
    }

    public void drawFrom(Deck deck) {
        cardList.add(deck.draw());
    }

    public Card topCard() {
        return cardList.getFirst();
    }

    public int value() {
        int handValue = cardList
            .stream()
            .mapToInt(value -> value.getRank().rankValue())
            .sum();

        // does the hand contain at least 1 Ace?
        boolean hasAce = cardList
            .stream()
            .anyMatch(card -> card.getRank().rankValue() == 1);

        // if the total hand value <= 11, then count the Ace as 11 by adding 10
        if (hasAce && handValue < 11) {
            handValue += 10;
        }

        return handValue;
    }

    public boolean isBust() {
        return value() > 21;
    }

    boolean underThreshold() {
        return value() <= 16;
    }

    boolean beats(Hand other) {
        return other.value() < value();
    }

    boolean pushes(Hand other) {
        return value() == other.value();
    }

    public void clear() {
        cardList.clear();
    }
}
