/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import java.util.ArrayList;
import java.util.List;

public class Project {

	/**
	 * Name of Project.
	 */
	public String name;

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
