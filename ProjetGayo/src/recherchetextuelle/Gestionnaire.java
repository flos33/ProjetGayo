
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import recherchetextuelle.model.Document;
import recherchetextuelle.util.Indexer;
import recherchetextuelle.util.Searcher;
import recherchetextuelle.view.EntreeUtilisateurController;
import recherchetextuelle.view.RechercheDocController;

public class Gestionnaire extends Application {

    private Stage primaryStage;
    private Stage secondaryStage;
    private BorderPane panneauUtilisateur;
    private ObservableList<recherchetextuelle.model.Document> docListe = FXCollections.observableArrayList();
       
    static Indexer indexer = null;
	static Searcher searcher = null;
	static String indexDir ="/Users/ccecqa/Desktop/Gayo/index";
	static String corpusDir ="/Users/ccecqa/Desktop/Gayo/corpus";
	static String synonymsFile = "/Users/ccecqa/Downloads/synonyms.txt";
	

   @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("recherchetextuelle");
        showEntreeUtilisateur();
        
    }
   
   public void starttwo(Stage secondaryStage) {
       this.secondaryStage = secondaryStage;
       this.secondaryStage.setTitle("recherchetextuelle");
       initPanneauUtilisateur();
       showRechercheDoc();
   }
    
    
    public void createSearcher(String indexDirectory, String synonymsFile) throws IOException, ParseException{
    	searcher = new Searcher(indexDirectory,synonymsFile);
    }
    
    public Gestionnaire() {
        // Add some sample data
 
    	
    /*
    	
    	Iterator<String> itr = docList.iterator();
    	while(itr.hasNext())
    	      system.out.println(itr.next());
    	
    	
    	
    	for(int i = 0 ; i < get.size; i++)
    	String pathi = recherchetextuelle.util.Searcher.ge	
    	
    	
    	*/
        docListe.add(new Document("Hans", "Muster"));
        docListe.add(new Document("Ruth", "Mueller"));
        
    }
    
       

    
   
   // panneau entrées des chemin par l'utilisateur 
    public boolean showEntreeUtilisateur() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Gestionnaire.class.getResource("view/entreeUtilisateur.fxml"));
            AnchorPane entreeUtilisateur = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Choix des chemins");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(entreeUtilisateur);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            EntreeUtilisateurController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

             return controller.isOkClicked();
            

        } catch (IOException e) {
            e.printStackTrace();
            return false;
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

    
    
   public void programme(){ 
    	 		initPanneauUtilisateur();
    		showRechercheDoc();
    		System.out.println("ok");
    	}

    
    
    public ObservableList<recherchetextuelle.model.Document> getDocListe() {
        return docListe;
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