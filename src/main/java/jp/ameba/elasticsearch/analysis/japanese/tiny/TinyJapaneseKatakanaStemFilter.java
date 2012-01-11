package jp.ameba.elasticsearch.analysis.japanese.tiny;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.analysis.KeywordMarkerFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;

/**
 * Convert a katakana word to a normalized form by stemming a 
 * final KATAKANA-HIRAGANA PROLONGED SOUND MARK (U+30FC) at the end of the
 * word. 
 * <p>
 * In general, most Japanese full-text search engines use more complicated
 * methods which need dictionaries, which can be better than this filter in
 * quality, but need a well-tuned dictionary. In contract, this filter is
 * simple and maintenance-free.
 * <p>
 * Note: This filter does not support half-width katakana characters, so you
 * should convert them with {@link CJKWidthFilter} first.
 * <p>
 * To prevent terms from being stemmed use an instance of
 * {@link KeywordMarkerFilter} or a custom {@link TokenFilter} that sets
 * the {@link KeywordAttribute} before this {@link TokenStream}.
 */
public final class TinyJapaneseKatakanaStemFilter extends TokenFilter {
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final KeywordAttribute keywordAtt = addAttribute(KeywordAttribute.class);

  public TinyJapaneseKatakanaStemFilter(TokenStream in) {
    super(in);
  }

  @Override
  public boolean incrementToken() throws IOException {
    if (input.incrementToken()) {
      if (!keywordAtt.isKeyword()) {
        final char buffer[] = termAtt.buffer();
        final int length = termAtt.length();
        if (length > 3 && buffer[length-1] == '\u30FC' && isKatakanaString(buffer, length-1)) {
          termAtt.setLength(length-1);
        }
      }
      return true;
    } else {
      return false;
    }
  }
  
  private static boolean isKatakanaString(char s[], int length) {
    for (int i = 0; i < length; i++) {
      final char c = s[i];
      if (c < '\u3099' || c > '\u30FF' || c == '\u309F') // not katakana or (semi)-voiced sound marks
        return false;
    }
    return true;
  }
}
