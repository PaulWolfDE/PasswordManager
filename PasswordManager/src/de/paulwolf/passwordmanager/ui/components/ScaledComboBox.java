package de.paulwolf.passwordmanager.ui.components;

import de.paulwolf.passwordmanager.Configuration;

import javax.swing.*;
import java.awt.*;

public class ScaledComboBox<T> extends JComboBox<T> {

    public ScaledComboBox(T[] objects) {
        super(objects);
        this.setPreferredSize(Configuration.SCALED_TEXT_FIELD_SIZE);
        this.setFont(Configuration.STANDARD_FONT);
    }
}
