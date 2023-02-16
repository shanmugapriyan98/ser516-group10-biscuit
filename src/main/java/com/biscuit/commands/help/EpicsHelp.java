package com.biscuit.commands.help;

import de.vandermeer.asciitable.v2.V2_AsciiTable;

public class EpicsHelp extends UniversalHelp {

    @Override
    public void executeChild(V2_AsciiTable at) {

        at.addRow(null, "Epics Commands").setAlignment(new char[]{'c', 'c'});
        at.addRule();
        at.addRow("epics", "List all epics").setAlignment(new char[]{'l', 'l'});
        at.addRow("back", "Go back to previous view (Project)").setAlignment(new char[]{'l', 'l'});
        at.addRow("go_to epic", "Go to a epic view (followed by a epic name)").setAlignment(new char[]{'l', 'l'});
    }

}
