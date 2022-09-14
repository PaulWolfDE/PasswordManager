package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;
import java.text.Format;

public class ScaledFormattedTextField extends JFormattedTextField {

    public ScaledFormattedTextField(Format format) {

        super(format);
        this.setPreferredSize(Configuration.SCALED_TEXT_FIELD_SIZE);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
