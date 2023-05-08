package edu.guilford;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        
        Scanner scan = new Scanner(System.in);
        //prompt for a user
        System.out.println("Welcome to Blackjack! Please enter your username: ");
        String username = scan.nextLine();
        //prompt for a password
        System.out.println("Please enter your password: ");
        String password = scan.nextLine();
        //create a user
        User user = new User(username, password);
        //add user to list of users
        ArrayList<User> users = new ArrayList<User>();
        users.add(user);
        //initialize the game
        BlackjackGame game = new BlackjackGame(users);
        System.out.println(game);
        //prompt each user for a bet
        for (User u : users){
            System.out.println(u.getName() + ", please enter your bet: ");
            double bet = scan.nextDouble();
            u.setBet(bet);
        }
        
        //deal the cards
        game.deal();
        System.out.println(game);
        //ask each user if they want to hit or stand
        String hitOrStand = "";
        for (User u : users){
            Hand playerHand = u.getHand();
            System.out.println(u.getName() + "'s Hand: \n\t" + playerHand);
            while (playerHand.getValue() < 21 && ! hitOrStand.equals("stand")){
                System.out.println("Would you like to hit or stand?");
                hitOrStand = scan.next();
                if (hitOrStand.equals("hit")){
                    Card playerCard = game.getDeck().draw();
                    if(playerCard.getRank().equals("Ace")) {
                        System.out.println("You have an Ace. Do you want to make it a 1? (Y/N)");
                        String aceValue = scan.next();
                        if (aceValue.equals("Y")){
                            playerCard.setValue(1);
                        }
                    }
                    playerHand.add(playerCard);
                    System.out.println("Player's Hand: \n\t" + playerHand);

                } else if (hitOrStand.equals("stand")){
                    break;
                }
            }
        }
        //dealer's turn
        game.dealerTurn();
        game.resolveAll();
        System.out.println(game);


        /*//create a deck
        Deck deck = new Deck();
        //deal a card to a player
        //check if the card is an ace and ask the user to choose a value
        Hand playerHand = new Hand();
        //Card playerCard = deck.remove(deck.indexOf(new Card("Hearts", "Ace")));
        Card playerCard = deck.draw();
        if(playerCard.getRank().equals("Ace")) {
            System.out.println("You have an Ace. Would you like it to be a 1 or 11?");
            int aceValue = scan.nextInt();
            playerCard.setValue(aceValue);
        }
        playerHand.add(playerCard);
        //deal a card to the dealer
        Hand dealerHand = new Hand(deck.draw());
        //deal a second card to the player
        playerHand.add(deck.draw());
        //deal a second card to the dealer
        dealerHand.add(deck.draw());
        //print the dealer's hand
        System.out.println("Dealer's Hand: \n\t" + dealerHand);
        //print the player's hand
        System.out.println("Player's Hand: \n\t" + playerHand);
        //ask the player if they want to hit or stand
        String hitOrStay = "";
        while (playerHand.getValue() < 21 && ! hitOrStay.equals("stay")){
            System.out.println("Would you like to hit or stay?");
            hitOrStay = scan.next();
            if (hitOrStay.equals("hit")){
                playerCard = deck.draw();
                if(playerCard.getRank().equals("Ace")) {
                    System.out.println("You have an Ace. Would you like it to be a 1 or 11?");
                    int aceValue = scan.nextInt();
                    playerCard.setValue(aceValue);
                }
                playerHand.add(playerCard);
                System.out.println("Player's Hand: \n\t" + playerHand);

            } else if (hitOrStay.equals("stand")){
                break;
            }
        }
        //if the player busts, the dealer wins
        if(playerHand.getValue() > 21){
            System.out.println("You busted. Dealer wins.");
        }
        else if (playerHand.getValue() == 21){
            System.out.println("You have 21. Dealer's turn.");
            //if the player stands, the dealer hits until they have 17 or more
            while (dealerHand.getValue() < 17){
                dealerHand.add(deck.draw());
                System.out.println("Dealer's Hand: \n\t" + dealerHand);
            }
            //if the dealer busts, the player wins
            if(dealerHand.getValue() > 21){
                System.out.println("Dealer busted. You win.");
            }
            //if the player and dealer both have the same, it's a push
            else if(playerHand.getValue() == 21 && dealerHand.getValue() == 21){
                System.out.println("Push.");
            }
            //if the player has 21 and the dealer doesn't, the player wins
            else if(playerHand.getValue() == 21 && dealerHand.getValue() != 21){
                System.out.println("You win.");
            }
        }// if the player's hand is less than 21, dealer's turn
        else if (playerHand.getValue() < 21) {
            System.out.println("You have " + playerHand.getValue() + ". Dealer's turn.");
            //if the player stands, the dealer hits until they have 17 or more
            while (dealerHand.getValue() < 17){
                dealerHand.add(deck.draw());
                System.out.println("Dealer's Hand: \n\t" + dealerHand);
            }
            //if the dealer busts, the player wins
            if(dealerHand.getValue() > 21){
                System.out.println("Dealer busted. You win.");
            }
            //if the player and dealer both have the same, it's a push
            else if(playerHand.getValue() == dealerHand.getValue()){
                System.out.println("Push.");
            }
            //if the player has more than the dealer, the player wins
            else if(playerHand.getValue() > dealerHand.getValue()){
                System.out.println("You win.");
            }
            //if the dealer has more than the player, the dealer wins
            else if(playerHand.getValue() < dealerHand.getValue()){
                System.out.println("Dealer wins.");
            }
        }*/

        //launch();
    }

}