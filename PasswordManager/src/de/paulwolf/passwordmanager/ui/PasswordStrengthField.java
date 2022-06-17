package de.paulwolf.passwordmanager.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

public class PasswordStrengthField extends JPasswordField implements KeyListener {



    public PasswordStrengthField(int c) {

        this.setColumns(c);
        this.addKeyListener(this);
    }

    public void evaluatePassword() {

        float passwordScore = 0;

        char[] password = this.getPassword();
        String cleanPassword = new String(password).replaceAll("(.)\\1+", "$1");

        Pattern noDigitsAndSpecials = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
        Pattern noSpecialChars = Pattern.compile("[^a-z\\d ]", Pattern.CASE_INSENSITIVE);

        if (noDigitsAndSpecials.matcher(cleanPassword).find()) { // Contains more than just letters
            if (noSpecialChars.matcher(cleanPassword).find()) { // Contains special characters
                passwordScore += 20;
            } else {
                passwordScore -= 10;
            }
        }
        else {
            passwordScore -= 30;
        }

        passwordScore += cleanPassword.length() * 5;
        if (passwordScore > 100)
            passwordScore = 100;

        if (password.length == 0) {
            this.setBorder(UIManager.getBorder("TextField.border"));
            return;
        }

        this.setBorder(new LineBorder(Color.getHSBColor((passwordScore * 3.6f / 3.0f) / 360, 1f, 0.7f), 2));
    }

    @Override
    public void keyTyped(KeyEvent e) {
        evaluatePassword();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        evaluatePassword();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        evaluatePassword();
    }
}
