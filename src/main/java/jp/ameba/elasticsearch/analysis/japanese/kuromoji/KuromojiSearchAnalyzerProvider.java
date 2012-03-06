package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import org.atilika.kuromoji.Tokenizer.Mode;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

/**
 * Kuromoji Analyzer
 * 
 * @author namura_suguru
 */
public class KuromojiSearchAnalyzerProvider extends AbstractIndexAnalyzerProvider<KuromojiAnalyzer> {
	
	private KuromojiAnalyzer analyzer;

	@Inject
	public KuromojiSearchAnalyzerProvider(
			Index index,
			@IndexSettings Settings indexSettings,
			@Assisted String name,
			@Assisted Settings settings) {
		super(index, indexSettings, name, settings);
		this.analyzer = new KuromojiAnalyzer(true, Mode.SEARCH);
	}
	
	@Override
	public final KuromojiAnalyzer get() {
		return analyzer;
	}
}
