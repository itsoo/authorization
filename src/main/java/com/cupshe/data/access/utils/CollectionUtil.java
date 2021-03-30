package com.cupshe.data.access.utils;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

public class CollectionUtil {

	public static Integer min(Collection<Integer> collection) {
		if(!CollectionUtils.isEmpty(collection)) {
			int min = Integer.MAX_VALUE;
			for (Integer c : collection) {
				min = Math.min(min, c);
			}
			return min;
		}
		return null;
	}
	
}
