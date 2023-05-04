package edu.guilford;

public class Card {
    //attributes
    private String suit;
    private String rank;
    private int value;
    private String color;

    //constructor
    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        //rank determins the value
        // 2-10 are value listed
        // J, Q, K = 10
        // A = 1 or 11, y default 1
        if (rank.equals("Ace")) {
            value = 1;
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
    }

    //create a random card
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
    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setColor(String color) {
        this.color = color;
    }
    
    //equals
    public boolean equals(Card card) {
        return suit.equals(card.getSuit()) && rank.equals(card.getRank()) && value == card.getValue();
    }

    public String toString() {
        return rank + " of " + suit;
    }
}
