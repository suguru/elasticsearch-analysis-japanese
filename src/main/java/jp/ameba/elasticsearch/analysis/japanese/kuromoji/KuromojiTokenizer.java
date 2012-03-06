package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import jp.ameba.elasticsearch.analysis.japanese.kuromoji.attr.BaseFormAttribute;

import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.atilika.kuromoji.Token;

/**
 * {@link KuromojiTokenizer} is based on KuromojiTokenizer implemented at kuromoji-solr project.
 */
public class KuromojiTokenizer extends Tokenizer {
	
	private final org.atilika.kuromoji.Tokenizer tokenizer;
	
	private final OffsetAttribute offsetAttr;
	
	private final CharTermAttribute termAttr;
	
	private final TypeAttribute typeAttr;
	
	private final BaseFormAttribute baseFormAttr;
	
	private List<Token> tokens;
	
	private int tokenIndex = 0;
	
	private String source;
	
	public KuromojiTokenizer(org.atilika.kuromoji.Tokenizer tokenizer, Reader aReader) throws IOException {
		super(aReader);
		this.tokenizer = tokenizer;
		this.offsetAttr = addAttribute(OffsetAttribute.class);
		this.termAttr = addAttribute(CharTermAttribute.class);
		this.typeAttr = addAttribute(TypeAttribute.class);
		this.baseFormAttr = addAttribute(BaseFormAttribute.class);
		reset(aReader);
	}
	
	@Override
	public void reset(Reader aReader) throws IOException {
		super.reset(input);
		this.tokenIndex = 0;
		this.source = IOUtils.toString(aReader);
		this.tokens = tokenizer.tokenize(this.source);
	}
	
	@Override
	public void end() throws IOException {
		int offset = correctOffset(source.length());
		offsetAttr.setOffset(offset, offset);
	}

	@Override
	public final boolean incrementToken() throws IOException {
		if (tokenIndex >= tokens.size()) {
			return false;
		}
		
		clearAttributes();
		
		Token token = tokens.get(tokenIndex++);
	
		String surface = token.getSurfaceForm();
		int position = token.getPosition();
		int length = surface.length();
		
		termAttr.setEmpty().append(surface);
		offsetAttr.setOffset(correctOffset(position), correctOffset(position+length));
		typeAttr.setType(token.getPartOfSpeech());
		baseFormAttr.setBaseForm(token.getBaseForm());
		return true;
	}

}
