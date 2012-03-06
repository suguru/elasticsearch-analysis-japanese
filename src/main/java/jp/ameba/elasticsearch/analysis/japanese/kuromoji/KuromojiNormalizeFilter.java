package jp.ameba.elasticsearch.analysis.japanese.kuromoji;

import java.io.IOException;

import org.apache.lucene.analysis.BaseCharFilter;
import org.apache.lucene.analysis.CharStream;
import org.apache.lucene.analysis.util.StemmerUtil;

/**
 * NormalizeFilter inspired from CJKWidthFilter
 * 
 * @author namura_suguru
 */
public class KuromojiNormalizeFilter extends BaseCharFilter {

	public KuromojiNormalizeFilter(CharStream in) {
		super(in);
	}

	@Override
	public final int read(char[] cbuf, int off, int len) throws IOException {
		int length = input.read(cbuf, off, len);
		for (int i = 0; i < len; i++) {
			final char ch = cbuf[i];
			if (ch >= 0xFF01 && ch <= 0xFF5E) {
				// Fullwidth ASCII variants
				cbuf[i] -= 0xFEE0;
			} else if (ch >= 0xFF65 && ch <= 0xFF9F) {
				// Halfwidth Katakana variants
				cbuf[i] = KANA_NORM[ch - 0xFF65];
				// Halfwidth Katakana variants
				if ((ch == 0xFF9E || ch == 0xFF9F) && i > 0
						&& combine(cbuf, i, length, ch)) {
					length = StemmerUtil.delete(cbuf, i--, length);
				} else {
					cbuf[i] = KANA_NORM[ch - 0xFF65];
				}
			} 
			if (Character.isUpperCase(cbuf[i])) {
				cbuf[i] = Character.toLowerCase(cbuf[i]);
			} else {
				switch (Character.getType(cbuf[i])) {
				case Character.SPACE_SEPARATOR:
				case Character.LINE_SEPARATOR:
				case Character.PARAGRAPH_SEPARATOR:
				case Character.CONTROL:
				case Character.FORMAT:
				case Character.DASH_PUNCTUATION:
				case Character.START_PUNCTUATION:
				case Character.END_PUNCTUATION:
				case Character.CONNECTOR_PUNCTUATION:
				case Character.OTHER_PUNCTUATION:
				case Character.MATH_SYMBOL:
				case Character.CURRENCY_SYMBOL:
				case Character.MODIFIER_SYMBOL:
				case Character.OTHER_SYMBOL:
				case Character.INITIAL_QUOTE_PUNCTUATION:
				case Character.FINAL_QUOTE_PUNCTUATION:
					cbuf[i] = ' ';
					break;
				default:
					break;
				}
			}
		}
		return length;
	}

	/*
	 * halfwidth kana mappings: 0xFF65-0xFF9D
	 * 
	 * note: 0xFF9C and 0xFF9D are only mapped to 0x3099 and 0x309A as a
	 * fallback when they cannot properly combine with a preceding character
	 * into a composed form.
	 */
	private static final char KANA_NORM[] = new char[] { 0x30fb, 0x30f2,
			0x30a1, 0x30a3, 0x30a5, 0x30a7, 0x30a9, 0x30e3, 0x30e5, 0x30e7,
			0x30c3, 0x30fc, 0x30a2, 0x30a4, 0x30a6, 0x30a8, 0x30aa, 0x30ab,
			0x30ad, 0x30af, 0x30b1, 0x30b3, 0x30b5, 0x30b7, 0x30b9, 0x30bb,
			0x30bd, 0x30bf, 0x30c1, 0x30c4, 0x30c6, 0x30c8, 0x30ca, 0x30cb,
			0x30cc, 0x30cd, 0x30ce, 0x30cf, 0x30d2, 0x30d5, 0x30d8, 0x30db,
			0x30de, 0x30df, 0x30e0, 0x30e1, 0x30e2, 0x30e4, 0x30e6, 0x30e8,
			0x30e9, 0x30ea, 0x30eb, 0x30ec, 0x30ed, 0x30ef, 0x30f3, 0x3099,
			0x309A };

	/* kana combining diffs: 0x30A6-0x30FD */
	private static final byte KANA_COMBINE_VOICED[] = new byte[] { 78, 0, 0, 0,
			0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1,
			0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 1,
			0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 8, 8, 8, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 };

	private static final byte KANA_COMBINE_HALF_VOICED[] = new byte[] { 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0,
			0, 2, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	/** returns true if we successfully combined the voice mark */
	private static boolean combine(char text[], int pos, int length, char ch) {
		final char prev = text[pos - 1];
		if (prev >= 0x30A6 && prev <= 0x30FD) {
			text[pos - 1] += (ch == 0xFF9F) ? KANA_COMBINE_HALF_VOICED[prev - 0x30A6]
					: KANA_COMBINE_VOICED[prev - 0x30A6];
			return text[pos - 1] != prev;
		}
		return false;
	}
}
