package com.biscuit.models;

import java.io.File;

public class Config {

	/**
	 * Home directory.
	 */
	private static String homeDir;

	/**
	 * Project directory.
	 */
	public static String projectsDir;

	static {
		homeDir = System.getProperty("user.home");
		projectsDir = homeDir + "/biscuit";
		File f = new File(projectsDir);

		if (!f.exists()) {
			f.mkdir();
		}
	}

}
