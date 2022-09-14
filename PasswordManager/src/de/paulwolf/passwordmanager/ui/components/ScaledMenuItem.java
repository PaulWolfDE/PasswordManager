package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledMenuItem extends JMenuItem {

    public ScaledMenuItem(String text) {

        this.setText(text);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
