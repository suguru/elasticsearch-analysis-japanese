package jp.ameba.elasticsearch.analysis.japanese.tiny;

import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.util.Version;

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

public class TinyJapaneseAnalyzer extends StopwordAnalyzerBase {
	
	private static final Version VERSION = Version.LUCENE_35;
	
	public TinyJapaneseAnalyzer() {
		super(VERSION, createStopWords());
	}
	
	private static final String[] STOP_WORDS = {
		"a", "an", "and", "are", "as", "at", "be", "but", "by",
		"for", "if", "in", "into", "is", "it",
		"no", "not", "of", "on", "or", "such",
		"that", "the", "their", "then", "there", "these",
		"they", "this", "to", "was", "will", "with",
		"これ", "それ", "あれ", "この", "その", "あの",
		"ここ", "そこ", "あそこ", "こちら", "どこ", "だれ",
		"なに", "なん", "何", "です", "あります", "おります", "います",
		"は", "が", "の", "に", "を", "で", "え", "へ", "て",
		"から", "まで", "より", "も", "どの", "と", "し", "それで", "しかし"
	};
	
	private static final Set<String> createStopWords() {
		Set<String> set = new HashSet<String>();
		for (String word : STOP_WORDS) {
			set.add(word);
		}
		return set;
	}
	
	@Override
	protected TokenStreamComponents createComponents(
			String fieldName,
			Reader aReader) {
		Tokenizer tokenizer = new TinyJapaneseTokenizer(aReader);
		TokenStream stream = new CJKWidthFilter(tokenizer);
		stream = new TinyJapaneseKatakanaStemFilter(stream);
		stream = new StopFilter(VERSION, stream, getStopwordSet());
		return new TokenStreamComponents(tokenizer, stream);
	}

}
