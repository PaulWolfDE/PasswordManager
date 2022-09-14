package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledLabel extends JLabel {

    public ScaledLabel(String text) {
        this.setText(text);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
