package com.xyzcorp.blackjack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Hand {
    private final List<Card> cardList;

    public Hand() {
        cardList = new ArrayList<>();
    }

    public Hand(Card... card) {
        cardList = List.of(card);
    }

    public Stream<Card> stream() {
        return cardList.stream();
    }

    public void dealFrom(Deck deck) {
        cardList.add(deck.draw());
    }

    public int handValueOf() {
        List<Card> hand1 = cardList;
        int handValue = hand1
            .stream()
            .mapToInt(Card::rankValue)
            .sum();

        // does the hand contain at least 1 Ace?
        boolean hasAce = hand1
            .stream()
            .anyMatch(card -> card.rankValue() == 1);

        // if the total hand value <= 11, then count the Ace as 11 by adding 10
        if (hasAce && handValue < 11) {
            handValue += 10;
        }

        return handValue;
    }

    Card topCard() {
        return cardList.getFirst();
    }

    boolean isBust() {
        return handValueOf() > 21;
    }
}
