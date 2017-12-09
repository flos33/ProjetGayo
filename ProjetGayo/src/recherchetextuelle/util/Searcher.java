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
	TopScoreDocCollector collector = TopScoreDocCollector.create(5);
	static FileReader synonymFileReader;
	CustomAnalyzer analyzer;
	
	public Searcher(String indexDir, String synonymFilePath) throws IOException, java.text.ParseException {
		FileReader synonymFileReader = new FileReader(new File(synonymFilePath));
		analyzer = new CustomAnalyzer(synonymFileReader);
	    reader = DirectoryReader.open( FSDirectory.open(new File(indexDir).toPath()));
	    searcher = new IndexSearcher(reader);
	}
	
	public void query(String queryString) throws ParseException, IOException {
		collector = TopScoreDocCollector.create(5);
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
		
        int distance = 3;
        boolean ordered = true;
        SpanQuery susdec = new SpanTermQuery(new Term("contents", "sus_dec"));
        SpanQuery negation = new SpanTermQuery(new Term("contents", "pas"));
        SpanQuery negationSusdec = new SpanNearQuery(new SpanQuery[] { susdec, negation },
        distance, ordered);

        Query q = new SpanNotQuery(susdec, negationSusdec);
        
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
		
	
	
	public void fuzzyQuery(String nom, String prenom, String sexe, String ddn) throws IOException {
		Builder booleanQuery = new BooleanQuery.Builder();
		
		FuzzyQuery query1 = new FuzzyQuery(new Term("nom", nom));
		FuzzyQuery query2 = new FuzzyQuery(new Term("prenom", prenom));
		FuzzyQuery query3 = new FuzzyQuery(new Term("sexe", sexe));
		FuzzyQuery query4 = new FuzzyQuery(new Term("ddn", ddn));
		
		booleanQuery.add(query1, Occur.MUST);
		booleanQuery.add(query2, Occur.MUST);
		booleanQuery.add(query3, Occur.MUST);
		booleanQuery.add(query4, Occur.MUST);
		
		searcher.search(booleanQuery.build(),this.collector);
 		ScoreDoc[] hits = this.collector.topDocs().scoreDocs;
		for(int i=0;i<hits.length;++i) {
	          int docId = hits[i].doc;
	          Document d = searcher.doc(docId);
	          System.out.println((i + 1) + ". " + d.get("nom") + d.get("prenom") + d.get("sexe") + d.get("ddn") + " score=" + hits[i].score);
	        }
	}
	
}
