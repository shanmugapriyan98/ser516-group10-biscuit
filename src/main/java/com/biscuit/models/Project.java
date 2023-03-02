/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import com.biscuit.Login;
import com.biscuit.models.services.apiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Project {

	/**
	 * Name of Project.
	 */
	public String name;

	public int id;

	public String slug_name;

	public HashMap<String,Integer> sprintDetails = new HashMap<>();

	public HashMap<String,Integer> userStoryDetails = new HashMap<>();

	public List<Integer> memberIDs =  new ArrayList<>();


	/**
	 * Description of project.
	 */
	public String description;

	/**
	 * Backlog for project.
	 */
	public Backlog backlog = new Backlog();

	/**
	 * List of releases for project.
	 */
	public List<Release> releases = new ArrayList<>();

	/**
	 * List of sprints for project.
	 */
	public List<Sprint> sprints = new ArrayList<>();
	public List<Epic> epics = new ArrayList<>();
	public String projectId;

	public void populateDetails() {
		if(!name.isEmpty()){
			populateProjectDetails();
			populateSprintDetails();
			populateUserStoryDetails();
		}
	}

	private void populateUserStoryDetails() {
		String requestDescription = "Get userstory details by project ID";
		String endpointPath = "userstories?project=" + id;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i=0;i< jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String userStoryName = jsonObject.getString("subject");
			Integer userstoryId = jsonObject.getInt("id");
			userStoryDetails.put(userStoryName,userstoryId);
		}
	}

	private void populateSprintDetails() {
		String requestDescription = "Get sprint details by project ID";
		String endpointPath = "milestones?project=" + id;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i=0;i< jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String sprintName = jsonObject.getString("name");
			Integer sprintId = jsonObject.getInt("id");
			sprintDetails.put(sprintName,sprintId);
		}
	}

	public void populateProjectDetails() {
		String requestDescription = "Get Project details by member ID";
		String endpointPath = "projects?member=" + Login.getInstance().memberId;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i=0;i< jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.getString("name").equals(name)) {
				description = jsonObject.getString("description");
				id = jsonObject.getInt("id");
				slug_name = jsonObject.getString("slug");
			}
		}
	}


	/**
	 * Save project.
	 */
	public void save() {
		ModelHelper.save(this, name);
	}


	/**
	 * Delete project.
	 */
	public void delete() {
		ModelHelper.delete(name);
	}


	/**
	 * Load a project by name.
	 * @param name Name of Project.
	 * @return Project object.
	 */
	static public Project load(String name) {
		return ModelHelper.loadProject(name);
	}


	/**
	 * Update Children References.
	 */
	public void updateChildrenReferences() {

		this.backlog.project = this;

		for (Release r : releases) {
			r.project = this;
			updateSprintReferences(r.sprints);
		}

		updateSprintReferences(sprints);
		updateUserStoryReferences(backlog.userStories);
	}


	/**
	 * Update Sprint References.
	 * @param sprints List of sprints.
	 */
	private void updateSprintReferences(List<Sprint> sprints) {
		for (Sprint s : sprints) {
			s.project = this;
			updateUserStoryReferences(s.userStories);
		}
	}

	//TODO updateEpicReferences function

	/**
	 * Update User Story References.
	 * @param userStories user stories list.
	 */
	private void updateUserStoryReferences(List<UserStory> userStories) {
		for (UserStory us : userStories) {
			us.project = this;

			for (Task t : us.tasks) {
				t.project = this;
			}

			// for (Bug b : us.bugs) {
			// b.project = this;
			// }

			// for (Test t : us.tests) {
			// t.project = this;
			// }
		}
	}


	/**
	 * Add release to Project.
	 * @param r Release object.
	 */
	public void addRelease(Release r) {
		releases.add(r);
	}


	/**
	 * Add a sprint to project.
	 * @param s Sprint object.
	 */
	public void addSprint(Sprint s) {
		sprints.add(s);
	}


	public void addEpic(Epic e) {
		epics.add(e);
	}


	@Override
	/**
	 * toString method for this class.
	 */
	public String toString() {
		return "project name: " + name + "\n\ndescription:-\n" + description;
	}

}