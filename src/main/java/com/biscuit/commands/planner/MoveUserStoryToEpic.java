package com.biscuit.commands.planner;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.commands.userStory.ShowUserStory;
import com.biscuit.models.Epic;
import com.biscuit.models.Project;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.Finder.Epics;
import com.biscuit.models.services.Finder.UserStories;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.Date;

public class MoveUserStoryToEpic implements Command {

    ConsoleReader reader = null;
    Project project = null;
    private String userStoryName;
    private String epicName;


    public MoveUserStoryToEpic(ConsoleReader reader, Project project, String userStoryName, String epicName) {
        super();
        this.reader = reader;
        this.project = project;
        this.userStoryName = userStoryName;
        this.epicName = epicName;
    }


    @Override
    public boolean execute() throws IOException {

        // get the user story
        UserStory us = UserStories.find(project.backlog, userStoryName);

        // get epic
        Epic e = Epics.find(project, epicName);

        // get release of this epic if any
        //Release r = Releases.findContains(project, e.name);

        if (us == null ) {
            return false;
        }
        // create a new epic if the input epic does not exist
        if  (e == null) {
            e = new Epic();
            e.name = epicName;
            project.addEpic(e);
        }

        // remove it from backlog
        project.backlog.userStories.remove(us);

        // add it to sprint
        e.addUserStory(userStoryName);
        project.save();

        reader.println(ColorCodes.GREEN + "User Story \"" + userStoryName + "\" has been added to epic " + epicName + "!" + ColorCodes.RESET);

        return true;
    }

}
