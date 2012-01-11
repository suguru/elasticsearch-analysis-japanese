package jp.ameba.elasticsearch.analysis.japanese.gosen;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

public class GosenAnalysisJapanesePlugin extends AbstractPlugin {

	@Override
	public String description() {
		return "japanese gosen alaysis module";
	}
	
	@Override
	public String name() {
		return "analysis-japanese";
	}
	
	@Override
	public void processModule(Module module) {
		if (module instanceof AnalysisModule) {
			AnalysisModule analysisModule = (AnalysisModule) module;
			analysisModule.addProcessor(new GosenAnalysisBinderProcessor());
		}
	}
	
}
