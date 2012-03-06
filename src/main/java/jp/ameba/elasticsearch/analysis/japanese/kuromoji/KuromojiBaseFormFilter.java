package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import java.io.IOException;

import jp.ameba.elasticsearch.analysis.japanese.kuromoji.attr.BaseFormAttribute;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

/**
 * 基本形の単語を扱う
 * @author namura_suguru
 */
public class KuromojiBaseFormFilter extends TokenFilter {
	
	// 基本形
	private final BaseFormAttribute baseFormAttr = addAttribute(BaseFormAttribute.class);
	// キーワードフラグ
	private final KeywordAttribute keywordAttr = addAttribute(KeywordAttribute.class);
	// 表記単語
	private final CharTermAttribute termAttr = addAttribute(CharTermAttribute.class);
	
	public KuromojiBaseFormFilter(TokenStream input) {
		super(input);
	}
	
	@Override
	public final boolean incrementToken() throws IOException {
		if (input.incrementToken()) {
			// キーワードは無視
			if (!keywordAttr.isKeyword()) {
				// 基本形を取得
				String base = baseFormAttr.getBaseForm();
				if (base != null) { 
					// 基本形があれば、置き換える
					termAttr.setEmpty().append(baseFormAttr.getBaseForm());
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
