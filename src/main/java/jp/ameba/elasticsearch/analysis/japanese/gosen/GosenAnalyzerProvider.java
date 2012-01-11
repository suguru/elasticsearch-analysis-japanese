package jp.ameba.elasticsearch.analysis.japanese.gosen;

import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;

public class GosenAnalyzerProvider extends
		AbstractIndexAnalyzerProvider<JapaneseAnalyzer> {
	
	private JapaneseAnalyzer analyzer;
	
	@Inject
	public GosenAnalyzerProvider(
			Index index,
			@IndexSettings Settings indexSettings,
			@Assisted String name,
			@Assisted Settings settings) {
		super(index, indexSettings, name, settings);
		analyzer = new JapaneseAnalyzer(Version.LUCENE_35);
	}

	@Override
	public JapaneseAnalyzer get() {
		return analyzer;
	}
}
