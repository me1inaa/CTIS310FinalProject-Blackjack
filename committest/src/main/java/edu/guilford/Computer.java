package edu.guilford;

public class Computer extends Player{
    //attributes
    //point system for card counting algorithm
    private double points;
    //variable that holds whether the Computer hits
    private boolean hit = false;

    //constructor
    /**
     * Creates a computer with a name, an empty hand, and a default balance of 1000.
     */
    public Computer() {
        super("Bot");
    }

    //methods
    /**
     * Returns the points of the computer.
     * @return double points
     */
    public double getPoints() {
        return points;
    }

    /**
     * Sets the points of the computer.
     * @param points
     */
    public void setPoints(double points) {
        this.points = points;
    }

    /**
     * Calculates the points of the computer.
     */
    public void calcPoints() {
        //TODO: implement card counting algorithm
    }

    private void decideHit() {
        //TODO: decide whether to hit or not
    }

    private void decideBet() {
        //TODO: decide how much to bet
        this.bet = 0;
    }

    /**
     * Returns whether the computer hits.
     * @return boolean hit
     */
    public boolean isHit() {
        return hit;
    }

    /**
     * Sets whether the computer hits.
     * @param hit
     */
    public void setHit(boolean hit) {
        this.hit = hit;
    }


    
}
