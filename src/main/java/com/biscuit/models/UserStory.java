/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import com.biscuit.models.enums.BusinessValue;
import com.biscuit.models.enums.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserStory {

	public transient Project project;
	public Epic epic = new Epic();
	public String title;
	public String description;
	public Status state;
	public BusinessValue businessValue;
	public Date initiatedDate = null;
	public Date plannedDate = null;
	public Date dueDate = null;
	public int points;

	public static String[] fields;

	public List<Task> tasks = new ArrayList<>();
	public List<Bug> bugs = new ArrayList<>();
	public List<Test> tests = new ArrayList<>();

	static {
		fields = new String[] { "title", "epic", "description", "state", "business_value", "initiated_date", "planned_date", "due_date", "tasks", "points" };
	}


	public void save() {
		project.save();
	}

}
