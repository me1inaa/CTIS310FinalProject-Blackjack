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

    private BlackjackGame game;
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
    private SimpleStringProperty loggedInUsername = new SimpleStringProperty();
    private HBox postGameButtonBox;
    private Stage gameStage;
    private HBox gameButtonBox;
    private static Path userFile;
    private User currentUser;
    private Label balanceLabel;
    private TextField betTextField;
    private Label betLabel;
    private Button placeBetButton;
    private SimpleBooleanProperty betPlaced = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty gameInProgress = new SimpleBooleanProperty(false);

    public static void main(String[] args) {
        try {
            userFile = Paths.get(BlackjackPane.class.getResource("/edu/guilford/data.txt").toURI());
            System.out.println(userFile);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Blackjack");
        primaryStage.setResizable(false);

        showLoginDialog(primaryStage);
    }

    private boolean createNewUser(String username, double balance) {
        try {
            String data = new String(Files.readAllBytes(userFile));
            String[] usernames = data.split("\n");
            boolean userExists = false;

            for (String user : usernames) {
                String[] userInfo = user.split(",");
                if (userInfo.length == 1 && userInfo[0].equals(username)) {
                    userExists = true;
                    break;
                }
            }

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
        betLabel.setText("Bet: $" + currentUser.getBet());
        balanceLabel.setText("Balance: $" + currentUser.getBalance());
    }

    private void showSignUpScreen(Stage primaryStage) {
        DialogPane dialogPane = new DialogPane();
        dialogPane.setHeaderText("Sign Up");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(40);
        gridPane.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        dialogPane.setContent(gridPane);

        ButtonType signUpButtonType = new ButtonType("Sign Up");
        dialogPane.getButtonTypes().add(signUpButtonType);

        ButtonType backButtonType = new ButtonType("Back");
        dialogPane.getButtonTypes().add(backButtonType);

        dialogPane.setPrefWidth(300);

        primaryStage.setScene(new Scene(dialogPane));
        primaryStage.show();

        dialogPane.lookupButton(signUpButtonType).setOnMouseClicked(event -> {
            String username = usernameField.getText();

            if (!username.isEmpty()) {
                double balance = 0.0; // Default balance
                if (createNewUser(username, balance)) {
                    showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "User created successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Failed to create user.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username", "Please enter a valid username.");
            }
        });

        dialogPane.lookupButton(backButtonType).setOnMouseClicked(event -> {
            primaryStage.close();
            showLoginDialog(primaryStage);
        });
    }

    private void showLoginDialog(Stage primaryStage) {
        DialogPane dialogPane = new DialogPane();
        dialogPane.setHeaderText("Login");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(40);
        gridPane.setVgap(40);
        gridPane.setPadding(new Insets(20));

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);

        dialogPane.setContent(gridPane);

        ButtonType loginButtonType = new ButtonType("Login");
        dialogPane.getButtonTypes().add(loginButtonType);

        ButtonType signUpButtonType = new ButtonType("Sign Up");
        dialogPane.getButtonTypes().add(signUpButtonType);

        dialogPane.setPrefWidth(300);

        primaryStage.setScene(new Scene(dialogPane));
        primaryStage.show();

        dialogPane.lookupButton(loginButtonType).setOnMouseClicked(event -> {
            String username = usernameField.getText();

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

    private User getUserFromDataFile(String username) {
        try {
            List<String> lines = Files.readAllLines(userFile);
            for (String line : lines) {
                String[] userInfo = line.split(",");
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

        this.balanceLabel = new Label("Current Balance: $" + currentUser.getBalance());
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

        depositButton.setOnAction(event -> {
            String depositAmountStr = depositTextField.getText();
            if (!depositAmountStr.isEmpty()) {
                try {
                    double depositAmount = Double.parseDouble(depositAmountStr);
                    double maxDepositAmount = 10000.0; // Set the maximum deposit amount here
                    if (depositAmount > maxDepositAmount) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Deposit Amount",
                                "The maximum deposit amount is $" + maxDepositAmount);
                    } else if (depositAmount < 0) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Deposit Amount",
                                "The minimum deposit amount is $0");
                    } else {
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

        withdrawButton.setOnAction(event -> {
            String withdrawAmountStr = withdrawTextField.getText();
            if (!withdrawAmountStr.isEmpty()) {
                try {
                    double withdrawAmount = Double.parseDouble(withdrawAmountStr);
                    if (withdrawAmount <= currentUser.getBalance()) {
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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

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

        /*
         * placeBetButton.setOnAction(event -> {
         * String betAmountStr = betTextField.getText();
         * if (!betAmountStr.isEmpty()) {
         * double betAmount = Double.parseDouble(betAmountStr);
         * currentUser.setBet(betAmount);
         * showAlert(Alert.AlertType.INFORMATION, "Bet Placed", "Bet of $" + betAmount +
         * " placed.");
         * disableBetInput(); // Disable the bet input after placing the bet
         * hitButton.setDisable(false);
         * standButton.setDisable(false);
         * placeBetButton.setDisable(true);
         * } else {
         * showAlert(Alert.AlertType.ERROR, "Invalid Bet Amount",
         * "Please enter a valid bet amount.");
         * }
         * });
         */

        hitButton.setOnAction(e -> handleHit());
        standButton.setOnAction(e -> handleStand());
    }

    private void enableButtons() {
        hitButton.setDisable(false);
        standButton.setDisable(false);
    }

    private ImageView getCardImageView(Card card) {
        File cardImageFile = new File(card.getImagePath().toString());
        ImageView cardImageView = new ImageView(cardImageFile.toURI().toString());
        cardImageView.setFitWidth(100);
        cardImageView.setPreserveRatio(true);
        return cardImageView;
    }

    private void initializeGame() {
        this.game = new BlackjackGame(this.currentUser);
        this.game.deal();
        deck = this.game.getDeck();
        dealerHand = this.game.getDealer().getHand();
        playerHand = this.game.getPlayers().get(1).getHand();
    }

    private void resetGame() {
        game.reset();
        this.game.deal();
        deck = this.game.getDeck();
        dealerHand = this.game.getDealer().getHand();
        playerHand = this.game.getPlayers().get(1).getHand();

        // Reset the GUI elements
        dealerLabel.setText("Dealer: ");
        playerLabel.setText("Player: ");
        resultLabel.setText("");
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

        for (Card card : playerHand) {
            ImageView cardImageView = getCardImageView(card);
            playerCardPane.getChildren().add(cardImageView);
        }
        // Enable the buttons
        hitButton.setDisable(false);
        standButton.setDisable(false);
        postGameButtonBox.setVisible(false);
    }

    private void resetBetInput() {
        betTextField.clear();
        betTextField.setDisable(false);
        placeBetButton.setDisable(false);
    }

    private void disableBetInput() {
        betTextField.setDisable(true);
        placeBetButton.setDisable(true);
    }

    private BorderPane createGUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        HBox dealerBox = createHandBox("Dealer", dealerHand, true);
        dealerLabel = (Label) dealerBox.getChildren().get(0);
        dealerValueLabel = new Label("Dealer Value:");

        HBox playerBox = createHandBox("Player", playerHand, false);
        playerLabel = (Label) playerBox.getChildren().get(0);
        int playerScore = playerHand.getValue();
        playerValueLabel = new Label("Player Value: " + playerScore);

        VBox scoreBox = new VBox(20);
        scoreBox.setAlignment(Pos.CENTER);

        TextField betTextField = new TextField();
        placeBetButton = new Button("Place Bet");

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

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        hitButton = new Button("Hit");
        hitButton.setOnAction(e -> handleHit());

        standButton = new Button("Stand");
        standButton.setOnAction(e -> handleStand());

        gameButtonBox = new HBox(20);
        gameButtonBox.setAlignment(Pos.CENTER);
        gameButtonBox.getChildren().addAll(hitButton, standButton);

        this.betTextField = new TextField();
        this.placeBetButton = new Button("Place Bet");
        Label betLabel = new Label();
        VBox betBox = new VBox(10);
        betBox.setAlignment(Pos.CENTER);
        betBox.getChildren().addAll(betTextField, placeBetButton);
        playerBox.getChildren().add(betBox);
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setOnAction(e -> {
            resetGame();
            gameButtonBox.getChildren().clear();
            gameButtonBox.getChildren().addAll(hitButton, standButton);
        });

        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            gameStage.close();
            showDepositWithdrawScreen(loggedInUsername.get());
        });

        placeBetButton.setOnAction(event -> {
            String betAmountStr = betTextField.getText();
            if (!betAmountStr.isEmpty()) {
                try {
                    double betAmount = Double.parseDouble(betAmountStr);
                    if (betAmount <= currentUser.getBalance() && betAmount > 0) {
                        currentUser.setBet(betAmount);
                        betLabel.setText("Bet Placed: $" + betAmount);
                        showAlert(Alert.AlertType.INFORMATION, "Bet Placed",
                                "Bet of $" + betAmount + " is placed.");
                        disableBetInput(); // Disable the bet input after placing the bet
                        hitButton.setDisable(false);
                        standButton.setDisable(false);
                        placeBetButton.setDisable(true);
                    } else if (betAmount < 0){
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

        postGameButtonBox = new HBox(20); // Assign the created HBox to the correct variable
        postGameButtonBox.setAlignment(Pos.CENTER);
        postGameButtonBox.getChildren().addAll(playAgainButton, returnButton);
        postGameButtonBox.setVisible(false);

        buttonBox.getChildren().addAll(gameButtonBox, postGameButtonBox);

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
                Card firstCard = game.getComputer().getDealerCard();
                ImageView firstCardView = getCardImageView(firstCard);
                cardPane.getChildren().add(firstCardView);
            }
        } else {
            for (Card card : hand) {
                // Label cardLabel = new Label(card.toString());
                // cardLabel.setFont(Font.font(16));
                // cardLabel.setTextFill(Color.BLACK);
                ImageView cardView = getCardImageView(card);
                cardPane.getChildren().add(cardView);

            }
        }

        box.getChildren().addAll(label, cardPane);

        return box;
    }

    private void handleHit() {
        Card card = deck.deal();
        // reduce the card if it is an ace and if it causes the player to go over 21
        if (card.getRank() == "Ace" && playerHand.getValue() + 11 > 21) {
            card.setValue(1);
        }
        playerHand.addCard(card);

        // Label cardLabel = new Label(card.toString());
        // cardLabel.setFont(Font.font(16));
        // cardLabel.setTextFill(Color.BLACK);

        ImageView cardImageView = getCardImageView(card);
        ((FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1)).getChildren().add(cardImageView);

        int playerScore = playerHand.getValue();
        playerValueLabel.setText("Player Value: " + playerScore);
        // dealerValueLabel.setText("Dealer Value: " + dealerHand.getValue());

        if (playerScore > 21) {
            resultLabel.setText("Player busts! Dealer wins!");
            handleStand();
        } else if (playerScore == 21) {
            handleStand();
        }
    }

    private void handleStand() {
        disableButtons();

        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);
        // Remove the "Hidden" label

        game.dealerTurn();

        for (Card card : dealerHand) {
            // if the card is the first card, don't add it
            if (card == dealerHand.get(0)) {
                continue;
            }
            ImageView cardView = getCardImageView(card);
            dealerCardPane.getChildren().add(cardView);
        }
        this.game.getComputer().decideBet();
        // this.game.getComputer().setBet(100);
        int playerScore = playerHand.getValue();
        int dealerScore = dealerHand.getValue();

        dealerValueLabel.setText("Dealer Value: " + dealerScore);
        playerValueLabel.setText("Player Value: " + playerScore);

        this.game.resolveAll();
        balanceLabel.setText("Balance: $" + currentUser.getBalance());
        postGameButtonBox.setVisible(true);
        updateBalanceInFile(loggedInUsername.get(), currentUser.getBalance());
    }

    private void disableButtons() {
        hitButton.setDisable(true);
        standButton.setDisable(true);
    }
}