package com.biscuit.views;

import com.biscuit.commands.epic.ListEpics;
import com.biscuit.commands.help.PlannerHelp;
import com.biscuit.commands.planner.*;
import com.biscuit.commands.release.ListReleases;
import com.biscuit.commands.sprint.ListSprints;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.PlannerCompleterFactory;
import com.biscuit.models.Project;
import com.biscuit.models.services.Finder.Releases;
import com.biscuit.models.services.Finder.Sprints;
import com.biscuit.models.services.Finder.UserStories;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class PlannerView extends View {

	Project project = null;


	public PlannerView(View previousView, Project p) {
		super(previousView, "planner");
		this.project = p;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(PlannerCompleterFactory.getPlannerCompleters(project));
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {
		if (words.length == 1) {
			return execute1Keywords(words);
		} else if (words.length == 2) {
			return execute2Keywords(words);
		} else if (words.length == 3) {
			return execute3Keywords(words);
		} else if (words.length == 4) {
			return execute4Keywords(words);
		}
		return false;
	}


	private boolean execute1Keywords(String[] words) throws IOException {
		if (words[0].equals("releases")) {
			(new ListReleases(project, "Releases")).execute();
			return true;
		} else if (words[0].equals("sprints")) {
			(new ListSprints(project, "Sprints")).execute();
			return true;
		} else if (words[0].equals("epics")) {
			(new ListEpics(project, "Epics")).execute();
			return true;
		} else if (words[0].equals("backlog")) {
			(new ListUserStories(project.backlog, "Backlog (User Stories)")).execute();
			return true;
		} else if (words[0].equals("user_stories")) {
			(new ListUserStories(UserStories.getAll(project), "All User Stories")).execute();
			return true;
		} else if (words[0].equals("plan")) {
			(new ShowPlan(project)).execute();
			return true;
		} else if (words[0].equals("help")) {
			return (new PlannerHelp()).execute();
		}

		return false;
	}


	private boolean execute2Keywords(String[] words) throws IOException {
		if (words[0].equals("show")) {
			if (words[1].equals("releases")) {
				(new ListReleases(project, "Releases")).execute();
				return true;
			} else if (words[1].equals("sprints")) {
				(new ListSprints(project, "Sprints")).execute();
				return true;
			} else if (words[0].equals("epics")) {
				(new ListEpics(project, "Epics")).execute();
				return true;
			} else if (words[1].equals("backlog")) {
				(new ListUserStories(project.backlog, "Backlog (User Stories)")).execute();
				return true;
			} else if (words[1].equals("user_stories")) {
				(new ListUserStories(UserStories.getAll(project), "All User Stories")).execute();
				return true;
			} else if (words[1].equals("plan")) {
				(new ShowPlan(project)).execute();
				return true;
			}
		} else if (words[0].equals("unplan")) {
			if (UserStories.getPlannedNames(project).contains(words[1])) {
				if ((new UnplanUserStory(reader, project, words[1])).execute()) {
					resetCompleters();
					return true;
				} else {
					return false;
				}
			} else if (Sprints.getPlannedNames(project).contains(words[1])) {
				if ((new UnplanSprint(reader, project, words[1])).execute()) {
					resetCompleters();
					return true;
				} else {
					return false;
				}
			} else if (words[1].equals("all")) {
				if ((new UnplanAll(reader, project)).execute()) {
					resetCompleters();
					return true;
				} else {
					return false;
				}
			}
		} else if (words[0].equals("plan")) {
			if (words[1].equals("details")) {
				(new ShowPlanDetails(project)).execute();
				return true;
			}
		}

		return false;
	}


	private boolean execute3Keywords(String[] words) throws IOException {
		if (words[0].equals("show")) {
			if (words[1].equals("plan")) {
				if (words[2].equals("details")) {
					(new ShowPlanDetails(project)).execute();
					return true;
				}
			}
		}
		return false;
	}


	private boolean execute4Keywords(String[] words) throws IOException {
		if (words[0].equals("move")) {
			if (UserStories.getAllNames(project.backlog).contains(words[1]) && Sprints.getAllNames(project).contains(words[3])) {
				if ((new MoveUserStoryToSprint(reader, project, words[1], words[3])).execute()) {
					resetCompleters();
					return true;
				} else {
					return false;
				}
			} else if (Sprints.getAllNames(project).contains(words[1]) && Releases.getAllNames(project).contains(words[3])) {
				if ((new MoveSprintToRelease(reader, project, words[1], words[3])).execute()) {
					resetCompleters();
					return true;
				} else {
					return false;
				}
			} /*else if (Epics.getAllNames(project).contains(words[3]) && UserStories.getAllNames(project.backlog).contains(words[1])){
				if((new MoveUserStoryToEpic(reader, project, words[1], words[3]).execute())) {
					System.out.println("here");
					resetCompleters();
					return true;
				} else {
					return false;
				}
			}*/
			else if(words[2].equals("epic")  && UserStories.getAllNames(project.backlog).contains(words[1])){
				if((new MoveUserStoryToEpic(reader, project, words[1], words[3]).execute())) {
					resetCompleters();
					return true;
				} else {
					return false;
				}
			}
			System.out.println("out");
		}
		return false;
	}
}
