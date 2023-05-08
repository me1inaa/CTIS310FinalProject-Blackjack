package edu.guilford;

/**
 * The Player class represents a player in the game of Blackjack.
 * @param name The name of the player.
 * @param hand The hand of the player.
 * @param bet The bet of the player.
 * @param balance The balance of the player.
 */
public class Player {
    //name
    protected String name;
    //hand
    protected Hand hand;
    protected Hand splitHand;
    //bet
    protected double bet;
    //balance
    protected double balance;

    //constructor
    /**
     * Creates a player with a name, an empty hand, and a balance.
     * @param name
     * @param balance
     */
    public Player(String name, double balance) {
        this.name = name;
        this.hand = new Hand();
        this.bet = 0;
        this.balance = balance;
    }
    
    /**
     * Creates a new player with a name, an empty hand, and a default baleance of 1000.
     * @param name
     */
    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
        this.bet = 0;
        this.balance = 1000;
    }

    //methods
    /**
     * Returns the name of the player.
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the hand of the player.
     * @return Hand hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns the bet of the player.
     * @return int bet
     */
    public double getBet() {
        return bet;
    }

    /**
     * Returns the balance of the player.
     * @return double balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the name of the player.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the hand of the player.
     * @param hand
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    /**
     * Sets the bet of the player.
     * @param bet
     */
    public void setBet(double bet) {
        this.bet = bet;
    }

    /**
     * Sets the balance of the player.
     * @param balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    //split
    /**
     * Splits the hand of the player into two hands.
     */
    public void split() {
        this.splitHand = hand.split();
    }

    /**
     * Returns the split hand of the player.
     * @return Hand splitHand
     */
    public Hand getSplitHand() {
        return splitHand;
    }

    //clear the hand and bet for the next round
    /**
     * Clears the hand and bet of the player for the next round.
     */
    public void reset() {
        this.hand = new Hand();
        this.splitHand = null;
        this.bet = 0;
    }

    //double down
    /**
     * Doubles the bet of the player.
     */
    public void doubleDown() {
        this.bet *= 2;
    }

    //toString
    /**
     * Returns a string representation of the player.
     * The name, hand, bet, and balance.
     * @return String representation of the player
     */
    @Override
    public String toString() {
        return name + ": \n\tHand: " + hand + "\n\tBet: " + bet + "\n\tBalance: $" + String.format("%.2f", balance);
    }



    
}
