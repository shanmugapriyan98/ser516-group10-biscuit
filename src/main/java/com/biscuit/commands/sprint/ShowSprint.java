package com.biscuit.commands.sprint;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Sprint;
import com.biscuit.models.services.DateService;

import java.io.IOException;

public class ShowSprint implements Command {
    Sprint s = null;


    public ShowSprint(Sprint s) {
        super();
        this.s = s;
    }


    @Override
    public boolean execute() throws IOException {

        System.out.println(ColorCodes.BLUE + "name: " + ColorCodes.RESET + s.name);
        System.out.println(ColorCodes.BLUE + "state: " + ColorCodes.RESET + s.state);
        System.out.println(
                ColorCodes.BLUE + "Start date: " + ColorCodes.RESET + ((s.startDate) == null ? "NULL" : DateService.getDateAsString(s.startDate)));
        System.out.println(ColorCodes.BLUE + "due date: " + ColorCodes.RESET + ((s.dueDate) == null ? "NULL" : DateService.getDateAsString(s.dueDate)));

        System.out.println();

        return true;
    }
}
