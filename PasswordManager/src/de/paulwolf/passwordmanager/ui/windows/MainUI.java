package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.information.WrongPasswordException;
import de.paulwolf.passwordmanager.ui.UIUtils;
import de.paulwolf.passwordmanager.ui.components.ScaledButton;
import de.paulwolf.passwordmanager.ui.components.ScaledComboBox;
import de.paulwolf.passwordmanager.ui.components.ScaledFileChooser;
import de.paulwolf.passwordmanager.ui.components.ScaledTextField;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Objects;

public class MainUI extends JFrame implements ActionListener, KeyListener {

    final JPanel wrapper;
    final ScaledFileChooser fileChooser;
    final ScaledButton browse, newDatabase, openDatabase;
    final ScaledTextField uri;
    public File databaseFile;
    ScaledComboBox<Object> box;

    public MainUI() {

        wrapper = new JPanel();
        fileChooser = new ScaledFileChooser();
        browse = new ScaledButton("Browse");
        newDatabase = new ScaledButton("Create New Database");
        openDatabase = new ScaledButton("Open Database At Entered Path");
        uri = new ScaledTextField();
        uri.setPreferredSize(Configuration.SCALED_PASSWORD_FIELD_SIZE);

        String[] compatibleFiles;
        try {
            compatibleFiles = FileWizard.getRecentlyOpenedFiles(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        box = new ScaledComboBox<>(compatibleFiles);
        box.addActionListener(e -> uri.setText(Objects.requireNonNull(box.getSelectedItem()).toString()));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files", "pmdtb");
        fileChooser.setFileFilter(filter);
        browse.addActionListener(this);
        openDatabase.addActionListener(this);
        newDatabase.addActionListener(this);

        GridBagConstraints gbc = new GridBagConstraints();
        wrapper.setLayout(new GridBagLayout());
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        wrapper.add(uri, gbc);

        gbc.gridy = 1;
        gbc.gridx = 1;
        wrapper.add(browse, UIUtils.createGBC(1, 1, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(box, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        wrapper.add(openDatabase, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        wrapper.add(newDatabase, gbc);

        uri.addKeyListener(this);

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Configuration.IMAGE);
        this.setTitle("PasswordManager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public boolean decryptDatabase(byte[] key) {

        try {
            if (FileWizard.openDatabase(databaseFile, key)) {
                this.setVisible(false);
                return true;
            }
        } catch (InvalidKeyException | FileNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | ParseException | IllegalStateException | LimitReachedException e) {
            e.printStackTrace();
        } catch (BadPaddingException | WrongPasswordException e) {
            return false;
        }

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == browse) {

            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {

                File file = fileChooser.getSelectedFile();
                uri.setText(file.getAbsoluteFile().toString());
            }
        }

        if (e.getSource() == openDatabase) {

            String tempURI = uri.getText();
            if (tempURI.length() > 6) {
                if (!tempURI.substring(tempURI.length() - 6).equalsIgnoreCase(".pmdtb")) tempURI += ".pmdtb";
            } else {
                tempURI += ".pmdtb";
            }
            File f = new File(tempURI);

            if (uri.getText().equals(""))
                f = new File((String) Objects.requireNonNull(box.getSelectedItem()));

            try {
                FileWizard.isCompatible(f);
                databaseFile = f;
                FileWizard.updateRecentlyOpened(f.getCanonicalPath());
                new OpenDatabaseUI(f.getCanonicalPath(), this);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid path or URI to a compatible file!", "Missing arguments", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if (e.getSource() == newDatabase) {

            new CreateDatabaseUI();
            this.setVisible(false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER) openDatabase.doClick();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
