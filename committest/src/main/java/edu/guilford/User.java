package edu.guilford;

/**
 * The User class represents a user in the game of Blackjack.
 * @extends Player
 */
public class User extends Player{

    //constructor
    /** Creates a user with a name, an empty hand, a default balance of 1000
     * @param username
     */
    public User(String username) {
        super(username);
    }

    /** Creates a user with a name, an empty hand, and a specified balance 
     * @param username
     * @param balance
     */
    public User(String username, double balance) {
        super(username, balance);
    }
        
}
