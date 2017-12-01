import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;


public class Main {
	private static FrenchAnalyzer analyzer = new FrenchAnalyzer();
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
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

}
