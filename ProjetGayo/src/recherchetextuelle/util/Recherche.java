package recherchetextuelle.util;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import java.io.*;
import java.util.ArrayList;

public class Recherche {
    private static StandardAnalyzer analyzer = new StandardAnalyzer();
      private IndexWriter writer;
      private ArrayList<File> queue = new ArrayList<File>();
      public static void main(String[] args) throws IOException {
        System.out.println("Entrez le chemin o� l'index doit �tre cr�e");
        String indexLocation = null;
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String s = br.readLine();
        TextFileIndexer indexer = null;
        try {
          indexLocation = s;
          indexer = new TextFileIndexer(s);
        } catch (Exception ex) {
          System.out.println("Ne peut cr�er l'index..." + ex.getMessage());
          System.exit(-1);
        }
//Test git
        while (!s.equalsIgnoreCase("q")) {
          try {
            System.out.println("Entrez le chemin des documents � ajouter � l'index");
            System.out.println("[Types de fichiers accept�s: .xml, .html, .html, .txt]");
            s = br.readLine();
            if (s.equalsIgnoreCase("q")) {
              break;
            }
            indexer.indexFileOrDirectory(s);
          } catch (Exception e) {
            System.out.println("Erreur pour indexer " + s + " : " + e.getMessage());
          }
        }
      // Possibilit� plusieurs index? 
        indexer.closeIndex();
        //=========================================================
        // Now search
        //=========================================================
        IndexReader reader = DirectoryReader.open( FSDirectory.open(new File(indexLocation).toPath()));
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector ;
        s = "";
        while (!s.equalsIgnoreCase("q")) {
          try {
            System.out.println("Entrez la requ�te (q=quitter):");
            s = br.readLine();
            if (s.equalsIgnoreCase("q")) {
              break;
            }
            collector = TopScoreDocCollector.create(5);
            Query q = new QueryParser("contents", analyzer).parse(s);
            searcher.search(q, collector);
            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            // 4. display results
            System.out.println("Trouv� " + hits.length + " hits.");
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
      /**
       * Constructor
       * @param indexDir the name of the folder in which the index should be created
       * @throws java.io.IOException when exception creating index.
       */
      TextFileIndexer(String indexDir) throws IOException {
        // the boolean true parameter means to create a new index everytime, 
        // potentially overwriting any existing files there.
        FSDirectory dir = FSDirectory.open(new File(indexDir).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        writer = new IndexWriter(dir, config);
      }
      /**
       * Indexes a file or directory
       * @param fileName the name of a text file or a folder we wish to add to the index
       * @throws java.io.IOException when exception
       */
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
            System.out.println("Ajout�: " + f);
          } catch (Exception e) {
            System.out.println("Impossible d'ajouter: " + f);
          } finally {
            fr.close();
          }
        }
        
        int newNumDocs = writer.numDocs();
        System.out.println("");
        System.out.println("************************");
        System.out.println((newNumDocs - originalNumDocs) + " documents ajout�s.");
        System.out.println("************************");
        queue.clear();
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
            System.out.println("Ignor� " + filename);
          }
        }
      }
      /**
       * Close the index.
       * @throws java.io.IOException when exception closing
       */
      public void closeIndex() throws IOException {
        writer.close();
      }
    }
