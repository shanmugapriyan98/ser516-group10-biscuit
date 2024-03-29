package com.biscuit.commands.userStory;

import java.io.IOException;
import java.util.*;

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

import java.io.IOException;
import java.util.Date;

public class AddUserStoryToBacklog implements Command {

	ConsoleReader reader = null;
	Project project = null;
	UserStory userStory = new UserStory();


	public AddUserStoryToBacklog(ConsoleReader reader, Project project) {
		super();
		this.reader = reader;
		this.project = project;
	}

	public String getProjectId(){
		String memberId = String.valueOf(Login.getInstance().memberId);
		String projectId = "";
		String requestDescription = "Get ProjectID of current Project";
		String endpointPath = "projects?member="+memberId;
		apiUtility utility = new apiUtility(endpointPath,requestDescription);
		JSONArray jsonArray = utility.apiGET();
		for(int i = 0; i< jsonArray.length(); i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.getString("name").equals(project.name))  {
				projectId= String.valueOf(jsonObject.getInt("id"));
				project.projectId = projectId;
			}
		}
		if(projectId==""){
			throw new RuntimeException("project Id for "+project.name+" is not found");
		}
		return projectId;
	}

	public boolean execute() throws IOException {
		StringBuilder description = new StringBuilder();
		String prompt = reader.getPrompt();
		char b = '"';

		userStory.project = project;
		setTitle();

		setDescription(description);
		setEpic();
		userStory.state = "new";
		setBusinessValue();
		setPoints();
		setTags();
		userStory.initiatedDate = new Date();
		userStory.plannedDate = new Date(0);
		userStory.dueDate = new Date(0);

		reader.setPrompt(prompt);

		project.backlog.addUserStory(userStory);
		project.save();

		reader.println();
		reader.println(ColorCodes.GREEN + "User Story \"" + userStory.title + "\" has been added to the backlog!" + ColorCodes.RESET );
		String requestDescription = "Create UserStory";
		String endpointPath = "userstories";
		HashMap<String , String > body = new HashMap<>();
		body.put("project",getProjectId());
		body.put("subject",userStory.title);
		body.put("total_points", String.valueOf(userStory.points));
		body.put("tags", "\""+userStory.tags+"\"");
		body.put("description",userStory.description);
		apiUtility utility = new apiUtility(endpointPath,requestDescription,body);
		utility.apiPOST();
		reader.println(ColorCodes.GREEN + "User Story \"" + userStory.title + "\" has been created to the backlog in Taiga!" + ColorCodes.RESET);
		return false;
	}

	private void setTags() throws IOException {
		String line;
		Completer oldCompleter = (Completer) reader.getCompleters().toArray()[0];

		Completer pointsCompleter = new ArgumentCompleter(new StringsCompleter(Points.values), new NullCompleter());

		reader.removeCompleter(oldCompleter);
		reader.addCompleter(pointsCompleter);

		reader.setPrompt(ColorCodes.BLUE + "\ntags:\n" + ColorCodes.YELLOW + "(for multiple tags, separate them by commas)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			line = line.trim();

			try {
				userStory.tags = line;
				break;
			} catch (NumberFormatException e) {
				System.out.println(ColorCodes.RED + "invalid value: must be a string value!" + ColorCodes.RESET);
			}
		}

		reader.removeCompleter(pointsCompleter);
		reader.addCompleter(oldCompleter);

	}


	private void setPoints() throws IOException {
		// List<String> points = new ArrayList<String>();
		String line;
		Completer oldCompleter = (Completer) reader.getCompleters().toArray()[0];

		// for (Points p : Points.values()) {
		// points.add(p.name().substring(1, p.name().length() - 2));
		// }

		Completer pointsCompleter = new ArgumentCompleter(new StringsCompleter(Points.values), new NullCompleter());

		reader.removeCompleter(oldCompleter);
		reader.addCompleter(pointsCompleter);

		reader.setPrompt(ColorCodes.BLUE + "\npoints:\n" + ColorCodes.YELLOW + "(hit Tab to see an example)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			line = line.trim();

			try {
				userStory.points = Integer.valueOf(line);
				break;
			} catch (NumberFormatException e) {
				System.out.println(ColorCodes.RED + "invalid value: must be an integer value!" + ColorCodes.RESET);
			}
		}

		reader.removeCompleter(pointsCompleter);
		reader.addCompleter(oldCompleter);
	}


	private void setBusinessValue() throws IOException {
		// List<String> businessValues = new ArrayList<String>();
		String line;
		Completer oldCompleter = (Completer) reader.getCompleters().toArray()[0];

		// for (BusinessValue bv : BusinessValue.values()) {
		// businessValues.add(bv.name().toLowerCase());
		// }

		Completer businessValuesCompleter = new ArgumentCompleter(new StringsCompleter(BusinessValue.values), new NullCompleter());

		reader.removeCompleter(oldCompleter);
		reader.addCompleter(businessValuesCompleter);

		reader.setPrompt(ColorCodes.BLUE + "\nbusiness value:\n" + ColorCodes.YELLOW + "(hit Tab to see valid values)\n" + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			line = line.trim().toUpperCase();

			try {
				userStory.businessValue = Integer.valueOf(line);
			} catch (IllegalArgumentException e) {
				System.out.println(ColorCodes.RED + "invalid value" + ColorCodes.RESET);
				continue;
			}

			reader.removeCompleter(businessValuesCompleter);
			reader.addCompleter(oldCompleter);
			break;
		}

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

		userStory.description = description.toString();
	}


	private void setTitle() throws IOException {
		reader.setPrompt(ColorCodes.BLUE + "title: " + ColorCodes.RESET);
		userStory.title = reader.readLine();
	}

	private void setEpic() throws IOException {
		String line;
		Completer oldCompleter = (Completer) reader.getCompleters().toArray()[0];

		List<String> epicsNames =  new ArrayList<>();
		for(int i = 0; i< project.epics.size();i++){
			epicsNames.add(project.epics.get(i).name);
		}
		Completer epicCompleter = new ArgumentCompleter(new StringsCompleter(epicsNames), new NullCompleter());

		reader.removeCompleter(oldCompleter);
		reader.addCompleter(epicCompleter);

		reader.setPrompt(ColorCodes.BLUE + "\nepics:\n" + ColorCodes.YELLOW + "(existing epics are: )\n " + epicsNames + ColorCodes.RESET);

		while ((line = reader.readLine()) != null) {
			line = line.trim();


			userStory.epic.name = line;
			break;
		}

		reader.removeCompleter(epicCompleter);
		reader.addCompleter(oldCompleter);
		//Epic epic = new Epic();
	}


}
