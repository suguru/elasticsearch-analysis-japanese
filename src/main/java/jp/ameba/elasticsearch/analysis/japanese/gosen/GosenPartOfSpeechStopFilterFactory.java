package jp.ameba.elasticsearch.analysis.japanese.gosen;

import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapanesePartOfSpeechStopFilter;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

public class GosenPartOfSpeechStopFilterFactory extends AbstractTokenFilterFactory {
	
	private boolean enablePositionIncrements = false;
	private Set<String> stopTags;
	
	@Inject
	public GosenPartOfSpeechStopFilterFactory(
			Index index,
			@IndexSettings Settings indexSettings,
			@Assisted String name,
			@Assisted Settings settings) {
		super(index, indexSettings, name, settings);
		
		enablePositionIncrements = settings.getAsBoolean("enablePositionIncrements", false);
		stopTags = SetUtils.toSet(settings.getAsArray("tags"));
		
	}
	
	@Override
	public TokenStream create(TokenStream stream) {
		return new JapanesePartOfSpeechStopFilter(enablePositionIncrements, stream, stopTags);
	}
}
