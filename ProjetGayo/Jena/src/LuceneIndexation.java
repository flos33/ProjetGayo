import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;


public class LuceneIndexation {

	private static FrenchAnalyzer analyzer = new FrenchAnalyzer(getStopWords());
	private IndexWriter writer;
	private ArrayList<File> queue = new ArrayList<File>();
	
	public LuceneIndexation(String indexDir) throws IOException {
	    // the boolean true parameter means to create a new index everytime, 
	    // potentially overwriting any existing files there.
	    FSDirectory dir = FSDirectory.open(new File(indexDir).toPath());


	    IndexWriterConfig config = new IndexWriterConfig(getAnalyzer());

	    writer = new IndexWriter(dir, config);
	  }
	
	
	private void addFiles(File file) {

	    if (!file.exists()) {
	      System.out.println(file + " n'existe ps.");
	    }
	    if (file.isDirectory()) {
	      for (File f : file.listFiles()) {
	        addFiles(f);
	      }
	    } else {
	      String filename = file.getName().toLowerCase();
	      //===================================================
	      // Only index text files
	      //===================================================
	      if (filename.endsWith(".htm") || filename.endsWith(".html") || 
	              filename.endsWith(".xml") || filename.endsWith(".txt")) {
	        queue.add(file);
	      } else {
	        System.out.println("Ignoré " + filename);
	      }
	    }
	  }
	
	public void indexFileOrDirectory(String fileName) throws IOException {
	    //===================================================
	    //gets the list of files in a folder (if user has submitted
	    //the name of a folder) or gets a single file name (is user
	    //has submitted only the file name) 
	    //===================================================
	    addFiles(new File(fileName));
	    
	    int originalNumDocs = writer.numDocs();
	    for (File f : queue) {
	      FileReader fr = null;
	      try {
	        Document doc = new Document();

	        //===================================================
	        // add contents of file
	        //===================================================
	        fr = new FileReader(f);
	        doc.add(new TextField("contents", fr));
	        doc.add(new StringField("path", f.getPath(), Field.Store.YES));
	        doc.add(new StringField("filename", f.getName(), Field.Store.YES));

	        writer.addDocument(doc);
	        System.out.println("Ajouté: " + f);
	      } catch (Exception e) {
	        System.out.println("Impossible d'ajouter: " + f);
	      } finally {
	        fr.close();
	      }
	    }
	    
	    int newNumDocs = writer.numDocs();
	    System.out.println("");
	    System.out.println("************************");
	    System.out.println((newNumDocs - originalNumDocs) + " documents ajoutés.");
	    System.out.println("************************");

	    queue.clear();
	  }

	public void closeIndex() throws IOException {
	    writer.close();
	  }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public void addDocumentToWriter(Document doc){
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static EnglishAnalyzer getAnalyzer() {
		return analyzer;
	}
	
	public static CharArraySet getStopWords(){
		System.out.println("Enter the path to the stop word file");
		BufferedReader br = new BufferedReader(
	            new InputStreamReader(System.in));
		String path="";
		
		String str; 
		ArrayList<String> wordList = new ArrayList<String>();
		
		try {
			path = br.readLine();
			BufferedReader in = new BufferedReader(new FileReader(path));
			while ((str=in.readLine()) != null) { 
				  wordList.add(str);
				}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new CharArraySet(wordList, true);
	}
}

