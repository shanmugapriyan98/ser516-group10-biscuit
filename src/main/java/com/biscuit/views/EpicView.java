package com.biscuit.views;

import com.biscuit.commands.epic.EditEpic;
import com.biscuit.commands.epic.ShowEpic;
import com.biscuit.commands.help.EpicHelp;
import com.biscuit.factories.EpicCompleterFactory;
import com.biscuit.models.Epic;
import com.biscuit.models.services.CommandService;
import jline.console.completer.Completer;

import java.io.IOException;
import java.util.List;

public class EpicView extends View {

    Epic epic = null;

    public String []epicCmdArr= new String[] {"show", "edit", "help"};


    public EpicView(){}

    public EpicView(View previousView, Epic epic) {
        super(previousView, epic.name);
        this.epic = epic;
    }


    @Override
    void addSpecificCompleters(List<Completer> completers) {
        completers.addAll(EpicCompleterFactory.getEpicCompleters(epic));
    }


    @Override
    boolean executeCommand(String[] words) throws IOException {
        if (words.length == 1) {
            return execute1Keyword(words);
        }

        return false;
    }


    private boolean execute1Keyword(String[] words) throws IOException {
        if(!(CommandService.checkCommand(words, epicCmdArr))) return true;
        if (words[0].equals("show")) {
            (new ShowEpic(epic)).execute();
            return true;
        } else if (words[0].equals("edit")) {
            (new EditEpic(reader, epic)).execute();
            this.name = epic.name;
            updatePromptViews();

            return true;
        } else if (words[0].equals("help")) {
            return (new EpicHelp()).execute();
        }
        return false;
    }

}
