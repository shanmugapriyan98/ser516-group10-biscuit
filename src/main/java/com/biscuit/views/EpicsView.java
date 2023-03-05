package com.biscuit.views;

import com.biscuit.commands.epic.AddEpic;
import com.biscuit.commands.epic.ListEpics;
import com.biscuit.commands.help.EpicsHelp;
import com.biscuit.factories.EpicsCompleterFactory;
import com.biscuit.models.Epic;
import com.biscuit.models.Project;
import com.biscuit.models.services.CommandService;
import com.biscuit.models.services.Finder.Epics;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class EpicsView extends View {

    Project project = null;

    public String []epicsCmdArr= new String[] {"add epic", "epics", "help"};


    public EpicsView(){}

    public EpicsView(View previousView, Project p) {
        super(previousView, "epics");
        this.project = p;
    }


    @Override
    void addSpecificCompleters(List<Completer> completers) {
        completers.addAll(EpicsCompleterFactory.getEpicsCompleters(project));
    }


    @Override
    boolean executeCommand(String[] words) throws IOException {
        if (words.length == 1) {
            return execute1Keywords(words);
        } else if (words.length == 2) {
            return execute2Keywords(words);
        }
        return false;
    }


    private boolean execute1Keywords(String[] words) throws IOException {
        if (words[0].equals("epics")) {

            (new ListEpics(project, "Epics")).execute();
            return true;
        } else if (words[0].equals("help")) {
            return (new EpicsHelp()).execute();
        }

        return false;
    }


    private boolean execute2Keywords(String[] words) throws IOException {
        if(words.equals("add") && !(CommandService.checkCommand(words, epicsCmdArr)))
        if (words[0].equals("add")) {
            if (words[1].equals("epic")) {
                (new AddEpic(reader, project)).execute();
                resetCompleters();

                return true;
            }
        } else if (words[0].equals("go_to")) {
            if (Epics.getAllNames(project).contains(words[1])) {
                Epic e = Epics.find(project, words[1]);
                if (e == null) {
                    return false;
                }

                EpicView ev = new EpicView(this, e);
                ev.view();
                return true;
            }
        }

        return false;
    }
}
