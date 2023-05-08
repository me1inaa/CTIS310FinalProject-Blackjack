package edu.guilford;

import java.util.ArrayList;


 /** This class represents a computer player in a game of Blackjack.
 * 
 * @extends Player
 * @param count
 * @param hit
 * @param visibleCards
 */
// https://www.freecodecamp.org/news/i-used-programming-to-figure-out-how-card-counting-really-works-7ecefdb1b8d4/
// https://towardsdatascience.com/beating-the-dealer-with-simple-statistics-71b5e3701638
public class Computer extends Player {
    // attributes
    // point system for card counting algorithm
    private double count;
    // variable that holds whether the Computer hits
    private boolean hit = false;
    // variable that holds teh visible cards to the computer
    private ArrayList<Card> visibleCards;
    private Card dealerCard;

    // constructor
     /** Creates a computer with a name, an empty hand, and a default balance of 1000.
     */
    public Computer() {
        super("Bot");
    }

    // methods
     /** Returns the points of the computer.
     * 
     * @return double points
     */
    public double getCount() {
        return count;
    }

    
     /** Sets the points of the computer.
     * 
     * @param points
     */
    public void setCount(double points) {
        this.count = points;
    }

    
     /** Returns the visible cards of the computer.
     * 
     * @return ArrayList<Card> visibleCards
     */
    public ArrayList<Card> getVisibleCards() {
        return visibleCards;
    }

    
     /** Sets the visible cards of the computer.
     * 
     * @param visibleCards
     */
    public void setVisibleCards(ArrayList<Card> visibleCards) {
        this.visibleCards = visibleCards;
    }

    
     /** Calculates the points of the computer.
     */
    private void calcPoints() {
        // TODO: implement card counting algorithm
        // if the card is 2-6, add 1 to the running count
        // if the card is 7-9, do nothing
        // if the card is 10-Ace, subtract 1 from the running count
        count = 0;
        for (Card card : visibleCards) {
            if (card.getValue() >= 2 && card.getValue() <= 6) {
                count++;
            } else if (card.getValue() >= 10) {
                count--;
            }
        }
    }

    private void decideHit() {
        // TODO: decide whether to hit or not
        calcPoints();
        // calculate teh number of unused Aces in the hand
        int numAces = 0;
        for (Card card : visibleCards) {
            if (card.getValue() == 11) {
                numAces++;
            }
        }
        int handVal = this.getHand().getValue();
        /*
         * SPLIT: If it is the first turn, the hand can be split, and the first card in
         * the hand is equal to 11, 9 (and the casino card is not 7, 10, or 11), 8, 7
         * (and the casino card is less than or equal to 7), 6 (and the casino card is
         * less than or equal to 6), or 4 (and the casino card is between 5 and 6), or
         * if the first card in the hand is between 2 and 3 (and the casino card is less
         * than or equal to 7).
         */
        if (this.getHand().size() == 2 && this.getHand().get(0).getValue() == this.getHand().get(1).getValue()) {
            if (this.getHand().get(0).getValue() == 11
                    || this.getHand().get(0).getValue() == 9 && dealerCard.getValue() != 7
                            && dealerCard.getValue() != 10 && dealerCard.getValue() != 11
                    || this.getHand().get(0).getValue() == 8
                    || this.getHand().get(0).getValue() == 7 && dealerCard.getValue() <= 7
                    || this.getHand().get(0).getValue() == 6 && dealerCard.getValue() <= 6
                    || this.getHand().get(0).getValue() == 4 && dealerCard.getValue() >= 5 && dealerCard.getValue() <= 6
                    || this.getHand().get(0).getValue() >= 2 && this.getHand().get(0).getValue() <= 3
                            && dealerCard.getValue() <= 7) {
                split();
                hit = false;
            }
        }

        /*
         * STAND: If the hand has an unused ace and the point value of the hand is
         * greater than or equal to 19, or if the hand has an unused ace and the point
         * value of the hand is 18 and the casino card is less than 9, or if the point
         * value of the hand is greater than 16, or if the point value of the hand is
         * greater than 12 and the casino card is less than 4, or if the point value of
         * the hand is greater than 11 and the casino card is between 4 and 6.
         */

        else if ((numAces > 0 && handVal >= 19) || (numAces > 0 && handVal == 18 && dealerCard.getValue() < 9)
                || handVal > 16 || (handVal > 12 && dealerCard.getValue() < 4)
                || (handVal > 11 && dealerCard.getValue() >= 4 && dealerCard.getValue() <= 6)
                || (numAces > 0 && dealerCard.getValue() >= 4 && dealerCard.getValue() <= 6 && handVal >= 18)) {
            hit = false;
        }
        /*
         * HIT: If the
         * hand has an unused ace and the casino card is 3 and the point value of the
         * hand is greater than or equal to 17, or if the hand has an unused ace and the
         * casino card is 4 and the point value of the hand is greater than or equal to
         * 15, or if the hand has an unused ace and the casino card is between 5 and 6,
         * or if the point value of the hand is 11 and it is the first turn, or if the
         * point value of the hand is 10 and the casino card is less than 10, or if the
         * point value of the hand is 9 and the casino card is between 3 and 6.
         */
        else if ((numAces > 0 && dealerCard.getValue() == 3 && handVal >= 17)
                || (numAces > 0 && dealerCard.getValue() == 4 && handVal >= 15)
                || (numAces > 0 && dealerCard.getValue() >= 5 && dealerCard.getValue() <= 6)
                || (handVal == 11 && this.getHand().size() == 2)
                || (handVal == 10 && dealerCard.getValue() < 10)
                || (handVal == 9 && dealerCard.getValue() >= 3 && dealerCard.getValue() <= 6)) {
            hit = true;
        }
        else {
            hit = true;
        }
    }

    private double decideBet() {
        // TODO: decide how much to bet
        this.bet = this.balance / 100;
        this.bet = (Math.round(this.bet * 100.0) / 100.0) * (count - 1);
        // force the bet to be between 1 dollar and the balance
        if (this.bet < 1) {
            this.bet = 1;
        } else if (this.bet > this.balance) {
            this.bet = this.balance;
        }
        return this.bet;
    }

    
     /** Returns whether the computer hits.
     * 
     * @return boolean hit
     */
    public boolean isHit() {
        decideHit();
        return hit;
    }

    
     /** Sets whether the computer hits.
     * 
     * @param hit
     */
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    // gets the dealer card
    
     /** Returns the dealer card.
     * 
     * @return Card dealerCard
     */
    public Card getDealerCard() {
        return dealerCard;
    }

    // sets the dealer card
    
     /** Sets the dealer card. Should be the top card in the dealer's hand.
     * @param dealerCard
     */
    public void setDealerCard(Card dealerCard) {
        this.dealerCard = dealerCard;
    }

    // override get bet
     /** Returns the bet of the computer.
     * 
     * @return double bet
     */
    @Override
    public double getBet() {
        return decideBet();
    }

}
