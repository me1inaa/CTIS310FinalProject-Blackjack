package edu.guilford;

import java.util.LinkedList;
import java.util.List;

/** The Hand class is a LinkedList of Cards.
 * 
 * @param value The value of the hand to determine if the player has busted.
 */
public class Hand extends LinkedList<Card> {
    // attributes
    private int value = 0;

    // constructor
    /** Creates an empty hand.
     */
    public Hand() {
        super();
    }

    /** Creates a hand with a card. Used for splitting.
     * 
     * @param card
     */
    public Hand(Card card) {
        super();
        this.add(card);
        this.getValue();
    }

    // methods
    /** Returns the value of the hand.
     * 
     * @return int value
     */
    public int getValue() {
        this.calcValue();
        return value;
    }

    /** Returns the value of the hand.
     * 
     * @return int value
     */
    public void setValue(int value) {
        this.value = value;
    }

    // calculate total value
    private void calcValue() {
        int total = 0;
        for (Card card : this) {
            total += card.getValue();
        }
        this.setValue(total);
    }

    // split
    /** Removes the second card from the hand and returns it.
     * 
     * @return an new hand with the second card.
     */
    public Hand split() {
        if (this.size() != 2 || this.get(0).getValue() != this.get(1).getValue()) {
            System.out.println("You can only split on the first turn and if the first two cards are the same.");
            return null;
        }
        Hand newHand = new Hand();
        newHand.add(this.remove(1));
        return newHand;
    }

    // toString
    /** Returns a string representation of the hand.
     * 
     * @return String output of each card separated by a comma.
     */
    @Override
    public String toString() {
        String output = "";
        for (Card card : this) {
            output += card.toString() + ", ";
        }
        return output;
    }
}
