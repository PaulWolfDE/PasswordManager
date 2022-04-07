package de.paulwolf.passwordmanager.ui;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class CreateDatabaseUI extends JFrame implements ActionListener, KeyListener {

    final JPanel wrapper = new JPanel();
    final JLabel eaLabel = new JLabel("Encryption Algorithm");
    final JLabel hashLabel = new JLabel("Hash Algorithm");
    final JLabel keyLabel = new JLabel("Master Key");
    final JLabel keyVerificationLabel = new JLabel("Repeat Master Key");
    final JLabel pathLabel = new JLabel("Save Database At");

    final JButton browseButton = new JButton("Browse");
    final JButton generateKey = new JButton("Generate Java Crypto Key");
    final JButton generateKey2 = new JButton("Generate Random Key");
    final JButton createDatabase = new JButton("Create Database");

    final JComboBox<String> eaBox = new JComboBox<>(Main.ENCRYPTION_ALGORITHMS);
    final JComboBox<String> hashBox = new JComboBox<>(Main.HASH_ALGORITHMS);

    final JToggleButton showKey = new JToggleButton("Show");
    final JToggleButton showKeyVerification = new JToggleButton("Show");

    final JPasswordField keyField = new JPasswordField(30);
    final JPasswordField keyVerificationField = new JPasswordField(30);

    final JTextField pathField = new JTextField(20);

    final JFileChooser fileChooser = new JFileChooser();

    public CreateDatabaseUI() {

        this.setTitle("PasswordManager - Create New Database");

        GridBagConstraints gbc = new GridBagConstraints();
        wrapper.setLayout(new GridBagLayout());
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(eaLabel, gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        wrapper.add(eaBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        wrapper.add(hashLabel, gbc);
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        wrapper.add(hashBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        wrapper.add(keyLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        wrapper.add(keyField, gbc);
        gbc.gridx = 2;
        wrapper.add(showKey, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        wrapper.add(keyVerificationLabel, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        wrapper.add(keyVerificationField, gbc);
        gbc.gridx = 2;
        wrapper.add(showKeyVerification, gbc);

        gbc.weightx = 0.5;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 4;
        wrapper.add(pathLabel, gbc);
        gbc.gridx = 1;
        wrapper.add(pathField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        wrapper.add(browseButton, gbc);

        gbc.weightx = 1;
        gbc.gridx = 2;
        gbc.gridy = 5;
        wrapper.add(generateKey2, gbc);
        gbc.gridwidth = 2;
        gbc.weightx = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;
        wrapper.add(generateKey, gbc);

        gbc.weightx = 1;
        gbc.gridwidth = 3;
        gbc.gridx = 0;
        gbc.gridy = 6;
        wrapper.add(createDatabase, gbc);

        browseButton.addActionListener(this);
        generateKey.addActionListener(this);
        generateKey2.addActionListener(this);
        createDatabase.addActionListener(this);
        showKey.addActionListener(this);
        showKeyVerification.addActionListener(this);
        pathField.addKeyListener(this);

        keyField.setFont(new Font("Consolas", Font.PLAIN, 14));
        keyField.putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyField.setPreferredSize(new Dimension(400, 26));
        keyVerificationField.setFont(new Font("Consolas", Font.PLAIN, 14));
        keyVerificationField.putClientProperty("JPasswordField.cutCopyAllowed", true);
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

    }

    static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.US_ASCII.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == generateKey2) {

            String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\"";
            SecureRandom sr = new SecureRandom();
            StringBuilder password = new StringBuilder();
            for (int i = 0; i < 24; i++)
                password.append(alphabet.charAt(sr.nextInt(alphabet.length())));
            keyField.setText(password.toString());
            keyVerificationField.setText(password.toString());
        }

        if (e.getSource() == generateKey) {

            try {
                keyField.setText(Base64.getEncoder().encodeToString(KeyGenerator.getInstance("AES").generateKey().getEncoded()));
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            keyVerificationField.setText(new String(keyField.getPassword()));
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

            if (Arrays.equals(toBytes(keyField.getPassword()), toBytes(keyVerificationField.getPassword()))) {

                if (!pathField.getText().equals("") && !Arrays.toString(toBytes(keyField.getPassword())).equals("")) {

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
                        boolean fileCreation = db.getPath().createNewFile();
                        if (!fileCreation) System.out.println("File could not be created.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    db.setMasterKey(toBytes(keyField.getPassword()));
                    db.setHashAlgorithm(Objects.requireNonNull(hashBox.getSelectedItem()).toString());
                    db.setEncryptionAlgorithm(Objects.requireNonNull(eaBox.getSelectedItem()).toString());
                    db.addEntry(new Entry("Example Entry", "John Doe", "john.doe@example.com", "password123", "Note"));

                    this.setVisible(false);
                    new DatabaseUI(db);

                    try {
                        FileWizard.saveDatabase(db, db.getPath());
                    } catch (NoSuchAlgorithmException | IOException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | IllegalStateException | LimitReachedException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments", JOptionPane.INFORMATION_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error", JOptionPane.ERROR_MESSAGE);
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
}
