package jp.ameba.elasticsearch.analysis.japanese.gosen;

import org.elasticsearch.index.analysis.AnalysisModule.AnalysisBinderProcessor;

public class GosenAnalysisBinderProcessor extends AnalysisBinderProcessor {

	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("japanese", GosenAnalyzerProvider.class);
	}
	
	@Override
	public void processTokenizers(TokenizersBindings tokenizersBindings) {
		tokenizersBindings.processTokenizer("japanese_sentence", GosenJapaneseTokenizerFactory.class);
	}
	
	@Override
	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
		
		tokenFiltersBindings.processTokenFilter("japanese_basic_form", GosenBasicFormFilterFactory.class);
		tokenFiltersBindings.processTokenFilter("japanese_katakana_stem", GosenKatakanaStemFilterFactory.class);
		tokenFiltersBindings.processTokenFilter("japanese_part_of_speech_keep", GosenPartOfSpeechKeepFilterFactory.class);
		tokenFiltersBindings.processTokenFilter("japanese_part_of_speech_stop", GosenPartOfSpeechStopFilterFactory.class);
		tokenFiltersBindings.processTokenFilter("japanese_puctuation", GosenPunctuationFilterFactory.class);
		tokenFiltersBindings.processTokenFilter("japanese_width", GosenWidthFilterFactory.class);
		
	}
	
}
