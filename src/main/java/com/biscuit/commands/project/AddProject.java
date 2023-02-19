package com.biscuit.commands.project;

import java.io.IOException;
import java.util.HashMap;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.Dashboard;
import jline.console.ConsoleReader;
import okhttp3.*;
import com.biscuit.models.services.apiUtility;
import org.json.JSONObject;

public class AddProject implements Command {

	Dashboard dashboard = Dashboard.getInstance();
	Project project = new Project();
	ConsoleReader reader = null;
	String authToken = Login.getInstance().authToken;
	private final OkHttpClient httpClient = new OkHttpClient();
	public AddProject(ConsoleReader reader) throws Exception {
		super();
		this.reader = reader;
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
		String requestDescription = "create project";
		HashMap<String,String > body = new HashMap<>();
		body.put("description",project.description);
		body.put("name",project.name);
		String endpoint = "projects";
		String method = "POST";
		apiUtility utility = new apiUtility(method,endpoint,requestDescription,body);
		JSONObject jsonObject = utility.apiCall();
		System.out.println(project.name + "created successfully");
		return false;
	}

}
