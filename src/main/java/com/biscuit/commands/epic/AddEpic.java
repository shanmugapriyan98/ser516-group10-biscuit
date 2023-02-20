package com.biscuit.commands.epic;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Epic;
import com.biscuit.models.Project;
import jline.console.ConsoleReader;

import java.io.IOException;

public class AddEpic implements Command {

    ConsoleReader reader = null;
    Project project = null;
    Epic epic = new Epic();

    public AddEpic(ConsoleReader reader, Project project) {
        super();
        this.reader = reader;
        this.project = project;
    }


    public boolean execute() throws IOException {
        StringBuilder description = new StringBuilder();
        String prompt = reader.getPrompt();

        epic.project = project;
        setName();

        setDescription(description);

        reader.setPrompt(prompt);

        project.addEpic(epic);
        project.save();

        reader.println();
        reader.println(ColorCodes.GREEN + "Epic \"" + epic.name + "\" has been added!" + ColorCodes.RESET);

        return false;
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

        epic.description = description.toString();
    }

    private void setName() throws IOException {
        reader.setPrompt(ColorCodes.BLUE + "name: " + ColorCodes.RESET);
        epic.name = reader.readLine();
    }

}