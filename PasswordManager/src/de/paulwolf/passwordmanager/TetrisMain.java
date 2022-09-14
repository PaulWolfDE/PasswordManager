package de.paulwolf.passwordmanager;

import de.paulwolf.passwordmanager.ui.tetris.Field;
import de.paulwolf.passwordmanager.ui.tetris.KeyHandler;
import de.paulwolf.passwordmanager.ui.tetris.Movement;
import de.paulwolf.passwordmanager.ui.tetris.Painting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;

public class TetrisMain {

    public static Painting fieldLabel;
    public static boolean windows = false;
    static int maxX = 10;
    static int maxY = 20;
    public static Field[][] fields = new Field[maxX][maxY];
    static JFrame frame;
    public static JToggleButton pauseButton;
    static Dimension frameSize;
    static Dimension fieldLabelSize;

    public static void runTetris(int level) {

        frameSize = Configuration.SCALED_TETRIS_WINDOW_SIZE;
        fieldLabelSize = Configuration.SCALED_TETRIS_LABEL_SIZE;

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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        pauseButton = new JToggleButton();
        pauseButton.setBounds(
                (int) (Configuration.SCALED_TETRIS_WIDTH_MARGIN + 70. / 1920 * screenSize.getWidth()),
                (int) (420. / 1080 * screenSize.getHeight()),
                (int) (50. / 1920 * screenSize.getWidth()),
                (int) (50. / 1080 * screenSize.getHeight())
        );
        pauseButton.setFocusable(false);
        pauseButton.setIcon(new ImageIcon(Painting.pause.getScaledInstance(pauseButton.getWidth(), pauseButton.getHeight(), Image.SCALE_SMOOTH)));
        pauseButton.setBorder(BorderFactory.createEmptyBorder());
        pauseButton.setContentAreaFilled(false);
        pauseButton.addActionListener(e -> {
            if (pauseButton.isSelected()) {
                pauseButton.setIcon(new ImageIcon(Painting.play.getScaledInstance(pauseButton.getWidth(), pauseButton.getHeight(), Image.SCALE_SMOOTH)));
                Movement.timer.cancel();
                Movement.pause = true;
                Movement.lockMove = true;
                Movement.lockRotate = true;
                fieldLabel.repaint();
            } else {
                pauseButton.setIcon(new ImageIcon(Painting.pause.getScaledInstance(pauseButton.getWidth(), pauseButton.getHeight(), Image.SCALE_SMOOTH)));
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
