/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.List;

public class Backlog {

	/**
	 * Project object.
	 */
	public transient Project project;

	/**
	 * List of user story in backlog.
	 */
	public List<UserStory> userStories = new ArrayList<UserStory>();


	/**
	 * Add a user story to backlog.
	 * @param userStory
	 */
	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
	}


	/**
	 * Method to save the backlog.
	 */
	public void save() {
		project.save();
	}
}
