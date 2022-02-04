package de.paulwolf.passwordmanager.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;

import de.paulwolf.passwordmanager.Main;

public class SettingsUI implements ActionListener {

	JFrame frame = new JFrame("Settings");;
	JPanel wrapper = new JPanel();
	JLabel eaLabel = new JLabel("Encryption Algorithm");
	JComboBox<String> eaBox = new JComboBox<String>(Main.ENCRYPTION_ALGORITHMS);
	JLabel hashLabel = new JLabel("Hash Algorithm");
	JComboBox<String> hashBox = new JComboBox<String>(Main.HASH_ALGORITHMS);
	JPasswordField keyField = new JPasswordField(30);
	JToggleButton showKey = new JToggleButton("Show");
	JLabel keyLabel = new JLabel("Master Key");
	JPasswordField keyVerificationField = new JPasswordField(30);
	JToggleButton showKeyVerification = new JToggleButton("Show");
	JLabel keyVerificationLabel = new JLabel("Repeat Master Key");
	JButton button = new JButton("Save Changes");

	JFrame f;
	JPanel p;
	JPasswordField pf;
	JToggleButton b;
	JButton b2;

	static byte[] toBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("ascii").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0);
		return bytes;
	}

	public SettingsUI() {

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
				;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					b2.doClick();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				;
			}
		});
	}

	private void go() {

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

		frame.add(wrapper);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setIconImage(Main.IMAGE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

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
						0);
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

					frame.setVisible(false);
				} else 
					JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments",
							JOptionPane.INFORMATION_MESSAGE);

			} else 
				JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error",
						JOptionPane.ERROR_MESSAGE);
		}
	}
}
