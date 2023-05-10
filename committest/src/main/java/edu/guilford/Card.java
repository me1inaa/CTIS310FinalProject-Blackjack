package edu.guilford;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/** The Card class is a singular card in a deck of cards.
 * @param suit The suit of the card.
 * @param rank The rank of the card. (2-10, J, Q, K, A)
 * @param value The value of the card. (2-10, J, Q, K = 10, A = 1 or 11)
 * @param color The color of the card. (Red or Black)
 * @param imagePath The Path to the image of the card.
 */
public class Card {
    //attributes
    private String suit;
    private String rank;
    private int value;
    private String color;
    private Path imagePath;

    //constructor
    /** Creates a card with a suit, rank, and value.
     * @param suit
     * @param rank
     * value and color are automatically determined by the rank
     * Aces are initially set to 11
     */
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        //rank determins the value
        // 2-10 are value listed
        // J, Q, K = 10
        // A = 1 or 11, by default 11
        if (rank.equals("Ace")) {
            value = 11;
        } else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
            value = 10;
        } else {
            value = Integer.parseInt(rank);
        }

        //set the color
        if (suit.equals("Hearts") || suit.equals("Diamonds")) {
            color = "Red";
        } else {
            color = "Black";
        }

        //set the image
        try {
            //System.out.println(this.toString().toLowerCase().replaceAll(" ", "_"));
            this.imagePath = Paths.get(BlackjackPane.class.getResource("/edu/guilford/cardImages/" + 
                    this.toString().toLowerCase().replaceAll(" ", "_") + ".png").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    //create a random card
    /** Creates a random card using random indexing of the suits and ranks arrays.
     */
    public Card() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        int suitIndex = (int) (Math.random() * suits.length);
        int rankIndex = (int) (Math.random() * ranks.length);
        suit = suits[suitIndex];
        rank = ranks[rankIndex];
        value = rankIndex + 1;
        if (suit.equals("Hearts") || suit.equals("Diamonds")) {
            color = "Red";
        } else {
            color = "Black";
        }
    }

    //methods
    /** Returns the suit of the card.
     * @return String suit
     */
    public String getSuit() {
        return suit;
    }

    /** Returns the rank of the card.
     * @return String rank
     */
    public String getRank() {
        return rank;
    }

    /** Returns the value of the card.
     * @return int value
     */
    public int getValue() {
        return value;
    }

    /** Returns the color of the card.
     * @return String color
     */
    public String getColor() {
        return color;
    }

    //setters
    /** Sets the suit of the card.
     * @param suit
     */
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /** Sets the rank of the card.
     * @param rank
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /** Sets the value of the card.
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /** Sets the color of the card.
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    //override equals
    /** Checks if two cards are equal.
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card card = (Card) obj;
            return suit.equals(card.getSuit()) && rank.equals(card.getRank()) && value == card.getValue();
        } else {
            return false;
        }
    }

    //toString
    /** Returns a string representation of the card.
     * @return String
     */
    public String toString() {
        return rank + " of " + suit;
    }
}
