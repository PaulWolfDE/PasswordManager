package de.paulwolf.passwordmanager.ui.windows;

import com.jcraft.jsch.JSchException;
import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.Main;
import de.paulwolf.passwordmanager.TetrisMain;
import de.paulwolf.passwordmanager.information.Database;
import de.paulwolf.passwordmanager.information.Entry;
import de.paulwolf.passwordmanager.ui.components.*;
import de.paulwolf.passwordmanager.wizards.FileWizard;
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
import java.util.ArrayList;

public class DatabaseUI extends JFrame {

    private static final String[] columnNames = {"Title", "Username", "Email", "Password", "Last modified"};
    static Database database;
    static JPanel wrapper;
    static JPanel buttonPanel;
    static JPanel tableWrapper;
    static JPanel searchPanel;
    static ScaledButton addEntry;
    static ScaledButton generatePassword;
    static ScaledButton saveDatabase;
    static ScaledButton saveDatabaseAs;
    static ScaledButton settings;
    static ScaledButton openDatabase;
    static JTable table;
    static JScrollPane scrollPane;
    private static int selectedRow;
    private static DefaultTableModel dtm;
    private static ScaledTextField filter;
    private static int t = 0;

    public DatabaseUI(Database db, Component parent) {

        database = db;

        searchPanel = new JPanel(new BorderLayout());
        tableWrapper = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(Configuration.SCALED_SCROLL_BAR_THICKNESS, 0));
        filter = new ScaledTextField();

        updateTable();

        this.setTitle(database.getPath()+ " : PasswordManager");
        wrapper = new JPanel();
        buttonPanel = new JPanel();
        addEntry = new ScaledButton("Add Entry");
        generatePassword = new ScaledButton("Generate Password");
        saveDatabase = new ScaledButton("Save Database");
        saveDatabaseAs = new ScaledButton("Save Database As");
        settings = new ScaledButton("Settings");
        openDatabase = new ScaledButton("Open Database");

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
        this.pack();
//        this.setMinimumSize(new Dimension(1000, 400));
        this.setIconImage(Configuration.IMAGE);
        this.setLocationRelativeTo(parent);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        addEntry.addActionListener(e -> new NewEntryUI(null, -1));

        generatePassword.addActionListener(e -> new PasswordGeneratorUI(null, "", -1));

        saveDatabase.addActionListener(e -> {

            try {
                FileWizard.saveDatabase(database, database.getPath());
            } catch (JSchException ex) {
                JOptionPane.showMessageDialog(this, "Due to a backup host connection error, no database backup could be saved. Check your host or your firewall for port 22.", "No backup made", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        saveDatabaseAs.addActionListener(e -> {

            ScaledFileChooser fileChooser = new ScaledFileChooser();
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
                } catch (JSchException ex) {
                    JOptionPane.showMessageDialog(this, "Due to a backup host connection error, no database backup could be saved. Check your host or your firewall for port 22.", "No backup made", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        settings.addActionListener(e -> new SettingsUI(this));

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
        table.setFont(Configuration.STANDARD_FONT);
        table.getTableHeader().setFont(Configuration.STANDARD_FONT);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(Configuration.SCALED_TABLE_ROW_HEIGHT);
        JPopupMenu popupMenu = new JPopupMenu();
        ScaledMenuItem menuCopyUsername = new ScaledMenuItem("Copy Username");
        ScaledMenuItem menuCopyEmail = new ScaledMenuItem("Copy Email Address");
        ScaledMenuItem menuCopyPassword = new ScaledMenuItem("Copy Password");
        ScaledMenuItem menuItemEdit = new ScaledMenuItem("Edit Entry");
        ScaledMenuItem menuItemRemove = new ScaledMenuItem("Remove Entry");
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

            StringSelection stringSelection = new StringSelection(new String(database.getEntries().get(table.convertRowIndexToModel(selectedRow)).getPassword(), Configuration.STANDARD_CHARSET));
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
        searchPanel.add(new ScaledLabel("   Enter search query:   "), BorderLayout.WEST);
        searchPanel.add(filter, BorderLayout.CENTER);

        filter.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = filter.getText();
                if (text.equalsIgnoreCase("Tetris187") || text.equalsIgnoreCase("Malle ist nur einmal im Jahr") || text.equalsIgnoreCase("69420")) {
                    TetrisMain.runTetris(0);
                    SwingUtilities.invokeLater(() -> filter.setText(""));
                }
                if (text.length() == 9 /*tetris:nn*/ && text.substring(0, 7).equalsIgnoreCase("tetris:") && Integer.parseInt(text.substring(7)) <= 29 && Integer.parseInt(text.substring(7)) >= 0) {
                    TetrisMain.runTetris(Integer.parseInt(text.substring(7)));
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

        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(Configuration.SCALED_SCROLL_BAR_THICKNESS, 0));

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
