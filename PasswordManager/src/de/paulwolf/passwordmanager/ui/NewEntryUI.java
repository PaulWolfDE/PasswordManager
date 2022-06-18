package de.paulwolf.passwordmanager.ui;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Entry;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;

public class NewEntryUI extends JFrame {

    private static final int NOTES_ROWS = 5;
    final JPanel wrapper = new JPanel();
    final JTextField title = new JTextField(20);
    final JLabel titleLabel = new JLabel("Title");
    final JTextField username = new JTextField(20);
    final JLabel usernameLabel = new JLabel("Username");
    final JTextField email = new JTextField(20);
    final JLabel emailLabel = new JLabel("Email Address");
    final PasswordStrengthField password = new PasswordStrengthField(20);
    final JLabel passwordLabel = new JLabel("Password");
    final JButton generatePassword = new JButton("Generate Password");
    final PasswordStrengthField confirmPassword = new PasswordStrengthField(20);
    final JLabel confirmPasswordLabel = new JLabel("Confirm Password");
    final JToggleButton showPassword = new JToggleButton("Show Password");
    final JButton submit = new JButton("Submit Entry");
    final JLabel notesLabel = new JLabel("Notes");
    final JTextArea textArea = new JTextArea(5, 0);
    final JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public NewEntryUI(Entry e, int index) {

        wrapper.setLayout(new GridBagLayout());

        int row = 0;
        GridBagConstraints gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(titleLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 2, 1);
        wrapper.add(title, gbc);

        row++;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(usernameLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 2, 1);
        wrapper.add(username, gbc);

        row++;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(emailLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 2, 1);
        wrapper.add(email, gbc);

        row++;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(passwordLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(password, gbc);
        gbc = UIUtils.createGBC(2, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(generatePassword, gbc);

        row++;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(confirmPasswordLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(confirmPassword, gbc);
        gbc = UIUtils.createGBC(2, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(showPassword, gbc);

        row++;
        int notesRows = NOTES_ROWS;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weighty = 1.0;
        wrapper.add(notesLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.BOTH, 2, notesRows);
        gbc.weighty = 1.0;
        wrapper.add(scrollPane, gbc);

        row += notesRows;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(new JLabel(""), gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 2, 1);
        wrapper.add(submit, gbc);

        if (index != -1) {
            title.setText(e.getTitle());
            username.setText(e.getUsername());
            email.setText(e.getEmail());
            password.setText(e.getPassword());
            confirmPassword.setText(e.getPassword());
            textArea.setText(e.getNotes().replaceAll("\\\\n", "\n"));
            this.setTitle("PasswordManager - Edit Entry");
        } else this.setTitle("PasswordManager - Create Entry");

        password.evaluatePassword();
        confirmPassword.evaluatePassword();

        password.setFont(new Font("Consolas", Font.PLAIN, 14));
        password.putClientProperty("JPasswordField.cutCopyAllowed", true);
        password.setPreferredSize(new Dimension(300, 26));
        confirmPassword.setFont(new Font("Consolas", Font.PLAIN, 14));
        confirmPassword.putClientProperty("JPasswordField.cutCopyAllowed", true);
        confirmPassword.setPreferredSize(new Dimension(300, 26));
        title.setPreferredSize(new Dimension(300, 26));
        username.setPreferredSize(new Dimension(300, 26));
        email.setPreferredSize(new Dimension(300, 26));

        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        confirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

        this.add(wrapper);
        this.setIconImage(Main.IMAGE);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        submit.addActionListener(e1 -> {

            if (new String(password.getPassword()).equals(new String(confirmPassword.getPassword())) && !new String(password.getPassword()).equals("")) {

                String notes = textArea.getText().replaceAll("\n", "\\\\n");

                if (index == -1) {
                    DatabaseUI.addEntry(new Entry(title.getText(), username.getText(), email.getText(), new String(password.getPassword()), notes));
                } else {
                    DatabaseUI.editEntry(new Entry(title.getText(), username.getText(), email.getText(), new String(password.getPassword()), notes), index);
                }

                this.setVisible(false);
            } else {

                JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error", JOptionPane.ERROR_MESSAGE);
            }
        });

        generatePassword.addActionListener(e12 -> {

            String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\"";
            SecureRandom sr = new SecureRandom();
            StringBuilder pw = new StringBuilder();
            for (int i = 0; i < 24; i++)
                pw.append(alphabet.charAt(sr.nextInt(alphabet.length())));
            password.setText(pw.toString());
            confirmPassword.setText(pw.toString());
            password.evaluatePassword();
            confirmPassword.evaluatePassword();
        });

        showPassword.addActionListener(e13 -> {

            if (!showPassword.isSelected()) {
                password.setEchoChar(Main.ECHO_CHAR);
                confirmPassword.setEchoChar(Main.ECHO_CHAR);
            } else {
                password.setEchoChar((char) 0);
                confirmPassword.setEchoChar((char) 0);
            }
        });
    }
}
