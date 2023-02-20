package com.biscuit.commands.userStory;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;
import com.biscuit.models.services.DateService;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
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
				ColorCodes.BLUE + "planned date: " + ColorCodes.RESET + DateService.getDateAsString(us.plannedDate));
		System.out.println(ColorCodes.BLUE + "due date: " + ColorCodes.RESET + DateService.getDateAsString(us.dueDate));
		System.out.println(ColorCodes.BLUE + "points: " + ColorCodes.RESET + us.points);
		System.out.println(ColorCodes.BLUE + "comments: " + ColorCodes.RESET + us.comments);
		System.out.println();
		return true;
	}

	public UserStory fetchUserStoryByNumber(String project, int usNumber) {
		UserStory userStory = null;

		HttpUrl httpUrl = new HttpUrl.Builder()
				.scheme("https")
				.host("api.taiga.io")
				.addPathSegment("api")
				.addPathSegment("v1")
				.addPathSegment("userstories")
				.addPathSegment("by_ref")
				.addQueryParameter("ref", String.valueOf(usNumber))
				.addQueryParameter("project__slug", project)
				.build();

		Request request = new Request.Builder()
				.url(httpUrl)
				.addHeader("Authorization", "Bearer " + authToken)
				.addHeader("Content-Type", "application/json")
				.get()
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
			if (response.body() == null) {
				throw new IOException("Response body is empty" + response);
			}
			JSONObject jsonObject = new JSONObject(response.body().string());
			userStory = setUserStoryData(jsonObject);
		} catch (Exception exception) {
			exception.printStackTrace();
			System.out.println("Error while fetching US details from Taiga. Please enter valid US number in project: " + project);
		}
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
		userStory.state = Status.valueOf(status); // Enum and Taiga's US status should match, else an exception will be thrown

		userStory.initiatedDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("created_date"));
		userStory.dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("due_date"));
		userStory.plannedDate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject.getString("due_date"));
		userStory.points = jsonObject.getInt("total_points"); // Could be float in Taiga
		return userStory;
	}

}
