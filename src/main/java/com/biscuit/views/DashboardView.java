/*
	Author: Hamad Al Marri;
 */

package com.biscuit.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.biscuit.Login;
import com.biscuit.commands.help.DashboardHelp;
import com.biscuit.commands.project.AddProject;
import com.biscuit.commands.project.EditProject;
import com.biscuit.commands.project.RemoveProject;
import com.biscuit.factories.DashboardCompleterFactory;
import com.biscuit.models.Dashboard;
import com.biscuit.models.Project;
import com.biscuit.models.UserStory;
import com.biscuit.models.services.DateService;
import com.biscuit.models.services.Finder.Projects;

import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestLine;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;
import jline.console.completer.Completer;

public class DashboardView extends View {

	public DashboardView() {
		super(null, "Dashboard");
	}


	@Override
	void addSpecificCompleters(List<Completer> completers) {
		completers.addAll(DashboardCompleterFactory.getDashboardCompleters());
	}


	@Override
	boolean executeCommand(String[] words) throws Exception {

		if (words.length == 1) {
			return execute1Keyword(words);
		} else if (words.length == 2) {
			return execute2Keyword(words);
		} else if (words.length == 3) {
			return execute3Keyword(words);
		}

		return false;
	}


	private boolean execute3Keyword(String[] words) throws IOException {
		if (words[0].equals("go_to")) {
			// "project#1", "users", "contacts", "groups"

			if (words[1].equals("project")) {
				// check if project name
				Project p = Projects.getProject(words[2]);
				if (p != null) {
					ProjectView pv = new ProjectView(this, p);
					pv.view();
					return true;
				}
				return false;
			}
		} else if (words[1].equals("project")) {
			if (words[0].equals("edit")) {
				Project p = Projects.getProject(words[2]);
				if (p != null) {
					(new EditProject(reader, p)).execute();
					resetCompleters();
					return true;
				}
				return false;
			} else if (words[0].equals("remove")) {
				Project p = Projects.getProject(words[2]);
				if (p != null) {
					(new RemoveProject(reader, p)).execute();
					resetCompleters();
					return true;
				}
				return false;
			}
		}

		return false;
	}


	private boolean execute2Keyword(String[] words) throws Exception {
		if (words[0].equals("go_to")) {
			// "project#1", "users", "contacts", "groups"

			// check if project name
			Project p = Projects.getProject(words[1]);
			if (p != null) {
				ProjectView pv = new ProjectView(this, p);
				pv.view();
				return true;
			}
			else{
				Dashboard.getInstance().getProjectNames();
				if(Dashboard.getInstance().projectsOnTaiga.contains(words[1])){
					System.out.println("Caching project " +words[1]+" to local");
					boolean local = false;
					boolean taiga = true;
					new AddProject(reader,local,taiga,words[1]).execute();
					p = Projects.getProject(words[1]);
					ProjectView pv = new ProjectView(this,p);
					pv.view();
					return true;
				}
			}
			return false;

		} else if (words[0].equals("list")) {
			Login.getInstance().getBackLogDataFromProject(words[1]);
			displayBacklog();
			return true;
		} else if (words[1].equals("project")) {
			if (words[0].equals("add")) {
				(new AddProject(reader,false,false,null)).execute();
				resetCompleters();
				return true;
			}
		}

		return false;
	}


	private boolean execute1Keyword(String[] words) throws IOException {
		if (words[0].equals("summary")) {
		} else if (words[0].equals("projects")) {
		} else if (words[0].equals("alerts")) {
		} else if (words[0].equals("check_alert")) {
		} else if (words[0].equals("search")) {
		} else if (words[0].equals("help")) {
			return (new DashboardHelp().execute());
		}

		return false;
	}

	public void displayBacklog(){
		String tableString;
		V2_AsciiTable at = new V2_AsciiTable();
		V2_AsciiTableRenderer rend = new V2_AsciiTableRenderer();
		List<List<String>> userStories = Login.getInstance().userStoriesList;

		at.addRule();
		at.addRow("User Story ID", "Description", "Points")
				.setAlignment(new char[] { 'l', 'l', 'c'});
		for (List<String> us : userStories) {
			at.addRule();
			at.addRow(us.get(0), us.get(1), us.get(2))
					.setAlignment(new char[] { 'c', 'l', 'c'});
		}
		at.addRule();

		rend.setTheme(V2_E_TableThemes.PLAIN_7BIT.get());
		rend.setWidth(new WidthLongestLine());

		RenderedTable rt = rend.render(at);
		tableString = rt.toString();

		System.out.println();
		System.out.println(tableString);
	}

}
