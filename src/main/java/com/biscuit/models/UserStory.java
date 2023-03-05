/*
	Author: Hamad Al Marri;
 */

package com.biscuit.models;

import com.biscuit.models.enums.BusinessValue;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.apiUtility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

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
    public String state;

    public String version;

    public String userStoryId;

    /**
     * Map of status.
     */
    public HashMap<String,String> taskStatuses =  new HashMap<>();

    public Set<String> statusNames = new HashSet<String>();

    /**
     * Business value per user story.
     */
    public int businessValue;

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

    public HashMap<String,String> userStoryStatuses =  new HashMap<>();
    /**
     * Story points per user story.
     */
    public int points;

    public String tags;


    /**
     * Array for fields.
     */
    public static String[] fields;

    public List<Task> tasks = new ArrayList<>();
    public List<Bug> bugs = new ArrayList<>();
    public List<Test> tests = new ArrayList<>();

    /**
     * List array to store comments.
     */
    public List<String> comments;

    static {
        fields = new String[]{"title", "description", "state", "business_value", "initiated_date", "planned_date", "due_date", "tags", "tasks", "points", "comments", "review demo comments"};
    }

    public void updateUserStoryStatuses(){
        String requestDescription = "Get User story Statuses for a project ";
        String endPointPath = "userstory-statuses?project="+project.projectId;
        apiUtility utility = new apiUtility(endPointPath,requestDescription);
        JSONArray jsonArray = utility.apiGET();
        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            userStoryStatuses.put(jsonObject.getString("slug"),String.valueOf(jsonObject.getInt("id")));
            statusNames.add(jsonObject.getString("slug"));
        }
    }
    public Integer getUserStoryVersion(){
        String requestDescription = "Get version  for a user story ";
        String endPointPath = "userstories/"+userStoryId;
        apiUtility utility = new apiUtility(endPointPath,requestDescription);
        JSONArray jsonArray = utility.apiGET();
        Integer version = null;
        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            version = jsonObject.getInt("version");
        }
        return version;
    }

    /**
     * Method to save user story.
     */
    public void save() {
        project.save();
    }

}
