package de.paulwolf.passwordmanager.ui.passwordfields;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.ui.components.ScaledPasswordField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Objects;

abstract public class PasswordStrengthField extends ScaledPasswordField implements KeyListener {

    private boolean displayPasswordStrength = true;

    public PasswordStrengthField() {

        this.addKeyListener(this);
    }

    public void evaluatePassword(int encoding) {

        if (!displayPasswordStrength)
            return;

        if (this.getPassword().length == 0 || !EncodingWizard.isEncodingValid(encoding, new String(this.getPassword()))) {
            this.setBorder(UIManager.getBorder("TextField.border"));
            return;
        }

        byte[] password;
        if (encoding == 0)
            password = new String(this.getPassword()).getBytes(Configuration.STANDARD_CHARSET);
        else if (encoding == 1)
            password = EncodingWizard.hexToBytes(new String(this.getPassword()));
        else
            password = Objects.requireNonNull(EncodingWizard.base64ToBytes(new String(this.getPassword()).getBytes(Configuration.STANDARD_CHARSET)));

        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(new String(password));

        Arrays.fill(password, (byte) 0);

        Border existingBorder = this.getBorder();
        Border coloredBorder = new LineBorder(Color.getHSBColor(1.0f / 3.0f / 4.0f * strength.getScore(), 1f, 0.7f), 3);
        Border passwordStrengthBorder = new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                // Die vorhandene Border zeichnen und dabei die gewünschte Farbe verwenden
                existingBorder.paintBorder(c, g, x, y, width, height);
                coloredBorder.paintBorder(c, g, x, y, width, height);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                // Die vorhandenen Border-Insets zurückgeben
                return existingBorder.getBorderInsets(c);
            }

            @Override
            public boolean isBorderOpaque() {
                // Die Opaque-Eigenschaft der vorhandenen Border zurückgeben
                return existingBorder.isBorderOpaque();
            }
        };

        this.setBorder(passwordStrengthBorder);
    }

    public void setDisplayPasswordStrength(boolean displayPasswordStrength) {
        this.displayPasswordStrength = displayPasswordStrength;
    }
}
