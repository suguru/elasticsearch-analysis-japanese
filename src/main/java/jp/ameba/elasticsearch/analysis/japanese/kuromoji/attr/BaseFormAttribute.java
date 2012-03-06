package jp.ameba.elasticsearch.analysis.japanese.kuromoji.attr;

import org.apache.lucene.util.Attribute;

public interface BaseFormAttribute extends Attribute {
	
	String getBaseForm();

	void setBaseForm(String baseForm);
	
}
