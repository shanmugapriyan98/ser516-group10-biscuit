package com.biscuit.commands.sprint;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Project;
import com.biscuit.models.Release;
import com.biscuit.models.Sprint;
import com.biscuit.models.UserStory;
import com.biscuit.models.services.DateService;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestLine;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import jline.console.ConsoleReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDashboard implements Command {

	Sprint sprint = null;
	String sprintName = "";
	ConsoleReader reader = null;
	String title = "";

	public ReviewDashboard(ConsoleReader reader, Sprint sprint, String s) {
		super();
		this.sprint = sprint;
		this.sprintName = s;
		this.reader = reader;
	}


	@Override
	public boolean execute() throws IOException {

		V2_AsciiTable at = new V2_AsciiTable();
		String tableString;

		List<UserStory> userStories = new ArrayList<>();

		if (sprint != null) {
			userStories.addAll(sprint.userStories);
		}

		at.addRule();
		if (!this.title.isEmpty()) {
			at.addRow(null, null, null, null, null, null, this.title).setAlignment(new char[] { 'c', 'c', 'c', 'c', 'c', 'c', 'c' });
			at.addRule();
		}
		at.addRow("User Story", "Comments")
				.setAlignment(new char[] { 'l', 'l' });

		if (userStories.size() == 0) {
			at.addRule();
			at.addRow(null, null);
		} else {
			for (UserStory s : userStories) {
				at.addRule();

				at.addRow(s.title, s.description).setAlignment(new char[] { 'l', 'l'});
			} // for
		}

		at.addRule();
		at.addRow(null, null);
		at.addRule();

		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		rend.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
		rend.setWidth(new WidthLongestLine());

		RenderedTable rt = rend.render(at);
		tableString = rt.toString();

		tableString = colorize(tableString);

		System.out.println();
		System.out.println(tableString);

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
