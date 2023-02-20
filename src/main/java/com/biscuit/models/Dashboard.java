package com.biscuit.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {

	// users
	// contacts
	// groups

	/**
	 * Initializing dashboard instance.
	 */
	private static transient Dashboard thisInstance = null;


	/**
	 * Get Dashboard instance.
	 * @return Dashboard object.
	 */
	public static Dashboard getInstance() {
		return thisInstance;
	}


	/**
	 * Set dashboard instance.
	 * @param r Dashboard object.
	 */
	public static void setInstance(Dashboard r) {
		thisInstance = r;
	}

	/**
	 * String variable for dashboard class.
	 */
	private transient String name = "dashboard";

	/**
	 * List of Projects in system.
	 */
	public List<String> projectsNames = new ArrayList<String>();


	/**
	 * Add a project.
	 * @param p Project object.
	 */
	public void addProject(Project p) {
		projectsNames.add(p.name);
	}


	/**
	 * Remove a project.
	 * @param p project object.
	 */
	public void removeProject(Project p) {
		projectsNames.remove(p.name);
	}


	/**
	 * Method to rename Project.
	 * @param oldName previouys project name.
	 * @param newName new project name.
	 * @throws IOException
	 */
	public void renameProject(String oldName, String newName) throws IOException {
		ModelHelper.rename(oldName, newName);
		projectsNames.remove(oldName);
		projectsNames.add(newName);
	}


	/**
	 * Save the current dashboard.
	 */
	public void save() {
		ModelHelper.save(this, name);
	}


	/**
	 * Load the current dashboard.
	 * @return
	 */
	static public Dashboard load() {
		return ModelHelper.loadDashboard("dashboard");
	}

}
