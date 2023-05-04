package edu.guilford;

import java.util.LinkedList;

public class Deck extends LinkedList<Card>{
    //create a realistic deck of cards (52 cards)
    public Deck() {
        super();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9",
                          "10", "Jack", "Queen", "King"};
        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                Card card = new Card(suit, ranks[i], i + 1);
                this.add(card);
            }
        }
        shuffle();
    }

    //shuffle the deck
    public void shuffle() {
        for (int i = 0; i < this.size(); i++) {
            int randomIndex = (int) (Math.random() * this.size());
            Card temp = this.get(i);
            this.set(i, this.get(randomIndex));
            this.set(randomIndex, temp);
        }
    }

    //deal a card
    public Card deal() {
        return this.remove(0);
    }

}
