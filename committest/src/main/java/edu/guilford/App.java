package edu.guilford;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

//exceptions
//can do negative bets
//deposit amount 
//draw amount 


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
        
        //create a user
        User user = new User(username);
        //add user to list of users
        ArrayList<User> users = new ArrayList<User>();
        users.add(user);
        //initialize the game
        BlackjackGame game = new BlackjackGame(user);
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


        //launch();
    }

}