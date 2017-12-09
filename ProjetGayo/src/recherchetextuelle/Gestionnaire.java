package recherchetextuelle;

import java.io.IOException;
import java.text.ParseException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import recherchetextuelle.util.Indexer;
import recherchetextuelle.util.Searcher;
import recherchetextuelle.view.RechercheDocController;

public class Gestionnaire extends Application {

    private Stage primaryStage;
    private BorderPane panneauUtilisateur;
    private ObservableList<recherchetextuelle.model.Document> docList = FXCollections.observableArrayList();
    static Indexer indexer = null;
	static Searcher searcher = null;
	static String indexDir ="/Users/ccecqa/Desktop/Gayo/index";
	static String corpusDir ="/Users/ccecqa/Desktop/Gayo/corpus";
	static String synonymsFile = "/Users/ccecqa/Downloads/synonyms.txt";
	
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
            // Give the controller access to the main app.
            RechercheDocController controller = loader.getController();
            controller.setGestionnaire(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ObservableList<recherchetextuelle.model.Document> getDocList() {
        return docList;
    }

    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public static Indexer getIndexer() {
		return indexer;
	}

	public static void setIndexer(Indexer indexer) {
		Gestionnaire.indexer = indexer;
	}

	public static Searcher getSearcher() {
		return searcher;
	}


	public static void setSearcher(Searcher searcher) {
		Gestionnaire.searcher = searcher;
	}


	public static String getIndexDir() {
		return indexDir;
	}

	public static void setIndexDir(String indexDir) {
		Gestionnaire.indexDir = indexDir;
	}


	public static String getCorpusDir() {
		return corpusDir;
	}


	public static void setCorpusDir(String corpusDir) {
		Gestionnaire.corpusDir = corpusDir;
	}

	public static String getSynonymsFile() {
		return synonymsFile;
	}


	public static void setSynonymsFile(String synonymsFile) {
		Gestionnaire.synonymsFile = synonymsFile;
	}
    
    

	public static void main(String[] args) throws ParseException {
		
		
		 
		try {
			/*indexer = new Indexer(indexDir);
			
			indexer.indexFileOrDirectory(corpusDir);
			
			indexer.closeIndex();
			*/
			
			
			searcher = new Searcher(indexDir, synonymsFile);
			
			/*ArrayList<String> terms = new ArrayList<String>();
			terms.add("sus");
			terms.add("dec");
			searcher.phraseQuery(terms);*/
			
			searcher.query(
					"sus dec doul tho"
					);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		launch(args);
		
		
	}
	 
    
}