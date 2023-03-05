package com.biscuit.views;

import com.biscuit.commands.help.SprintHelp;
import com.biscuit.commands.sprint.ChangeStatusSprint;
import com.biscuit.commands.sprint.EditSprint;
import com.biscuit.commands.sprint.ReviewDashboard;
import com.biscuit.commands.sprint.ShowSprint;
import com.biscuit.commands.userStory.AddUserStoryToSprint;
import com.biscuit.commands.userStory.ListUserStories;
import com.biscuit.factories.SprintCompleterFactory;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.CommandService;
import com.biscuit.models.services.Finder.UserStories;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class SprintView extends View {

    Sprint sprint = null;

	public String []sprintCmdArr= new String[] {"show", "edit", "user_stories", "help"};

	public SprintView(){}

    public SprintView(View previousView, Sprint sprint) {
        super(previousView, sprint.name);
        this.sprint = sprint;
    }


    @Override
    void addSpecificCompleters(List<Completer> completers) {
        completers.addAll(SprintCompleterFactory.getSprintCompleters(sprint));
    }


    @Override
    boolean executeCommand(String[] words) throws Exception {
        if (words.length == 1) {
            return execute1Keyword(words);
        } else if (words.length == 2) {
            return execute2Keywords(words);
        } else if (words.length == 4) {
            return execute4Keyword(words);
        }

        return false;
    }


    private boolean execute4Keyword(String[] words) throws IOException {
        if (words[0].equals("list")) {
            if (words[1].equals("user_stories")) {
                if (words[2].equals("filter")) {
                    (new ListUserStories(sprint, "List of user stories for sprint " + sprint.name, true, words[3], false, "")).execute();
                    return true;
                } else if (words[2].equals("sort")) {
                    (new ListUserStories(sprint, "List of user stories for sprint " + sprint.name, false, "", true, words[3])).execute();
                    return true;
                }
            }
        }

        return false;
    }


    private boolean execute2Keywords(String[] words) throws Exception {
        if (words[0].equals("change_status_to")) {
            if (Status.values.contains(words[1])) {
                (new ChangeStatusSprint(sprint, Status.valueOf(words[1].toUpperCase()))).execute();
                return true;
            }
        } else if (words[0].equals("list")) {
            if (words[1].equals("user_stories")) {
                (new ListUserStories(sprint, "List of user stories for sprint " + sprint.name)).execute();
                return true;
            }
        } else if (words[0].equals("add")) {
            if (words[1].equals("user_story")) {
                (new AddUserStoryToSprint(reader, sprint)).execute();
                resetCompleters();

                return true;
            }
        } else if (words[0].equals("go_to")) {
            if (UserStories.getAllNames(sprint).contains(words[1])) {
                UserStory us = UserStories.find(sprint, words[1]);
                if (us == null) {
                    return false;
                }

                UserStoryView usv = new UserStoryView(this, us);
                usv.view();
                return true;
            }
        } else if (words[0].equals("review")) {
            if (words[1].equals("dashboard")) {
                (new ReviewDashboard(reader, sprint, sprint.name + " Review", "Sprint Review Dashboard")).execute();
                return true;
            }
            return true;
        }
        return false;
    }


    private boolean execute1Keyword(String[] words) throws Exception {
        if(!(CommandService.checkCommand(words, sprintCmdArr))) return true;
        if (words[0].equals("show")) {
			(new ShowSprint(sprint)).execute();
			return true;
		} else if (words[0].equals("edit")) {
			(new EditSprint(reader, sprint)).execute();
			this.name = sprint.name;
			updatePromptViews();
            return true;
        } else if (words[0].equals("user_stories")) {
            (new ListUserStories(sprint, "List of user stories for sprint " + sprint.name)).execute();
            return true;
        } else if (words[0].equals("help")) {
            return (new SprintHelp()).execute();
        }
        return false;
    }

}
