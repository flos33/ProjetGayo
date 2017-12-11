package recherchetextuelle.view;

import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import recherchetextuelle.Gestionnaire;
import recherchetextuelle.model.Document;

public class RechercheDocController {

	@FXML
    private TableView<Document> docTable;
    @FXML
    private TableColumn<Document, String> iColumn;
    @FXML
    private TableColumn<Document, String> pathColumn;
   /* @FXML
    private TableColumn<Document, Float> scoreColumn;*/

    @FXML
    private Label iLabel;
    @FXML
    private Label pathLabel;
    /*
    @FXML
    private Label scoreLabel;
*/
    
    // Reference to the main application.
    private Gestionnaire gestionnaire;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RechercheDocController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Initialize the docTable with the two columns.
    	
        iColumn.setCellValueFactory(cellData -> cellData.getValue().iProperty());
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().pathProperty());

            }

    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setGestionnaire(Gestionnaire gestionnaire) {
        this.gestionnaire = gestionnaire;

        // Add observable list data to the table
        docTable.setItems(gestionnaire.getDocListe());
    }
}


