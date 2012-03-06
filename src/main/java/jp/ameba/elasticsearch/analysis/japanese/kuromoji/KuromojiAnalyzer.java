package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.CharReader;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;
import org.atilika.kuromoji.Tokenizer;
import org.atilika.kuromoji.Tokenizer.Mode;

public class KuromojiAnalyzer extends StopwordAnalyzerBase {
	
	private Tokenizer tokenizer;
	
	public KuromojiAnalyzer(boolean split, Mode mode) {
		super(Version.LUCENE_35, stopWords());
		this.tokenizer = Tokenizer
				.builder()
				.mode(mode)
				.split(split)
				.build();
	}
	
	private static final String[] stopWords = {
		"a", "and", "are", "as", "at", "be",
		"but", "by", "for", "if", "in",
		"into", "is", "it", "no", "not",
		"of", "on", "or", "s", "such", "t",
		"that", "the", "their", "then",
		"there", "these", "they", "this",
		"to", "was", "will", "with", "",
		"www",
	};
	
	private static final Set<?> stopWords() {
		Set<String> set = new HashSet<String>();
		for (String stopWord : stopWords) {
			set.add(stopWord);
		}
		return set;
	}
	
	@Override
	protected final TokenStreamComponents createComponents(
			String fieldName,
			Reader aReader) {
		
		try {
			KuromojiNormalizeFilter filtered = new KuromojiNormalizeFilter(CharReader.get(aReader));
			KuromojiTokenizer tokenizer = new KuromojiTokenizer(this.tokenizer, filtered);
			TokenStream stream = new KuromojiTypeFilter(true, tokenizer);
			stream = new KuromojiBaseFormFilter(stream);
			stream = new KuromojiKatakanaStemFilter(stream);
			return new TokenStreamComponents(tokenizer, stream);
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		
	}
	
}
