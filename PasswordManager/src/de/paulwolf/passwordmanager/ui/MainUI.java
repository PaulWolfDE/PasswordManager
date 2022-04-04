package de.paulwolf.passwordmanager.ui;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MainUI implements ActionListener, KeyListener {

	static JFrame frame = new JFrame("Password Manager");
	static JPanel wrapper = new JPanel();
	static JFileChooser fileChooser = new JFileChooser();
	static JComboBox<Object> box;
	static JButton browse = new JButton("Browse");
	static JButton newDatabase = new JButton("Create New Database");
	static JButton openDatabase = new JButton("Open Database At Entered Path");
	static JTextField uri = new JTextField(25);
	public static File databaseFile;

	static ArrayList<String> data = new ArrayList<>();

	public static boolean openDatabaseWithPassword(byte[] password) {

		try {
			if (FileWizard.openDatabase(databaseFile, password)) {
				frame.setVisible(false);
				return true;
			}
		} catch (InvalidKeyException | FileNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
				| UnsupportedEncodingException | ParseException | IllegalStateException | LimitReachedException e) {
			e.printStackTrace();
		}

		return false;
	}

	public void initUI() {

		File recentlyOpened = new File(System.getenv("Appdata") + "/PasswordManager/.pmrc");
		if (Files.exists(recentlyOpened.toPath())) {
			Scanner scanner = null;
			try {
				scanner = new Scanner(recentlyOpened);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while (true) {
				assert scanner != null;
				if (!scanner.hasNextLine()) break;
				String s = scanner.nextLine();
				if (Files.exists(new File(s).toPath())) {
					File file = new File(s);
					if (Files.exists(file.toPath())) {
						Scanner scanner2 = null;
						try {
							scanner2 = new Scanner(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						assert scanner2 != null;
						String ver = scanner2.nextLine();
						if (FileWizard.isCompatible(ver))
							if (!data.contains(file.getAbsolutePath()))
								data.add(file.getAbsolutePath());
						scanner2.close();
					}
				}
			}
			scanner.close();
		}

		System.out.println(data.toString());

		box = new JComboBox<>(data.toArray());
		box.addActionListener(e -> uri.setText(Objects.requireNonNull(box.getSelectedItem()).toString()));

		FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files", "pmdtb");
		fileChooser.setFileFilter(filter);
		browse.addActionListener(this);
		openDatabase.addActionListener(this);
		newDatabase.addActionListener(this);

		GridBagConstraints gbc = new GridBagConstraints();
		wrapper.setLayout(new GridBagLayout());
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weightx = 1;
		gbc.weighty = 1;

		gbc.gridx = 0;
		gbc.gridy = 1;
		wrapper.add(uri, gbc);

		gbc.gridy = 1;
		gbc.gridx = 1;
		wrapper.add(browse, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		wrapper.add(box, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		wrapper.add(openDatabase, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		wrapper.add(newDatabase, gbc);

		uri.addKeyListener(this);
		uri.setPreferredSize(new Dimension(400, 26));

		frame.add(wrapper);
		frame.pack();
		frame.setMinimumSize(frame.getSize());
		frame.setIconImage(Main.IMAGE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == browse) {

			int returnVal = fileChooser.showOpenDialog(frame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = fileChooser.getSelectedFile();
				uri.setText(file.getAbsoluteFile().toString());
			}
		}

		if (e.getSource() == openDatabase) {

			String tempURI = uri.getText();

			if (tempURI.length() > 6) {
				if (!tempURI.substring(tempURI.length() - 6).equalsIgnoreCase(".pmdtb"))
					tempURI += ".pmdtb";
			} else {
				tempURI += ".pmdtb";
			}

			Path path = Paths.get(tempURI);

			if (Files.exists(path) && !uri.getText().equals("")) {

				new OpenDatabaseUI(path.toFile().getAbsolutePath());
				databaseFile = path.toFile();

				File rc = new File(System.getenv("Appdata") + "/PasswordManager/.pmrc");
				File rcDir = new File(System.getenv("Appdata") + "/PasswordManager/");
				boolean directoryCreation = rcDir.mkdirs();
				if (!directoryCreation)
					System.out.println("Directory creation failed.");
				try {
					boolean fileCreation = rc.createNewFile();
					if (!fileCreation)
						System.out.println("File could not be created.");
					FileWriter writer = new FileWriter(rc);
					Object[] arr = data.toArray();
					writer.write(databaseFile.getAbsolutePath() + "\n");
					for (Object o : arr) {
						if (!new File(o.toString()).getAbsolutePath().equals(databaseFile.getAbsolutePath()))
							writer.write(new File(o.toString()).getAbsolutePath() + "\n");
					}

					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else
				JOptionPane.showMessageDialog(null, "Please enter a valid path or URI!", "Missing arguments",
						JOptionPane.INFORMATION_MESSAGE);
		}
		if (e.getSource() == newDatabase) {

			new CreateDatabaseUI();
			frame.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

    }

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			openDatabase.doClick();
	}

	@Override
	public void keyReleased(KeyEvent e) {

    }
}
