package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

public class KuromojiAnalysisJapanesePlugin extends AbstractPlugin {

	@Override
	public String description() {
		return "japanese alaysis module uses Kuromoji tokenizer";
	}
	
	@Override
	public String name() {
		return "analysis-japanese";
	}
	
	@Override
	public void processModule(Module module) {
		if (module instanceof AnalysisModule) {
			AnalysisModule analysisModule = (AnalysisModule) module;
			analysisModule.addProcessor(new KuromojiAnalysisBindingProcessor());
		}
	}
	
}
