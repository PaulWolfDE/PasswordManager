package de.paulwolf.passwordmanager.ui.passwordfields;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.ui.components.ScaledPasswordField;
import de.paulwolf.passwordmanager.ui.components.ScaledTextField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;

import javax.swing.*;
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

        this.setBorder(new LineBorder(Color.getHSBColor(1.0f / 3.0f / 4.0f * strength.getScore(), 1f, 0.7f), 2));
    }

    public void setDisplayPasswordStrength(boolean displayPasswordStrength) {
        this.displayPasswordStrength = displayPasswordStrength;
    }
}
