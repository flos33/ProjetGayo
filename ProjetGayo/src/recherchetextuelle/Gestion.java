package recherchetextuelle;



import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.spans.SpanNotQuery;

import javafx.application.Application;
import javafx.stage.Stage;
import recherchetextuelle.util.Indexer;
import recherchetextuelle.util.Searcher;



public class Gestion  {
	static Indexer indexer = null;
	static Searcher searcher = null;
	static String indexDir ="/Users/pierreo/Projects/M2SITIS/gayo/index";
	static String corpusDir ="/Users/pierreo/Projects/M2SITIS/gayo/corpus";
	static String synonymsFile = "/Users/pierreo/Projects/M2SITIS/gayo/synonyms.txt";
	
	public static void main(String[] args) throws ParseException {
		
		 
		try {
			/*indexer = new Indexer(indexDir,synonymsFile);
			
			indexer.indexFileOrDirectory(corpusDir);
			
			indexer.closeIndex();*/
			
			
			
			searcher = new Searcher(indexDir, synonymsFile);
			
			/*ArrayList<String> terms = new ArrayList<String>();
			terms.add("sus");
			terms.add("dec");
			searcher.phraseQuery(terms);*/
			//TODO Gerer un chargement des char arrayset de phrase à autophraser depuis file ou user input
			/*System.out.println("tous les doc contenant sus dec:");
			searcher.query(
					" sus_decalage"
					);*/
			System.out.println("Les doc contenant sus dec pas proche d'une negation:");
			searcher.findSusDec();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.apache.lucene.queryparser.classic.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

	public static Indexer getIndexer() {
		return indexer;
	}




	public static void setIndexer(Indexer indexer) {
		Gestion.indexer = indexer;
	}




	public static Searcher getSearcher() {
		return searcher;
	}




	public static void setSearcher(Searcher searcher) {
		Gestion.searcher = searcher;
	}




	public static String getIndexDir() {
		return indexDir;
	}




	public static void setIndexDir(String indexDir) {
		Gestion.indexDir = indexDir;
	}




	public static String getCorpusDir() {
		return corpusDir;
	}




	public static void setCorpusDir(String corpusDir) {
		Gestion.corpusDir = corpusDir;
	}




	public static String getSynonymsFile() {
		return synonymsFile;
	}




	public static void setSynonymsFile(String synonymsFile) {
		Gestion.synonymsFile = synonymsFile;
	}




}