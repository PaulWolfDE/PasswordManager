package de.paulwolf.passwordmanager.ui;

import de.paulwolf.passwordmanager.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class OpenDatabaseUI extends JFrame implements ActionListener, KeyListener {

    final JPanel wrapper = new JPanel();
    final JPasswordField field = new JPasswordField(20);
    final JToggleButton show = new JToggleButton("Show");
    final JButton submit = new JButton("Submit Password");
    final JButton changeFile = new JButton("Change file");

    public OpenDatabaseUI(String path) {

        this.setTitle("PasswordManager - " + path);

        GridBagConstraints gbc = new GridBagConstraints();
        wrapper.setLayout(new GridBagLayout());
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(field, gbc);

        gbc.gridx = 1;
        wrapper.add(show, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        wrapper.add(submit, gbc);

        gbc.gridx = 1;
        wrapper.add(changeFile, gbc);

        submit.addActionListener(this);
        changeFile.addActionListener(this);
        show.addActionListener(this);
        field.addKeyListener(this);

        field.setFont(new Font("Consolas", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(400, 26));

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Main.IMAGE);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == submit) {
            if (Main.ui.openDatabaseWithPassword(CreateDatabaseUI.toBytes(field.getPassword())))
                this.setVisible(false);
            else
                JOptionPane.showMessageDialog(null, "The entered password is incorrect!", "Insufficient credentials",
                        JOptionPane.ERROR_MESSAGE);
        } else if (e.getSource() == changeFile) {
            this.setVisible(false);
            Main.ui = new MainUI();
        } else {
            if (show.isSelected())
                field.setEchoChar((char) 0);
            else
                field.setEchoChar(Main.ECHO_CHAR);
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
