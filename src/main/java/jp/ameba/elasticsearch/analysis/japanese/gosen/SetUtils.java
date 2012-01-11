package jp.ameba.elasticsearch.analysis.japanese.gosen;

import java.util.HashSet;
import java.util.Set;

class SetUtils {

	static final Set<String> toSet(String[] array) {
		Set<String> set = new HashSet<String>();
		if (array == null) {
			return set;
		}
		for (int i = 0; i < array.length; i++) {
			set.add(array[i]);
		}
		return set;
	}
	
}
