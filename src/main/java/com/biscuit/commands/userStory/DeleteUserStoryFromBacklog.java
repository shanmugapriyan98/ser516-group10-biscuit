package com.biscuit.commands.userStory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.BusinessValue;
import com.biscuit.models.enums.Points;
import com.biscuit.models.enums.Status;

import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;
import com.biscuit.models.services.apiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

public class DeleteUserStoryFromBacklog implements Command {

    ConsoleReader reader = null;
    Project project = null;
    UserStory userStory = new UserStory();


    public DeleteUserStoryFromBacklog(ConsoleReader reader, Project project) {
        super();
        this.reader = reader;
        this.project = project;
    }

    public String getUserStoryId(){
        String userStoryId = "";
        String projectId = new AddUserStoryToBacklog(reader,project).getProjectId();
        String requestDescription = "Get user story Id of: "+userStory.title;
        String endpointPath = "userstories?project="+projectId;
        apiUtility utility = new apiUtility(endpointPath,requestDescription);
        JSONArray jsonArray = utility.apiGET();
        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if(jsonObject.getString("subject").equals(userStory.title))  userStoryId= String.valueOf(jsonObject.getInt("id"));
        }
        if(userStoryId==""){
            throw new RuntimeException("user story Id for "+userStory.title+" is not found");
        }
        return userStoryId;
    }

    public boolean execute() throws IOException {
        String prompt = reader.getPrompt();

        userStory.project = project;
        getTitle();

        reader.setPrompt(prompt);
        String requestDescription = "Delete UserStory";
        String endpointPath = "userstories/"+getUserStoryId();
        apiUtility utility = new apiUtility(endpointPath,requestDescription);
        utility.apiDELETE();
        reader.println(ColorCodes.GREEN + "User Story \"" + userStory.title + "\" has been deleted from the backlog in Taiga!" + ColorCodes.RESET);
        return false;
    }



    private void getTitle() throws IOException {
        reader.setPrompt(ColorCodes.BLUE + "title: " + ColorCodes.RESET);
        userStory.title = reader.readLine();
    }

}
