package recherchetextuelle.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    
    // Reference to the main application.
    private Gestionnaire gestionaire;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
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