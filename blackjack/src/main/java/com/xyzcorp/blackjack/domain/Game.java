package com.xyzcorp.blackjack.domain;

import java.util.Optional;

/**
 * Source: Ted Young
 * <a href="https://moretestable.com/">MoreTestable.com</a>
 */
public class Game {

    private final Deck deck;
    private final Hand playerHand = new Hand();
    private final Hand dealerHand = new Hand();

    public Hand getPlayerHand() {
        return playerHand;
    }

    public Hand getDealerHand() {
        return dealerHand;
    }

    public Game() {
        deck = new Deck();
        initialDeal();
    }

    public void initialDeal() {
        dealRoundOfCards();
        dealRoundOfCards();
    }

    private void dealRoundOfCards() {
        playerHand.drawFrom(deck);
        dealerHand.drawFrom(deck);
    }

    public void stand() {
        dealerPlays();
    }

    public void hit() {
        if (!playerHand.isBust()) {
            playerHand.drawFrom(deck);
        }
    }

    public void reset() {
        playerHand.clear();
        dealerHand.clear();
        initialDeal();
    }

    public void dealerPlays() {
        // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>=stand)
        if (!playerHand.isBust()) {
            while (dealerHand.underThreshold()) {
                dealerHand.drawFrom(deck);
            }
        }
    }

    public Optional<GameOutcome> getGameOutcomeOptional() {
        GameOutcome result;
        if (playerHand.isBust()) {
            result = GameOutcome.BUST;
        } else if (dealerHand.isBust()) {
            result = GameOutcome.DEALER_BUST;
        } else if (playerHand.beats(dealerHand)) {
            result = GameOutcome.WIN;
        } else if (dealerHand.pushes(playerHand)) {
            result = GameOutcome.PUSH;
        } else {
            result = GameOutcome.LOST;
        }
        return Optional.of(result);
    }
}
