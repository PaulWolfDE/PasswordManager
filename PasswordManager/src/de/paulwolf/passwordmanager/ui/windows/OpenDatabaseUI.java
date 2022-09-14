package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.ui.UIUtils;
import de.paulwolf.passwordmanager.ui.components.ScaledButton;
import de.paulwolf.passwordmanager.ui.components.ScaledToggleButton;
import de.paulwolf.passwordmanager.ui.passwordfields.PasswordEncodingField;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OpenDatabaseUI extends JFrame implements ActionListener, KeyListener {

    final JPanel wrapper = new JPanel();
    final PasswordEncodingField field = new PasswordEncodingField();
    final ScaledToggleButton show = new ScaledToggleButton("Show");
    final ScaledButton submit = new ScaledButton("Submit Password");
    final ScaledButton changeFile = new ScaledButton("Change file");

    boolean hasParent;

    public OpenDatabaseUI(String path, Component parent) {

        hasParent = parent != null;

        this.setTitle(path + " : PasswordManager");

        wrapper.setLayout(new GridBagLayout());

        wrapper.add(field, UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 1, 1));
        wrapper.add(show, UIUtils.createGBC(1, 0, GridBagConstraints.HORIZONTAL, 1, 1, .0));
        wrapper.add(submit, UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1, 1.));
        wrapper.add(changeFile, UIUtils.createGBC(1, 1, GridBagConstraints.HORIZONTAL, 1, 1, .0));

        submit.addActionListener(this);
        changeFile.addActionListener(this);
        show.addActionListener(this);
        field.getPasswordField().addKeyListener(this);

        field.getPasswordField().setDisplayPasswordStrength(false);

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Configuration.IMAGE);
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == submit) {

            if (EncodingWizard.isEncodingValid(field.getSelectedEncoding(), new String(field.getPassword()))) {

                String password = EncodingWizard.decodeString(field.getSelectedEncoding(), new String(field.getPassword()));

                if (Main.ui.decryptDatabase(password.getBytes(Configuration.STANDARD_CHARSET)))
                    this.setVisible(false);
                else
                    JOptionPane.showMessageDialog(null, "The entered password is incorrect!", "Insufficient credentials",
                            JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "The key is not correctly encoded!", "Malformed encoding", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == changeFile) {
            this.setVisible(false);
            if (!hasParent)
               Main.ui = new MainUI();
        } else {
            if (show.isSelected())
                field.setEchoChar((char) 0);
            else
                field.setEchoChar(Configuration.ECHO_CHAR);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER)
            submit.doClick();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
