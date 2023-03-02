package com.biscuit.models.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BusinessValue {

	MUST_HAVE(0), GREAT(1), GOOD(2), AVERAGE(3), NICE_TO_HAVE(4);

	private final int value;
	public static List<String> values = new ArrayList<>(
			Arrays.asList("nice_to_have", "average", "good", "great", "must_have"));

//	private final int value;
//	_0PT, _1PT, _2PT, _3PT, _5PT, _8PT, _13PT, _21PT, _34PT;
//
//	public static List<String> values = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "5", "8", "13", "21"));

	private BusinessValue(int value) {
		this.value = value;
	}


	public int getValue() {
		return value;
	}
}
