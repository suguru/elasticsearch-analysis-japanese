package jp.ameba.elasticsearch.analysis.japanese.gosen;

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ja.JapaneseTokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;
import org.elasticsearch.index.settings.IndexSettings;

public class GosenJapaneseTokenizerFactory extends AbstractTokenizerFactory {
	
	@Inject
	public GosenJapaneseTokenizerFactory(
			Index index,
			@IndexSettings Settings indexSettings,
			@Assisted String name,
			@Assisted Settings settings) {
		super(index, indexSettings, name, settings);
	}
	
	@Override
	public Tokenizer create(Reader reader) {
		return new JapaneseTokenizer(reader);
	}
}
