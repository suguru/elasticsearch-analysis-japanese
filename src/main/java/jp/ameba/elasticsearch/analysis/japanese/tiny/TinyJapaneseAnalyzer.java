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
	
	private static final String[] STOP_WORDS = 
		("a,able,about,across,after,all,almost,also,am,among,an,and,"
				+ "any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,"
				+ "do,does,either,else,ever,every,for,from,get,got,had,has,have,he,"
				+ "her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,"
				+ "let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,"
				+ "off,often,on,only,or,other,our,own,rather,said,say,says,she,should,"
				+ "since,so,some,than,that,the,their,them,then,there,these,they,this,"
				+ "tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,"
				+ "who,whom,why,will,with,would,yet,you,your").split(",");
	
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
