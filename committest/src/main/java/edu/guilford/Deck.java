package edu.guilford;

import java.util.LinkedList;

/**
 * The Deck class is a LinkedList of Cards.
 */

public class Deck extends LinkedList<Card>{
    //create a realistic deck of cards (52 cards)
    /**
     * Creates a realistic deck of cards (52 cards) in sequence.
     * Shuffles the deck.
     */
    public Deck() {
        super();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9",
                          "10", "Jack", "Queen", "King"};
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                Card card = new Card(suit, ranks[i]);
                this.add(card);
            }
        }
        shuffle();
    }

    //shuffle the deck
    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        for (int i = 0; i < this.size(); i++) {
            int randomIndex = (int) (Math.random() * this.size());
            Card temp = this.get(i);
            this.set(i, this.get(randomIndex));
            this.set(randomIndex, temp);
        }
    }

    //draw a card
    /**
     * Removes the first card from the deck and returns it.
     * @return The first card in the deck.
     */
    public Card draw() {
        return this.remove(0);
    }
   public Card drawCard() {
        return this.removeFirst();
      }
      public Card deal() {
        return this.remove(0);
    }

    //add cards to the deck
    /**
     * Adds cards to the deck.
     * @param LinkedList<card> The cards to be added to the deck.
     */
    public void addCards(LinkedList<Card> cards) {
        this.addAll(cards);
    }

    //resets the deck
    /**
     * Resets the deck to a realistic deck of cards (52 cards) in sequence.
     * Shuffles the deck.
     */
    public void reset() {
        this.clear();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9",
                          "10", "Jack", "Queen", "King"};
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                Card card = new Card(suit, ranks[i]);
                this.add(card);
            }
        }
        shuffle();
    }

}
