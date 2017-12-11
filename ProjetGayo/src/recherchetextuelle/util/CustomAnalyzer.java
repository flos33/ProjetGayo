package recherchetextuelle.util;

import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.core.UnicodeWhitespaceAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.fr.FrenchLightStemFilter;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SolrSynonymParser;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.ElisionFilter;
import org.apache.lucene.util.IOUtils;


public class CustomAnalyzer extends StopwordAnalyzerBase {

	  /** File containing default French stopwords. */
	  public final static String DEFAULT_STOPWORD_FILE = "french_stop.txt";
	  FileReader synonymFileReader;
	  SolrSynonymParser synonymParser = new SolrSynonymParser(true, true, new SynonymsAnalyzer());
	  
	  /** Default set of articles for ElisionFilter */
	  public static final CharArraySet DEFAULT_ARTICLES = CharArraySet.unmodifiableSet(
	      new CharArraySet(Arrays.asList(
	          "l", "m", "t", "qu", "n", "s", "j", "d", "c", "jusqu", "quoiqu", "lorsqu", "puisqu"), true));

	  public static final CharArraySet defaultStopWords = CharArraySet.unmodifiableSet(
		      new CharArraySet(Arrays.asList(
			          ":","d","un"), true));
	  
	  /**
	   * Contains words that should be indexed but not stemmed.
	   */
	  private  CharArraySet excltable = CharArraySet.unmodifiableSet(
		      new CharArraySet(Arrays.asList(
			          "pas"), true));;

	  /**
	   * Returns an unmodifiable instance of the default stop-words set.
	   * @return an unmodifiable instance of the default stop-words set.
	   */
	  public static CharArraySet getDefaultStopSet(){
	    return DefaultSetHolder.DEFAULT_STOP_SET;
	  }
	  
	  private static class DefaultSetHolder {
	    static final CharArraySet DEFAULT_STOP_SET;
	    static {
	      try {
	        DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, 
	                DEFAULT_STOPWORD_FILE, StandardCharsets.UTF_8));
	      } catch (IOException ex) {
	        // default set should always be present as it is part of the
	        // distribution (JAR)
	        throw new RuntimeException("Unable to load default stopword set");
	      }
	    }
	  }

	  /**
	   * Builds an analyzer with a synonym map.
	 * @throws ParseException 
	 * @throws IOException 
	   */
	  public CustomAnalyzer(FileReader synonymFileReader) throws IOException, ParseException {
	    this(DefaultSetHolder.DEFAULT_STOP_SET);
	    this.synonymFileReader=synonymFileReader;
	    synonymParser.parse(synonymFileReader);
	  }
	  
	  public CustomAnalyzer(){
		    this(DefaultSetHolder.DEFAULT_STOP_SET);
		  }
	  
	  /**
	   * Builds an analyzer with the given stop words
	   * 
	   * @param stopwords
	   *          a stopword set
	   */
	  public CustomAnalyzer(CharArraySet stopwords){
	    this(stopwords, CharArraySet.EMPTY_SET);
	  }
	  
	  /**
	   * Builds an analyzer with the given stop words
	   * 
	   * @param stopwords
	   *          a stopword set
	   * @param stemExclutionSet
	   *          a stemming exclusion set
	   */
	  public CustomAnalyzer(CharArraySet stopwords,
	      CharArraySet stemExclutionSet) {
	    super(stopwords);
	    this.excltable = CharArraySet.unmodifiableSet(CharArraySet
	        .copy(stemExclutionSet));
	  }

	  /**
	   * Creates
	   * {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
	   * used to tokenize all the text in the provided {@link Reader}.
	   * 
	   * @return {@link org.apache.lucene.analysis.Analyzer.TokenStreamComponents}
	   *         built from a {@link StandardTokenizer} filtered with
	   *         {@link StandardFilter}, {@link ElisionFilter},
	   *         {@link LowerCaseFilter}, {@link StopFilter},
	   *         {@link SetKeywordMarkerFilter} if a stem exclusion set is
	   *         provided, and {@link FrenchLightStemFilter}
	   */
	@Override
	  protected TokenStreamComponents createComponents(String fieldName) {
	    final Tokenizer source = new WhitespaceTokenizer();
	    TokenStream result = new StandardFilter(source);
	    result = new ElisionFilter(result, DEFAULT_ARTICLES);
	    result = new LowerCaseFilter(result);
	    //result = new StopFilter(result, stopwords);
	    result = new StopFilter(result, defaultStopWords);
	    @SuppressWarnings("deprecation")
		CharArraySet phrases = new org.apache.lucene.analysis.util.CharArraySet(Arrays.asList(
	            "sus dec", "sus decalage", "sus ST", "no flow","arret cardio respiratoire"
	            ), false);
		result = new AutoPhrasingTokenFilter(result, phrases, false);

	    
		    try {
		    		SynonymMap map = synonymParser.build();
				result = new SynonymGraphFilter(result, map, false);
				System.out.println("used synonym map");

			} catch (IOException e) {
					// TODO Auto-generated catch block
				System.out.println("couldn't build synonym map from synonym file");
				e.printStackTrace();
			}
	    
	    
	    return new TokenStreamComponents(source, result);
	  }
	
	 @Override
	  protected TokenStream normalize(String fieldName, TokenStream in) {
	    TokenStream result = new StandardFilter(in);
	    result = new ElisionFilter(result, DEFAULT_ARTICLES);
	    result = new LowerCaseFilter(result);
	    return result;
	  }
}
