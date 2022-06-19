package de.paulwolf.passwordmanager.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.regex.Pattern;

public class PasswordStrengthField extends JPasswordField implements KeyListener {

    private static double log2(double N) {

        return Math.log(N) / Math.log(2);
    }

    public PasswordStrengthField(int c) {

        this.setColumns(c);
        this.addKeyListener(this);
    }

    public void evaluatePassword() {

        double passwordEntropy;
        int characterPool = 0;

        String password = new String(this.getPassword());

        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern uppercase = Pattern.compile("[A-Z]");
        Pattern digits = Pattern.compile("\\d");
        Pattern specials = Pattern.compile("[!\"$%&/()=?{\\[\\]}\\\\+*~#\\-_.:,;|<>']");

        if (lowercase.matcher(password).find())
            characterPool += 26;
        if (uppercase.matcher(password).find())
            characterPool += 26;
        if (digits.matcher(password).find())
            characterPool += 10;
        if (specials.matcher(password).find())
            characterPool += 29;

        passwordEntropy = log2(Math.pow(characterPool, password.length()));
        passwordEntropy = Math.min(128, passwordEntropy);

        if (password.length() == 0) {
            this.setBorder(UIManager.getBorder("TextField.border"));
            return;
        }

        this.setBorder(new LineBorder(Color.getHSBColor((float) ((passwordEntropy / 1.28d * 3.6d) / 3 / 360), 1f, 0.7f), 2));
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
