package de.paulwolf.passwordmanager.ui;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.WrongPasswordException;
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
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MainUI extends JFrame implements ActionListener, KeyListener {

    final ArrayList<String> data = new ArrayList<>();
    final JPanel wrapper;
    final JFileChooser fileChooser;
    final JButton browse;
    final JButton newDatabase;
    final JButton openDatabase;
    final JTextField uri;
    public File databaseFile;
    JComboBox<Object> box;

    public MainUI() {

        wrapper = new JPanel();
        fileChooser = new JFileChooser();
        browse = new JButton("Browse");
        newDatabase = new JButton("Create New Database");
        openDatabase = new JButton("Open Database At Entered Path");
        uri = new JTextField(25);

        File recentlyOpened;
        if (System.getenv("Appdata") == null)
            recentlyOpened = new File(System.getProperty("user.home") + "/PasswordManager/.pmrc"); // Linux
        else
            recentlyOpened = new File(System.getenv("Appdata") + "/PasswordManager/.pmrc"); // Windows

        if (Files.exists(recentlyOpened.toPath())) {
            Scanner scanner = null;
            try {
                scanner = new Scanner(recentlyOpened);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (true) {
                assert scanner != null;
                if (!scanner.hasNextLine()) break;
                String s = scanner.nextLine();
                if (Files.exists(new File(s).toPath())) {
                    File file = new File(s);
                    if (Files.exists(file.toPath())) {
                        Scanner scanner2 = null;
                        try {
                            scanner2 = new Scanner(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        assert scanner2 != null;
                        String ver = scanner2.nextLine();
                        if (FileWizard.isCompatible(ver))
                            if (!data.contains(file.getAbsolutePath())) data.add(file.getAbsolutePath());
                        scanner2.close();
                    }
                }
            }
            scanner.close();
        }

        System.out.println(data.toString());

        box = new JComboBox<>(data.toArray());
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
        wrapper.add(browse, gbc);

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
        uri.setPreferredSize(new Dimension(400, 26));

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Main.IMAGE);
        this.setTitle("PasswordManager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    public boolean openDatabaseWithPassword(byte[] password) {

        try {
            if (FileWizard.openDatabase(databaseFile, password)) {
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

            Path path = Paths.get(tempURI);

            if (Files.exists(path) && !uri.getText().equals("")) {

                new OpenDatabaseUI(path.toFile().getAbsolutePath());
                databaseFile = path.toFile();

                File rc, rcDir;
                if (System.getenv("Appdata") == null) { // Linux
                    rc = new File(System.getProperty("user.home") + "/PasswordManager/.pmrc");
                    rcDir = new File(System.getProperty("user.home") + "/PasswordManager/");
                } else { // Windows
                    rc = new File(System.getenv("Appdata") + "/PasswordManager/.pmrc");
                    rcDir = new File(System.getenv("Appdata") + "/PasswordManager/");
                }
                boolean directoryCreation = rcDir.mkdirs();
                if (!directoryCreation) System.out.println("Directory creation failed.");
                try {
                    boolean fileCreation = rc.createNewFile();
                    if (!fileCreation) System.out.println("File could not be created.");
                    FileWriter writer = new FileWriter(rc);
                    Object[] arr = data.toArray();
                    writer.write(databaseFile.getAbsolutePath() + "\n");
                    for (Object o : arr) {
                        if (!new File(o.toString()).getAbsolutePath().equals(databaseFile.getAbsolutePath()))
                            writer.write(new File(o.toString()).getAbsolutePath() + "\n");
                    }

                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else
                JOptionPane.showMessageDialog(null, "Please enter a valid path or URI!", "Missing arguments", JOptionPane.INFORMATION_MESSAGE);
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
