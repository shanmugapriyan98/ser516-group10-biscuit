/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.Status;

public class Sprint {

	public transient Project project;

	// info
	/**
	 * Name of Sprint.
	 */
	public String name;

	/**
	 * Description of Sprint.
	 */
	public String description;

	/**
	 * Sprint state.
	 */
	public Status state;

	/**
	 * Start date of sprint.
	 */
	public Date startDate;

	/**
	 * Due date of sprint.
	 */
	public Date dueDate;

	/**
	 * Assigned effort.
	 */
	public int assignedEffort;

	/**
	 * Sprint velocity.
	 */
	public int velocity;
	public Boolean sprintGoalAchieved;

	/**
	 * List of user stories.
	 */
	public List<UserStory> userStories = new ArrayList<>();

	/**
	 * List of bugs.
	 */
	public List<Bug> bugs;

	/**
	 * List of tests.
	 */
	public List<Test> tests;

	// Completed 0pt 0% ToDo 8pt

	/**
	 * Array of fields.
	 */
	public static String[] fields;

	/**
	 * Array of header fields.
	 */
	public static String[] fieldsAsHeader;

	static {
		fields = new String[] { "name", "description", "state", "start_date", "due_date",
				"assigned_effort", "velocity" };
		fieldsAsHeader = new String[] { "Name", "Description", "State", "Start Date",
				"Due Date", "Assigned Effort", "Velocity" };
	}

	/**
	 * Add user story to sprint.
	 * @param userStory user storu object.
	 */
	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
	}

	/**
	 * Save sprint details.
	 */
	public void save() {
		project.save();
	}

}
