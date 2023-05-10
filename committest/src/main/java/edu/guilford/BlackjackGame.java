package edu.guilford;

import java.util.ArrayList;

/** The BlackjackGame class represents a game of Blackjack.
 * 
 * @param players   The list of players in the game.
 * @param dealer    The dealer in the game.
 * @param computer  The computer in the game.
 * @param state     The state of the game.
 * @param deck      The deck of cards in the game.
 * @param gameState The state of the game.
 */
public class BlackjackGame {

    // attributes
    // list of players
    private ArrayList<Player> players = new ArrayList<Player>();
    // dealer
    private Player dealer;
    // computer
    private Computer computer;

    // game state
    private GameState state;
    // deck
    private Deck deck;
    // game state
    private GameState gameState;

    // game state machine
    public static enum GameState {
        BETTING, DEALING, PLAYING, DEALERPLAYING, RESOLVING, GAMEOVER
    }

    // constructor
    /** Creates a game of Blackjack with a list of players.
     * 
     * @param users The list of users in the game.
     */
    public BlackjackGame(User user) {
        // instantiates a new deck
        deck = new Deck();
        // instantiates a new computer
        computer = new Computer();
        // instantiates a new dealer
        dealer = new Player("Dealer", 1000000);
        // adds the computer to the list of players
        this.players.add(computer);
        // add the player associated with the user to the list of players
        this.players.add(user);
        // sets the game state to betting
        this.gameState = GameState.BETTING;

    }

    // methods
    /** Returns the list of players.
     * 
     * @return ArrayList<Player> players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /** Returns the dealer.
     * 
     * @return Player dealer
     */
    public Player getDealer() {
        return dealer;
    }

    /** Returns the computer.
     * 
     * @return Computer computer
     */
    public void addUser(User user) {
        players.add(user);
    }

    /** Adds a user to the list of players.
     * 
     * @param user
     */
    public void removeUser(User user) {
        players.remove(user);
    }

    /** Adds a computer to the list of players.
     * 
     * @param computer
     */
    public void addComputer(Computer computer) {
        players.add(computer);
    }

    /** Removes a computer from the list of players.
     * 
     * @param computer
     */
    public void removeComputer(Computer computer) {
        players.remove(computer);
    }

    /** Sets the game state.
     * 
     * @param state
     */
    public void setGameState(GameState state) {
        this.gameState = state;
    }

    /** Returns the game state.
     * 
     * @return GameState gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /** Sets the deck.
     * 
     * @param deck
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /** Returns the deck.
     * 
     * @return Deck deck
     */
    public Deck getDeck() {
        return deck;
    }

    /** Sets the computer.
     * 
     * @param computer
     */
    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    /** Returns the computer.
     * 
     * @return Computer computer
     */
    public Computer getComputer() {
        return computer;
    }

    /** Sets the list of players.
     * 
     * @param players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /** Sets the dealer.
     * 
     * @param dealer
     */
    public void setDealer(Player dealer) {
        this.dealer = dealer;
    }

    /** Deals two cards to each player and two cards to the dealer.
     */
    public void deal() {
        // sets the game state to dealing
        gameState = GameState.DEALING;
        // deal two cards to each player
        for (Player player : players) {
            player.getHand().add(deck.draw());
            player.getHand().add(deck.draw());
        }
        // deal two cards to the dealer
        dealer.getHand().add(deck.draw());
        dealer.getHand().add(deck.draw());
        updateVisibleCards();
    }

    /**Updates the cards the computer can see */
    public void updateVisibleCards() {
        computer.setDealerCard(dealer.getHand().get(1));
        //clear the visible cards if there are cards in the list
        if (computer.getVisibleCards().size() > 0) {
            computer.getVisibleCards().clear();
        }
        //add the dealer's visible cards
        computer.getVisibleCards().add(dealer.getHand().get(1));
        for (Player player : players) {
            System.out.println(player);
            //adds the cards from teh player's hand to the computer's visible cards
            computer.getVisibleCards().addAll(player.getHand());
        }
    }

    /**hits for a specific player */
    public void hit(Player player) {
        player.getHand().add(deck.draw());
    }

    // increments the game state
    /** Increments the game state.
     */
    public void incrementGameState() {
        switch (gameState) {
            case BETTING:
                gameState = GameState.DEALING;
                break;
            case DEALING:
                gameState = GameState.PLAYING;
                break;
            case PLAYING:
                gameState = GameState.DEALERPLAYING;
                break;
            case DEALERPLAYING:
                gameState = GameState.RESOLVING;
                break;
            case RESOLVING:
                gameState = GameState.BETTING;
                break;
        }
    }

    // resets the game
    /** Resets the game.
     */
    public void reset() {
        // clears the hands of all players
        for (Player player : players) {
            player.getHand().clear();
        }
        // clears the hand of the dealer
        dealer.getHand().clear();
        // resets the deck
        deck.reset();
        // sets the game state to betting
        gameState = GameState.BETTING;
    }

    // dealer's turn
    /** Makes the dealer play.
     */
    public void dealerTurn() {
        // sets the gameState to dealer playing
        gameState = GameState.DEALERPLAYING;
        // dealer hits until the value of the hand is 17 or greater
        Hand dealerHand = dealer.getHand();
        while (dealerHand.getValue() < 17) {
            Card card = deck.draw();
            //prevents te dealer from busting if he draws an Ace
            if (card.getValue() == 11 && dealerHand.getValue() + 11 > 21) {
                card.setValue(1);
            }
            dealerHand.add(card);
            // prints out the dealer's hand
            System.out.println("Dealer's hand: \n\t" + dealerHand);
        }

    }

    // resolves for all players
    /** Resolves for all players.
     */
    public void resolveAll() {
        // sets the gameState to resolving
        gameState = GameState.RESOLVING;
        // dealer's hand
        Hand dealerHand = dealer.getHand();
        // for each player
        for (Player player : players) {
            resolve(player);
        }
    }

    // resolves for a single player
    private void resolve(Player player) {
        
        Hand playerHand = player.getHand();
        Hand dealerHand = dealer.getHand();
        // if the player busts, the dealer wins
        if (playerHand.getValue() > 21) {
            System.out.println("You busted. Dealer wins.");
            // removes the bet from the player's balance
            player.setBalance(player.getBalance() - player.getBet());
        } else if (playerHand.getValue() == 21) {
            // if the dealer busts, the player wins
            if (dealerHand.getValue() > 21) {
                System.out.println("Dealer busted. You win.");
                // adds the bet to the player's balance
                player.setBalance(player.getBalance() + player.getBet());
            }
            // if the player and dealer both have the same, it's a push
            else if (playerHand.getValue() == 21 && dealerHand.getValue() == 21) {
                System.out.println("Push.");
                // balance remains the same, nobody wins or loses
            }
            // if the player has 21 and the dealer doesn't, the player wins
            else if (playerHand.getValue() == 21 && dealerHand.getValue() != 21) {
                System.out.println("You win.");
                // adds the bet to the player's balance
                player.setBalance(player.getBalance() + player.getBet());
            }
        } // if the player's hand is less than 21, dealer's turn
        else if (playerHand.getValue() < 21) {
            // if the dealer busts, the player wins
            if (dealerHand.getValue() > 21) {
                System.out.println("Dealer busted. You win.");
                // adds the bet to the player's balance
                player.setBalance(player.getBalance() + player.getBet());
            }
            // if the player and dealer both have the same, it's a push
            else if (playerHand.getValue() == dealerHand.getValue()) {
                System.out.println("Push.");
                // balance remains the same, nobody wins or loses
            }
            // if the player has more than the dealer, the player wins
            else if (playerHand.getValue() > dealerHand.getValue()) {
                System.out.println("You win.");
                // adds the bet to the player's balance
                player.setBalance(player.getBalance() + player.getBet());
            }
            // if the dealer has more than the player, the dealer wins
            else if (playerHand.getValue() < dealerHand.getValue()) {
                System.out.println("Dealer wins.");
                // removes the bet from the player's balance
                player.setBalance(player.getBalance() - player.getBet());
            }
        }
        
        // resets the bet
        player.setBet(0);
    }

    // end the game
    /** Ends the game.
     */
    public void endGame() {
        // sets the gameState to betting
        gameState = GameState.GAMEOVER;
        // clears the hands of all players
        for (Player player : players) {
            player.getHand().clear();
        }
        // clears the hand of the dealer
        dealer.getHand().clear();
        // resets the deck
        deck.reset();
    }

    // toString
    /** Returns a string representation of the game.
     * If the game state is betting or game over, it returns just the players
     * If the game state is dealing or playing, it returns the players and the dealer's first card
     * If the game state is dealer playing or resolving, it returns the players and the dealer's hand
     * @return a string representation of the game
     */
    public String toString() {
        String str = "";
        // if the game state is betting or game over, it returns just the players
        if (gameState == GameState.BETTING || gameState == GameState.GAMEOVER) {
            System.out.println("Players:");
            for (Player player : players) {
                str += player + "\n";
            }
        }
        // if the game state is dealing, it returns the players and the dealer's first card
        else if (gameState == GameState.DEALING || gameState == GameState.PLAYING) {
            System.out.println("Players:");
            for (Player player : players) {
                str += player + "\n";
            }
            str += "Dealer's first card: \n\t" + dealer.getHand().get(1);
        }
        // if the game state is dealer playing or resolving, it returns the players and the dealer's hand
        else if (gameState == GameState.DEALERPLAYING || gameState == GameState.RESOLVING) {
            System.out.println("Players:");
            for (Player player : players) {
                str += player + "\n";
            }
            str += "Dealer's hand: \n\t" + dealer.getHand();
        }
        // if the game state is game over, it returns just the players
        else if (gameState == GameState.GAMEOVER) {
            System.out.println("Players:");
            for (Player player : players) {
                str += player + "\n";
            }
        }
        return str;
    }
    /*
     * public User getUser(String username, String password) {
     * for (Player user : players) {
     * if (user.getName().equals(username) && user.getPassword().equals(password)) {
     * return user;
     * }
     * }
     * return null;
     * }
     */
}