package de.paulwolf.passwordmanager.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.security.SecureRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	JLabel notesLabel = new JLabel("Notes");
	JTextArea textArea = new JTextArea(5, 0);
	JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

	private static int NOTES_ROWS = 5;

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
			textArea.setText(e.getNotes());
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

		title.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
		confirmPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));

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

						String notes = textArea.getText().replace("\n", "\\n");

						if (index == -1)
							Main.dbui.addEntry(new Entry(title.getText().toString(), username.getText().toString(),
									email.getText().toString(), new String(password.getPassword()), notes));
						else
							Main.dbui.editEntry(
									new Entry(title.getText().toString(), username.getText().toString(),
											email.getText().toString(), new String(password.getPassword()), notes),
									index);

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
