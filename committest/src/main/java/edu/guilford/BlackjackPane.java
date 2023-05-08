package edu.guilford;

import javafx.scene.Node;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BlackjackPane extends Application {

    private Deck deck;
    private Hand dealerHand;
    private Hand playerHand;
    private Label dealerLabel;
    private Label playerLabel;
    private Label resultLabel;
    private Button hitButton;
    private Button standButton;
    private Label playerValueLabel;
    private Label dealerValueLabel;
    private BlackjackGame game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blackjack");
        primaryStage.setResizable(false);

        User user = new User("test", "test");
        this.game = new BlackjackGame(user);
        this.game.deal();
        deck = this.game.getDeck();
        dealerHand = this.game.getDealer().getHand();
        playerHand = this.game.getPlayers().get(1).getHand();


        BorderPane root = createGUI();

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeGame() {
        /*deck = new Deck();
        dealerHand = new Hand();
        playerHand = new Hand();

        deck.shuffle();

        // Deal initial cards
        dealerHand.addCard(deck.draw());
        

        playerHand.addCard(deck.drawCard());*/
        
    }

    private BorderPane createGUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        HBox dealerBox = createHandBox("Dealer", dealerHand, true);
        dealerLabel = (Label) dealerBox.getChildren().get(0);

        HBox playerBox = createHandBox("Player", playerHand, false);
        playerLabel = (Label) playerBox.getChildren().get(0);

        VBox scoreBox = new VBox(20);
        scoreBox.setAlignment(Pos.CENTER);

        Label dealerScoreLabel = new Label("Dealer: ");
        dealerScoreLabel.setFont(Font.font(20));
        dealerScoreLabel.setTextFill(Color.BLACK);

        Label playerScoreLabel = new Label("Player: ");
        playerScoreLabel.setFont(Font.font(20));
        playerScoreLabel.setTextFill(Color.BLACK);

        resultLabel = new Label(); // Result label for displaying win/tie message
        resultLabel.setFont(Font.font(16));
        resultLabel.setTextFill(Color.BLACK);

        playerValueLabel = new Label();
        dealerValueLabel = new Label();

        scoreBox.getChildren().addAll(dealerScoreLabel, dealerValueLabel, playerScoreLabel, playerValueLabel,
                resultLabel);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        hitButton = new Button("Hit");
        hitButton.setOnAction(e -> handleHit());

        standButton = new Button("Stand");
        standButton.setOnAction(e -> handleStand());

        buttonBox.getChildren().addAll(hitButton, standButton);

        root.setTop(dealerBox);
        root.setCenter(playerBox);
        root.setBottom(buttonBox);
        root.setRight(scoreBox);

        return root;
    }

    private HBox createHandBox(String name, Hand hand, boolean isDealer) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);
    
        Label label = new Label(name + ": ");
        label.setFont(Font.font(16));
        label.setTextFill(Color.BLACK);
    
        FlowPane cardPane = new FlowPane(10, 10);
        cardPane.setPadding(new Insets(10));
        cardPane.setAlignment(Pos.CENTER_LEFT);
        cardPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
    
        if (isDealer) {
            if (!hand.isEmpty()) {
                // Add the first card without the "Hidden" label
                Card firstCard = hand.get(0);
                Label firstCardLabel = new Label(firstCard.toString());
                firstCardLabel.setFont(Font.font(16));
                firstCardLabel.setTextFill(Color.BLACK);
                cardPane.getChildren().add(firstCardLabel);
            }
            } else {
                for (Card card : hand) {
                    Label cardLabel = new Label(card.toString());
                    cardLabel.setFont(Font.font(16));
                    cardLabel.setTextFill(Color.BLACK);
                    cardPane.getChildren().add(cardLabel);
                }
            }
        
            box.getChildren().addAll(label, cardPane);
        
            return box;
        }
    private void handleHit() {
        Card card = deck.draw();
        playerHand.add(card);
    
        Label cardLabel = new Label(card.toString());
        cardLabel.setFont(Font.font(16));
        cardLabel.setTextFill(Color.BLACK);
    
        ((FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1)).getChildren().add(cardLabel);
    
        int playerScore = playerHand.getValue();
        playerValueLabel.setText("Player Value: " + playerScore);
    
        if (playerScore > 21) {
            resultLabel.setText("Player busts! Dealer wins!");
            disableButtons();
        } else if (playerScore == 21) {
            handleStand();
        }
    }

    private void handleStand() {
        disableButtons();
    
        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);
        // Remove the "Hidden" label
    
        boolean isFirstCard = true;
    
        while (dealerHand.getValue() < 17) {
            Card card = deck.draw();
            dealerHand.add(card);
    
            Label cardLabel = new Label(card.toString());
            cardLabel.setFont(Font.font(16));
            cardLabel.setTextFill(Color.BLACK);
            
    
            dealerCardPane.getChildren().add(cardLabel);
        }
    
        int playerScore = playerHand.getValue();
        int dealerScore = dealerHand.getValue();
    
        dealerValueLabel.setText("Dealer Value: " + dealerScore);
    
        this.game.resolveAll();
    }
    private void disableButtons() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }
}