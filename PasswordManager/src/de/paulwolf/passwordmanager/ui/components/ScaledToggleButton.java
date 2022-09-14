package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledToggleButton extends JToggleButton {

    public ScaledToggleButton(String text) {

        this.setText(text);
        this.setPreferredSize(Configuration.SCALED_BUTTON_SIZE);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
