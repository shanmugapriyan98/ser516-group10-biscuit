package com.biscuit.commands.project;

import java.io.IOException;
import java.util.HashMap;

import com.biscuit.ColorCodes;
import com.biscuit.Login;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.Dashboard;
import jline.console.ConsoleReader;
import com.biscuit.models.services.apiUtility;

public class AddProject implements Command {

	Dashboard dashboard = Dashboard.getInstance();
	Project project = new Project();
	boolean local = false;
	boolean remote = false;
	ConsoleReader reader = null;
	String authToken = Login.getInstance().authToken;
	public AddProject(ConsoleReader reader, boolean local, boolean remote, String name) throws Exception {
		super();
		this.reader = reader;
		this.local = local;
		this.remote = remote;
		project.name = name;
	}
	public boolean execute() throws IOException {

		StringBuilder description = new StringBuilder();
		if (!local) {
			String line;
			String prompt = reader.getPrompt();

			project.backlog.project = project;

			if (project.name == null) {
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
			}
			reader.setPrompt(prompt);

			dashboard.addProject(project);

			dashboard.save();
			project.save();

			reader.println();
			reader.println(ColorCodes.GREEN + "Project \"" + project.name + "\" has been added!" + ColorCodes.RESET);
		}
		if (!remote) {
			String requestDescription = "create project";
			HashMap<String,String > body = new HashMap<>();
			body.put("description",project.description);
			body.put("name",project.name);
			String endpoint = "projects";
			apiUtility utility = new apiUtility(endpoint,requestDescription,body);
			utility.apiPOST();
			System.out.println(project.name + "created successfully on Taiga");
		}
		return false;
	}

}
