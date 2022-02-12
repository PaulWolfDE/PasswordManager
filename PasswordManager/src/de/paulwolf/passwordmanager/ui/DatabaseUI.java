package de.paulwolf.passwordmanager.ui;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

public class DatabaseUI {

	static Database database;

	static JFrame frame;
	static JPanel wrapper;
	static JPanel buttonPanel;
	static JPanel tableWrapper;
	static JPanel searchPanel;
	static JButton addEntry;
	static JButton saveDatabase;
	static JButton saveDatabaseAs;
	static JButton settings;
	static JButton openDatabase;
	static JTable table;
	static JScrollPane scrollPane;
	private static String[] columnNames = { "Title", "Username", "Email", "Password", "Last modified" };
	private static int selectedRow;
	private static Object[] entries;
	private static DefaultTableModel dtm;
	private static JTextField filter;
	private static int t = 0;

	public static void updateTable() {

		entries = database.getEntries().toArray();
		String[][] asteriskContents = new String[entries.length][5];

		for (int i = 0; i < entries.length; i++) {

			asteriskContents[i] = ((Entry) entries[i]).getInformationAsArray();
			int t = asteriskContents[i][3].length();
			asteriskContents[i][3] = "";
			asteriskContents[i][3] = new String(new char[t]).replace('\0', Main.ECHO_CHAR);
		}

		table = new JTable(asteriskContents, columnNames);
		table.setAutoCreateRowSorter(true);
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem menuCopyUsername = new JMenuItem("Copy Username");
		JMenuItem menuCopyEmail = new JMenuItem("Copy Email Address");
		JMenuItem menuCopyPassword = new JMenuItem("Copy Password");
		JMenuItem menuItemEdit = new JMenuItem("Edit Entry");
		JMenuItem menuItemRemove = new JMenuItem("Remove Entry");
		popupMenu.add(menuCopyUsername);
		popupMenu.add(menuCopyEmail);
		popupMenu.add(menuCopyPassword);
		popupMenu.add(menuItemEdit);
		popupMenu.add(menuItemRemove);

		table.setComponentPopupMenu(popupMenu);
		popupMenu.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						selectedRow = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
						if (selectedRow > -1) {
							table.setRowSelectionInterval(selectedRow, selectedRow);
						}
					}
				});
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				;
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {
				;
			}
		});

		menuCopyUsername.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				StringSelection stringSelection = new StringSelection(
						database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getUsername());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		menuCopyEmail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				StringSelection stringSelection = new StringSelection(
						database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getEmail());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		menuCopyPassword.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				StringSelection stringSelection = new StringSelection(
						database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getPassword());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});

		menuItemEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new NewEntryUI(database.getEntries().get(table.convertRowIndexToModel(selectedRow)), selectedRow);
			}
		});

		menuItemRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ArrayList<Entry> dtb = database.getEntries();
				if (dtb.size() != 1) {
					dtb.remove(table.convertRowIndexToModel(selectedRow));
					dtm.removeRow(table.convertRowIndexToModel(selectedRow));
					database.setEntries(dtb);
				} else
					JOptionPane.showMessageDialog(null, "The database must have at least 1 entry!",
							"Database can't be empty!", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		dtm = new DefaultTableModel(asteriskContents, columnNames) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table.setModel(dtm);
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);
		searchPanel.add(new JLabel("   Enter search query:   "), BorderLayout.WEST);
		searchPanel.add(filter, BorderLayout.CENTER);

		filter.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = filter.getText();
				text = text.replaceAll("\\*", "").replaceAll("\\+", "").replaceAll("\\?", "").replaceAll("\\\\", "");
				if (text.trim().length() == 0)
					rowSorter.setRowFilter(null);
				else
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = filter.getText();
				text = text.replaceAll("\\*", "").replaceAll("\\+", "").replaceAll("\\?", "").replaceAll("\\\\", "");
				if (text.trim().length() == 0)
					rowSorter.setRowFilter(null);
				else
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		});

		scrollPane = new JScrollPane(table);

		if (t == 0) {
			table.getRowSorter().toggleSortOrder(0);
			t++;
		}
	}

	public static void initUI(Database db) {

		database = db;

		searchPanel = new JPanel(new BorderLayout());
		tableWrapper = new JPanel(new BorderLayout());
		scrollPane = new JScrollPane(table);
		filter = new JTextField();

		updateTable();

		frame = new JFrame("Password Manager - " + database.getPath());
		wrapper = new JPanel();
		buttonPanel = new JPanel();
		addEntry = new JButton("Add Entry");
		saveDatabase = new JButton("Save Database");
		saveDatabaseAs = new JButton("Save Database As");
		settings = new JButton("Settings");
		openDatabase = new JButton("Open Database");

		buttonPanel.add(addEntry);
		buttonPanel.add(saveDatabase);
		buttonPanel.add(saveDatabaseAs);
		buttonPanel.add(settings);
		buttonPanel.add(openDatabase);

		tableWrapper.add(scrollPane, BorderLayout.CENTER);
		tableWrapper.add(searchPanel, BorderLayout.SOUTH);

		wrapper.setLayout(new BorderLayout());
		wrapper.add(tableWrapper, BorderLayout.CENTER);
		wrapper.add(buttonPanel, BorderLayout.PAGE_END);

		frame.add(wrapper);
		frame.setMinimumSize(new Dimension(800, 300));
		frame.setIconImage(Main.IMAGE);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		addEntry.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new NewEntryUI(null, -1);
			}
		});

		saveDatabase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					FileWizard.saveDatabase(database, database.getPath());
				} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
						| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
						| IOException | IllegalStateException | LimitReachedException e1) {
					e1.printStackTrace();
				}
			}
		});

		saveDatabaseAs.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Database.pmdtb"));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files",
						"pmdtb");
				fileChooser.setFileFilter(filter);

				int returnVal = fileChooser.showSaveDialog(frame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					String pathString = fileChooser.getSelectedFile().toString();

					if (pathString.length() > 6) {

						if (pathString.substring(pathString.length() - 6).equalsIgnoreCase(".pmdtb")) {

							database.setPath(new File(pathString));
						} else {

							pathString += ".pmdtb";
							database.setPath(new File(pathString));
						}

					} else {

						pathString += ".pmdtb";
						database.setPath(new File(pathString));
					}

					try {
						FileWizard.saveDatabase(database, database.getPath().getAbsoluteFile());
					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException
							| IOException | IllegalStateException | LimitReachedException e1) {
						e1.printStackTrace();
					}

				}

			}
		});

		settings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new SettingsUI();
			}
		});
		
		openDatabase.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				frame.setVisible(false);
				MainUI mainui = new MainUI();
				MainUI.uri.setText("");
				mainui.initUI();
			}
		});
	}

	public static void addEntry(Entry e) {

		database.addEntry(e);
		String[] asteriskContents = new String[5];
		asteriskContents = database.getEntries().get(database.getEntries().size() - 1).getInformationAsArray();
		int t = asteriskContents[3].length();
		asteriskContents[3] = "";
		asteriskContents[3] = new String(new char[t]).replace('\0', Main.ECHO_CHAR);

		dtm.addRow(asteriskContents);
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);
	}

	public static void editEntry(Entry e, int index) {

		ArrayList<Entry> en = database.getEntries();
		en.set(table.convertRowIndexToModel(index), e);
		for (int i = 0; i < 5; i++)
			dtm.setValueAt(i != 3 ? en.get(table.convertRowIndexToModel(index)).getInformationAsArray()[i]
					: new String(
							new char[en.get(table.convertRowIndexToModel(index)).getInformationAsArray()[i].length()])
									.replace('\0', Main.ECHO_CHAR),
					table.convertRowIndexToModel(index), i);
	}
}
