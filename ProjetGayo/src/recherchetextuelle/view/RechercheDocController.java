package recherchetextuelle.view;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

import com.google.inject.Stage;

import javafx.beans.property.ReadOnlyFloatProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.scene.control.Alert.AlertType;
import recherchetextuelle.Gestionnaire;
import recherchetextuelle.model.Document;
import recherchetextuelle.util.Indexer;
import recherchetextuelle.util.Searcher;

public class RechercheDocController {

	// Reference � l'application principale.
	private Gestionnaire gestionnaire;
	private Window dialogStage;
	static Indexer indexer = null;
	static Searcher searcher = null;
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
		if (isInputValidOne()) {
			String selectedPath = corpusField.getText();
			gestionnaire.setCorpusDir(selectedPath);
		}
	}
	@FXML
	private void handleDefinirIndexBtn() {
		if (isInputValidTwo()) {
			String indexDir = indexField.getText();
			gestionnaire.setIndexDir(indexDir);
		}
	}

	@FXML
	private void handleDefinirSynonymesBtn() {
		if (isInputValidThree()) {
			String synonymsFile = synonymesField.getText();
			gestionnaire.setSynonymsFile(synonymsFile);
		}
	}

	@FXML
	private void handleLaunchIndexingBtn() {
		String corpusDir = gestionnaire.getCorpusDir();
		String indexDir = gestionnaire.getIndexDir();
		String synonymesFile = gestionnaire.getSynonymsFile();
		
		try {
			gestionnaire.setIndexer(new Indexer(indexDir, synonymesFile));
			gestionnaire.getIndexer().indexFileOrDirectory(corpusDir);
			gestionnaire.getIndexer().closeIndex();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@FXML
	private void handleLaunchSearchBtn() {
		String indexDir = gestionnaire.getIndexDir();
		String synonymsFile = gestionnaire.getSynonymsFile();
		try {
			gestionnaire.setSearcher(new Searcher(indexDir, synonymsFile));

			int i = 1;
			for (Iterator<org.apache.lucene.document.Document> iterator = gestionnaire.getSearcher().findSusDec().iterator(); iterator.hasNext();) {
				org.apache.lucene.document.Document d = (org.apache.lucene.document.Document) iterator.next();
				recherchetextuelle.model.Document doc = 
						new recherchetextuelle.model.Document(d.get("filename"),String.valueOf(i));
				gestionnaire.getDocListe().add(doc);
				i++;
			}

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
		/*
        corpusField.setText(gestionnaire.getCorpusDir());
        indexField.setText(gestionnaire.getIndexDir());
        synonymesField.setText(gestionnaire.getSynonymsFile());*/

	}

	private boolean isInputValidOne() {
		String errorMessage = "";

		if (corpusField.getText() == null || corpusField.getText().length() == 0) {
			errorMessage += "Chemin du corpus vide!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Champ vide");
			alert.setHeaderText("Merci de le remplir");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	private boolean isInputValidTwo() {
		String errorMessage = "";

		if (indexField.getText() == null || indexField.getText().length() == 0) {
			errorMessage += "Chemin de l'index vide!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Champ vide");
			alert.setHeaderText("Merci de le remplir");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	private boolean isInputValidThree() {
		String errorMessage = "";

		if (synonymesField.getText() == null || synonymesField.getText().length() == 0) {
			errorMessage += "Chemin du fichier synonyme vide!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Champ vide");
			alert.setHeaderText("Merci de le remplir");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}


