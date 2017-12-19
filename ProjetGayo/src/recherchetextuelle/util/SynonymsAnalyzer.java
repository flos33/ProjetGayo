package recherchetextuelle.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class SynonymsAnalyzer extends Analyzer {
	  
	  /**
	   * Creates a new {@link WhitespaceAnalyzer}
	   */
	  public SynonymsAnalyzer() {
	  }
	  
	  @Override
	  protected TokenStreamComponents createComponents(final String fieldName) {
		  final Tokenizer source = new WhitespaceTokenizer();
		  TokenStream result = new LowerCaseFilter(source);
		    result = new ASCIIFoldingFilter(result);
		  return new TokenStreamComponents(source, result);
	  }
	}
