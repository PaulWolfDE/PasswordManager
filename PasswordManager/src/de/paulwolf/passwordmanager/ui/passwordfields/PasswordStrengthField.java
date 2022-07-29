package de.paulwolf.passwordmanager.ui.passwordfields;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import de.paulwolf.passwordmanager.wizards.ConversionWizard;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

abstract public class PasswordStrengthField extends JPasswordField implements KeyListener {

    public PasswordStrengthField(int c) {

        this.setColumns(c);
        this.addKeyListener(this);
    }

    public void evaluatePassword() {
        evaluatePassword(0);
    }

    public void evaluatePassword(int encoding) {

        String password;
        if (encoding == 0)
            password = new String(this.getPassword());
        else if (encoding == 1)
            password = ConversionWizard.bytesToHex(new String(this.getPassword()).getBytes());
        else
            password = new String(Objects.requireNonNull(ConversionWizard.bytesToBase64(new String(this.getPassword()).getBytes())));

        if (password.length() == 0 || !PasswordEncodingField.isEncodingValid(encoding, new String(this.getPassword()))) {
            this.setBorder(UIManager.getBorder("TextField.border"));
            return;
        }

        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);

        this.setBorder(new LineBorder(Color.getHSBColor(1.0f / 3.0f / 4.0f * strength.getScore(), 1f, 0.7f), 2));
    }
}
