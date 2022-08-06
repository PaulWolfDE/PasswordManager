package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.ui.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UpdateUI extends JFrame implements ActionListener {

    JPanel wrapper = new JPanel();
    JLabel message = new JLabel("<html>Your version is outdated.<br>Do you want do download a newer version?</html>", SwingConstants.CENTER);
    JButton accept = new JButton("Accept");
    JButton deny = new JButton("Deny");

    public UpdateUI(Component parent) {

        accept.addActionListener(this);
        deny.addActionListener(this);

        wrapper.setLayout(new GridBagLayout());
        GridBagConstraints gbc = UIUtils.createGBC(0, 0, GridBagConstraints.HORIZONTAL, 2, 1);
        wrapper.add(message, gbc);
        (gbc = UIUtils.createGBC(0, 1, GridBagConstraints.HORIZONTAL, 1, 1)).weightx = 1;
        wrapper.add(accept, gbc);
        gbc = UIUtils.createGBC(1, 1, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(deny, gbc);

        this.add(wrapper);
        this.setTitle("Update available");
        this.setIconImage(Main.IMAGE);
        this.setAlwaysOnTop(true);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == accept) {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/PaulWolfDE/PasswordManager/releases/"));
            } catch (IOException | URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
            System.exit(0);
        } else if (e.getSource() == deny) {
            this.setVisible(false);
        }
    }
}
