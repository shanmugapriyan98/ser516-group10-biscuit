package com.biscuit.factories;

import com.biscuit.models.Project;
import com.biscuit.models.services.Finder;
import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import jline.console.completer.NullCompleter;
import jline.console.completer.StringsCompleter;

import java.util.ArrayList;
import java.util.List;

public class EpicsCompleterFactory {

    public static List<Completer> getEpicsCompleters(Project project) {
        List<Completer> completers = new ArrayList<Completer>();

        completers.add(new ArgumentCompleter(new StringsCompleter("back", "epics"), new NullCompleter()));

        completers.add(new ArgumentCompleter(new StringsCompleter("add"), new StringsCompleter("epic"), new NullCompleter()));

        completers.add(new ArgumentCompleter(new StringsCompleter("go_to"), new StringsCompleter(Finder.Epics.getAllNames(project)), new NullCompleter()));

        return completers;

    }

}
