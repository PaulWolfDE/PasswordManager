package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledTextField extends JTextField {

    public ScaledTextField() {
        this.setPreferredSize(Configuration.SCALED_TEXT_FIELD_SIZE);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
