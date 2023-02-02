package com.biscuit.commands.epic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Epic;
import com.biscuit.models.Project;
import com.biscuit.models.Release;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthLongestLine;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class ListEpics implements Command {

	Project project = null;
	Release release = null;
	String title = "";
	boolean isFilter = false;
	boolean isSort = false;
	static boolean isReverse = false;
	private String filterBy;
	private String sortBy;
	private static String lastSortBy = "";


	public ListEpics(Project project, String title) {
		super();
		this.project = project;
		this.title = title;
	}


	public ListEpics(Project project, String title, boolean isFilter, String filterBy, boolean isSort, String sortBy) {
		super();
		this.project = project;
		this.title = title;
		this.isFilter = isFilter;
		this.filterBy = filterBy.toLowerCase();
		this.isSort = isSort;
		this.sortBy = sortBy.toLowerCase();
	}


	


	@Override
	public boolean execute() throws IOException {

		V2_AsciiTable at = new V2_AsciiTable();
		String tableString;

		List<Epic> epics = new ArrayList<>();

		if (project != null) {
			epics.addAll(project.epics);
		} 

		if (isFilter) {
			doFilter(epics);
		}

		if (isSort) {
			doSort(epics);
		}

		at.addRule();
		if (!this.title.isEmpty()) {
			at.addRow(null, this.title).setAlignment(new char[] { 'c', 'c', 'c', 'c', 'c', 'c', 'c' });
			at.addRule();
		}
		at.addRow("Name", "Description")
				.setAlignment(new char[] { 'l', 'l' });

		if (epics.size() == 0) {
			String message;
			if (!isFilter) {
				message = "There are no epics!";
			} else {
				message = "No results";
			}
			at.addRule();
			at.addRow(null, null, null, null, null, null, message);
		} else {
			for (Epic e : epics) {
				at.addRule();

				at.addRow(e.name, e.description).setAlignment(new char[] { 'l', 'l' });
			} // for
		}

		at.addRule();
		at.addRow(null, null, null, null, null, null, "Total: " + epics.size());
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


	private void doSort(List<Epic> epics) {

		Comparator<Epic> byName = (s1, s2) -> s1.name.compareTo(s2.name);
		Comparator<Epic> byDescription = (s1, s2) -> s1.description.compareTo(s2.description);
		Comparator<Epic> byFiled = null;

		if (sortBy.equals(Epic.fields[0])) {
			byFiled = byName;
		} else if (sortBy.equals(Epic.fields[1])) {
			byFiled = byDescription;
		} else {
			return;
		}

		List<Epic> sorted = epics.stream().sorted(byFiled).collect(Collectors.toList());

		if (sortBy.equals(lastSortBy)) {
			isReverse = !isReverse;
			if (isReverse) {
				Collections.reverse(sorted);
			}
		} else {
			lastSortBy = sortBy;
			isReverse = false;
		}

		epics.clear();
		epics.addAll(sorted);
	}


	private void doFilter(List<Epic> epics) {
		List<Epic> filtered = epics.stream()
				.filter(r -> r.name.toLowerCase().contains(filterBy) || r.description.toLowerCase().contains(filterBy))
				.collect(Collectors.toList());
		epics.clear();
		epics.addAll(filtered);
	}


	private String colorize(String tableString) {
		for (String header : Epic.fieldsAsHeader) {
			tableString = tableString.replaceFirst(header, ColorCodes.BLUE + header + ColorCodes.RESET);
		}

		return tableString;
	}

}
