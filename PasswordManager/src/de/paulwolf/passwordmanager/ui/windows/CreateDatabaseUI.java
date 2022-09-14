package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.ui.*;
import de.paulwolf.passwordmanager.ui.components.*;
import de.paulwolf.passwordmanager.ui.passwordfields.PasswordEncodingField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class CreateDatabaseUI extends JFrame implements PasswordAcceptingUI, ActionListener, KeyListener {

    final JPanel wrapper = new JPanel();
    final ScaledLabel eaLabel = new ScaledLabel("Encryption Algorithm");
    final ScaledLabel hashLabel = new ScaledLabel("Hash Algorithm");
    final ScaledLabel keyLabel = new ScaledLabel("Master Key");
    final ScaledLabel keyVerificationLabel = new ScaledLabel("Repeat Master Key");
    final ScaledLabel pathLabel = new ScaledLabel("Save Database At");

    final ScaledButton browseButton = new ScaledButton("Browse");
    final ScaledButton generateKey = new ScaledButton("Generate Random Password");
    final ScaledButton createDatabase = new ScaledButton("Create Database");

    final ScaledComboBox<String> eaBox = new ScaledComboBox<>(Configuration.ENCRYPTION_ALGORITHMS);
    final ScaledComboBox<String> hashBox = new ScaledComboBox<>(Configuration.HASH_ALGORITHMS);

    final ScaledToggleButton showKey = new ScaledToggleButton("Show");
    final ScaledToggleButton showKeyVerification = new ScaledToggleButton("Show");

    final PasswordEncodingField keyField = new PasswordEncodingField();
    final PasswordEncodingField keyVerificationField = new PasswordEncodingField();

    final ScaledTextField pathField = new ScaledTextField();

    final ScaledFileChooser fileChooser = new ScaledFileChooser();

    public CreateDatabaseUI() {

        this.setTitle("PasswordManager - Create New Database");

        wrapper.setLayout(new GridBagLayout());

        wrapper.add(eaLabel, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(eaBox, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(hashLabel, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(hashBox, UIUtils.createGBC(1, 1, GridBagConstraints.HORIZONTAL, 2, 1));

        wrapper.add(keyLabel, UIUtils.createGBC(0, 2, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(keyField, UIUtils.createGBC(1, 2, GridBagConstraints.HORIZONTAL, 1, 1, 1.0));
        wrapper.add(showKey, UIUtils.createGBC(2, 2, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(keyVerificationLabel, UIUtils.createGBC(0, 3, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(keyVerificationField, UIUtils.createGBC(1, 3, GridBagConstraints.HORIZONTAL, 1, 1, 1.0));
        wrapper.add(showKeyVerification, UIUtils.createGBC(2, 3, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(pathLabel, UIUtils.createGBC(0, 4, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(pathField, UIUtils.createGBC(1, 4, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(browseButton, UIUtils.createGBC(2, 4, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        wrapper.add(generateKey, UIUtils.createGBC(0, 5, GridBagConstraints.HORIZONTAL, 3, 1));

        wrapper.add(createDatabase, UIUtils.createGBC(0, 6, GridBagConstraints.HORIZONTAL, 3, 1));

        browseButton.addActionListener(this);
        generateKey.addActionListener(this);
        createDatabase.addActionListener(this);
        showKey.addActionListener(this);
        showKeyVerification.addActionListener(this);
        pathField.addKeyListener(this);

        keyField.getPasswordField().setFont(Configuration.MONOSPACE_FONT);
        keyField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyField.setPreferredSize(Configuration.SCALED_PASSWORD_FIELD_SIZE);
        keyVerificationField.getPasswordField().setFont(Configuration.MONOSPACE_FONT);
        keyVerificationField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyVerificationField.setPreferredSize(Configuration.SCALED_PASSWORD_FIELD_SIZE);
        pathField.setFont(Configuration.STANDARD_FONT);
        pathField.setPreferredSize(Configuration.SCALED_TEXT_FIELD_SIZE);

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Configuration.IMAGE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fileChooser.setSelectedFile(new File("Database.pmdtb"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files", "pmdtb");
        fileChooser.setFileFilter(filter);

        this.keyField.getEncodingButton().addActionListener(
                e -> this.keyVerificationField.setEncoding((this.keyVerificationField.getSelectedEncoding() + 1) % 3)
        );
        this.keyVerificationField.getEncodingButton().addActionListener(
                e -> this.keyField.setEncoding((this.keyField.getSelectedEncoding() + 1) % 3)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == generateKey) {
            new PasswordGeneratorUI(this, new String(this.keyField.getPassword()), this.keyField.getSelectedEncoding());
        }

        if (e.getSource() == showKey) {

            if (showKey.isSelected()) keyField.setEchoChar((char) 0);
            else keyField.setEchoChar(Configuration.ECHO_CHAR);
        }

        if (e.getSource() == showKeyVerification) {

            if (showKeyVerification.isSelected()) keyVerificationField.setEchoChar((char) 0);
            else keyVerificationField.setEchoChar(Configuration.ECHO_CHAR);
        }

        if (e.getSource() == browseButton) {

            int returnVal = fileChooser.showSaveDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                pathField.setText(file.getAbsoluteFile().toString());

            }
        }

        if (e.getSource() == createDatabase) {

            Path path = Paths.get(pathField.getText());

            if (EncodingWizard.isEncodingValid(keyField.getSelectedEncoding(), new String(keyField.getPassword()))) {

                if (Arrays.equals(keyField.getPassword(), keyVerificationField.getPassword())) {

                    if (!pathField.getText().equals("") && keyField.getPassword().length > 0) {

                        Database db = new Database();

                        String dbPath = path.toString();
                        if (dbPath.length() > 6) {

                            if (dbPath.substring(dbPath.length() - 6).equalsIgnoreCase(".pmdtb")) {
                                db.setPath(new File(path.toString()));
                            } else {
                                String tpath = path.toString();
                                tpath += ".pmdtb";
                                db.setPath(new File(tpath));
                            }
                        } else {
                            String tpath = path.toString();
                            tpath += ".pmdtb";
                            db.setPath(new File(tpath));
                        }

                        try {
                            db.getPath().createNewFile();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        db.setMasterKey(new SecretKeySpec(
                                EncodingWizard.decodeString(keyField.getSelectedEncoding(), new String(keyField.getPassword())).getBytes(Configuration.STANDARD_CHARSET),
                                Objects.requireNonNull(((String) eaBox.getSelectedItem())).contains("Blowfish") ? "Blowfish" :"AES"
                        ));
                        db.setHashAlgorithm(Objects.requireNonNull(hashBox.getSelectedItem()).toString());
                        db.setEncryptionAlgorithm(Objects.requireNonNull(eaBox.getSelectedItem()).toString());
                        db.addEntry(new Entry("Example Entry", "John Doe", "john.doe@example.com", "password123".getBytes(Configuration.STANDARD_CHARSET), "Note"));
                        db.addEntry(new Entry(Configuration.BACKUP_TITLE, "", "", ".".getBytes(Configuration.STANDARD_CHARSET), ""));

                        this.setVisible(false);
                        new DatabaseUI(db, this);

                        try {
                            FileWizard.saveDatabase(db, db.getPath());
                        } catch (NoSuchAlgorithmException | IOException | InvalidKeyException | NoSuchPaddingException |
                                 InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException |
                                 IllegalStateException | LimitReachedException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "At least one key is not correctly encoded!", "Malformed encoding", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) createDatabase.doClick();
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
            keyField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(password.getBytes(Configuration.STANDARD_CHARSET))), Configuration.STANDARD_CHARSET));
            keyVerificationField.setText(new String(keyField.getPassword()));
        }

        keyField.getPasswordField().evaluatePassword(keyField.getSelectedEncoding());
        keyVerificationField.getPasswordField().evaluatePassword(keyVerificationField.getSelectedEncoding());
    }
}
