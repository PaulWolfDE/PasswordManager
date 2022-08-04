package de.paulwolf.passwordmanager.ui.passwordfields;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PasswordEncodingField extends JComponent {

    private static final String[] ENCODINGS = {"UTF-8", "HEX", "BASE64"};
    private int selectedEncoding;

    private final PasswordStrengthField passwordField = new PasswordStrengthField(20) {
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
