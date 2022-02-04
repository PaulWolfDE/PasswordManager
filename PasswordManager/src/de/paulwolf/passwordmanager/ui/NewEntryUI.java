package de.paulwolf.passwordmanager.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Entry;

public class NewEntryUI {

	JFrame frame = new JFrame("Create New Entry");
	JPanel wrapper = new JPanel();
	JTextField title = new JTextField(20);
	JLabel titleLabel = new JLabel("Title");
	JTextField username = new JTextField(20);
	JLabel usernameLabel = new JLabel("Username");
	JTextField email = new JTextField(20);
	JLabel emailLabel = new JLabel("Email Address");
	JPasswordField password = new JPasswordField(20);
	JLabel passwordLabel = new JLabel("Password");
	JButton generatePassword = new JButton("Generate Password");
	JPasswordField confirmPassword = new JPasswordField();
	JLabel confirmPasswordLabel = new JLabel("Confirm Password");
	JToggleButton showPassword = new JToggleButton("Show Password");
	JButton submit = new JButton("Submit Entry");

	public NewEntryUI(Entry e, int index) {

		GridBagConstraints gbc = new GridBagConstraints();
		wrapper.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 1;
		gbc.weighty = 1;

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		wrapper.add(titleLabel, gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		wrapper.add(title, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		wrapper.add(usernameLabel, gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		wrapper.add(username, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		wrapper.add(emailLabel, gbc);
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		wrapper.add(email, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		wrapper.add(passwordLabel, gbc);
		gbc.gridx = 1;
		wrapper.add(password, gbc);
		gbc.gridx = 2;
		wrapper.add(generatePassword, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 4;
		wrapper.add(confirmPasswordLabel, gbc);
		gbc.gridx = 1;
		wrapper.add(confirmPassword, gbc);
		gbc.gridx = 2;
		wrapper.add(showPassword, gbc);

		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 3;
		wrapper.add(submit, gbc);

		if (index != -1) {
			title.setText(e.getTitle());
			username.setText(e.getUsername());
			email.setText(e.getEmail());
			password.setText(e.getPassword());
			confirmPassword.setText(e.getPassword());
		}

		password.setFont(new Font("Consolas", Font.PLAIN, 14));
		password.putClientProperty("JPasswordField.cutCopyAllowed", true);
		password.setPreferredSize(new Dimension(300, 26));
		confirmPassword.setFont(new Font("Consolas", Font.PLAIN, 14));
		confirmPassword.putClientProperty("JPasswordField.cutCopyAllowed", true);
		confirmPassword.setPreferredSize(new Dimension(300, 26));
		title.setPreferredSize(new Dimension(300, 26));
		username.setPreferredSize(new Dimension(300, 26));
		email.setPreferredSize(new Dimension(300, 26));
		
		frame.add(wrapper);
		frame.setIconImage(Main.IMAGE);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (title.getText().toString().equals("") || username.getText().toString().equals("")
						|| email.getText().toString().equals("") || new String(password.getPassword()).equals("")
						|| new String(confirmPassword.getPassword()).equals("")) {
					JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					if (new String(password.getPassword()).equals(new String(confirmPassword.getPassword()))) {

						if (index == -1) {
							Main.dbui.addEntry(new Entry(title.getText().toString(), username.getText().toString(),
									email.getText().toString(), new String(password.getPassword())));
						} else {
							Main.dbui.editEntry(new Entry(title.getText().toString(), username.getText().toString(),
									email.getText().toString(), new String(password.getPassword())), index);
						}

						frame.setVisible(false);
					} else {

						JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		generatePassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\"";
				SecureRandom sr = new SecureRandom();
				String pw = "";
				for (int i = 0; i < 24; i++)
					pw += alphabet.charAt(sr.nextInt(alphabet.length()));
				password.setText(pw);
				confirmPassword.setText(pw);
			}
		});

		showPassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!showPassword.isSelected()) {
					password.setEchoChar(Main.ECHO_CHAR);
					confirmPassword.setEchoChar(Main.ECHO_CHAR);
				}

				else {
					password.setEchoChar((char) 0);
					confirmPassword.setEchoChar((char) 0);
				}
			}
		});
	}
}
