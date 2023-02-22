package com.biscuit.models;

//import jdk.jpackage.internal.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biscuit.Login;
import com.biscuit.models.services.apiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

public class Dashboard {

	// users
	// contacts
	// groups

	/**
	 * Initializing dashboard instance.
	 */
	private static transient Dashboard thisInstance = null;

/*	public Dashboard() {
		getProjectNames();
	}
 	public void getProjectNames(){
		String requestDescription = "Get project names";
		String endpointPath = "projects?member="+ Login.getInstance().memberId;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i = 0; i< jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String name = jsonObject.getString("name");
			projectsNames.add(name);
		}
	}*/
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
