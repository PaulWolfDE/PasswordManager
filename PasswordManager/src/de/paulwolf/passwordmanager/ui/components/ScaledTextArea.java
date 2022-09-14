package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledTextArea extends JTextArea {

    public ScaledTextArea(int rows) {

        this.setFont(Configuration.STANDARD_FONT);
        this.setRows(rows);
    }
}
