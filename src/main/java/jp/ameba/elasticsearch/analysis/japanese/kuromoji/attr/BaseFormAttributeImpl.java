package jp.ameba.elasticsearch.analysis.japanese.kuromoji.attr;

import org.apache.lucene.util.AttributeImpl;

public class BaseFormAttributeImpl extends AttributeImpl implements BaseFormAttribute {
	
	private static final long serialVersionUID = 1779658631803091844L;
	
	private String baseForm;

	@Override
	public String getBaseForm() {
		return baseForm;
	}

	@Override
	public void setBaseForm(String baseForm) {
		this.baseForm = baseForm;
	}

	@Override
	public void clear() {
		this.baseForm = "";
	}

	@Override
	public void copyTo(AttributeImpl target) {
		((BaseFormAttribute) target).setBaseForm(baseForm);
	}

}
