package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledCheckBox extends JCheckBox {

    public ScaledCheckBox(String text) {

        this.setFont(Configuration.STANDARD_FONT);
        this.setPreferredSize(Configuration.SCALED_BUTTON_SIZE);
        this.setText(text);
    }
}
