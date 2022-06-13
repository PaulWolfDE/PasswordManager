package de.paulwolf.passwordmanager;

import de.paulwolf.passwordmanager.ui.MainUI;
import de.paulwolf.passwordmanager.ui.OpenDatabaseUI;
import de.paulwolf.passwordmanager.ui.UpdateUI;
import de.paulwolf.passwordmanager.ui.tetris.Field;
import de.paulwolf.passwordmanager.ui.tetris.KeyHandler;
import de.paulwolf.passwordmanager.ui.tetris.Movement;
import de.paulwolf.passwordmanager.ui.tetris.Painting;
import de.paulwolf.passwordmanager.utility.JSONParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    // STANDARD HASH ALGORITHM
    public static final String HASH_ALGORITHM = "SHA-256";
    public static final String[] HASH_ALGORITHMS = {"SHA-256", "MD5"};
    // STANDARD ENCRYPTION ALGORITHM
    public static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    public static final String[] ENCRYPTION_ALGORITHMS = {"AES/GCM/NoPadding", "AES/CTR/NoPadding",
            "AES/CBC/PKCS5Padding", "AES/ECB/PKCS5Padding", "Serpent/CTR/NoPadding", "Serpent/CBC/ISO10126",
            "Serpent/ECB/ISO10126", "Twofish/CTR/NoPadding", "Twofish/CBC/ISO10126", "Twofish/ECB/ISO10126",
            "Blowfish/CTR/NoPadding", "Blowfish/CBC/PKCS5Padding", "Blowfish/ECB/PKCS5Padding"};
    // PBKDF2 HMAC algorithm
    public static final String HMAC_ALGORITHM = "HMAC-SHA-256";
    // PBKDF2 # of iterations
    public static final int ITERATIONS = 310000;
    // DATE FORMAT
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd@HH:mm");
    // VERSION NUMBER
    public static final String VERSION_NUMBER = "1.4.5";
    // VERSIONS COMPATIBLE WITH
    public static final String[] COMPATIBLE_VERSIONS = {"1.4.5", "1.4.4", "1.4.3", "1.4.2", "1.4.1", "1.4.0", "1.3.9", "1.3.8", "1.3.7", "1.3.6", "1.3.5", "1.3.4", "1.3.3", "1.3.2"};
    // STANDARD ECHO CHAR
    public static final char ECHO_CHAR = 0x2022;
    // JFRAME ICON
    public static Image IMAGE;
    public static MainUI ui;
    public static Painting fieldLabel;
    public static boolean windows = false;
    static int maxX = 10;
    static int maxY = 20;
    public static Field[][] fields = new Field[maxX][maxY];
    static JFrame frame;
    public static JToggleButton pauseButton;
    static Dimension frameSize;
    static Dimension fieldLabelSize;

    public static void main(String[] args) {

        loadIconImage();

        ui = new MainUI();
       if (!JSONParser.isUpToDate())
            new UpdateUI();

        if (args.length > 0) {

            ui.databaseFile = new File(args[0]);
            new OpenDatabaseUI(new File(args[0]).getAbsolutePath());
        }
    }

    public static void runTetris(int level) {

        frameSize = new Dimension(400 + 200, 800);
        fieldLabelSize = new Dimension(320 + 200, 640);

        Movement.pause = false;
        Movement.lockMove = false;
        Movement.lockRotate = false;
        Movement.lose = false;
        Movement.level = level;
        Movement.score = 0;
        Movement.achievedLines = 0;
        for (int i = 0; i < 20; i++)
            Movement.lines[i] = 0;

        loadFields();
        loadGui();
        Movement.startMovement();
    }

    private static void loadIconImage() {

        try {
            IMAGE = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/icon.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadGui() {

        frame = new JFrame();
        frame.setSize(frameSize);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Tetris");
        frame.addKeyListener(new KeyHandler());
        frame.setResizable(false);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(60, 63, 65));

        try {
            Painting.loadImages();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pauseButton = new JToggleButton();
        pauseButton.setBounds(420, 420, 50, 50);
        pauseButton.setFocusable(false);
        pauseButton.setIcon(new ImageIcon(Painting.pause));
        pauseButton.setBorder(BorderFactory.createEmptyBorder());
        pauseButton.setContentAreaFilled(false);
        pauseButton.addActionListener(e -> {
            if (pauseButton.isSelected()) {
                pauseButton.setIcon(new ImageIcon(Painting.play));
                Movement.timer.cancel();
                Movement.pause = true;
                Movement.lockMove = true;
                Movement.lockRotate = true;
                fieldLabel.repaint();
            } else {
                pauseButton.setIcon(new ImageIcon(Painting.pause));
                Movement.pause = false;
                Movement.lockMove = false;
                Movement.lockRotate = false;
                frame.requestFocus();
                fieldLabel.repaint();
                (Movement.timer = new Timer()).scheduleAtFixedRate(new TimerTask() {

                    @Override
                    public void run() {
                        if (Movement.lose) {
                            Movement.timer.cancel();
                            return;
                        }
                        Movement.moveDown();
                    }
                }, Movement.getLevelSpeed(), Movement.getLevelSpeed());
            }
        });

        JLabel cpmLabel = new JLabel();
        cpmLabel.add(pauseButton);
        cpmLabel.setSize(fieldLabelSize);

        fieldLabel = new Painting();
        fieldLabel.setSize(fieldLabelSize);

        // 20px = width of title bar, 10px borders on both sides (WINDOWS ONLY)

        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            fieldLabel.setLocation((frameSize.width - fieldLabelSize.width) / 2 - 10, (frameSize.height - fieldLabelSize.height) / 2 - 20);
            windows = true;
        } else
            fieldLabel.setLocation((frameSize.width - fieldLabelSize.width) / 2, (frameSize.height - fieldLabelSize.height) / 2 - 20);

        // fieldLabel.setBackground(Color.BLACK);
        fieldLabel.setVisible(true);
        frame.add(cpmLabel);
        frame.add(fieldLabel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Movement.lose = true;
            }
        });

        frame.setVisible(true);
    }

    private static void loadFields() {

        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {

                fields[x][y] = new Field();
                fields[x][y].setOccupied(false);
                fields[x][y].setColor(Color.WHITE);
            }
        }
    }
}
