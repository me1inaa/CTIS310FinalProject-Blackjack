package edu.guilford;

/**
 * The User class represents a user in the game of Blackjack.
 * @extends Player
 * @param password The password of the user.
 */
public class User extends Player{
    
        private String password;
        
        //constructor
        /**
         * Creates a user with a name, an empty hand, a default balance of 1000, and a password
         * @param username
         * @param password
         */
        public User(String username, String password) {
            super(username);
            this.password = password;
        }
        
        //methods
        /**
         * Returns the password of the user.
         * @return String password
         */
        public String getPassword() {
            return password;
        }

        //setters
        /**
         * Sets the password of the user.
         * @param password
         */
        public void setPassword(String password) {
            this.password = password;
        }
    }
    
    
