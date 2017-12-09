package recherchetextuelle;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Gestionnaire extends Application {

    private Stage primaryStage;
    private BorderPane panneauUtilisateur;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("recherchetextuelle");

        initPanneauUtilisateur();

        showRechercheDoc();
    }

    /**
     * Initialise le panneau Utilisateur.
     */
    public void initPanneauUtilisateur() {
        try {
            // Charge le fichier fxml panneauUtilisateur.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Gestionnaire.class.getResource("view/panneauUtilisateur.fxml"));
            panneauUtilisateur = (BorderPane) loader.load();

            // Affiche le panneau Utilisateur.
            Scene scene = new Scene(panneauUtilisateur);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Afficher les champs de recherche dans le panneau Utilisateur
     */
    public void showRechercheDoc() {
        try {
            // Charge le fichier fxml rechercheDoc.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Gestionnaire.class.getResource("view/rechercheDoc.fxml"));
            AnchorPane rechercheDoc = (AnchorPane) loader.load();

            // Affiche le doc au centre du panneau Utilisateur.
            panneauUtilisateur.setCenter(rechercheDoc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}