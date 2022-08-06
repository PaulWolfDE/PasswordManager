package de.paulwolf.passwordmanager.ui.windows;

import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.wizards.FileWizard;
import gnu.crypto.prng.LimitReachedException;

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
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DatabaseUI extends JFrame {

    private static final String[] columnNames = {"Title", "Username", "Email", "Password", "Last modified"};
    static Database database;
    static JPanel wrapper;
    static JPanel buttonPanel;
    static JPanel tableWrapper;
    static JPanel searchPanel;
    static JButton addEntry;
    static JButton generatePassword;
    static JButton saveDatabase;
    static JButton saveDatabaseAs;
    static JButton settings;
    static JButton openDatabase;
    static JTable table;
    static JScrollPane scrollPane;
    private static int selectedRow;
    private static DefaultTableModel dtm;
    private static JTextField filter;
    private static int t = 0;

    public DatabaseUI(Database db) {

        database = db;

        searchPanel = new JPanel(new BorderLayout());
        tableWrapper = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(table);
        filter = new JTextField();

        updateTable();

        this.setTitle("Password Manager - " + database.getPath());
        wrapper = new JPanel();
        buttonPanel = new JPanel();
        addEntry = new JButton("Add Entry");
        generatePassword = new JButton("Generate Password");
        saveDatabase = new JButton("Save Database");
        saveDatabaseAs = new JButton("Save Database As");
        settings = new JButton("Settings");
        openDatabase = new JButton("Open Database");

        buttonPanel.add(addEntry);
        buttonPanel.add(generatePassword);
        buttonPanel.add(saveDatabase);
        buttonPanel.add(saveDatabaseAs);
        buttonPanel.add(settings);
        buttonPanel.add(openDatabase);

        tableWrapper.add(scrollPane, BorderLayout.CENTER);
        tableWrapper.add(searchPanel, BorderLayout.SOUTH);

        wrapper.setLayout(new BorderLayout());
        wrapper.add(tableWrapper, BorderLayout.CENTER);
        wrapper.add(buttonPanel, BorderLayout.PAGE_END);

        this.add(wrapper);
        this.setMinimumSize(new Dimension(1000, 400));
        this.setIconImage(Main.IMAGE);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        addEntry.addActionListener(e -> new NewEntryUI(null, -1));

        generatePassword.addActionListener(e -> new PasswordGeneratorUI(null, "", -1));

        saveDatabase.addActionListener(e -> {

            try {
                FileWizard.saveDatabase(database, database.getPath());
            } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | IOException | IllegalStateException | LimitReachedException e1) {
                e1.printStackTrace();
            }
        });

        saveDatabaseAs.addActionListener(e -> {

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File("Database.pmdtb"));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Password Manager Database Files", "pmdtb");
            fileChooser.setFileFilter(filter);

            int returnVal = fileChooser.showSaveDialog(this);

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
                } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | IOException | IllegalStateException | LimitReachedException e1) {
                    e1.printStackTrace();
                }

            }

        });

        settings.addActionListener(e -> new SettingsUI());

        openDatabase.addActionListener(e -> {

            this.setVisible(false);
            Main.ui.uri.setText("");
            Main.ui = new MainUI();
        });

        // this.requestFocus();
    }

    public static void updateTable() {

        Object[] entries = database.getEntries().toArray();
        String[][] asteriskContents = new String[entries.length][5];

        for (int i = 0; i < entries.length; i++)
            asteriskContents[i] = ((Entry) entries[i]).getAsteriskArray();

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

                SwingUtilities.invokeLater(() -> {
                    selectedRow = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
                    if (selectedRow > -1) {
                        table.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });

        menuCopyUsername.addActionListener(e -> {

            StringSelection stringSelection = new StringSelection(database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getUsername());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        menuCopyEmail.addActionListener(e -> {

            StringSelection stringSelection = new StringSelection(database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getEmail());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        menuCopyPassword.addActionListener(e -> {

            StringSelection stringSelection = new StringSelection(new String(database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getPassword(), Main.STANDARD_CHARSET));
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        menuItemEdit.addActionListener(e -> new NewEntryUI(database.getEntries().get(table.convertRowIndexToModel(selectedRow)), selectedRow));

        menuItemRemove.addActionListener(e -> {

            ArrayList<Entry> dtb = database.getEntries();
            if (dtb.size() != 1) {
                dtb.remove(table.convertRowIndexToModel(selectedRow));
                dtm.removeRow(table.convertRowIndexToModel(selectedRow));
                database.setEntries(dtb);
            } else
                JOptionPane.showMessageDialog(null, "The database must have at least 1 entry!", "Database can't be empty!", JOptionPane.INFORMATION_MESSAGE);
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
                if (text.equalsIgnoreCase("Tetris187") || text.equalsIgnoreCase("Malle ist nur einmal im Jahr") || text.equalsIgnoreCase("69420")) {
                    Main.runTetris(0);
                    SwingUtilities.invokeLater(() -> filter.setText(""));
                }
                if (text.length() == 9 /*tetris:nn*/ && text.substring(0, 7).equalsIgnoreCase("tetris:") && Integer.parseInt(text.substring(7)) <= 29 && Integer.parseInt(text.substring(7)) >= 0) {
                    Main.runTetris(Integer.parseInt(text.substring(7)));
                    SwingUtilities.invokeLater(() -> filter.setText(""));
                }
                text = text.replaceAll("\\*", "").replaceAll("\\+", "").replaceAll("\\?", "").replaceAll("\\\\", "");
                if (text.trim().length() == 0) rowSorter.setRowFilter(null);
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = filter.getText();
                text = text.replaceAll("\\*", "").replaceAll("\\+", "").replaceAll("\\?", "").replaceAll("\\\\", "");
                if (text.trim().length() == 0) rowSorter.setRowFilter(null);
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
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

    public static void addEntry(Entry e) {

        database.addEntry(e);
        String[] asteriskContents = database.getEntries().get(database.getEntries().size() - 1).getAsteriskArray();

        dtm.addRow(asteriskContents);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
    }

    public static void editEntry(Entry e, int index) {

        ArrayList<Entry> entries = database.getEntries();
        entries.set(table.convertRowIndexToModel(index), e);
        String[] entry = entries.get(table.convertRowIndexToModel(index)).getAsteriskArray();
        for (int i = 0; i < 5; i++)
            dtm.setValueAt(entry[i], table.convertRowIndexToModel(index), i);
    }
}
