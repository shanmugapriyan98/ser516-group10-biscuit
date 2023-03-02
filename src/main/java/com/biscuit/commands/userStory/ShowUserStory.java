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
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

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
		System.out.println();
		return true;
	}

	public UserStory fetchUserStoryByNumber(int projectID, int usNumber) throws Exception {
		UserStory userStory = null;
		String requestDescription = "Show user story";
		String endpointPath = "userstories/by_ref?ref="+usNumber+"&project="+projectID;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONObject jsonObject = utility.apiGET().getJSONObject(0);
		userStory = setUserStoryData(jsonObject);
		return userStory;
	}

	public void displayUserStory(UserStory userStory) throws Exception {
		new ShowUserStory(userStory).execute();
	}

	public UserStory setUserStoryData(JSONObject jsonObject) throws Exception {
		UserStory userStory = new UserStory();
		userStory.title = jsonObject.get("subject") == JSONObject.NULL ? null : jsonObject.getString("subject");
		userStory.description = jsonObject.get("description") == JSONObject.NULL ? null : jsonObject.getString("description");
		if(jsonObject.getJSONObject("status_extra_info") != JSONObject.NULL && jsonObject.getJSONObject("status_extra_info").getString("name") != JSONObject.NULL){
			try {
				userStory.state = Status.valueOf(jsonObject.getJSONObject("status_extra_info").getString("name").toUpperCase());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("Please make sure that Taiga US status and Biscuit CLI status enum key are in sync");
			}
		} else{
			userStory.state = Status.valueOf("CREATED");
		}
		userStory.initiatedDate = jsonObject.get("created_date") == JSONObject.NULL ? null : new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("created_date"));
		userStory.dueDate = jsonObject.get("due_date") == JSONObject.NULL ? null : new SimpleDateFormat("yyyy-MM-dd").parse((String) jsonObject.get("due_date"));
		userStory.plannedDate = jsonObject.get("due_date") == JSONObject.NULL ? null : new SimpleDateFormat("yyyy-MM-dd").parse((String) jsonObject.get("due_date"));
		userStory.points = jsonObject.get("total_points") == JSONObject.NULL ? 0 : jsonObject.getInt("total_points");
		return userStory;
	}

}
