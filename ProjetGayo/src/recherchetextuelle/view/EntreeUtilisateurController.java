package recherchetextuelle.view;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import recherchetextuelle.Gestionnaire;


public class EntreeUtilisateurController {

    @FXML
    private TextField corpusString;
    @FXML
    private TextField indexString;
    @FXML
    private TextField synonymString;
    
    private Stage dialogStage;
    private boolean okClicked = false;
    private Stage secondaryStage;
    private BorderPane panneauUtilisateur;
    
    // Reference to the main application.
    private Gestionnaire gestionaire;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	   
    	   }
    
    public void starttwo(Stage secondaryStage) {
	       this.secondaryStage = secondaryStage;
	       this.secondaryStage.setTitle("recherchetextuelle");
	       initPanneauUtilisateur();
	       showRechercheDoc();
    	    
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
            // Give the controller access to the main app.
            RechercheDocController controller = loader.getController();
            controller.setGestionnaire(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            secondaryStage.setScene(scene);
            secondaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    public void handleOk() {
        if (isInputValid()) {
            String corpusDir = corpusString.getText();
            String indexDir = indexString.getText();
            String synonymDir = synonymString.getText();
            

            okClicked = true;
            dialogStage.close();
            
            
        }
    }
    
    public void setGestionaire(Gestionnaire gestionaire) {
        this.gestionaire = gestionaire;

    }
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (corpusString.getText() == null || corpusString.getText().length() == 0) {
            errorMessage += "Chemin non valide !\n";
        }
        if (indexString.getText() == null || indexString.getText().length() == 0) {
            errorMessage += "Chemin non valide !\n";
        }
        if (synonymString.getText() == null || synonymString.getText().length() == 0) {
            errorMessage += "Chemin non valide !\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }
    
}