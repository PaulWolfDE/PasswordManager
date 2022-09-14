package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.ui.components.*;
import de.paulwolf.passwordmanager.ui.passwordfields.PasswordEncodingField;
import de.paulwolf.passwordmanager.ui.UIUtils;
import de.paulwolf.passwordmanager.wizards.EncodingWizard;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class NewEntryUI extends JFrame implements PasswordAcceptingUI {

    private static final int NOTES_ROWS = 5;
    final JPanel wrapper = new JPanel();
    final ScaledTextField title = new ScaledTextField();
    final ScaledLabel titleLabel = new ScaledLabel("Title");
    final ScaledTextField username = new ScaledTextField();
    final ScaledLabel usernameLabel = new ScaledLabel("Username");
    final ScaledTextField email = new ScaledTextField();
    final ScaledLabel emailLabel = new ScaledLabel("Email Address");
    final PasswordEncodingField password = new PasswordEncodingField();
    final ScaledLabel passwordLabel = new ScaledLabel("Password");
    final ScaledButton generatePassword = new ScaledButton("Generate Password");
    final PasswordEncodingField confirmPassword = new PasswordEncodingField();
    final ScaledLabel confirmPasswordLabel = new ScaledLabel("Confirm Password");
    final ScaledToggleButton showPassword = new ScaledToggleButton("Show Password");
    final ScaledButton submit = new ScaledButton("Submit Entry");
    final ScaledLabel notesLabel = new ScaledLabel("Notes");
    final ScaledTextArea textArea = new ScaledTextArea(5);
    final JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public NewEntryUI(Entry e, int index) {

        if (index != -1 && e.getTitle().equals(Configuration.BACKUP_TITLE)) {
            title.setEditable(false);
            emailLabel.setText("Hostname");
            this.setTitle("Edit SFTP Backup Settings");
        }

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
        gbc = UIUtils.createGBC(2, row, GridBagConstraints.HORIZONTAL, 1, 1, 0.0);
        wrapper.add(generatePassword, gbc);

        row++;
        gbc = UIUtils.createGBC(0, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(confirmPasswordLabel, gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 1, 1);
        wrapper.add(confirmPassword, gbc);
        gbc = UIUtils.createGBC(2, row, GridBagConstraints.HORIZONTAL, 1, 1, 0.0);
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
        wrapper.add(new ScaledLabel(""), gbc);
        gbc = UIUtils.createGBC(1, row, GridBagConstraints.HORIZONTAL, 2, 1);
        wrapper.add(submit, gbc);

        if (index != -1) {
            title.setText(e.getTitle());
            username.setText(e.getUsername());
            email.setText(e.getEmail());
            password.setText(new String(e.getPassword(), Configuration.STANDARD_CHARSET));
            confirmPassword.setText(new String(e.getPassword(), Configuration.STANDARD_CHARSET));
            textArea.setText(e.getNotes().replaceAll("\\\\n", "\n"));
            this.setTitle("PasswordManager - Edit Entry");
        } else this.setTitle("PasswordManager - Create Entry");

        password.getPasswordField().evaluatePassword(password.getSelectedEncoding());
        confirmPassword.getPasswordField().evaluatePassword(confirmPassword.getSelectedEncoding());

        password.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);
        confirmPassword.getPasswordField().putClientProperty("JPasswordField.cutCopyAllowed", true);

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(Configuration.SCALED_SCROLL_BAR_THICKNESS, 0));

        this.add(wrapper);
        this.setIconImage(Configuration.IMAGE);
        this.pack();
        this.setMinimumSize(this.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        submit.addActionListener(e1 -> {

            if (!new String(password.getPassword()).equals(new String(confirmPassword.getPassword()))) {

                JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error", JOptionPane.ERROR_MESSAGE);

            } else if (new String(password.getPassword()).equals("")) {

                JOptionPane.showMessageDialog(null, "Passwords can't be left empty!", "Argument error", JOptionPane.ERROR_MESSAGE);

            } else if (title.getText().equals(Configuration.BACKUP_TITLE) && title.isEditable()) {

                JOptionPane.showMessageDialog(null, "Entry name is reserved!", "Argument error", JOptionPane.ERROR_MESSAGE);

            } else {

                String notes = textArea.getText().replaceAll("\n", "\\\\n");

                if (index == -1)
                    DatabaseUI.addEntry(new Entry(title.getText(), username.getText(), email.getText(), EncodingWizard.decodeString(password.getSelectedEncoding(), new String(password.getPassword())).getBytes(Configuration.STANDARD_CHARSET), notes));
                else
                    DatabaseUI.editEntry(new Entry(title.getText(), username.getText(), email.getText(), EncodingWizard.decodeString(password.getSelectedEncoding(), new String(password.getPassword())).getBytes(Configuration.STANDARD_CHARSET), notes), index);

                this.setVisible(false);
            }
        });

        generatePassword.addActionListener(e12 -> new PasswordGeneratorUI(this, new String(this.password.getPassword()), this.password.getSelectedEncoding()));

        showPassword.addActionListener(e13 -> {

            if (!showPassword.isSelected()) {
                password.setEchoChar(Configuration.ECHO_CHAR);
                confirmPassword.setEchoChar(Configuration.ECHO_CHAR);
            } else {
                password.setEchoChar((char) 0);
                confirmPassword.setEchoChar((char) 0);
            }
        });

        this.password.getEncodingButton().addActionListener(e14 -> {
            if (this.confirmPassword.getSelectedEncoding() == 0) {
                if (EncodingWizard.isEncodingValid(0, new String(this.password.getPassword())))
                    this.password.setText(EncodingWizard.bytesToHex(new String(this.password.getPassword()).getBytes(Configuration.STANDARD_CHARSET)));
                if (EncodingWizard.isEncodingValid(0, new String(this.confirmPassword.getPassword())))
                    this.confirmPassword.setText(EncodingWizard.bytesToHex(new String(this.confirmPassword.getPassword()).getBytes(Configuration.STANDARD_CHARSET)));
            } else if (this.confirmPassword.getSelectedEncoding() == 1) {
                if (EncodingWizard.isEncodingValid(1, new String(this.password.getPassword())))
                    this.password.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(EncodingWizard.hexToBytes(new String(this.password.getPassword()))))));
                if (EncodingWizard.isEncodingValid(1, new String(this.confirmPassword.getPassword())))
                    this.confirmPassword.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(EncodingWizard.hexToBytes(new String(this.confirmPassword.getPassword()))))));
            } else {
                if (EncodingWizard.isEncodingValid(2, new String(this.password.getPassword())))
                    this.password.setText(new String(Objects.requireNonNull(EncodingWizard.base64ToBytes(new String(this.password.getPassword()).getBytes(StandardCharsets.US_ASCII))), Configuration.STANDARD_CHARSET));
                if (EncodingWizard.isEncodingValid(2, new String(this.confirmPassword.getPassword())))
                    this.confirmPassword.setText(new String(Objects.requireNonNull(EncodingWizard.base64ToBytes(new String(this.confirmPassword.getPassword()).getBytes(StandardCharsets.US_ASCII))), Configuration.STANDARD_CHARSET));
            }
            this.confirmPassword.setEncoding((this.confirmPassword.getSelectedEncoding() + 1) % 3);
        });
        this.confirmPassword.getEncodingButton().addActionListener(e15 -> {
            if (this.password.getSelectedEncoding() == 0) {
                if (EncodingWizard.isEncodingValid(0, new String(this.password.getPassword())))
                    this.password.setText(EncodingWizard.bytesToHex(new String(this.password.getPassword()).getBytes(Configuration.STANDARD_CHARSET)));
                if (EncodingWizard.isEncodingValid(0, new String(this.confirmPassword.getPassword())))
                    this.confirmPassword.setText(EncodingWizard.bytesToHex(new String(this.confirmPassword.getPassword()).getBytes(Configuration.STANDARD_CHARSET)));
            } else if (this.password.getSelectedEncoding() == 1) {
                if (EncodingWizard.isEncodingValid(1, new String(this.password.getPassword())))
                    this.password.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(EncodingWizard.hexToBytes(new String(this.password.getPassword()))))));
                if (EncodingWizard.isEncodingValid(1, new String(this.confirmPassword.getPassword())))
                    this.confirmPassword.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(EncodingWizard.hexToBytes(new String(this.confirmPassword.getPassword()))))));
            } else {
                if (EncodingWizard.isEncodingValid(2, new String(this.password.getPassword())))
                    this.password.setText(new String(Objects.requireNonNull(EncodingWizard.base64ToBytes(new String(this.password.getPassword()).getBytes(StandardCharsets.US_ASCII))), Configuration.STANDARD_CHARSET));
                if (EncodingWizard.isEncodingValid(2, new String(this.confirmPassword.getPassword())))
                    this.confirmPassword.setText(new String(Objects.requireNonNull(EncodingWizard.base64ToBytes(new String(this.confirmPassword.getPassword()).getBytes(StandardCharsets.US_ASCII))), Configuration.STANDARD_CHARSET));
            }
            this.password.setEncoding((this.password.getSelectedEncoding() + 1) % 3);
        });
    }

    @Override
    public void setPassword(String password) {

        if (this.password.getSelectedEncoding() == 0) {
            this.password.setText(password);
            this.confirmPassword.setText(password);
        } else if (this.password.getSelectedEncoding() == 1) {
            this.password.setText(EncodingWizard.bytesToHex(password.getBytes(Configuration.STANDARD_CHARSET)));
            this.confirmPassword.setText(new String(this.password.getPassword()));
        } else {
            this.password.setText(new String(Objects.requireNonNull(EncodingWizard.bytesToBase64(password.getBytes(Configuration.STANDARD_CHARSET)))));
            this.confirmPassword.setText(new String(this.password.getPassword()));
        }

        this.password.getPasswordField().evaluatePassword(this.password.getSelectedEncoding());
        this.confirmPassword.getPasswordField().evaluatePassword(this.confirmPassword.getSelectedEncoding());
    }
}
