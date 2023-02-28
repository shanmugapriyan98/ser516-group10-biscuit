package com.biscuit.models.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Status {

	NEW(0), READY(1), PLANNED(2), UNPLANNED(3), IN_PROGRESS(4), READY_FOR_TEST(5), DONE(6), BLOCKED(7), ARCHIVED(8);

	private final int value;
	public static List<String> values = new ArrayList<>(
			Arrays.asList("new", "ready", "planned", "unplanned", "in_progress", "ready-for-test", "done", "blocked", "archived"));


	private Status(int value) {
		this.value = value;
	}


	public int getValue() {
		return value;
	}


	public static String allStatus() {
		StringBuilder sb = new StringBuilder();

		for (String s : values) {
			sb.append(s).append(", ");
		}

		return sb.substring(0, sb.length() - 2);
	}

}
