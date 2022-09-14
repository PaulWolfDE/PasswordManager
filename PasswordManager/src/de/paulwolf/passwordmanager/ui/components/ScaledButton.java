package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;

public class ScaledButton extends JButton {

    public ScaledButton(String text) {
        this.setText(text);
        this.setPreferredSize(Configuration.SCALED_BUTTON_SIZE);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
