package de.paulwolf.passwordmanager.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;

import de.paulwolf.passwordmanager.Main;

import java.awt.event.*;
import java.awt.*;

public class OpenDatabaseUI implements ActionListener, KeyListener {

	JFrame frame = new JFrame("Enter Master Password");
	JPanel wrapper = new JPanel();
	JPasswordField field = new JPasswordField(20);
	JToggleButton show = new JToggleButton("Show");
	JButton submit = new JButton("Submit Password");
	JButton changeFile = new JButton("Change file");

	public OpenDatabaseUI(String path) {

		frame.setTitle(path);
		
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

		frame.add(wrapper);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setIconImage(Main.IMAGE);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == submit) {
			if (MainUI.openDatabaseWithPassword(CreateDatabaseUI.toBytes(field.getPassword())))
				frame.setVisible(false);
		} else if (e.getSource() == changeFile) {
			frame.setVisible(false);
			MainUI mainui = new MainUI();
			mainui.initUI();
		} else {
			if (show.isSelected())
				field.setEchoChar((char) 0);
			else
				field.setEchoChar(Main.ECHO_CHAR);
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			submit.doClick();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		;
	}
}
