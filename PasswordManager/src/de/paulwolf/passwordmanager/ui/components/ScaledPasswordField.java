package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledPasswordField extends JPasswordField {

    public ScaledPasswordField() {
        this.setPreferredSize(Configuration.SCALED_PASSWORD_FIELD_SIZE);
        this.setFont(Configuration.MONOSPACE_FONT);
    }
}
