package com.biscuit.commands.sprint;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.commands.userStory.EditUserStory;
import com.biscuit.models.Project;
import com.biscuit.models.Release;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.services.DateService;
import com.biscuit.models.services.Finder;
import com.biscuit.views.UserStoryView;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestLine;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReviewDashboard implements Command {

	Sprint sprint = null;
	String sprintName = "";
	ConsoleReader reader = null;
	String title = "";
	String sprGoal;
	String wasSprGoalAchieved;

	public ReviewDashboard(ConsoleReader reader, Sprint sprint, String s, String title) {
		super();
		this.sprint = sprint;
		this.sprintName = s;
		this.reader = reader;
		this.title = title;
	}


	@Override
	public boolean execute() throws IOException {

		V2_AsciiTable at = new V2_AsciiTable();
		String prompt = reader.getPrompt();
		reader.setPrompt(prompt);

		String tableString;
		String num = "";
		String demoComment;
		List<String> demoUS = new ArrayList<>();

		List<UserStory> userStories = new ArrayList<>();

		reader.setPrompt(ColorCodes.BLUE + "Enter the sprint goal: " + ColorCodes.RESET);
		sprGoal = reader.readLine();
		sprGoal = "Sprint Goal : "+ sprGoal;
		reader.setPrompt(ColorCodes.BLUE + "Was the sprint goal achieved? Enter yes or no. : " + ColorCodes.RESET);
		wasSprGoalAchieved = reader.readLine();
		wasSprGoalAchieved = "Was the sprint goal achieved: "+ wasSprGoalAchieved;
		reader.setPrompt(ColorCodes.BLUE + "Which user stories are you demoing today? Enter None to stop. " + ColorCodes.RESET);

		while(!Objects.equals(num, "None")){
			reader.setPrompt(ColorCodes.BLUE + "Enter user story number or enter none : " + ColorCodes.RESET);
			num = reader.readLine();
			if(!Objects.equals(num, "None")){
				demoUS.add(num);
			}
		}

		for(String dUS: demoUS){
			System.out.println("Please add demo comments for the user stories that are being demoed, else enter No");
			reader.setPrompt(ColorCodes.BLUE + "Do you want to add any demo comments to user story no. " + ColorCodes.RESET);
			demoComment = reader.readLine();
			if(!Objects.equals(demoComment, "No")) {
				reader.setPrompt(ColorCodes.BLUE + "Enter demo comment: " + ColorCodes.RESET);
				demoComment = reader.readLine();
				if (Finder.UserStories.getAllNames(sprint).contains("US-"+dUS)) {
					UserStory us = Finder.UserStories.find(sprint, "US-"+dUS);
					userStories.add(us);
					if (us == null) {
						System.out.println("User story not found");
					}
					new EditUserStory(reader, us).setReviewDemoComments(demoComment);
				}
			}
		}

		at.addRule();
		if (!this.title.isEmpty()) {
			at.addRow(null, null, null, null, null, null, null, null, null, this.title).setAlignment(new char[] { 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c' });
			at.addRule();
		}
		if (!this.sprGoal.isEmpty()) {
			at.addRow(null, null, null, null, null, null, null, null, null, this.sprGoal).setAlignment(new char[] { 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c' });
			at.addRule();
		}
		if (!this.wasSprGoalAchieved.isEmpty()) {
			at.addRow(null, null, null, null, null, null, null, null, null, this.wasSprGoalAchieved).setAlignment(new char[] { 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c' });
			at.addRule();
		}
		at.addRow("Title", "Description", "State", "Business Value", "Initiated Date", "Planned Date", "Due Date", "Tasks #", "Points", "Review Comments")
				.setAlignment(new char[] { 'l', 'l', 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c' });


		if (userStories.size() == 0) {
			String message = "There are no user stories!";
			at.addRule();
			at.addRow(null, null, null, null, null, null, null, null, null, message);
		} else {
			for (UserStory us : userStories) {
				at.addRule();

				at.addRow(us.title, us.description, us.state, us.businessValue, DateService.getDateAsString(us.initiatedDate),
								DateService.getDateAsString(us.plannedDate), DateService.getDateAsString(us.dueDate), us.tasks.size(), us.points, us.reviewDemoComments)
						.setAlignment(new char[] { 'l', 'l', 'c', 'c', 'c', 'c', 'c', 'c', 'c', 'c' });
			} // for
		}

		at.addRule();
		at.addRow(null, null, null, null, null, null, null, null, null, "Total: " + userStories.size());
		at.addRule();

		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
		rend.setWidth(new WidthLongestLine());

		RenderedTable rt = rend.render(at);
		tableString = rt.toString();

		tableString = colorize(tableString);

		System.out.println();
		System.out.println(tableString);

		reader.setPrompt(prompt);
		return false;
	}

	private String colorize(String tableString) {
		for (String header : Sprint.fieldsAsHeader) {
			tableString = tableString.replaceFirst(header, ColorCodes.BLUE + header + ColorCodes.RESET);
		}

		return tableString;
	}

	private void setComments() throws IOException {
		StringBuilder comment = new StringBuilder();
		String line;
		String prompt = ColorCodes.BLUE + "comments: " + ColorCodes.YELLOW + "(\\q to end writing) "
				+ ColorCodes.RESET;

		reader.resetPromptLine(prompt, "", 0);
		reader.print("\r");

		while ((line = reader.readLine()) != null) {
			if (line.equals("\\q")) {
				break;
			}
			comment.append(line).append("\n");
			reader.setPrompt("");
		}

//		userStory.comments.add(comment.toString().replace("<newline>", "\n").replace("<exclamation-mark>", "!"));
	}
}
