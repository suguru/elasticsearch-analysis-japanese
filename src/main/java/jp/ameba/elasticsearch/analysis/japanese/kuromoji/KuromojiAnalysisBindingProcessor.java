package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import org.elasticsearch.index.analysis.AnalysisModule.AnalysisBinderProcessor;

public class KuromojiAnalysisBindingProcessor extends AnalysisBinderProcessor {

	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("japanese", KuromojiAnalyzerProvider.class);
		analyzersBindings.processAnalyzer("japanese_search", KuromojiSearchAnalyzerProvider.class);
	}
	
}