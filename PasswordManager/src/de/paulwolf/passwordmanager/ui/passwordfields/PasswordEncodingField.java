package de.paulwolf.passwordmanager.ui.passwordfields;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.ui.components.ScaledButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PasswordEncodingField extends JComponent {

    private static final String[] ENCODINGS = {"UTF-8", "HEX", "BASE64"};
    private int selectedEncoding;

    private final PasswordStrengthField passwordField = new PasswordStrengthField() {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {
            this.evaluatePassword(getSelectedEncoding());
        }
    };
    private final ScaledButton encodingButton = new ScaledButton(ENCODINGS[selectedEncoding]);

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
        encodingButton.setPreferredSize(new Dimension(
                (int) (Configuration.SCALED_BUTTON_SIZE.getWidth() - 50.0 / 1920 * Toolkit.getDefaultToolkit().getScreenSize().getWidth()),
                (int) (Configuration.SCALED_BUTTON_SIZE.getHeight())
        ));
        this.add(this.passwordField, BorderLayout.CENTER);
        this.add(this.encodingButton, BorderLayout.EAST);
        this.encodingButton.addActionListener(e -> {
            this.selectedEncoding = (this.selectedEncoding + 1) % 3;
            this.encodingButton.setText(ENCODINGS[selectedEncoding]);
            this.passwordField.evaluatePassword(this.getSelectedEncoding());
        });
    }
}
