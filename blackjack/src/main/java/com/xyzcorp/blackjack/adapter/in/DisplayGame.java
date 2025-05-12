package com.xyzcorp.blackjack.adapter.in;


import com.xyzcorp.blackjack.domain.Game;
import org.fusesource.jansi.Ansi;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class DisplayGame {

    public static void displayWelcomeMessage() {
        System.out.println(ansi()
                               .bgBright(Ansi.Color.WHITE)
                               .eraseScreen()
                               .cursor(1, 1)
                               .fgGreen().a("Welcome to")
                               .fgRed().a(" Jitterted's")
                               .fgBlack().a(" BlackJack"));
    }

    public static void resetScreen() {
        System.out.println(ansi().reset());
    }

    public static String inputFromPlayer() {
        System.out.println("[H]it or [S]tand?");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static void displayFinalGameState(Game game) {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        DisplayHand.displayHand(game.getDealerHand());
        System.out.println(" (" + game.getDealerHand().value() + ")");

        System.out.println();
        System.out.println("Player has: ");
        DisplayHand.displayHand(game.getPlayerHand());
        System.out.println(" (" + game.getPlayerHand().value() + ")");
    }

    public static void displayGameState(Game game) {
        System.out.print(ansi().eraseScreen().cursor(1, 1));
        System.out.println("Dealer has: ");
        System.out.println(DisplayCard.display(game.getDealerHand().topCard()));

        DisplayCard.displayBackOfCard();

        System.out.println();
        System.out.println("Player has: ");
        DisplayHand.displayHand(game.getPlayerHand());
        System.out.println(" (" + game.getPlayerHand().value() + ")");
    }

    public static void main(String[] args) {
        Game game = new Game();
        displayWelcomeMessage();
        play(game);
        resetScreen();
    }

    public static void displayOutcome(Game game) {
        game.getGameOutcomeOptional().ifPresentOrElse(gameOutcome -> {
            switch (gameOutcome) {
                case WIN -> System.out.println("You beat the Dealer! 💵");
                case BUST -> System.out.println("You Busted, so you lose.  💸");
                case DEALER_BUST -> System.out.println("Dealer went BUST, Player wins! Yay for you!! 💵");
                case PUSH -> System.out.println("Push: You tie with the Dealer. 💸");
                case LOST -> System.out.println("You lost to the Dealer. 💸");
            }
        }, () -> System.out.println("No status yet"));
    }

    public static void play(Game game) {
        // get Player's decision: hit until they stand, then they're done (or they go bust)
        while (!game.getPlayerHand().isBust()) {
            displayGameState(game);
            String playerChoice = inputFromPlayer().toLowerCase();
            if (playerChoice.startsWith("s")) {
                game.stand();
            }
            if (playerChoice.startsWith("h")) {
                game.hit();
            } else {
                System.out.println("You need to [H]it or [S]tand");
            }
        }
        displayFinalGameState(game);
        displayOutcome(game);
    }
}
