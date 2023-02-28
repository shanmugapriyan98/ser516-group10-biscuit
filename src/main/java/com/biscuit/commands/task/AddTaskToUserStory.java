package com.biscuit.commands.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.commands.userStory.AddUserStoryToBacklog;
import com.biscuit.models.Project;
import com.biscuit.models.Task;
import com.biscuit.models.UserStory;
import com.biscuit.models.enums.Status;

import com.biscuit.models.services.apiUtility;
import jline.console.ConsoleReader;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddTaskToUserStory implements Command {

	ConsoleReader reader = null;
	Project project = null;
	UserStory userStory = null;
	Task task = new Task();


	public AddTaskToUserStory(ConsoleReader reader, Project project, UserStory userStory) {
		super();
		this.reader = reader;
		this.project = project;
		this.userStory = userStory;
	}

	public String getUserStoryId(){
		String userstoryID = "";
		String requestDescription = "Get userstory of current story";
		String endpointPath = "userstories?project="+project.projectId;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i = 0; i< jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.has("subject") && jsonObject.getString("subject").equals(userStory.title))  userstoryID = String.valueOf(jsonObject.getInt("id"));
		}
		if(userstoryID==""){
			throw new RuntimeException("User Story Id for "+userStory.title+" is not found");
		}
		return userstoryID;
	}


	public boolean execute() throws IOException {
		StringBuilder description = new StringBuilder();
		String prompt = reader.getPrompt();

		task.project = project;
		setTitle();
		setDescription(description);
		task.state = Status.OPEN;
		setTime();
		task.initiatedDate = new Date();
		task.plannedDate = new Date(0);
		task.dueDate = new Date(0);

		reader.setPrompt(prompt);

		userStory.tasks.add(task);
		project.save();

		reader.println();
		reader.println(ColorCodes.GREEN + "Task \"" + task.title + "\" has been added!" + ColorCodes.RESET);
		String requestDescription = "Create Tasks";
		String endpointPath = "tasks";
		HashMap<String,String> body = new HashMap<>();
		body.put("project" ,project.projectId);
		body.put("user_story",getUserStoryId());
		body.put("subject", task.title);
		body.put("description",task.description);
		apiUtility utility = new apiUtility(endpointPath,requestDescription,body);
		utility.apiPOST();
		reader.println(ColorCodes.GREEN + "Task \"" + userStory.title + "\" has been added to the user story in Taiga!" + ColorCodes.RESET);
		return false;
	}


	private void setTime() throws IOException {
		String line;
		Completer oldCompleter = (Completer) reader.getCompleters().toArray()[0];

		Completer timeCompleter = new ArgumentCompleter(new StringsCompleter("1", "1.5", "2", "2.25", "3"), new NullCompleter());

		reader.removeCompleter(oldCompleter);
		reader.addCompleter(timeCompleter);

		reader.setPrompt(ColorCodes.BLUE + "\nestimated time (in hours):\n" + ColorCodes.YELLOW + "(hit Tab to see an example)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			line = line.trim();

			try {
				task.estimatedTime = Float.valueOf(line);
				break;
			} catch (NumberFormatException e) {
				System.out.println(ColorCodes.RED + "invalid value: must be a float value!" + ColorCodes.RESET);
			}
		}

		reader.removeCompleter(timeCompleter);
		reader.addCompleter(oldCompleter);
	}


	private void setDescription(StringBuilder description) throws IOException {
		String line;
		reader.setPrompt(ColorCodes.BLUE + "\ndescription:\n" + ColorCodes.YELLOW + "(\\q to end writing)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			if (line.equals("\\q")) {
				break;
			}
			description.append(line).append("\n");
			reader.setPrompt("");
		}

		task.description = description.toString();
	}


	private void setTitle() throws IOException {
		reader.setPrompt(ColorCodes.BLUE + "title: " + ColorCodes.RESET);
		task.title = reader.readLine();
	}

}
