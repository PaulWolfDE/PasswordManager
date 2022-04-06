package de.paulwolf.passwordmanager.ui;

import de.paulwolf.passwordmanager.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SettingsUI extends JFrame implements ActionListener {

    final JPanel wrapper = new JPanel();
    final JLabel eaLabel = new JLabel("Encryption Algorithm");
    final JComboBox<String> eaBox = new JComboBox<>(Main.ENCRYPTION_ALGORITHMS);
    final JLabel hashLabel = new JLabel("Hash Algorithm");
    final JComboBox<String> hashBox = new JComboBox<>(Main.HASH_ALGORITHMS);
    final JPasswordField keyField = new JPasswordField(30);
    final JToggleButton showKey = new JToggleButton("Show");
    final JLabel keyLabel = new JLabel("Master Key");
    final JPasswordField keyVerificationField = new JPasswordField(30);
    final JToggleButton showKeyVerification = new JToggleButton("Show");
    final JLabel keyVerificationLabel = new JLabel("Repeat Master Key");
    final JButton button = new JButton("Save Changes");

    final JFrame f;
    final JPanel p;
    final JPasswordField pf;
    final JToggleButton b;
    final JButton b2;

    public SettingsUI() {

        this.setTitle("Settings");

        f = new JFrame("Enter Master Password");
        p = new JPanel();
        pf = new JPasswordField(20);
        b = new JToggleButton("Show");
        b2 = new JButton("Submit Password");

        GridBagConstraints gbc = new GridBagConstraints();
        p.setLayout(new GridBagLayout());
        gbc.gridwidth = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(pf, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        p.add(b, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        p.add(b2, gbc);

        f.add(p);
        f.pack();
        f.setMinimumSize(f.getSize());
        f.setIconImage(Main.IMAGE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        b.addActionListener(this);
        b2.addActionListener(this);
        pf.setFont(new Font("Consolas", Font.PLAIN, 14));
        pf.setPreferredSize(new Dimension(400, 26));

        pf.addKeyListener(new KeyListener() {

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
    }

    static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.US_ASCII.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0);
        return bytes;
    }

    private void go() {

        GridBagConstraints gbc = new GridBagConstraints();
        wrapper.setLayout(new GridBagLayout());
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(eaLabel, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
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

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        wrapper.add(button, gbc);

        showKey.addActionListener(this);
        showKeyVerification.addActionListener(this);
        button.addActionListener(this);
        keyField.setFont(new Font("Consolas", Font.PLAIN, 14));
        keyField.putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyField.setPreferredSize(new Dimension(400, 26));
        keyVerificationField.setFont(new Font("Consolas", Font.PLAIN, 14));
        keyVerificationField.putClientProperty("JPasswordField.cutCopyAllowed", true);
        keyVerificationField.setPreferredSize(new Dimension(400, 26));

        eaBox.setSelectedItem(DatabaseUI.database.getEncryptionAlgorithm());
        hashBox.setSelectedItem(DatabaseUI.database.getHashAlgorithm());
        keyField.setText(new String(DatabaseUI.database.getMasterKey()));
        keyVerificationField.setText(new String(DatabaseUI.database.getMasterKey()));

        this.add(wrapper);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setIconImage(Main.IMAGE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == b) {

            if (b.isSelected())
                pf.setEchoChar((char) 0);
            else
                pf.setEchoChar(Main.ECHO_CHAR);
        } else if (e.getSource() == b2) {

            if (new String(pf.getPassword()).equals(new String(DatabaseUI.database.getMasterKey()))) {
                f.setVisible(false);
                go();
            } else
                JOptionPane.showMessageDialog(null, "The entered password is incorrect!", "Insufficient credentials",
                        JOptionPane.ERROR_MESSAGE);
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

            if (Arrays.equals(toBytes(keyField.getPassword()), toBytes(keyVerificationField.getPassword()))) {

                if (keyField.getPassword().length != 0) {

                    DatabaseUI.database.setMasterKey(toBytes(keyField.getPassword()));
                    DatabaseUI.database.setHashAlgorithm((String) hashBox.getSelectedItem());
                    DatabaseUI.database.setEncryptionAlgorithm((String) eaBox.getSelectedItem());

                    this.setVisible(false);
                } else
                    JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments",
                            JOptionPane.INFORMATION_MESSAGE);

            } else
                JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error",
                        JOptionPane.ERROR_MESSAGE);
        }
    }
}
