package com.biscuit.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biscuit.models.Epic;
import com.biscuit.models.UserStory;

import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;

public class EpicCompleterFactory {

	public static Collection<? extends Completer> getEpicCompleters(Epic epic) {
		List<Completer> completers = new ArrayList<Completer>();

		// TODO: sprint commands
		// completers.add(new ArgumentCompleter(
		// new StringsCompleter("summary", "show", "times", "edit", "back",
		// "user_stories"), new NullCompleter()));

		completers.add(new ArgumentCompleter(new StringsCompleter("show", "edit", "back", "user_stories"), new NullCompleter()));

		completers.add(
				new ArgumentCompleter(new StringsCompleter("list"), new StringsCompleter("user_stories"), new StringsCompleter("filter"), new NullCompleter()));

		completers.add(new ArgumentCompleter(new StringsCompleter("list"), new StringsCompleter("user_stories"), new StringsCompleter("sort"),
				new StringsCompleter(UserStory.fields), new NullCompleter()));

		return completers;
	}
}
