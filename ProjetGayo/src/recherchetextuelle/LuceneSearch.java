package recherchetextuelle;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
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


public class LuceneSearch {
	static IndexReader reader ;
	static  IndexSearcher searcher =null;
	static TopScoreDocCollector collector = TopScoreDocCollector.create(5);


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static void search(String indexLocation) throws IOException{
		/*
		String indexLocation = null;
	    BufferedReader br = new BufferedReader(
	            new InputStreamReader(System.in));
	    String s = br.readLine();

	    TextFileIndexer indexer = null;
	    try {
	      indexLocation = s;
	      indexer = new TextFileIndexer(s);
	    } catch (Exception ex) {
	      System.out.println("Ne peut créer l'index..." + ex.getMessage());
	      System.exit(-1);
	    }
	    */
		
		BufferedReader br = new BufferedReader(
	            new InputStreamReader(System.in));
	    
	    reader = DirectoryReader.open( FSDirectory.open(new File(indexLocation).toPath()));
	    searcher = new IndexSearcher(reader);
	    
	    String s = "";
	    while (!s.equalsIgnoreCase("q")) {
	      try {
	        System.out.println("Entrez la requête (q=quitter):");
	        s = br.readLine();
	        if (s.equalsIgnoreCase("q")) {
	          break;
	        }
	        collector = TopScoreDocCollector.create(5);
	        FrenchAnalyzer analyzer = LuceneIndexation.getAnalyzer();
	        Query q = new QueryParser("contents", analyzer).parse(s);
	        searcher.search(q, collector);
	        ScoreDoc[] hits = collector.topDocs().scoreDocs;

	        // 4. display results
	        System.out.println("Trouvé " + hits.length + " hits.");
	        for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
	        }

	      } catch (Exception e) {
	        System.out.println("Erreur pour chercher " + s + " : " + e.getMessage());
	      }
	    }
	}
	

}
