package de.paulwolf.passwordmanager.ui;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PasswordStrengthField extends JPasswordField implements KeyListener {

    public PasswordStrengthField(int c) {

        this.setColumns(c);
        this.addKeyListener(this);
    }

    public void evaluatePassword() {

        String password = new String(this.getPassword());

        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);

        if (password.length() == 0) {
            this.setBorder(UIManager.getBorder("TextField.border"));
            return;
        }

        this.setBorder(new LineBorder(Color.getHSBColor(1.0f / 3.0f / 4.0f * strength.getScore(), 1f, 0.7f), 2));
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        evaluatePassword();
    }
}
