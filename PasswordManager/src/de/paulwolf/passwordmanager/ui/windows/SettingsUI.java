package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.ui.UIUtils;
import de.paulwolf.passwordmanager.ui.passwordfields.PasswordEncodingField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Objects;

public class SettingsUI extends JFrame implements ActionListener, PasswordAcceptingUI {

    final JPanel wrapper = new JPanel();
    final JLabel eaLabel = new JLabel("Encryption Algorithm");
    final JComboBox<String> eaBox = new JComboBox<>(Main.ENCRYPTION_ALGORITHMS);
    final JLabel hashLabel = new JLabel("Hash Algorithm");
    final JComboBox<String> hashBox = new JComboBox<>(Main.HASH_ALGORITHMS);
    final PasswordEncodingField keyField = new PasswordEncodingField();
    final JToggleButton showKey = new JToggleButton("Show");
    final JLabel keyLabel = new JLabel("Master Key");
    final PasswordEncodingField keyVerificationField = new PasswordEncodingField();
    final JToggleButton showKeyVerification = new JToggleButton("Show");
    final JLabel keyVerificationLabel = new JLabel("Repeat Master Key");
    final JButton generatePassword = new JButton("Generate Password");
    final JButton button = new JButton("Save Changes");

    final JFrame f;
    final JPanel p;
    final PasswordEncodingField pf;
    final JToggleButton b;
    final JButton b2;

    public SettingsUI() {

        this.setTitle("Settings");

        f = new JFrame("Enter Master Password");
        p = new JPanel();
        pf = new PasswordEncodingField();
        b = new JToggleButton("Show");
        b2 = new JButton("Submit Password");

        p.setLayout(new GridBagLayout());
        p.add(pf, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1, 1.0));
        p.add(b, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 1, 1, .0));
        p.add(b2, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 2, 1));

        b.addActionListener(this);
        b2.addActionListener(this);
        pf.getPasswordField().setFont(new Font("Consolas", Font.PLAIN, 14));
        pf.getPasswordField().setDisplayPasswordStrength(false);
        pf.setPreferredSize(new Dimension(400, 26));

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
        f.setIconImage(Main.IMAGE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }


    private void go() {

        wrapper.setLayout(new GridBagLayout());

        wrapper.add(eaLabel, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(eaBox, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(hashLabel, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(hashBox, UIUtils.createGBC(1, 1, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(keyLabel, UIUtils.createGBC(0, 2, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(keyField, UIUtils.createGBC(1, 2, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(showKey, UIUtils.createGBC(2, 2, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(keyVerificationLabel, UIUtils.createGBC(0, 3, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(keyVerificationField, UIUtils.createGBC(1, 3, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(showKeyVerification, UIUtils.createGBC(2, 3, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(generatePassword, UIUtils.createGBC(0, 4, GridBagConstraints.HORIZONTAL, 3, 1));
        wrapper.add(button, UIUtils.createGBC(0, 5, GridBagConstraints.HORIZONTAL, 3, 1));

        showKey.addActionListener(this);
        showKeyVerification.addActionListener(this);
        button.addActionListener(this);
        generatePassword.addActionListener(this);
        keyField.getPasswordField().setFont(new Font("Consolas", Font.PLAIN, 14));
        keyField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyField.setPreferredSize(new Dimension(400, 26));
        keyVerificationField.getPasswordField().setFont(new Font("Consolas", Font.PLAIN, 14));
        keyVerificationField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyVerificationField.setPreferredSize(new Dimension(400, 26));

        eaBox.setSelectedItem(DatabaseUI.database.getEncryptionAlgorithm());
        hashBox.setSelectedItem(DatabaseUI.database.getHashAlgorithm());
        keyField.setText(new String(pf.getPassword()));
        keyField.setEncoding(pf.getSelectedEncoding());
        keyField.getPasswordField().evaluatePassword(keyField.getSelectedEncoding());
        keyVerificationField.setText(new String(pf.getPassword()));
        keyVerificationField.setEncoding(pf.getSelectedEncoding());
        keyVerificationField.getPasswordField().evaluatePassword(keyVerificationField.getSelectedEncoding());

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Main.IMAGE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.keyField.getEncodingButton().addActionListener(e14 -> this.keyVerificationField.setEncoding((this.keyVerificationField.getSelectedEncoding() + 1) % 3));
        this.keyVerificationField.getEncodingButton().addActionListener(e15 -> this.keyField.setEncoding((this.keyField.getSelectedEncoding() + 1) % 3));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b) {

            if (b.isSelected())
                pf.setEchoChar((char) 0);
            else
                pf.setEchoChar(Main.ECHO_CHAR);
        } else if (e.getSource() == b2) {

            if (EncodingWizard.isEncodingValid(pf.getSelectedEncoding(), new String(pf.getPassword()))) {

                String password = EncodingWizard.decodeString(pf.getSelectedEncoding(), new String(pf.getPassword()));

                if (password.equals(new String(DatabaseUI.database.getMasterKey()))) {
                    this.setVisible(false);
                    go();
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
                keyField.setEchoChar(Main.ECHO_CHAR);
        } else if (e.getSource() == showKeyVerification) {
            if (showKeyVerification.isSelected())
                keyVerificationField.setEchoChar((char) 0);
            else
                keyVerificationField.setEchoChar(Main.ECHO_CHAR);
        } else if (e.getSource() == button) {

            if (Arrays.equals(EncodingWizard.charsToBytes(keyField.getPassword()), EncodingWizard.charsToBytes(keyVerificationField.getPassword()))) {

                if (keyField.getPassword().length != 0) {

                    DatabaseUI.database.setMasterKey(EncodingWizard.charsToBytes(keyField.getPassword()));
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
            keyField.setText(EncodingWizard.bytesToHex(password.getBytes()));
            keyVerificationField.setText(new String(keyField.getPassword()));
        } else {
            keyField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(password.getBytes()))));
            keyVerificationField.setText(new String(keyField.getPassword()));
        }

        keyField.getPasswordField().evaluatePassword(keyField.getSelectedEncoding());
        keyVerificationField.getPasswordField().evaluatePassword(keyVerificationField.getSelectedEncoding());
    }
}
