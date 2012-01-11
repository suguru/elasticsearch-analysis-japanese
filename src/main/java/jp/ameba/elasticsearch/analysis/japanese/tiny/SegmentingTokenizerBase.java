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
import java.io.Reader;
import java.text.BreakIterator;
import java.text.CharacterIterator;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 * Breaks text into sentences with a {@link BreakIterator} and
 * allows subclasses to decompose these sentences into words.
 * <p>
 * This can be used by subclasses that need sentence context 
 * for tokenization purposes, such as CJK segmenters.
 * <p>
 * Additionally it can be used by subclasses that want to mark
 * sentence boundaries (with a custom attribute, extra token, position
 * increment, etc) for downstream processing.
 * 
 * @lucene.experimental
 */
public abstract class SegmentingTokenizerBase extends Tokenizer {
  protected static final int BUFFERMAX = 4096;
  protected final char buffer[] = new char[BUFFERMAX];
  /** true length of text in the buffer */
  private int length = 0; 
  /** length in buffer that can be evaluated safely, up to a safe end point */
  private int usableLength = 0; 
  /** accumulated offset of previous buffers for this reader, for offsetAtt */
  protected int offset = 0;
  
  private final BreakIterator iterator;
  private final CharArrayIterator wrapper = new CharArrayIterator();

  private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);

  /**
   * Construct a new SegmenterBase from the given Reader, using
   * the provided BreakIterator for sentence segmentation.
   * <p>
   * Note that you should never share BreakIterators across different
   * TokenStreams, instead a newly created or cloned one should always
   * be provided to this constructor.
   */
  public SegmentingTokenizerBase(Reader input, BreakIterator iterator) {
    super(input);
    this.iterator = iterator;
  }

  @Override
  public final boolean incrementToken() throws IOException {
    if (length == 0 || !incrementWord()) {
      while (!incrementSentence()) {
        refill();
        if (length <= 0) // no more bytes to read;
          return false;
      }
    }
    
    return true;
  }
  
  @Override
  public void reset() throws IOException {
    wrapper.setText(buffer, 0, 0);
    iterator.setText(wrapper);
    length = usableLength = offset = 0;
  }

  @Override
  public void reset(Reader input) throws IOException {
    this.input = input;
    reset();
  }
  
  @Override
  public final void end() throws IOException {
    final int finalOffset = correctOffset(length < 0 ? offset : offset + length);
    offsetAtt.setOffset(finalOffset, finalOffset);
  }  

  /** Returns the last unambiguous break position in the text. */
  private int findSafeEnd() {
    for (int i = length - 1; i >= 0; i--)
      if (isSafeEnd(buffer[i]))
        return i + 1;
    return -1;
  }
  
  /** For sentence tokenization, these are the unambiguous break positions. */
  protected boolean isSafeEnd(char ch) {
    switch(ch) {
      case 0x000D:
      case 0x000A:
      case 0x0085:
      case 0x2028:
      case 0x2029:
        return true;
      default:
        return false;
    }
  }

  /**
   * Refill the buffer, accumulating the offset and setting usableLength to the
   * last unambiguous break position
   */
  private void refill() throws IOException {
    offset += usableLength;
    int leftover = length - usableLength;
    System.arraycopy(buffer, usableLength, buffer, 0, leftover);
    int requested = buffer.length - leftover;
    int returned = input.read(buffer, leftover, requested);
    length = returned < 0 ? leftover : returned + leftover;
    if (returned < requested) /* reader has been emptied, process the rest */
      usableLength = length;
    else { /* still more data to be read, find a safe-stopping place */
      usableLength = findSafeEnd();
      if (usableLength < 0)
        usableLength = length; /*
                                * more than IOBUFFER of text without breaks,
                                * gonna possibly truncate tokens
                                */
    }

    wrapper.setText(buffer, 0, Math.max(0, usableLength));
    iterator.setText(wrapper);
  }

  /**
   * return true if there is a token from the buffer, or null if it is
   * exhausted.
   */
  private boolean incrementSentence() throws IOException {
    if (length == 0) // we must refill the buffer
      return false;
    
    while (true) {
      int start = iterator.current();

      if (start == BreakIterator.DONE)
        return false; // BreakIterator exhausted

      // find the next set of boundaries
      int end = iterator.next();

      if (end == BreakIterator.DONE)
        return false; // BreakIterator exhausted

      setNextSentence(start, end);
      if (incrementWord()) {
        return true;
      }
    }
  }
  
  /** Provides the next input sentence for analysis */
  protected abstract void setNextSentence(int sentenceStart, int sentenceEnd);
  /** Returns true if another word is available */
  protected abstract boolean incrementWord();
  
  /** A CharacterIterator used internally for sentence breaks */
  static class CharArrayIterator implements CharacterIterator {
    private char array[];
    private int start;
    private int index;
    private int length;
    private int limit;

    public char [] getText() {
      return array;
    }
    
    public int getStart() {
      return start;
    }
    
    public int getLength() {
      return length;
    }
    
    /**
     * Set a new region of text to be examined by this iterator
     * 
     * @param array text buffer to examine
     * @param start offset into buffer
     * @param length maximum length to examine
     */
    void setText(final char array[], int start, int length) {
      this.array = array;
      this.start = start;
      this.index = start;
      this.length = length;
      this.limit = start + length;
    }

    public char current() {
      return (index == limit) ? DONE : jreBugWorkaround(array[index]);
    }
    
    // on modern jres, supplementary codepoints with [:Sentence_Break=Format:]
    // trigger a bug in RulebasedBreakIterator! work around this for now
    // by lying about all surrogates to the sentence tokenizer, instead
    // we treat them all as SContinue so we won't break around them.
    final char jreBugWorkaround(char ch) {
      return ch >= 0xD800 && ch <= 0xDFFF ? 0x002C : ch;
    }

    public char first() {
      index = start;
      return current();
    }

    public int getBeginIndex() {
      return 0;
    }

    public int getEndIndex() {
      return length;
    }

    public int getIndex() {
      return index - start;
    }

    public char last() {
      index = (limit == start) ? limit : limit - 1;
      return current();
    }

    public char next() {
      if (++index >= limit) {
        index = limit;
        return DONE;
      } else {
        return current();
      }
    }

    public char previous() {
      if (--index < start) {
        index = start;
        return DONE;
      } else {
        return current();
      }
    }

    public char setIndex(int position) {
      if (position < getBeginIndex() || position > getEndIndex())
        throw new IllegalArgumentException("Illegal Position: " + position);
      index = start + position;
      return current();
    }

    @Override
    public Object clone() {
      CharArrayIterator clone = new CharArrayIterator();
      clone.setText(array, start, length);
      clone.index = index;
      return clone;
    }
  }
}
