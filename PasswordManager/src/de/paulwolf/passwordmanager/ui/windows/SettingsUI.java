package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.ui.UIUtils;
import de.paulwolf.passwordmanager.ui.components.ScaledButton;
import de.paulwolf.passwordmanager.ui.components.ScaledComboBox;
import de.paulwolf.passwordmanager.ui.components.ScaledLabel;
import de.paulwolf.passwordmanager.ui.components.ScaledToggleButton;
import de.paulwolf.passwordmanager.ui.passwordfields.PasswordEncodingField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;
import de.paulwolf.passwordmanager.wizards.FileWizard;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class SettingsUI extends JFrame implements ActionListener, PasswordAcceptingUI {

    final JPanel wrapper = new JPanel();
    final ScaledLabel eaLabel = new ScaledLabel("Encryption Algorithm");
    final ScaledComboBox<String> eaBox = new ScaledComboBox<>(Configuration.ENCRYPTION_ALGORITHMS);
    final ScaledLabel hashLabel = new ScaledLabel("Hash Algorithm");
    final ScaledComboBox<String> hashBox = new ScaledComboBox<>(Configuration.HASH_ALGORITHMS);
    final ScaledLabel themeLabel = new ScaledLabel("GUI Theme");
    final ScaledComboBox<String> themeBox = new ScaledComboBox<>(Configuration.FLATLAF_THEMES);
    final PasswordEncodingField keyField = new PasswordEncodingField();
    final ScaledToggleButton showKey = new ScaledToggleButton("Show");
    final ScaledLabel keyLabel = new ScaledLabel("Master Key");
    final PasswordEncodingField keyVerificationField = new PasswordEncodingField();
    final ScaledToggleButton showKeyVerification = new ScaledToggleButton("Show");
    final ScaledLabel keyVerificationLabel = new ScaledLabel("Repeat Master Key");
    final ScaledButton generatePassword = new ScaledButton("Generate Password");
    final ScaledButton button = new ScaledButton("Save Changes");

    final JFrame f;
    final JPanel p;
    final PasswordEncodingField pf;
    final ScaledToggleButton b;
    final ScaledButton b2;

    public SettingsUI(Component parent) {

        this.setTitle("Settings");

        f = new JFrame("Enter Master Password");
        p = new JPanel();
        pf = new PasswordEncodingField();
        b = new ScaledToggleButton("Show");
        b2 = new ScaledButton("Submit Password");

        p.setLayout(new GridBagLayout());
        p.add(pf, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 1.0));
        p.add(b, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 1, 1, .0));
        p.add(b2, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 2, 1));

        b.addActionListener(this);
        b2.addActionListener(this);
        pf.getPasswordField().setDisplayPasswordStrength(false);

        pf.getPasswordField().addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    b2.doClick();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        f.add(p);
        f.pack();
        f.setMinimumSize(f.getSize());
        f.setIconImage(Configuration.IMAGE);
        f.setLocationRelativeTo(parent);
        f.setVisible(true);
    }


    private void go(Component parent) {

        wrapper.setLayout(new GridBagLayout());

        wrapper.add(eaLabel, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(eaBox, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(hashLabel, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(hashBox, UIUtils.createGBC(1, 1, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(themeLabel, UIUtils.createGBC(0, 2, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(themeBox, UIUtils.createGBC(1, 2, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(keyLabel, UIUtils.createGBC(0, 3, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(keyField, UIUtils.createGBC(1, 3, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(showKey, UIUtils.createGBC(2, 3, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(keyVerificationLabel, UIUtils.createGBC(0, 4, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(keyVerificationField, UIUtils.createGBC(1, 4, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(showKeyVerification, UIUtils.createGBC(2, 4, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(generatePassword, UIUtils.createGBC(0, 5, GridBagConstraints.HORIZONTAL, 3, 1));

        wrapper.add(button, UIUtils.createGBC(0, 6, GridBagConstraints.HORIZONTAL, 3, 1));

        showKey.addActionListener(this);
        showKeyVerification.addActionListener(this);
        button.addActionListener(this);
        generatePassword.addActionListener(this);
        keyField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyVerificationField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);

        eaBox.setSelectedItem(DatabaseUI.database.getEncryptionAlgorithm());
        hashBox.setSelectedItem(DatabaseUI.database.getHashAlgorithm());
        themeBox.setSelectedItem(Configuration.ACTIVE_THEME);
        keyField.setText(new String(pf.getPassword()));
        keyField.setEncoding(pf.getSelectedEncoding());
        keyField.getPasswordField().evaluatePassword(keyField.getSelectedEncoding());
        keyVerificationField.setText(new String(pf.getPassword()));
        keyVerificationField.setEncoding(pf.getSelectedEncoding());
        keyVerificationField.getPasswordField().evaluatePassword(keyVerificationField.getSelectedEncoding());

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Configuration.IMAGE);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);

        this.keyField.getEncodingButton().addActionListener(e14 -> this.keyVerificationField.setEncoding((this.keyVerificationField.getSelectedEncoding() + 1) % 3));
        this.keyVerificationField.getEncodingButton().addActionListener(e15 -> this.keyField.setEncoding((this.keyField.getSelectedEncoding() + 1) % 3));
        this.themeBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                try {
                    Configuration.setTheme((String) e.getItem());
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                SwingUtilities.updateComponentTreeUI(this);
                SwingUtilities.updateComponentTreeUI(Main.dui);
                try {
                    FileWizard.updateSelectedTheme(Configuration.ACTIVE_THEME);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b) {

            if (b.isSelected())
                pf.setEchoChar((char) 0);
            else
                pf.setEchoChar(Configuration.ECHO_CHAR);
        } else if (e.getSource() == b2) {

            if (EncodingWizard.isEncodingValid(pf.getSelectedEncoding(), new String(pf.getPassword()))) {

                String password = EncodingWizard.decodeString(pf.getSelectedEncoding(), new String(pf.getPassword()));

                if (password.equals(new String(DatabaseUI.database.getMasterKey(), Configuration.STANDARD_CHARSET))) {
                    this.f.setVisible(false);
                    go(this);
                } else
                    JOptionPane.showMessageDialog(null, "The entered password is incorrect!", "Insufficient credentials",
                            JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "The key is not correctly encoded!", "Malformed encoding", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == showKey) {
            if (showKey.isSelected())
                keyField.setEchoChar((char) 0);
            else
                keyField.setEchoChar(Configuration.ECHO_CHAR);
        } else if (e.getSource() == showKeyVerification) {
            if (showKeyVerification.isSelected())
                keyVerificationField.setEchoChar((char) 0);
            else
                keyVerificationField.setEchoChar(Configuration.ECHO_CHAR);
        } else if (e.getSource() == button) {

            if (Arrays.equals(EncodingWizard.charsToBytes(keyField.getPassword()), EncodingWizard.charsToBytes(keyVerificationField.getPassword()))) {

                if (keyField.getPassword().length != 0) {

                    DatabaseUI.database.setMasterKey(new SecretKeySpec(
                            EncodingWizard.decodeString(keyField.getSelectedEncoding(), new String(keyField.getPassword())).getBytes(Configuration.STANDARD_CHARSET),
                            Objects.requireNonNull(((String) eaBox.getSelectedItem())).contains("Blowfish") ? "Blowfish" :"AES"
                    ));
                    DatabaseUI.database.setHashAlgorithm((String) hashBox.getSelectedItem());
                    DatabaseUI.database.setEncryptionAlgorithm((String) eaBox.getSelectedItem());

                    this.setVisible(false);
                } else
                    JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments",
                            JOptionPane.INFORMATION_MESSAGE);

            } else
                JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error",
                        JOptionPane.ERROR_MESSAGE);
        } else if (e.getSource() == generatePassword) {
            new PasswordGeneratorUI(this, new String(keyField.getPassword()), keyField.getSelectedEncoding());
        }
    }

    @Override
    public void setPassword(String password) {

        if (keyField.getSelectedEncoding() == 0) {
            keyField.setText(password);
            keyVerificationField.setText(password);
        } else if (keyField.getSelectedEncoding() == 1) {
            keyField.setText(EncodingWizard.bytesToHex(password.getBytes(Configuration.STANDARD_CHARSET)));
            keyVerificationField.setText(new String(keyField.getPassword()));
        } else {
            keyField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(password.getBytes(Configuration.STANDARD_CHARSET))), StandardCharsets.US_ASCII));
            keyVerificationField.setText(new String(keyField.getPassword()));
        }

        keyField.getPasswordField().evaluatePassword(keyField.getSelectedEncoding());
        keyVerificationField.getPasswordField().evaluatePassword(keyVerificationField.getSelectedEncoding());
    }
}
