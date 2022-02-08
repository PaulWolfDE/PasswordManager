package de.paulwolf.passwordmanager.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

public class CreateDatabaseUI implements ActionListener, KeyListener {

	JFrame frame;

	JPanel wrapper = new JPanel();

	JLabel eaLabel = new JLabel("Encryption Algorithm");
	JLabel hashLabel = new JLabel("Hash Algorithm");
	JLabel keyLabel = new JLabel("Master Key");
	JLabel keyVerificationLabel = new JLabel("Repeat Master Key");
	JLabel pathLabel = new JLabel("Save Database At");

	JButton browseButton = new JButton("Browse");
	JButton generateKey = new JButton("Generate Java Crypto Key");
	JButton generateKey2 = new JButton("Generate Random Key");
	JButton createDatabase = new JButton("Create Database");

	JComboBox<String> eaBox = new JComboBox<String>(Main.ENCRYPTION_ALGORITHMS);
	JComboBox<String> hashBox = new JComboBox<String>(Main.HASH_ALGORITHMS);

	JToggleButton showKey = new JToggleButton("Show");
	JToggleButton showKeyVerification = new JToggleButton("Show");

	JPasswordField keyField = new JPasswordField(30);
	JPasswordField keyVerificationField = new JPasswordField(30);

	JTextField pathField = new JTextField(20);

	JFileChooser fileChooser = new JFileChooser();

	File databaseFile;

	static byte[] toBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("ascii").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0);
		return bytes;
	}

	public CreateDatabaseUI() {

		frame = new JFrame("Create New Database");

		GridBagConstraints gbc = new GridBagConstraints();
		wrapper.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 1;
		gbc.weighty = 1;

		gbc.gridx = 0;
		gbc.gridy = 0;
		wrapper.add(eaLabel, gbc);
		gbc.gridwidth = 2;
		gbc.gridx = 1;
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

		gbc.weightx = 0.5;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 4;
		wrapper.add(pathLabel, gbc);
		gbc.gridx = 1;
		wrapper.add(pathField, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		wrapper.add(browseButton, gbc);

		gbc.weightx = 1;
		gbc.gridx = 2;
		gbc.gridy = 5;
		wrapper.add(generateKey2, gbc);
		gbc.gridwidth = 2;
		gbc.weightx = 2;
		gbc.gridx = 0;
		gbc.gridy = 5;
		wrapper.add(generateKey, gbc);

		gbc.weightx = 1;
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy = 6;
		wrapper.add(createDatabase, gbc);

		browseButton.addActionListener(this);
		generateKey.addActionListener(this);
		generateKey2.addActionListener(this);
		createDatabase.addActionListener(this);
		showKey.addActionListener(this);
		showKeyVerification.addActionListener(this);
		pathField.addKeyListener(this);

		keyField.setFont(new Font("Consolas", Font.PLAIN, 14));
		keyField.putClientProperty("JPasswordField.cutCopyAllowed", true);
		keyField.setPreferredSize(new Dimension(400, 26));
		keyVerificationField.setFont(new Font("Consolas", Font.PLAIN, 14));
		keyVerificationField.putClientProperty("JPasswordField.cutCopyAllowed", true);
		keyVerificationField.setPreferredSize(new Dimension(400, 26));
		pathField.setFont(new Font("Consolas", Font.PLAIN, 14));
		pathField.setPreferredSize(new Dimension(400, 26));

		frame.add(wrapper);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setIconImage(Main.IMAGE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		fileChooser.setSelectedFile(new File("Database.pmdtb"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files", "pmdtb");
		fileChooser.setFileFilter(filter);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == generateKey2) {

			String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#$%&'()*+,-./:;<=>?@[\\]^_`{|}~\"";
			SecureRandom sr = new SecureRandom();
			String password = "";
			for (int i = 0; i < 24; i++)
				password += alphabet.charAt(sr.nextInt(alphabet.length()));
			keyField.setText(password);
			keyVerificationField.setText(password);
		}

		if (e.getSource() == generateKey) {

			try {
				keyField.setText(
						Base64.getEncoder().encodeToString(KeyGenerator.getInstance("AES").generateKey().getEncoded()));
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			keyVerificationField.setText(new String(keyField.getPassword()));
		}

		if (e.getSource() == showKey) {

			if (showKey.isSelected())
				keyField.setEchoChar((char) 0);
			else
				keyField.setEchoChar(Main.ECHO_CHAR);
		}

		if (e.getSource() == showKeyVerification) {

			if (showKeyVerification.isSelected())
				keyVerificationField.setEchoChar((char) 0);
			else
				keyVerificationField.setEchoChar(Main.ECHO_CHAR);
		}

		if (e.getSource() == browseButton) {

			int returnVal = fileChooser.showSaveDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				pathField.setText(file.getAbsoluteFile().toString());

			}
		}

		if (e.getSource() == createDatabase) {

			Path path = Paths.get(pathField.getText().toString());

			if (Arrays.equals(toBytes(keyField.getPassword()), toBytes(keyVerificationField.getPassword()))) {

				if (!pathField.getText().toString().equals("")
						&& !toBytes(keyField.getPassword()).toString().equals("")) {

					Database db = new Database();

					String dbPath = path.toString();
					if (dbPath.length() > 6) {

						if (dbPath.substring(dbPath.length() - 6).equalsIgnoreCase(".pmdtb")) {
							db.setPath(new File(path.toString()));
						} else {
							String tpath = path.toString();
							tpath += ".pmdtb";
							db.setPath(new File(tpath));
						}
					} else {
						String tpath = path.toString();
						tpath += ".pmdtb";
						db.setPath(new File(tpath));
					}

					try {
						db.getPath().createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					db.setMasterKey(toBytes(keyField.getPassword()));
					db.setHashAlgorithm(hashBox.getSelectedItem().toString());
					db.setEncryptionAlgorithm(eaBox.getSelectedItem().toString());
					db.addEntry(new Entry("Example Entry", "John Doe", "john.doe@example.com", "password123", "Note"));

					frame.setVisible(false);
					DatabaseUI.initUI(db);

					try {
						FileWizard.saveDatabase(db, db.getPath());
					} catch (NoSuchAlgorithmException | IOException | InvalidKeyException | NoSuchPaddingException
							| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
							| IllegalStateException | LimitReachedException e1) {
						e1.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please fill out the form!", "Missing arguments",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} else {
				JOptionPane.showMessageDialog(null, "Passwords do not match up!", "Argument error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			createDatabase.doClick();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		;
	}
}
