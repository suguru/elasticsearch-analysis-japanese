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

import java.io.Reader;
import java.text.BreakIterator;
import java.util.Locale;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

import static jp.ameba.elasticsearch.analysis.japanese.tiny.TinyJapaneseSegmenterConstants.*;

/**
 * Tokenizer for Japanese text based on 
 * <a href="http://www.chasen.org/~taku/software/TinySegmenter/">TinySegmenter</a>.
 * <p>
 * This tokenizer uses no dictionary for segmentation, instead the algorithm is
 * machine-learned. Text is segmented by sliding a six-character window across
 * the sentence and combining the cost from features such as n-grams of characters
 * and character categories.
 * <p>
 * Some modifications from the original algorithm:
 * <ul>
 *   <li>Runs of digits are emitted as a single token, e.g. "1234"
 *   <li>Punctuation tokens such as "." are not produced 
 * </ul>
 * Neither of these change the segmentation, for example, punctuation is still 
 * taken into context when computing the algorithm, just not produced as tokens.
 */
public final class TinyJapaneseTokenizer extends SegmentingTokenizerBase {
  /** three context state variables, indicates if we broke at n-3,n-2,n-1 */
  private int p1, p2, p3;
  
  /** sentence being analyzed */
  private int sentenceStart, sentenceEnd;
  
  /** current word boundary info */
  private int start, end;

  /** we use a japanese sentence break iterator */
  private static final BreakIterator proto = BreakIterator.getSentenceInstance(Locale.JAPAN);
  
  private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

  /** Create a tokenizer working on the supplied reader */
  public TinyJapaneseTokenizer(Reader input) {
    super(input, (BreakIterator) proto.clone());
  }

  @Override
  protected void setNextSentence(int sentenceStart, int sentenceEnd) {
    p1 = p2 = p3 = PU;
    this.sentenceStart = this.start = this.end = sentenceStart;
    this.sentenceEnd = sentenceEnd;
  }

  @Override
  protected boolean incrementWord() {
    if (end >= sentenceEnd)
      return false;
    
    start = end;
    for (end++; end < sentenceEnd; end++) {
      if (isBoundary(end)) {
        switch(tokenStatus(start, end)) {
          case SKIP:
            start = end;
            continue;
          case CONTINUE:
            continue;
          case BREAK:
            return outputToken();
        }
      }
    }

    // last token of the string, return it unless its a skip token
    return tokenStatus(start, end) == TokenStatus.SKIP ? false : outputToken();
  }

  private boolean outputToken() {
    clearAttributes();
    termAtt.copyBuffer(buffer, start, end-start);
    offsetAtt.setOffset(correctOffset(offset+start), correctOffset(offset+end));
    return true;
  }
  
  private static enum TokenStatus { SKIP, CONTINUE, BREAK };

  /** 
   * ok, we found a break from the algorithm, but if its a punctuation token,
   * we want to SKIP it, if its a digit or surrogate pair we want to CONTINUE.
   */
  private TokenStatus tokenStatus(int start, int end) {
    final char ch = buffer[start];
    if (Character.isLetter(ch)) {
      return TokenStatus.BREAK;
    } else if (Character.isHighSurrogate(ch)) { // never break between surrogate pair
      return TokenStatus.CONTINUE;
    } else if (Character.isDigit(ch)) { // never break between runs of digits
      return (end < sentenceEnd && Character.isDigit(buffer[end])) ? TokenStatus.CONTINUE : TokenStatus.BREAK;
    } else { // skip punctuation-only tokens
      return TokenStatus.SKIP;
    }
  }

  /** returns the character (or special sentence start/end marker) at this position */
  private int charAt(int pos) {
    if (pos == sentenceStart-3)
      return B3;
    else if (pos == sentenceStart-2)
      return B2;
    else if (pos == sentenceStart-1)
      return B1;
    else if (pos == sentenceEnd)
      return E1;
    else if (pos == sentenceEnd+1)
      return E2;
    else if (pos == sentenceEnd+2)
      return E3;
    else
      return buffer[pos];
  }
  
  /** true if there is a boundary at pos */
  private boolean isBoundary(int pos) {
    final int c1 = charAt(pos-3);
    final int t1 = charType(c1);
    final int c2 = charAt(pos-2);
    final int t2 = charType(c2);
    final int c3 = charAt(pos-1);
    final int t3 = charType(c3);
    final int c4 = charAt(pos);
    final int t4 = charType(c4);
    final int c5 = charAt(pos+1);
    final int t5 = charType(c5);
    final int c6 = charAt(pos+2);
    final int t6 = charType(c6);
    
    final int score = BIAS
      // unigram context
      + up1(p1) + up2(p2) + up3(p3)
      // bigram context
      + bp1(p1, p2) + bp2(p2, p3)
      // unigram char
      + uw1(c1) + uw2(c2) + uw3(c3) + uw4(c4) + uw5(c5) + uw6(c6)
      // bigram char
      + bw1(c2, c3) + bw2(c3, c4) + bw3(c4, c5)
      // trigram char
      + tw1(c1, c2, c3) + tw2(c2, c3, c4) + tw3(c3, c4, c5) + tw4(c4, c5, c6)
      // unigram category
      + uc1(t1) + uc2(t2) + uc3(t3) + uc4(t4) + uc5(t5) + uc6(t6)
      // bigram category
      + bc1(t2, t3) + bc2(t3, t4) + bc3(t4, t5)
      // trigram category
      + tc1(t1, t2, t3) + tc2(t2, t3, t4) + tc3(t3, t4, t5) + tc4(t4, t5, t6)
      // unigram context+category
      + uq1(p1, t1) + uq2(p2, t2) + uq3(p3, t3)
      // bigram context+category
      + bq1(p2, t2, t3) + bq2(p2, t3, t4) + bq3(p3, t2, t3) + bq4(p3, t3, t4)
      // trigram context+category
      + tq1(p2, t1, t2, t3) + tq2(p2, t2, t3, t4) + tq3(p3, t1, t2, t3) + tq4(p3, t2, t3, t4);
    
    // shift contextual state variables back one position
    p1 = p2;
    p2 = p3;
    
    if (score > 0) {
      p3 = PB; // contextual state for n-1 was a break
      return true;
    } else {
      p3 = PO; // not a break
      return false;
    }
  }
}
