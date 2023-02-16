package com.biscuit.commands.help;


import de.vandermeer.asciitable.v2.V2_AsciiTable;

public class EpicHelp extends UniversalHelp {

    @Override
    public void executeChild(V2_AsciiTable at) {

        at.addRow(null, "Epic Commands").setAlignment(new char[]{'c', 'c'});
        at.addRule();
        at.addRow("show", "Show Epic information").setAlignment(new char[]{'l', 'l'});
        at.addRow("edit", "Edit Epic").setAlignment(new char[]{'l', 'l'});
        at.addRow("back", "Go back to previous view").setAlignment(new char[]{'l', 'l'});

    }

}