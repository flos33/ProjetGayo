package recherchetextuelle.util;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class NegationFilter extends TokenFilter {
	protected CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
	
	protected NegationFilter(TokenStream input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean incrementToken() throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

}
