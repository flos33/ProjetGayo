package recherchetextuelle.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanNotQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

public class Searcher {
	static IndexReader reader;
	static IndexSearcher searcher;
	static TopScoreDocCollector collector = TopScoreDocCollector.create(5);
	static FileReader synonymFileReader;
	CustomAnalyzer analyzer;
	
	public Searcher(String indexDir, String synonymFilePath) throws IOException, java.text.ParseException {
		FileReader synonymFileReader = new FileReader(new File(synonymFilePath));
		analyzer = new CustomAnalyzer(synonymFileReader);
	    reader = DirectoryReader.open( FSDirectory.open(new File(indexDir).toPath()));
	    searcher = new IndexSearcher(reader);
	}
	
	public void query(String queryString) throws ParseException, IOException {
		collector = TopScoreDocCollector.create(150);
        Query q = new ComplexPhraseQueryParser("contents", analyzer).parse(queryString);
        
        System.out.println(q);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        // 4. display results
        System.out.println("Trouv� " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document d = searcher.doc(docId);
          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
        }
	}
	public void findSusDec() throws ParseException, IOException {
		//-----Create all susDecs query-----
        int distance = 3;
<<<<<<< HEAD
        boolean ordered = true;
=======
        boolean ordered = false;
>>>>>>> refs/heads/testinIndexSynonym
        SpanQuery susdec = new SpanTermQuery(new Term("contents", "sus_dec"));
        SpanQuery negation = new SpanTermQuery(new Term("contents", "pas"));
        SpanQuery negationSusdec = new SpanNearQuery(new SpanQuery[] { negation, susdec },
        distance, ordered);
        Query susDecTot = new SpanNotQuery(susdec, negationSusdec,distance);
        System.out.println(susDecTot);
        //-----created all susDecs query-----
        
        
		//-----Create susDecs and acr query-----

        Builder susDecAndAcrBQ = new BooleanQuery.Builder();
        susDecAndAcrBQ.add(susDecTot, Occur.MUST);
		TermQuery acr = new TermQuery(new Term("contents","acr"));
		susDecAndAcrBQ.add(acr, Occur.MUST);
        
		//-----created susDecs and acr query-----
		
		//-----Obtain susDecsAndAcr docIDs-----
		ArrayList<Integer> susDecAndAcrDocIds = new ArrayList<>();
		ArrayList<Float> susDecAndAcrScores = new ArrayList<>();
        
        searcher.search(susDecAndAcrBQ.build(), collector);
        ScoreDoc[] susDecAndAcrHits = collector.topDocs().scoreDocs;

        for(int i=0;i<susDecAndAcrHits.length;++i) {
          int docId = susDecAndAcrHits[i].doc;
          susDecAndAcrDocIds.add(docId);
          susDecAndAcrScores.add((float) susDecAndAcrHits[i].score);
        }
        //-----obtained susDecsAndAcr docIDs-----
        
      //-----Create susDecsNoAcr query-----

        Builder susDecNoAcrBQ = new BooleanQuery.Builder();
        susDecNoAcrBQ.add(susDecTot, Occur.MUST);
		susDecNoAcrBQ.add(acr, Occur.MUST_NOT);
        
		//-----created susDecsNoAcr query-----
        
        //-----Obtain susDecsNoAcr docIDs-----
  		ArrayList<Integer> susDecNoAcrDocIds = new ArrayList<>();
<<<<<<< HEAD
  		ArrayList<Float> susDecNoAcrScores = new ArrayList<>();

=======
>>>>>>> refs/heads/testinIndexSynonym
  		collector = TopScoreDocCollector.create(150);
          searcher.search(susDecNoAcrBQ.build(), collector);
          ScoreDoc[] susDecNoAcrHits = collector.topDocs().scoreDocs;

          for(int i=0;i<susDecNoAcrHits.length;++i) {
            int docId = susDecNoAcrHits[i].doc;
            susDecNoAcrDocIds.add(docId);
            susDecNoAcrScores.add((float) susDecNoAcrHits[i].score);

          }
        //-----obtained susDecsNoAcr docIDs-----
          
        //-----Display susDecNoAcr -----
          System.out.println("Textes avec susDecs et pas de ACR:");
          for(int i=0;i<susDecNoAcrDocIds.size();++i) {
	          int docId = susDecNoAcrDocIds.get(i);
	          Document d = searcher.doc(docId);
	          System.out.println((i + 1) + ". " + d.get("path") + "\tscore= "+ susDecNoAcrScores.get(i) );
	          }
        //-----displayed susDecNoAcr -----
        //-----Display susDecAndAcr -----
          System.out.println("Textes avec susDecs et ACR:");
          for(int i=0;i<susDecAndAcrDocIds.size();++i) {
	          int docId = susDecAndAcrDocIds.get(i);
	          Document d = searcher.doc(docId);
	          System.out.println((i + 1) + ". " + d.get("path") + "\tscore= "+ susDecAndAcrScores.get(i));
	          }
        //-----displayed susDecAndAcr -----
	}
	
	public void phraseQuery(ArrayList<String> termsList) throws IOException {
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		
		termsList.stream()
		.map(x -> new Term("contents", x))
		.forEach(builder::add);
        System.out.println(builder.build());

		searcher.search(builder.build(),collector);
 		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          System.out.println((i + 1) + ". " + d.get("path") + " score=" + hits[i].score);
	          }
		}
		

	
}
