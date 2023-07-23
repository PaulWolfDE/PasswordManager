package de.paulwolf.passwordmanager;

import de.paulwolf.passwordmanager.ui.windows.DatabaseUI;
import de.paulwolf.passwordmanager.ui.windows.MainUI;
import de.paulwolf.passwordmanager.ui.windows.OpenDatabaseUI;
import de.paulwolf.passwordmanager.ui.windows.UpdateUI;
import de.paulwolf.passwordmanager.utility.JSONParser;

import java.io.File;

public class Main {

    public static MainUI ui;
    public static DatabaseUI dui;

    public static void main(String[] args) {

        Configuration.loadResources();

        ui = new MainUI();
        if (!JSONParser.isUpToDate())
            new UpdateUI(ui);

        if (args.length > 0) {

            ui.databaseFile = new File(args[0]);
            new OpenDatabaseUI(new File(args[0]).getAbsolutePath(), null);
        }
    }

}
