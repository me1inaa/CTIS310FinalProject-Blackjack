package edu.guilford;

import java.util.LinkedList;

public class Hand extends LinkedList<Card>{
    //attributes
    int value = 0;
    
    //constructor
    public Hand() {
        super();
    }

    public Hand(Card card) {
        super();
        this.add(card);
        this.getValue();
    }

    //methods
    public int getValue() {
        value = 0;
        for (Card card : this) {
            value += card.getValue();
        }
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    //split
    public Hand split() {
        Hand newHand = new Hand();
        newHand.add(this.remove(1));
        return newHand;
    }
}
