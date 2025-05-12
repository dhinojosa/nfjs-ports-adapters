package com.xyzcorp.blackjack.adapter.in;


import com.xyzcorp.blackjack.domain.Hand;

import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.ansi;

public class DisplayHand {
    public static void displayHand(Hand hand) {
        System.out.println(hand.stream()
            .map(DisplayCard::display)
            .collect(Collectors.joining(
                ansi().cursorUp(6).cursorRight(1).toString())));
    }
}
