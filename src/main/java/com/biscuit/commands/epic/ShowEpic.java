package com.biscuit.commands.epic;

import com.biscuit.ColorCodes;
import com.biscuit.commands.Command;
import com.biscuit.models.Epic;

import java.io.IOException;

public class ShowEpic implements Command {
    Epic e = null;


    public ShowEpic(Epic e) {
        super();
        this.e = e;
    }


    @Override
    public boolean execute() throws IOException {

        System.out.println(ColorCodes.BLUE + "name: " + ColorCodes.RESET + e.name);
        System.out.println(ColorCodes.BLUE + "description: ");
        System.out.println(ColorCodes.RESET + e.description);
        System.out.println();

        return true;
    }
}
