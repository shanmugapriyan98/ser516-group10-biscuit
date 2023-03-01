/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.*;

import com.biscuit.models.enums.Status;
import com.biscuit.models.services.apiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

public class Task {

	/**
	 * Project object.
	 */
	public transient Project project;

	/**
	 * Task title.
	 */
	public String title;

	/**
	 * Task description.
	 */
	public String description;

	/**
	 * State of task.
	 */
	public String state;

	/**
	 * Initiated date of Task.
	 */
	public Date initiatedDate = null;
	public String version;

	/**
	 * Planned date of Task.
	 */
	public Date plannedDate = null;

	/**
	 * Due date of task.
	 */
	public Date dueDate = null;

	/**
	 * Estimated time of task.
	 */
	public float estimatedTime;

	/**
	 * List of fields.
	 */
	public static String[] fields;

	public String taskId;

	/**
	 * Map of status.
	 */
	public HashMap<String,String> taskStatuses =  new HashMap<>();

	public Set<String> statusNames = new HashSet<String>();

	/**
	 * List of bugs.
	 */
	List<Bug> bugs;

	/**
	 * List of Tests.
	 */
	List<Test> tests;
	static {
		fields = new String[8];
		fields[0] = "title";
		fields[1] = "description";
		fields[2] = "state";
		fields[3] = "initiated_date";
		fields[4] = "planned_date";
		fields[5] = "due_date";
		fields[6] = "estimated_time";
	}


	public void updateTaskStatuses(){
		String requestDescription = "Get task Statuses for a project ";
		String endPointPath = "task-statuses?project="+project.projectId;
		apiUtility utility = new apiUtility(endPointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i = 0; i< jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			taskStatuses.put(jsonObject.getString("slug"),String.valueOf(jsonObject.getInt("id")));
			statusNames.add(jsonObject.getString("slug"));
		}
	}
	public Integer getTaskVersion(){
		String requestDescription = "Get version  for a task ";
		String endPointPath = "tasks/"+taskId;
		apiUtility utility = new apiUtility(endPointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		Integer version = null;
		for(int i = 0; i< jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			version = jsonObject.getInt("version");
		}
		return version;
	}
	/**
	 * Save tasks to project.
	 */
	public void save() {
		project.save();
	}
}
