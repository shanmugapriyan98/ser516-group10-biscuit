package com.biscuit.commands.userStory;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.DateService;
import com.biscuit.models.services.apiUtility;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShowUserStory implements Command {

	UserStory us;

	Login login =  Login.getInstance();
	String authToken = login.authToken;
	private final OkHttpClient httpClient = new OkHttpClient();


	public ShowUserStory(UserStory us) throws Exception {
		super();
		this.us = us;
	}


	@Override
	public boolean execute() throws IOException {

		System.out.println(ColorCodes.BLUE + "title: " + ColorCodes.RESET + us.title);
		System.out.println(ColorCodes.BLUE + "description: ");
		System.out.println(ColorCodes.RESET + us.description);
		System.out.println(ColorCodes.BLUE + "state: " + ColorCodes.RESET + us.state);
		System.out.println(ColorCodes.BLUE + "business value: " + ColorCodes.RESET + us.businessValue);
		System.out.println(ColorCodes.BLUE + "initiated date: " + ColorCodes.RESET
				+ DateService.getDateAsString(us.initiatedDate));
		System.out.println(
				ColorCodes.BLUE + "planned date: " + ColorCodes.RESET +  ((us.plannedDate == null) ? "NULL" : DateService.getDateAsString(us.plannedDate)));
		System.out.println(ColorCodes.BLUE + "due date: " + ColorCodes.RESET +  ((us.dueDate == null) ? "NULL" : DateService.getDateAsString(us.dueDate)));
		System.out.println(ColorCodes.BLUE + "points: " + ColorCodes.RESET + us.points);
		System.out.println(ColorCodes.BLUE + "comments: " + ColorCodes.RESET + us.comments);
		System.out.println(ColorCodes.BLUE + "Tasks: ");
		if(us.taskDetails.size()==0) System.out.println(ColorCodes.RESET + "No Tasks Available");
		else {
			for(Map.Entry<String, String> taskEntry : us.taskDetails.entrySet()){
				System.out.println(ColorCodes.RESET + taskEntry.getKey());
			}
		}
		System.out.println();
		return true;
	}

	public UserStory fetchUserStoryByNumber(int projectID, int usNumber) throws Exception {
		UserStory userStory = null;
		List<String> tasks = new ArrayList<>();
		String requestDescription = "Show user story";
		String endpointPath = "userstories/by_ref?ref="+usNumber+"&project="+projectID;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONObject jsonObject = utility.apiGET().getJSONObject(0);
		userStory = setUserStoryData(jsonObject);
		requestDescription = "Get tasks for userstory";
		endpointPath = "tasks?project=" + projectID;
		apiUtility utility2 = new apiUtility(endpointPath, requestDescription);
		JSONArray jsonArray = utility2.apiGET();
		for(int i=0;i<jsonArray.length();i++){
			JSONObject jsonObject1 = jsonArray.getJSONObject(i);
			if(String.valueOf(jsonObject1.get("user_story")).equals(userStory.usId)){
				tasks.add(jsonObject1.get("subject").toString());
			}
		}
		userStory.tasksNames.addAll(tasks);
		return userStory;
	}

	public void displayUserStory(UserStory userStory) throws Exception {
		new ShowUserStory(userStory).execute();
	}

	public UserStory setUserStoryData(JSONObject jsonObject) throws Exception {
		UserStory userStory = new UserStory();
		userStory.title = jsonObject.getString("subject");
		userStory.description = jsonObject.getString("description");
		String status = jsonObject.getJSONObject("status_extra_info").getString("name").toUpperCase();
		userStory.state = status; // Enum and Taiga's US status should match, else an exception will be thrown
		userStory.initiatedDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("created_date"));
		userStory.dueDate = jsonObject.get("due_date") == JSONObject.NULL ? null : new SimpleDateFormat("yyyy-MM-dd").parse((String) jsonObject.get("due_date"));
		userStory.plannedDate = jsonObject.get("due_date") == JSONObject.NULL ? null : new SimpleDateFormat("yyyy-MM-dd").parse((String) jsonObject.get("due_date"));
		userStory.points = jsonObject.get("total_points") == JSONObject.NULL ? 0 : jsonObject.getInt("total_points");
		userStory.usId = String.valueOf(jsonObject.get("id"));
		return userStory;
	}

}
