package com.xyzcorp.blackjack;

import com.xyzcorp.blackjack.adapter.in.DisplayCard;
import com.xyzcorp.blackjack.domain.Card;
import com.xyzcorp.blackjack.domain.Rank;
import com.xyzcorp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Source: Ted Young
 * <a href="https://moretestable.com/">MoreTestable.com</a>
 */
class CardTest {

  @Test
  public void withNumberCardHasNumericValueOfTheNumber() throws Exception {
    Card card = new Card(Suit.DIAMONDS, Rank.SEVEN);

    assertThat(card.getRank().rankValue())
        .isEqualTo(7);
  }

  @Test
  public void withValueOfQueenHasNumericValueOf10() throws Exception {
    Card card = new Card(Suit.DIAMONDS, Rank.QUEEN);

    assertThat(card.getRank().rankValue())
        .isEqualTo(10);
  }

  @Test
  public void withAceHasNumericValueOf1() throws Exception {
    Card card = new Card(Suit.DIAMONDS, Rank.ACE);

    assertThat(card.getRank().rankValue())
        .isEqualTo(1);
  }

  @Test
  public void suitOfHeartsOrDiamondsIsDisplayedInRed() throws Exception {
    // given a card with Hearts or Diamonds
    Card heartsCard = new Card(Suit.HEARTS, Rank.TEN);
    Card diamondsCard = new Card(Suit.DIAMONDS, Rank.EIGHT);

    // when we ask for its display representation
    String ansiRedString = ansi().fgRed().toString();

    // then we expect a red color ansi sequence
    assertThat(DisplayCard.display(heartsCard))
        .contains(ansiRedString);
    assertThat(DisplayCard.display(diamondsCard))
        .contains(ansiRedString);
  }
}
