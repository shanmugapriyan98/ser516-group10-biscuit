package com.biscuit.views;

import java.io.IOException;
import java.util.List;

import com.biscuit.Login;
import com.biscuit.commands.help.UserStoryHelp;
import com.biscuit.commands.task.AddTaskToUserStory;
import com.biscuit.commands.task.ChangeStatusTask;
import com.biscuit.commands.task.ListTasks;
import com.biscuit.commands.userStory.ChangeStatusUserStory;
import com.biscuit.commands.userStory.EditUserStory;
import com.biscuit.commands.userStory.ShowUserStory;
import com.biscuit.factories.UserStoryCompleterFactory;
import com.biscuit.models.Task;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.CommandService;
import com.biscuit.models.services.Finder.Tasks;

import jline.console.completer.Completer;

import static org.apache.commons.lang3.StringUtils.isNumeric;

public class UserStoryView extends View {

	UserStory userStory = new UserStory();

	public String []userStoryCmdArr= new String[] {"show", "edit", "tasks", "help"};


	public UserStoryView(){}

	public UserStoryView(View previousView, UserStory userStory) {
		super(previousView, userStory.title);
		this.userStory = userStory;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(UserStoryCompleterFactory.getUserStoryCompleters(userStory));
	}


	@Override
	boolean executeCommand(String[] words) throws Exception {
		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keywords(words);
		} else if (words.length == 3) {
			return execute3Keywords(words);
		}
		return false;
	}


	private boolean execute3Keywords(String[] words) throws Exception {
		if (words[0].equals("go_to")) {
			if (words[1].equals("task")) {
				if (Tasks.getAllNames(userStory).contains(words[2])) {
					Task t = Tasks.find(userStory, words[2]);
					if (t == null) {
						return false;
					}
					t.project = userStory.project;
					userStory.populateTaskDetails();
					t.taskId = userStory.taskDetails.get(words[2]);
					TaskView usv = new TaskView(this, t);
					usv.view();
					return true;
				}
			}
		}
		return false;
	}


	private boolean execute2Keywords(String[] words) throws IOException {
		if (words[0].equals("change_status_to")) {
			if (userStory.statusNames.contains(words[1])) {
				(new ChangeStatusUserStory(userStory, words[1].toUpperCase())).execute();				return true;
			}
		} else if (words[0].equals("add")) {
			if (words[1].equals("task")) {
				(new AddTaskToUserStory(reader, userStory.project, userStory)).execute();
				resetCompleters();

				return true;
			}
		}

		return false;
	}


	private boolean execute1Keyword(String[] words) throws Exception {
		if(!(CommandService.checkCommand(words, userStoryCmdArr))) return true;
		if (words[0].equals("show")) {
			(new ShowUserStory(userStory)).execute();
			return true;
		} else if (words[0].equals("edit")) {
			(new EditUserStory(reader, userStory)).execute();
			this.name = userStory.title;
			updatePromptViews();

			return true;
		} else if (words[0].equals("tasks")) {
			(new ListTasks(userStory, "")).execute();
			return true;
		} else if (words[0].equals("help")) {
			return (new UserStoryHelp()).execute();
		}

		return false;
	}

}
