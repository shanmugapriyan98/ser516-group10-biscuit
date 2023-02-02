package com.biscuit.views;

import java.io.IOException;
import java.util.List;

import com.biscuit.commands.epic.ListEpics;
import com.biscuit.commands.help.SprintHelp;
import com.biscuit.commands.help.EpicHelp;
import com.biscuit.commands.sprint.ChangeStatusSprint;
import com.biscuit.commands.sprint.EditSprint;
import com.biscuit.commands.sprint.ShowSprint;
import com.biscuit.commands.epic.EditEpic;
import com.biscuit.commands.epic.ShowEpic;
import com.biscuit.commands.userStory.AddUserStoryToSprint;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.EpicCompleterFactory;
import com.biscuit.factories.SprintCompleterFactory;
import com.biscuit.models.Epic;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.Finder.UserStories;

import jline.console.completer.Completer;

public class EpicView extends View {

	Epic epic = null;


	public EpicView(View previousView, Epic epic) {
		super(previousView, epic.name);
		this.epic = epic;
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(EpicCompleterFactory.getEpicCompleters(epic));
	}


	@Override
	boolean executeCommand(String[] words) throws IOException {
		if (words.length == 1) {
			return execute1Keyword(words);
		} 

		return false;
	}






	private boolean execute1Keyword(String[] words) throws IOException {
		if (words[0].equals("show")) {
			(new ShowEpic(epic)).execute();
			return true;
		} else if (words[0].equals("edit")) {
			(new EditEpic(reader, epic)).execute();
			this.name = epic.name;
			updatePromptViews();

			return true;
		} else if (words[0].equals("help")) {
			return (new EpicHelp()).execute();
		}
		return false;
	}

}
