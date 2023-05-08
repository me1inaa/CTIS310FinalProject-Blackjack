package edu.guilford;

import javafx.scene.Node;

import java.util.ArrayList;
import java.io.FileNotFoundException;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class BlackjackPane extends Application {

    private BlackjackGame game;
    private Deck deck;
    private Hand dealerHand;
    private Hand playerHand;
    private User user;


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
    public void start(Stage primaryStage) throws IOException{
        primaryStage.setTitle("Blackjack");
        primaryStage.setResizable(false);
        
        

        //initializeGame();
        
        //BorderPane root = createGUI();

        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);

        showLoginDialog(primaryStage);
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
        Label balanceLabel = new Label("Balance:");
        TextField balanceField = new TextField();
    
        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(balanceLabel, 0, 1);
        gridPane.add(balanceField, 1, 1);
    
        dialogPane.setContent(gridPane);
    
        ButtonType loginButtonType = new ButtonType("Login");
        dialogPane.getButtonTypes().add(loginButtonType);
    
        ButtonType exitButtonType = new ButtonType("Exit");
        dialogPane.getButtonTypes().add(exitButtonType);
    
        dialogPane.setPrefWidth(300);
    
        primaryStage.setScene(new Scene(dialogPane));
        primaryStage.show();
    
        dialogPane.lookupButton(loginButtonType).setOnMouseClicked(event -> {
            String username = usernameField.getText();
            String balance = balanceField.getText();
    
            if (authenticateUser(username, balance)) {
                loggedInUsername.set(username);
                primaryStage.close();
                 initializeGame();
                showDepositWithdrawScreen(username);
            } else {
                showAlert(Alert.AlertType.ERROR, "Invalid username or balance", "Please enter a valid username and balance.");
            }
        });
    
        dialogPane.lookupButton(exitButtonType).setOnMouseClicked(event -> {
            primaryStage.close();
        });
    }

    
    //Set the user attribute to a User object, that is created either with data from the userFile or as a new User for someone who hasn't loggged in before.
    private boolean authenticateUser(String username, String balance) {
        try {
            //Path dataLocation = Paths.get(BlackjackPane.class.getResource("/data.txt").toURI());
            //FileReader dataFile = new F
            String data = new String(Files.readAllBytes(userFile));
            String[] usernames = data.split("\n");
            boolean userExists = false;
    
            for (String user : usernames) {
                String[] userInfo = user.split(",");
                if (userInfo.length == 2 && userInfo[0].equals(username) && userInfo[1].equals(balance)) {
                    userExists = true;
                    break;
                }
            }
            // If the entered username and password were not found, append them to the data.txt file
            if (!userExists) {
                // Append the new username and password to the file
                String newUser = username + "," + balance;
                Files.write(userFile, (newUser + "\n").getBytes(), StandardOpenOption.APPEND);
            }
            user = new User(username, Double.parseDouble(balance));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return false;
    }
   
    
    
    private double userBalance; 
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to show the deposit/withdraw screen
    private void showDepositWithdrawScreen(String username) {
        Stage depositWithdrawStage = new Stage();
        depositWithdrawStage.setTitle("Deposit/Withdraw");
        depositWithdrawStage.setResizable(false);
    
        Label balanceLabel = new Label("Current Balance: $" + userBalance);
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
                double depositAmount = Double.parseDouble(depositAmountStr);
                double maxDepositAmount = 10000.0; // Set the maximum deposit amount here
                if (depositAmount > maxDepositAmount) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Deposit Amount", "The maximum deposit amount is $" + maxDepositAmount);
                } else {
                    userBalance += depositAmount;
                    balanceLabel.setText("Current Balance: $" + userBalance);
                    showAlert(Alert.AlertType.INFORMATION, "Deposit Successful", "Deposit of $" + depositAmount + " is successful.");
                    updateBalanceInFile(username, userBalance);
                }
            }
        });
    
        withdrawButton.setOnAction(event -> {
            String withdrawAmountStr = withdrawTextField.getText();
            if (!withdrawAmountStr.isEmpty()) {
                double withdrawAmount = Double.parseDouble(withdrawAmountStr);
                if (withdrawAmount <= userBalance) {
                    userBalance -= withdrawAmount;
                    balanceLabel.setText("Current Balance: $" + userBalance);
                    showAlert(Alert.AlertType.INFORMATION, "Withdraw Successful", "Withdraw of $" + withdrawAmount + " is successful.");
                    updateBalanceInFile(username, userBalance);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Insufficient Balance", "Insufficient balance to make the withdrawal.");
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
    
        Scene scene = new Scene(root, 1000, 800);
        gameStage.setScene(scene);
        gameStage.show();
    }
    
    private void enableButtons() {
        hitButton.setDisable(false);
        standButton.setDisable(false);
    }

    private void initializeGame() {
        this.game = new BlackjackGame(this.user);
        this.game.deal();
        deck = this.game.getDeck();
        dealerHand = this.game.getDealer().getHand();
        playerHand = this.game.getPlayers().get(1).getHand();
        
    }

    private void resetGame() {
        deck = new Deck();
        dealerHand = new Hand();
        playerHand = new Hand();
    
        deck.shuffle();
    
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());

        game.reset();
        game.deal();
        playerHand = game.getPlayers().get(1).getHand();
        dealerHand = game.getDealer().getHand();
    
        // Reset the GUI elements
        dealerLabel.setText("Dealer: ");
        playerLabel.setText("Player: ");
        resultLabel.setText("");
        playerValueLabel.setText("");
        dealerValueLabel.setText("");
    
        FlowPane dealerCardPane = (FlowPane) ((HBox) dealerLabel.getParent()).getChildren().get(1);
        FlowPane playerCardPane = (FlowPane) ((HBox) playerLabel.getParent()).getChildren().get(1);
    
        dealerCardPane.getChildren().clear();
        playerCardPane.getChildren().clear();
    
        // Display the dealer's card
        for (Card card : dealerHand) {
            Label cardLabel = new Label(card.toString());
            cardLabel.setFont(Font.font(16));
            cardLabel.setTextFill(Color.BLACK);
            dealerCardPane.getChildren().add(cardLabel);
        }
        for (Card card : playerHand) {
            Label cardLabel = new Label(card.toString());
            cardLabel.setFont(Font.font(16));
            cardLabel.setTextFill(Color.BLACK);
            playerCardPane.getChildren().add(cardLabel);
        }
        // Enable the buttons
        hitButton.setDisable(false);
        standButton.setDisable(false);
        postGameButtonBox.setVisible(false);
    }

    private BorderPane createGUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
    
        HBox dealerBox = createHandBox("Dealer", dealerHand, true);
        dealerLabel = (Label) dealerBox.getChildren().get(0);
        dealerValueLabel = new Label();
    
        HBox playerBox = createHandBox("Player", playerHand, false);
        playerLabel = (Label) playerBox.getChildren().get(0);
        playerValueLabel = new Label();
    
        VBox scoreBox = new VBox(20);
        scoreBox.setAlignment(Pos.CENTER);
    
        Label dealerScoreLabel = new Label("Dealer: ");
        dealerScoreLabel.setFont(Font.font(20));
        dealerScoreLabel.setTextFill(Color.BLACK);
    
        Label playerScoreLabel = new Label("Player: ");
        playerScoreLabel.setFont(Font.font(20));
        playerScoreLabel.setTextFill(Color.BLACK);
    
        resultLabel = new Label();
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
    
        gameButtonBox = new HBox(20);
        gameButtonBox.setAlignment(Pos.CENTER);
        gameButtonBox.getChildren().addAll(hitButton, standButton);
    
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
        dealerValueLabel.setText("Dealer Value: " + dealerHand.getValue());
    
        if (playerScore > 21) {
            resultLabel.setText("Player busts! Dealer wins!");
            disableButtons();
            postGameButtonBox.setVisible(true);
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