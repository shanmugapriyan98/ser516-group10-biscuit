/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.Status;

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
	public Status state;

	/**
	 * Initiated date of Task.
	 */
	public Date initiatedDate = null;

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


	/**
	 * Save tasks to project.
	 */
	public void save() {
		project.save();
	}
}
