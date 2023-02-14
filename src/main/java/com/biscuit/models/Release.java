/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biscuit.models.enums.Status;

public class Release {

	/**
	 * Project object.
	 */
	public transient Project project;

	// info
	/**
	 * Name of release.
	 */
	public String name;

	/**
	 * Description of release.
	 */
	public String description;

	/**
	 * State of release.
	 */
	public Status state;

	/**
	 * Startdate of release.
	 */
	public Date startDate;

	/**
	 *  Due Date of release.
	 */
	public Date dueDate;

	/**
	 * Assigned effort for release.
	 */
	public int assignedEffort;

	/**
	 * List of sprints.
	 */
	public List<Sprint> sprints = new ArrayList<>();

	public List<Bug> bugs;
	public List<Test> tests;

	// Completed 0pt 0% ToDo 8pt

	/**
	 * Array to store fields.
	 */
	public static String[] fields;

	/**
	 * Array to store header fields.
	 */
	public static String[] fieldsAsHeader;
	static {
		fields = new String[] { "name", "description", "state",
				"start_date", "due_date", "assigned_effort" };
		fieldsAsHeader = new String[] { "Name", "Description", "State",
				"Start Date", "Due Date", "Assigned Effort" };
	}

	/**
	 * Save a release.
	 */
	public void save() {
		project.save();
	}
}
