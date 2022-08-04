package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.ui.*;
import de.paulwolf.passwordmanager.ui.passwordfields.PasswordEncodingField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
    final JLabel eaLabel = new JLabel("Encryption Algorithm");
    final JLabel hashLabel = new JLabel("Hash Algorithm");
    final JLabel keyLabel = new JLabel("Master Key");
    final JLabel keyVerificationLabel = new JLabel("Repeat Master Key");
    final JLabel pathLabel = new JLabel("Save Database At");

    final JButton browseButton = new JButton("Browse");
    final JButton generateKey = new JButton("Generate Random Password");
    final JButton createDatabase = new JButton("Create Database");

    final JComboBox<String> eaBox = new JComboBox<>(Main.ENCRYPTION_ALGORITHMS);
    final JComboBox<String> hashBox = new JComboBox<>(Main.HASH_ALGORITHMS);

    final JToggleButton showKey = new JToggleButton("Show");
    final JToggleButton showKeyVerification = new JToggleButton("Show");

    final PasswordEncodingField keyField = new PasswordEncodingField();
    final PasswordEncodingField keyVerificationField = new PasswordEncodingField();

    final JTextField pathField = new JTextField(20);

    final JFileChooser fileChooser = new JFileChooser();

    public CreateDatabaseUI() {

        this.setTitle("PasswordManager - Create New Database");

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

        keyField.getPasswordField().setFont(new Font("Consolas", Font.PLAIN, 14));
        keyField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyField.setPreferredSize(new Dimension(400, 26));
        keyVerificationField.getPasswordField().setFont(new Font("Consolas", Font.PLAIN, 14));
        keyVerificationField.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyVerificationField.setPreferredSize(new Dimension(400, 26));
        pathField.setFont(new Font("Consolas", Font.PLAIN, 14));
        pathField.setPreferredSize(new Dimension(400, 26));

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Main.IMAGE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fileChooser.setSelectedFile(new File("Database.pmdtb"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files", "pmdtb");
        fileChooser.setFileFilter(filter);

        this.keyField.getEncodingButton().addActionListener(e -> this.keyVerificationField.setEncoding((this.keyVerificationField.getSelectedEncoding() + 1) % 3));
        this.keyVerificationField.getEncodingButton().addActionListener(e -> this.keyField.setEncoding((this.keyField.getSelectedEncoding() + 1) % 3));

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == generateKey) {
            new PasswordGeneratorUI(this, new String(this.keyField.getPassword()), this.keyField.getSelectedEncoding());
        }

        if (e.getSource() == showKey) {

            if (showKey.isSelected()) keyField.setEchoChar((char) 0);
            else keyField.setEchoChar(Main.ECHO_CHAR);
        }

        if (e.getSource() == showKeyVerification) {

            if (showKeyVerification.isSelected()) keyVerificationField.setEchoChar((char) 0);
            else keyVerificationField.setEchoChar(Main.ECHO_CHAR);
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

                if (Arrays.equals(EncodingWizard.charsToBytes(keyField.getPassword()), EncodingWizard.charsToBytes(keyVerificationField.getPassword()))) {

                    if (!pathField.getText().equals("") && !Arrays.toString(EncodingWizard.charsToBytes(keyField.getPassword())).equals("")) {

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

                        db.setMasterKey(EncodingWizard.decodeString(keyField.getSelectedEncoding(), new String(keyField.getPassword())).getBytes(Main.STANDARD_CHARSET));
                        db.setHashAlgorithm(Objects.requireNonNull(hashBox.getSelectedItem()).toString());
                        db.setEncryptionAlgorithm(Objects.requireNonNull(eaBox.getSelectedItem()).toString());
                        db.addEntry(new Entry("Example Entry", "John Doe", "john.doe@example.com", "password123", "Note"));
                        db.addEntry(new Entry(Main.BACKUP_TITLE, "", "", ".", ""));

                        this.setVisible(false);
                        new DatabaseUI(db);

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
            keyField.setText(EncodingWizard.bytesToHex(password.getBytes(Main.STANDARD_CHARSET)));
            keyVerificationField.setText(new String(keyField.getPassword()));
        } else {
            keyField.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(password.getBytes(Main.STANDARD_CHARSET))), Main.STANDARD_CHARSET));
            keyVerificationField.setText(new String(keyField.getPassword()));
        }

        keyField.getPasswordField().evaluatePassword(keyField.getSelectedEncoding());
        keyVerificationField.getPasswordField().evaluatePassword(keyVerificationField.getSelectedEncoding());
    }
}
