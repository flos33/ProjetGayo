package recherchetextuelle.view;

import java.io.IOException;
import java.text.ParseException;

import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import recherchetextuelle.Gestionnaire;
import recherchetextuelle.model.Document;
import recherchetextuelle.util.Searcher;

public class RechercheDocController {
	
	// Reference to the main application.
    private Gestionnaire gestionnaire;
    
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
    	@FXML
    private TextField corpusField;
    	
    	@FXML
    private TextField indexField;
    	
    	@FXML
    private TextField synonymesField;
    	
    	@FXML
    private void handleDefinirCorpusBtn() {
        String selectedPath = corpusField.getText();
        gestionnaire.setCorpusDir(selectedPath);
    }
    	@FXML
        private void handleDefinirIndexBtn() {
            String selectedPath = indexField.getText();
            gestionnaire.setIndexDir(selectedPath);
        }
    	
    	@FXML
        private void handleDefinirSynonymesBtn() {
            String selectedPath = synonymesField.getText();
            gestionnaire.setSynonymsFile(selectedPath);
        }
    	@FXML
        private void handleLaunchIndexingBtn() {
    			String corpusDir = gestionnaire.getCorpusDir();
            try {
				gestionnaire.getIndexer().indexFileOrDirectory(corpusDir);
			} catch (IOException e) {
				e.printStackTrace();
			}

        }
    	@FXML
        private void handleLaunchSearchBtn() {
    			String indexDir = gestionnaire.getIndexDir();
    			String synonymesFile = gestionnaire.getSynonymsFile();
            try {
            	gestionnaire.setSearcher(new Searcher(indexDir, synonymesFile));
            	gestionnaire.getSearcher().findSusDec();
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			} catch (org.apache.lucene.queryparser.classic.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    	
    

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
        docTable.setItems(gestionnaire.getDocListe());
        corpusField.setText(gestionnaire.getCorpusDir());
        indexField.setText(gestionnaire.getIndexDir());
        synonymesField.setText(gestionnaire.getSynonymsFile());

    }
}


