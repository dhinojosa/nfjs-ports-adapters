package com.xyzcorp.blackjack;

import com.xyzcorp.blackjack.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Source: Ted Young
 * <a href="https://moretestable.com/">MoreTestable.com</a>
 */
public class HandValueAceTest {

    @Test
    public void handWithOneAceTwoCardsIsValuedAt11() throws Exception {
        Hand hand = new Hand(new Card(Suit.SPADES, Rank.ACE), new Card(Suit.SPADES, Rank.FIVE));
        assertThat(hand.value())
            .isEqualTo(11 + 5);
    }

    @Test
    public void handWithOneAceAndOtherCardsEqualTo11IsValuedAt1() throws Exception {
        Hand hand = new Hand(new Card(Suit.SPADES, Rank.ACE),
            new Card(Suit.SPADES, Rank.EIGHT),
            new Card(Suit.SPADES, Rank.THREE));
        assertThat(hand.value())
            .isEqualTo(1 + 8 + 3);
    }
}
