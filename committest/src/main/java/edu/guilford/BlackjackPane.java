package edu.guilford;

import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.nio.file.Path;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import edu.guilford.User;
import java.util.ArrayList;

public class BlackjackPane extends Application {

    // Game properties
    private BlackjackGame game;
    private Deck deck;
    private Hand dealerHand;
    private Hand playerHand;

    // User properties
    private static Path userFile;
    private User currentUser;

    // UI elements
    private Label dealerLabel;
    private Label playerLabel;
    private Label resultLabel;
    private Button hitButton;
    private Button standButton;
    private Label playerValueLabel;
    private Label dealerValueLabel;
    private SimpleStringProperty loggedInUsername = new SimpleStringProperty();
    private HBox postGameButtonBox;
    private Stage gameStage;
    private HBox gameButtonBox;
    private Label balanceLabel;
    private TextField betTextField;
    private Label betLabel;
    private Button placeBetButton;
    private SimpleBooleanProperty betPlaced = new SimpleBooleanProperty(false);

    public static void main(String[] args) {
        // Get the path to the data file
        try {
            userFile = Paths.get(BlackjackPane.class.getResource("/edu/guilford/data.txt").toURI());
            System.out.println(userFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    /** Creates the login dialog, the beginning window and shows it.
     * 
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blackjack");
        primaryStage.setResizable(false);

        // Create the login dialog
        showLoginDialog(primaryStage);
    }

    private boolean createNewUser(String username, double balance) {
        // Read the file and check if the username already exists
        try {
            String data = new String(Files.readAllBytes(userFile));
            String[] usernames = data.split("\n");
            boolean userExists = false;

            for (String user : usernames) {
                String[] userInfo = user.split(",");
                // If the username already exists, return false
                if (userInfo.length == 1 && userInfo[0].equals(username)) {
                    userExists = true;
                    break;
                }
            }

            // If the username doesn't exist, write a new user to the file
            if (!userExists) {
                // Append the new username and balance to the file
                String newUser = username + "," + balance;
                Files.write(userFile, (newUser + "\n").getBytes(), StandardOpenOption.APPEND);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void updateBetAndBalanceLabels() {
        // Update the bet and balance labels
        betLabel.setText("Bet: $" + currentUser.getBet());
        balanceLabel.setText("Balance: $" + String.format("%.2f", currentUser.getBalance()));
    }

    private void showSignUpScreen(Stage primaryStage) {
        // Create the sign up dialog
        DialogPane dialogPane = new DialogPane();
        dialogPane.setHeaderText("Sign Up");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(40);
        gridPane.setPadding(new Insets(20));

        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        dialogPane.setContent(gridPane);
        
        // Sign up and back buttons
        ButtonType signUpButtonType = new ButtonType("Sign Up");
        dialogPane.getButtonTypes().add(signUpButtonType);

        ButtonType backButtonType = new ButtonType("Back");
        dialogPane.getButtonTypes().add(backButtonType);

        dialogPane.setPrefWidth(300);

        primaryStage.setScene(new Scene(dialogPane));
        primaryStage.show();

        // Handle sign up and back button clicks
        dialogPane.lookupButton(signUpButtonType).setOnMouseClicked(event -> {
            String username = usernameField.getText();

            // If the username is not empty, create a new user
            if (!username.isEmpty()) {
                double balance = 0.0; // Default balance
                if (createNewUser(username, balance)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "User created successfully. Return back to the Login screen.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Failed to create user.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username", "Please enter a valid username.");
            }
        });

        // Return to the login screen
        dialogPane.lookupButton(backButtonType).setOnMouseClicked(event -> {
            primaryStage.close();
            showLoginDialog(primaryStage);
        });
    }

    // Show the login dialog
    private void showLoginDialog(Stage primaryStage) {
        DialogPane dialogPane = new DialogPane();
        dialogPane.setHeaderText("Login");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(40);
        gridPane.setPadding(new Insets(20));

        // Username field
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        dialogPane.setContent(gridPane);

        // Login and sign up buttons
        ButtonType loginButtonType = new ButtonType("Login");
        dialogPane.getButtonTypes().add(loginButtonType);

        ButtonType signUpButtonType = new ButtonType("Sign Up");
        dialogPane.getButtonTypes().add(signUpButtonType);

        dialogPane.setPrefWidth(300);

        primaryStage.setScene(new Scene(dialogPane));
        primaryStage.show();

        // Handle login and sign up button clicks
        dialogPane.lookupButton(loginButtonType).setOnMouseClicked(event -> {
            String username = usernameField.getText();
            // if the user exists, login and show the deposit/withdraw screen
            if (authenticateUser(username)) {
                loggedInUsername.set(username);
                primaryStage.close();
                showDepositWithdrawScreen(username);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username", "Please enter a valid username.");
            }
        });

        dialogPane.lookupButton(signUpButtonType).setOnMouseClicked(event -> {
            primaryStage.close();
            showSignUpScreen(primaryStage);
        });
    }

    private boolean authenticateUser(String username) {
        try {
            List<String> lines = Files.readAllLines(userFile);

            // Check if the username exists
            for (String line : lines) {
                String[] userInfo = line.split(",");
                if (userInfo.length >= 1 && userInfo[0].equals(username)) {
                    return true; // Username exists, login successful
                }
            }

            return false; // Username does not exist
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Updates the balance of the given user in the file
    private void updateBalanceInFile(String username, double newBalance) {
        try {
            List<String> lines = Files.readAllLines(userFile);

            // Find the line corresponding to the given username
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] userInfo = line.split(",");
                if (userInfo.length == 2 && userInfo[0].equals(username)) {
                    // Update the balance in the line
                    lines.set(i, username + "," + newBalance);
                    break;
                }
            }

            // Write the updated contents back to the file
            Files.write(userFile, lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            // Update the balance of the current user if applicable
            if (currentUser != null && currentUser.getName().equals(username)) {
                currentUser.setBalance(newBalance);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create a new user from file data
    private User getUserFromDataFile(String username) {
        try {
            List<String> lines = Files.readAllLines(userFile);
            for (String line : lines) {
                String[] userInfo = line.split(",");
                // If the username matches, create a new user object
                if (userInfo.length == 2 && userInfo[0].equals(username)) {
                    String name = userInfo[0];
                    double balance = Double.parseDouble(userInfo[1]);
                    return new User(name, balance);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to show the deposit/withdraw screen
    private void showDepositWithdrawScreen(String username) {
        // Retrieve the user's information from the data.txt file
        this.currentUser = getUserFromDataFile(username);

        Stage depositWithdrawStage = new Stage();
        depositWithdrawStage.setTitle("Deposit/Withdraw");
        depositWithdrawStage.setResizable(false);

        //labels and buttons
        this.balanceLabel = new Label("Current Balance: $" + String.format("%.2f", currentUser.getBalance()));
        TextField depositTextField = new TextField();
        Button depositButton = new Button("Deposit");
        TextField withdrawTextField = new TextField();
        Button withdrawButton = new Button("Withdraw");
        Button playButton = new Button("Play Game");

        GridPane depositWithdrawPane = new GridPane();
        depositWithdrawPane.setHgap(20);
        depositWithdrawPane.setVgap(20);
        depositWithdrawPane.setPadding(new Insets(100));
        depositWithdrawPane.add(balanceLabel, 0, 0, 2, 1);
        depositWithdrawPane.add(new Label("Deposit Amount:"), 0, 1);
        depositWithdrawPane.add(depositTextField, 1, 1);
        depositWithdrawPane.add(depositButton, 2, 1);
        depositWithdrawPane.add(new Label("Withdraw Amount:"), 0, 2);
        depositWithdrawPane.add(withdrawTextField, 1, 2);
        depositWithdrawPane.add(withdrawButton, 2, 2);
        depositWithdrawPane.add(playButton, 0, 3);

        Scene depositWithdrawScene = new Scene(depositWithdrawPane);
        depositWithdrawStage.setScene(depositWithdrawScene);
        depositWithdrawStage.show();

        // Handle deposit button click
        depositButton.setOnAction(event -> {
            String depositAmountStr = depositTextField.getText();
            if (!depositAmountStr.isEmpty()) {
                try {
                    // Parse the deposit amount
                    double depositAmount = Double.parseDouble(depositAmountStr);
                    double maxDepositAmount = 10000.0; // Set the maximum deposit amount here
                    if (depositAmount > maxDepositAmount) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Deposit Amount",
                                "The maximum deposit amount is $" + maxDepositAmount);
                    } else if (depositAmount < 0) {
                        // Check if the deposit amount is negative
                        showAlert(Alert.AlertType.ERROR, "Invalid Deposit Amount",
                                "The minimum deposit amount is $0");
                    } else {
                        //round the deposit amount down to the nearest 2 decimal places
                        depositAmount = Math.floor(depositAmount * 100) / 100;
                        //update the balance of the current user and the label
                        double newBalance = currentUser.getBalance() + depositAmount;
                        currentUser.setBalance(newBalance);
                        balanceLabel.setText("Current Balance: $" + newBalance);
                        showAlert(Alert.AlertType.INFORMATION, "Deposit Successful",
                                "Deposit of $" + depositAmount + " is successful.");
                        updateBalanceInFile(username, newBalance);
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Deposit Amount",
                            "Please enter a valid deposit amount.");
                }
            }
        });

        // Handle withdraw button click
        withdrawButton.setOnAction(event -> {
            String withdrawAmountStr = withdrawTextField.getText();
            if (!withdrawAmountStr.isEmpty()) {
                try {
                    // Parse the withdraw amount
                    double withdrawAmount = Double.parseDouble(withdrawAmountStr);
                    if (withdrawAmount <= currentUser.getBalance()) {
                        //round the withdraw amount down to the nearest 2 decimal places
                        withdrawAmount = Math.floor(withdrawAmount * 100) / 100;
                        //update the balance of the current user and the label
                        double newBalance = currentUser.getBalance() - withdrawAmount;
                        currentUser.setBalance(newBalance);
                        balanceLabel.setText("Current Balance: $" + newBalance);
                        showAlert(Alert.AlertType.INFORMATION, "Withdraw Successful",
                                "Withdraw of $" + withdrawAmount + " is successful.");
                        updateBalanceInFile(username, newBalance);
                    } else if (withdrawAmount < 0){
                        showAlert(Alert.AlertType.ERROR, "Invalid Withdraw Amount",
                                "The minimum withdraw amount is $0");
                    }
                    else {
                        showAlert(Alert.AlertType.ERROR, "Insufficient Balance",
                                "Insufficient balance to make the withdrawal.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Withdraw Amount",
                            "Please enter a valid withdraw amount.");
                }
            }
        });

        playButton.setOnAction(event -> {
            depositWithdrawStage.close();
            launchGameWindow();
        });
    }

    // Shows an alert with the specified alert type, title and message
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // opens the game window
    private void launchGameWindow() {
        gameStage = new Stage();
        gameStage.setTitle("Blackjack");
        gameStage.setResizable(false);

        initializeGame();

        BorderPane root = createGUI();

        Scene scene = new Scene(root, 1400, 800);
        gameStage.setScene(scene);
        gameStage.show();

        // Disable the hit and stand buttons initially
        hitButton.setDisable(true);
        standButton.setDisable(true);

        hitButton.setOnAction(e -> handleHit());
        standButton.setOnAction(e -> handleStand());
    }

    // Control whether the hit and stand buttons are enabled or disabled
    private void enableButtons() {
        hitButton.setDisable(false);
        standButton.setDisable(false);
    }

    private void disableButtons() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }

    // creates an ImageView object for the specified card
    private ImageView getCardImageView(Card card) {
        File cardImageFile = new File(card.getImagePath().toString());
        ImageView cardImageView = new ImageView(cardImageFile.toURI().toString());
        cardImageView.setFitWidth(100);
        cardImageView.setPreserveRatio(true);
        return cardImageView;
    }

    // creates a BlackjackGame object and initializes the deck, dealerHand and playerHand
    private void initializeGame() {
        this.game = new BlackjackGame(this.currentUser);
        this.game.deal();
        deck = this.game.getDeck();
        dealerHand = this.game.getDealer().getHand();
        playerHand = this.game.getPlayers().get(1).getHand();
    }

    // handles GUI on game reset
    private void resetGame() {
        game.reset();
        this.game.deal();
        deck = this.game.getDeck();
        dealerHand = this.game.getDealer().getHand();
        playerHand = this.game.getPlayers().get(1).getHand();

        // Reset the GUI elements
        dealerLabel.setText("Dealer:");
        playerLabel.setText("Player: ");
        resultLabel.setText("");
        // Update value and balance labels
        int playerScore = playerHand.getValue();
        playerValueLabel.setText("Player Value: " + playerScore);
        dealerValueLabel.setText("Dealer Value: ");
        balanceLabel.setText("Current Balance: $" + currentUser.getBalance());
        betPlaced.set(false);

        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);
        FlowPane playerCardPane = (FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1);

        dealerCardPane.getChildren().clear();
        playerCardPane.getChildren().clear();
        resetBetInput();

        // Display the dealer's card
        ImageView dealerCardImage = getCardImageView(game.getComputer().getDealerCard());
        dealerCardPane.getChildren().add(dealerCardImage);

        // Display the player's cards
        for (Card card : playerHand) {
            ImageView cardImageView = getCardImageView(card);
            playerCardPane.getChildren().add(cardImageView);
        }
        
        // Enable the buttons
        hitButton.setDisable(true);
        standButton.setDisable(true);
        postGameButtonBox.setVisible(false);

        // hides the cards until the bet is made
        hideCards();
    }

    // control whether the player can bet or not
    private void resetBetInput() {
        betTextField.clear();
        betTextField.setDisable(false);
        placeBetButton.setDisable(false);
    }

    private void disableBetInput() {
        betTextField.setDisable(true);
        placeBetButton.setDisable(true);
    }

    // shows the cards in the GUI
    private void showCards() {
        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);
        FlowPane playerCardPane = (FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1);

        //iterate through the children in the pane and setVisible to true
        for (Node node : dealerCardPane.getChildren()) {
            node.setVisible(true);
            node.managedProperty().bind(node.visibleProperty());
        }

        for (Node node : playerCardPane.getChildren()) {
            node.setVisible(true);
            node.managedProperty().bind(node.visibleProperty());
        }

        playerValueLabel.setText("Player: " + playerHand.getValue());
    }

    // hides the cards in the GUI and from the better
    private void hideCards(){
        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);
        FlowPane playerCardPane = (FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1);

        //iterate through the children in the pane and setVisible to true
        for (Node node : dealerCardPane.getChildren()) {
            node.setVisible(false);
        }

        for (Node node : playerCardPane.getChildren()) {
            node.setVisible(false);
        }

        playerValueLabel.setText("Player: ");
    }

    // creates a GUI for the game
    private BorderPane createGUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Creates the dealer's hand
        HBox dealerBox = createHandBox("Dealer", dealerHand, true);
        dealerLabel = (Label) dealerBox.getChildren().get(0);
        dealerValueLabel = new Label("Dealer Value:");

        // Creates the player's hand
        HBox playerBox = createHandBox("Player", playerHand, false);
        playerLabel = (Label) playerBox.getChildren().get(0);
        int playerScore = playerHand.getValue();
        playerValueLabel = new Label("Player Value: " + playerScore);

        // Creates a betting box for the player
        this.betTextField = new TextField();
        this.placeBetButton = new Button("Place Bet");
        Label betLabel = new Label();
        VBox betBox = new VBox(10);
        betBox.setAlignment(Pos.CENTER);
        betBox.getChildren().addAll(betTextField, placeBetButton);
        playerBox.getChildren().add(betBox);

        // creates a box to show the score, values, balance, and result
        VBox scoreBox = new VBox(20);
        scoreBox.setAlignment(Pos.CENTER);

        // creates labels for the score, and result
        Label dealerScoreLabel = new Label("Dealer: ");
        dealerScoreLabel.setFont(Font.font(20));
        dealerScoreLabel.setTextFill(Color.BLACK);

        Label playerScoreLabel = new Label("Player: ");
        playerScoreLabel.setFont(Font.font(20));
        playerScoreLabel.setTextFill(Color.BLACK);

        resultLabel = new Label();
        resultLabel.setFont(Font.font(16));
        resultLabel.setTextFill(Color.BLACK);

        scoreBox.getChildren().addAll(dealerScoreLabel, dealerValueLabel, playerScoreLabel, playerValueLabel,
                resultLabel, balanceLabel);
        scoreBox.setPrefWidth(250);

        // creates a box for the buttons at the bottom of the screen
        HBox buttonBox = new HBox(20);
        buttonBox.setPrefHeight(100);
        buttonBox.setAlignment(Pos.CENTER);

        // creates the hit and stand buttons and adds them to a box
        hitButton = new Button("Hit");
        hitButton.setPrefSize(75, 40);
        hitButton.setOnAction(e -> handleHit());

        standButton = new Button("Stand");
        standButton.setPrefSize(75, 40);
        standButton.setOnAction(e -> handleStand());

        gameButtonBox = new HBox(20);
        gameButtonBox.setAlignment(Pos.CENTER);
        gameButtonBox.getChildren().addAll(hitButton, standButton);

        // creates a box with the post game buttons (return, play again)
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setPrefSize(75, 40);

        Button returnButton = new Button("Return");
        returnButton.setPrefSize(75, 40);

        postGameButtonBox = new HBox(20); // Assign the created HBox to the correct variable
        postGameButtonBox.setAlignment(Pos.CENTER);
        postGameButtonBox.getChildren().addAll(playAgainButton, returnButton);
        postGameButtonBox.setVisible(false);

        // adds the button boxes to the main button box
        buttonBox.getChildren().addAll(gameButtonBox, postGameButtonBox);

        // adds the boxes to the root, and aligns them
        root.setTop(dealerBox);
        root.setCenter(playerBox);
        root.setBottom(buttonBox);
        root.setRight(scoreBox);

        // event handlers for the buttons
        playAgainButton.setOnAction(e -> {
            resetGame();
            gameButtonBox.getChildren().clear();
            gameButtonBox.getChildren().addAll(hitButton, standButton);
        });

        returnButton.setOnAction(e -> {
            gameStage.close();
            // Show the deposit/withdraw screen
            showDepositWithdrawScreen(loggedInUsername.get());
        });

        placeBetButton.setOnAction(event -> {
            String betAmountStr = betTextField.getText();
            if (!betAmountStr.isEmpty()) {
                try {
                    // Parse the bet amount
                    double betAmount = Double.parseDouble(betAmountStr);
                    if (betAmount <= currentUser.getBalance() && betAmount > 0) {
                        //round down the bet to the nearest 2 decimal places
                        betAmount = Math.floor(betAmount * 100) / 100;
                        // updates the bet of the user and the label
                        currentUser.setBet(betAmount);
                        betLabel.setText("Bet Placed: $" + betAmount);
                        showAlert(Alert.AlertType.INFORMATION, "Bet Placed",
                                "Bet of $" + betAmount + " is placed.");
                        disableBetInput(); // Disable the bet input after placing the bet
                        // Enable the hit and stand buttons
                        hitButton.setDisable(false);
                        standButton.setDisable(false);
                        placeBetButton.setDisable(true);
                        showCards();
                    } else if (betAmount <= 0){
                        showAlert(Alert.AlertType.ERROR, "Invalid Input",
                                "Please enter a positive number.");
                    } else if(betAmount > currentUser.getBalance()){
                        showAlert(Alert.AlertType.ERROR, "Insufficient Balance",
                                "Insufficient balance to place the bet.");
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input",
                            "Please enter a valid number.");
                }

            }
        });
        
        hideCards();

        return root;
    }

    // Creates a box for a hand
    private HBox createHandBox(String name, Hand hand, boolean isDealer) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);

        // Creates a label for the hand
        Label label = new Label(name + ": ");
        label.setFont(Font.font(16));
        label.setTextFill(Color.BLACK);
        label.setPrefWidth(100);

        // Creates a pane for the cards
        FlowPane cardPane = new FlowPane(10, 10);
        cardPane.setPadding(new Insets(10));
        cardPane.setAlignment(Pos.CENTER_LEFT);
        cardPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        // If the hand is the dealer's hand, only show the first card
        if (isDealer) {
            if (!hand.isEmpty()) {
                Card firstCard = game.getComputer().getDealerCard();
                ImageView firstCardView = getCardImageView(firstCard);
                cardPane.getChildren().add(firstCardView);
            }
        } 
        // If the hand is the player's hand, show all the cards
        else {
            for (Card card : hand) {
                ImageView cardView = getCardImageView(card);
                cardPane.getChildren().add(cardView);
            }
        }

        box.getChildren().addAll(label, cardPane);

        return box;
    }

    // Handles the hit button
    private void handleHit() {
        Card card = deck.deal();
        
        // Saves the player from busting if the hand has an ace
        if (playerHand.getValue() + card.getValue() > 21) {
            // if there is an ace in the dealer's hand set it to one
            for (Card c : playerHand) {
                if (c.getValue() == 11) {
                    c.setValue(1);
                    System.out.println("Dealer's hand: \n\t" + dealerHand);
                }
            }
            // if the card is an ace set it to one
            if (card.getValue() == 11) {
                card.setValue(1);
            }
        }
        playerHand.add(card);

        // adds the card to the player's hand
        ImageView cardImageView = getCardImageView(card);
        ((FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1)).getChildren().add(cardImageView);

        // updates the player's score
        int playerScore = playerHand.getValue();
        playerValueLabel.setText("Player Value: " + playerScore);

        // checks if the player has busted or has blackjack
        if (playerScore >= 21) {
            handleStand();
        }
    }

    // Handles the stand button
    private void handleStand() {
        disableButtons();

        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);

        // dealer plays out their turn (draws cards until they have 17 or more)
        game.dealerTurn();

        // adds the cards to the dealer's hand
        for (Card card : dealerHand) {
            // if the card is the first card (already shown), don't add it
            if (card == game.getComputer().getDealerCard()) {
                continue;
            }
            ImageView cardView = getCardImageView(card);
            dealerCardPane.getChildren().add(cardView);
        }

        this.game.getComputer().decideBet();

        // updates the dealer's and player's score
        int playerScore = playerHand.getValue();
        int dealerScore = dealerHand.getValue();

        // updates the labels for the scores
        dealerValueLabel.setText("Dealer Value: " + dealerScore);
        playerValueLabel.setText("Player Value: " + playerScore);

        // shows a message depending on if the player won, lost, or tied
        resultLabel.setText(this.game.resolveAll().get(1));

        // updates the balance label and the balance in the file
        balanceLabel.setText("Balance: $" + String.format("%.2f", currentUser.getBalance()));
        postGameButtonBox.setVisible(true);
        updateBalanceInFile(loggedInUsername.get(), currentUser.getBalance());
    }

}