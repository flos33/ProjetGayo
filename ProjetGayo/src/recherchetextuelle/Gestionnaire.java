
package recherchetextuelle;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.DocumentListener;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import recherchetextuelle.model.Document;
import recherchetextuelle.util.Indexer;
import recherchetextuelle.util.Searcher;
import recherchetextuelle.view.RechercheDocController;

public class Gestionnaire extends Application {

    private Stage primaryStage;
    private BorderPane panneauUtilisateur;
    private ObservableList<recherchetextuelle.model.Document> docListe = FXCollections.observableArrayList();
       
    static Indexer indexer = null;
	static Searcher searcher = null;
	static String indexDir /*="/Users/ccecqa/Desktop/Gayo/index"*/;
	static String corpusDir /*="/Users/ccecqa/Desktop/Gayo/corpus"*/;
	static String synonymsFile /*= "/Users/ccecqa/Downloads/synonyms.txt"*/;
	

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("recherchetextuelle");
        initPanneauUtilisateur();
        showRechercheDoc();
    }
    public void createSearcher(String indexDirectory, String synonymsFile) throws IOException, ParseException{
    	searcher = new Searcher(indexDirectory,synonymsFile);
    }
    
    public Gestionnaire() {
        
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
            // Give the controller access to the main app.
            RechercheDocController controller = loader.getController();
            controller.setGestionnaire(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<recherchetextuelle.model.Document> getDocListe() {
        return docListe;
    }

    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public  Indexer getIndexer() {
		return indexer;
	}

	public  void setIndexer(Indexer indexer) {
		Gestionnaire.indexer = indexer;
	}

	public  Searcher getSearcher() {
		return searcher;
	}


	public  void setSearcher(Searcher searcher) {
		Gestionnaire.searcher = searcher;
	}


	public  String getIndexDir() {
		return indexDir;
	}

	public  void setIndexDir(String indexDir) {
		Gestionnaire.indexDir = indexDir;
		System.out.println(Gestionnaire.indexDir);
		
	}


	public String getCorpusDir() {
		return corpusDir;
	}


	public  void setCorpusDir(String corpusDir) {
		Gestionnaire.corpusDir = corpusDir;
System.out.println(Gestionnaire.corpusDir);
	}

	public  String getSynonymsFile() {
		return synonymsFile;
	}


	public  void setSynonymsFile(String synonymsFile) {
		Gestionnaire.synonymsFile = synonymsFile;
		System.out.println(Gestionnaire.synonymsFile);

	}
    
   

	public static void main(String[] args) throws ParseException {

		launch(args);
		
		
		
	}
	 
    
}
