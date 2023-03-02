/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import com.biscuit.models.enums.BusinessValue;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.apiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserStory {

    /**
     * Project object to store current project details.
     */

    public transient Project project;
	public Epic epic = new Epic();
	  public String reviewDemoComments="";
    
    /**
     * Title of the project.
     */
    public String title;

    /**
     * Description of the project.
     */
    public String description;

    /**
     * State of the user story.
     */
    public Status state;


    /**
     * Business value per user story.
     */
    public BusinessValue businessValue;

    /**
     * Initial date per user story.
     */
    public Date initiatedDate = null;

    /**
     * Planned date per user story.
     */
    public Date plannedDate = null;

    /**
     * Due Date per user story.
     */
    public Date dueDate = null;

    public String usId;

    /**
     * Story points per user story.
     */
    public int points;


    /**
     * Array for fields.
     */
    public static String[] fields;

    public List<Task> tasks = new ArrayList<>();
    public List<Bug> bugs = new ArrayList<>();
    public List<Test> tests = new ArrayList<>();
    public HashMap<String,String> taskDetails = new HashMap<>();

    /**
     * List array to store comments.
     */
    public List<String> comments;

    static {
        fields = new String[]{"title", "description", "state", "business_value", "initiated_date", "planned_date", "due_date", "tasks", "points", "comments", "review demo comments"};
    }

    public void populateTaskDetails(){
        String requestDescription = "Get task details by userstory ID";
        String endpointPath = "tasks?user_story=" + usId;
        apiUtility utility = new apiUtility(endpointPath,requestDescription);
        JSONArray jsonArray = utility.apiGET();
        for(int i=0;i< jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String taskName = jsonObject.getString("subject");
            Integer taskId = jsonObject.getInt("id");
            taskDetails.put(taskName, String.valueOf(taskId));
        }
    }
    /**
     * Method to save user story.
     */
    public void save() {
        project.save();
    }

}
