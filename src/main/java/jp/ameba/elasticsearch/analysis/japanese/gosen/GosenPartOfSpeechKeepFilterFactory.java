package jp.ameba.elasticsearch.analysis.japanese.gosen;

import java.util.Set;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ja.JapanesePartOfSpeechKeepFilter;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;

public class GosenPartOfSpeechKeepFilterFactory extends AbstractTokenFilterFactory {
	
	private boolean enablePositionIncrements = false;
	private Set<String> keepTags;
	
	@Inject
	public GosenPartOfSpeechKeepFilterFactory(
			Index index,
			@IndexSettings Settings indexSettings,
			@Assisted String name,
			@Assisted Settings settings) {
		super(index, indexSettings, name, settings);
		
		enablePositionIncrements = settings.getAsBoolean("enablePositionIncrements", false);
		keepTags = SetUtils.toSet(settings.getAsArray("tags"));
		
	}
	
	@Override
	public TokenStream create(TokenStream stream) {
		return new JapanesePartOfSpeechKeepFilter(enablePositionIncrements, stream, keepTags);
	}
}
