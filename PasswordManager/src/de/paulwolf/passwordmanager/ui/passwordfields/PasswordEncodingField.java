package de.paulwolf.passwordmanager.ui.passwordfields;

import de.paulwolf.passwordmanager.wizards.ConversionWizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class PasswordEncodingField extends JComponent {

    private static final String[] ENCODINGS = {"ASCII", "HEX", "BASE64"};
    private int selectedEncoding;

    private final PasswordStrengthField passwordField = new PasswordStrengthField(30) {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {
            this.evaluatePassword(getSelectedEncoding());
        }
    };
    private final JButton encodingButton = new JButton(ENCODINGS[selectedEncoding]);

    public char[] getPassword() {
        return this.passwordField.getPassword();
    }

    public void setEchoChar(char c) {
        this.passwordField.setEchoChar(c);
    }

    public void setText(String t) {
        this.passwordField.setText(t);
    }

    public int getSelectedEncoding() {
        return this.selectedEncoding;
    }

    public void setEncoding(int e) {
        this.selectedEncoding = e;
        this.encodingButton.setText(ENCODINGS[selectedEncoding]);
        this.passwordField.evaluatePassword(this.selectedEncoding);
    }

    public PasswordStrengthField getPasswordField() {
        return this.passwordField;
    }

    public JButton getEncodingButton() {
        return this.encodingButton;
    }

    public static boolean isEncodingValid(int encoding, String str) {

        if (encoding == 0) {
            return str.matches("\\A\\p{ASCII}*\\z");
        } else if (encoding == 1) {
            return str.matches("-?[0-9a-fA-F]+") && str.length() % 2 == 0;
        } else {
            return ConversionWizard.base64ToBytes(str.getBytes()) != null;
        }
    }

    public PasswordEncodingField() {

        this.setLayout(new BorderLayout(5, 0));
        this.add(this.passwordField, BorderLayout.CENTER);
        this.add(this.encodingButton, BorderLayout.EAST);
        this.encodingButton.setPreferredSize(new Dimension(100, 24));
        this.encodingButton.addActionListener(e -> {
            this.selectedEncoding = (this.selectedEncoding + 1) % 3;
            this.encodingButton.setText(ENCODINGS[selectedEncoding]);
            this.passwordField.evaluatePassword(this.getSelectedEncoding());
        });
    }
}
