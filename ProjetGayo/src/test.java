import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.xml.sax.InputSource;



public class test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//-------------load ontology-------------------------
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		InputStream in = FileManager.get().open("P:/Documents/INF203/Acopier/onto/nci_small_fma.owl");
		ontModel.read(in,null); //: si pb de resolution d'uri, il completera avec cet argument, mais le fichier la ocntient deja
		//-------------loaded ontology-------------------------
		
		
		//-------------initialize index-------------------------
		System.out.println("Entrez le chemin où  l'index doit être créé: (e.g. /tmp/index or c://temp//index)");

		String indexLocation = null;
	    BufferedReader br = new BufferedReader(
	            new InputStreamReader(System.in));
	    String s = br.readLine();

	    LuceneIndexation indexer = null;
	    try {
	      indexLocation = s;
	      indexer = new LuceneIndexation(s);
	    } catch (Exception ex) {
	      System.out.println("Ne peut créer l'index..." + ex.getMessage());
	      System.exit(-1);
	    }
	  //-------------initialized index-------------------------
	   
	  
		
	  //-------------create documents from classes and add to indexWriter-------------------------
		ExtendedIterator iterator = ontModel.listClasses();
		
		while (iterator.hasNext()){
			OntClass currentClass = (OntClass) iterator.next();
			indexer.addDocumentToWriter( test.createDocumentFromClass(currentClass) );
			
			}
		//-------------created documents from classes and added to indexWriter-------------------------

		
		 //===================================================
	    //after adding, we always have to call the
	    //closeIndex, otherwise the index is not created    
	    //===================================================
	    indexer.closeIndex();
	    
	    
		//=========================================================
	    // Now search
	    //=========================================================
	    LuceneSearch.search(indexLocation);
		}
		
	
	
	private static Document createDocumentFromClass(OntClass ontClass){
		
		Document doc = new Document();
	    
        doc.add(new StringField("uri", ontClass.getURI(), Field.Store.YES) );
        doc.add(new TextField("localName", ontClass.getLocalName(), Field.Store.YES));
        
        ExtendedIterator<RDFNode> labels = ontClass.listLabels(null);
		while( labels.hasNext()){
			doc.add(new TextField("label", labels.next().toString(), Field.Store.YES));
		}
		System.out.println("added a doc: "+doc.toString());
		return doc;
		
	}

}
