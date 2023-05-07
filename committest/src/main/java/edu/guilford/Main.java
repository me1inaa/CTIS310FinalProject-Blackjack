package edu.guilford;


    // Main.java
    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.stage.Stage;
    
    public class Main extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("blackjack.fxml"));
            Parent root = loader.load();
            BlackjackController controller = loader.getController();
    
            Scene scene = new Scene(root);
            primaryStage.setTitle("Blackjack");
            primaryStage.setScene(scene);
            primaryStage.show();
    
            controller.initialize();
        }
    
        public static void main(String[] args) {
            launch(args);
        }
    }