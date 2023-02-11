package com.biscuit.commands.project;

import java.io.IOException;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.Dashboard;
import jline.console.ConsoleReader;
import okhttp3.*;
import org.json.JSONObject;

public class AddProject implements Command {

	Dashboard dashboard = Dashboard.getInstance();
	Project project = new Project();
	ConsoleReader reader = null;
	Login login =  Login.getInstance();
	String authToken = login.authToken;
	private final OkHttpClient httpClient = new OkHttpClient();
	public AddProject(ConsoleReader reader) throws Exception {
		super();
		this.reader = reader;
	}

	public void sendPost(String projectName, String description){
		RequestBody formBody = new FormBody.Builder()
				.add("description", description)
				.add("is_backlog_activated", "true")
				.add("name", projectName)
				.build();

		Request request = new Request.Builder()
				.url("https://api.taiga.io/api/v1/projects")
				.addHeader("Content-Type", "application/json")
				.addHeader("Authorization", "Bearer "+authToken)
				.post(formBody)
				.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
			JSONObject jsonObject = new JSONObject(response.body().string());
			System.out.println("Project: was " + projectName +" created successfully on Taiga ");

		} catch (IOException ioException){
			System.out.println("Creation of project on Taiga was unsuccessful");
			System.exit(1);
		}

	}
	public boolean execute() throws IOException {

		StringBuilder description = new StringBuilder();
		String line;
		String prompt = reader.getPrompt();
		
		project.backlog.project = project;
		
		reader.setPrompt(ColorCodes.BLUE + "project name: " + ColorCodes.RESET);
		project.name = reader.readLine();
		reader.setPrompt(ColorCodes.BLUE + "\ndescription: " + ColorCodes.YELLOW + "\n(\\q to end writing)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			if (line.equals("\\q")) {
				break;
			}
			description.append(line).append("\n");
			reader.setPrompt("");
		}

		project.description = description.toString();

		reader.setPrompt(prompt);

		dashboard.addProject(project);

		dashboard.save();
		project.save();

		reader.println();
		reader.println(ColorCodes.GREEN + "Project \"" + project.name + "\" has been added!" + ColorCodes.RESET);
		sendPost(project.name,project.description);

		return false;
	}

}
