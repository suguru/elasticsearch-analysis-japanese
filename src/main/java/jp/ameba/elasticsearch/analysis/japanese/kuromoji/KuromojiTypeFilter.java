package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import java.io.IOException;

import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * 品詞タイプによるストッパー
 */
public class KuromojiTypeFilter extends FilteringTokenFilter {
	
	private char[] stopPrefixes = {
		'接', // 接続,接頭
		'助', // 助詞,助動詞
		'記', // 記号
		'そ', // その他
		'フ', // フィラー
		'非', // 非言語音
	};
	
	private String[] stopTags = {
			"動詞,非自立"
	};
	
	public KuromojiTypeFilter(
			boolean enablePositionIncrements,
			TokenStream input) {
		super(enablePositionIncrements, input);
	}

	TypeAttribute typeAttr = addAttribute(TypeAttribute.class);

	@Override
	protected final boolean accept() throws IOException {
		String type = typeAttr.type();
		char first = type.charAt(0);
		for (int i = 0; i < stopPrefixes.length; i++) {
			if (first == stopPrefixes[i]) {
				return false;
			}
		}
		for (int i = 0; i < stopTags.length; i++) {
			if (type.startsWith(stopTags[i])) {
				return false;
			}
		}
		return true;
	}
	
}
