package edu.guilford;

// BlackjackController.java
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BlackjackController {

    @FXML
    private Label dealerScoreLabel;

    @FXML
    private Label playerScoreLabel;

    @FXML
    private Label resultLabel;

    @FXML
    private Button hitButton;

    @FXML
    private Button standButton;

    private int dealerScore;
    private int playerScore;
    private ArrayList<Integer> deck;
    private Random random;

    public void initialize() {
        random = new Random();
        deck = new ArrayList<Integer>();
        for (int i = 1; i <= 10; i++) {
            deck.add(i);
        }
        Collections.shuffle(deck);
        dealerScore = 0;
        playerScore = 0;
        updateScores();
    }

    @FXML
    private void onHitButtonClicked() {
        int card = dealCard();
        playerScore += card;
        updateScores();
        if (playerScore > 21) {
            resultLabel.setText("Player busts!");
            hitButton.setDisable(true);
            standButton.setDisable(true);
        }
    }

    @FXML
    private void onStandButtonClicked() {
        while (dealerScore < 17) {
            int card = dealCard();
            dealerScore += card;
            updateScores();
        }
        if (dealerScore > 21 || playerScore > dealerScore) {
            resultLabel.setText("Player wins!");
        } else if (dealerScore > playerScore) {
            resultLabel.setText("Dealer wins!");
        } else {
            resultLabel.setText("It's a tie!");
        }
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }

    private int dealCard() {
        int card = deck.get(0);
        deck.remove(0);
        return card;
    }

    private void updateScores() {
        dealerScoreLabel.setText("Dealer: " + dealerScore);
        playerScoreLabel.setText("Player: " + playerScore);
    }
}