package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.TetrisMain;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Movement {

    private static final int LEVEL_00_SPEED = 800;
    private static final int LEVEL_01_SPEED = 750;
    private static final int LEVEL_02_SPEED = 630;
    private static final int LEVEL_03_SPEED = 550;
    private static final int LEVEL_04_SPEED = 470;
    private static final int LEVEL_05_SPEED = 390;
    private static final int LEVEL_06_SPEED = 300;
    private static final int LEVEL_07_SPEED = 220;
    private static final int LEVEL_08_SPEED = 140;
    private static final int LEVEL_09_SPEED = 100;
    private static final int LEVEL_10_12_SPEED = 90;
    private static final int LEVEL_13_15_SPEED = 70;
    private static final int LEVEL_16_18_SPEED = 50;
    private static final int LEVEL_19_28_SPEED = 35;
    private static final int LEVEL_29_SPEED = 15;

    private static final int POINTS_MULTIPLICATION_1_LINE = 40;
    private static final int POINTS_MULTIPLICATION_2_LINES = 100;
    private static final int POINTS_MULTIPLICATION_3_LINES = 300;
    private static final int POINTS_MULTIPLICATION_4_LINES = 1200;

    public static Block block;
    public static Timer timer = new Timer();
    public static boolean lose = false;
    public static boolean temp = false;
    public static boolean pause = false;
    public static int[] lines = new int[20];

    public static boolean lockRotate, lockMove;
    public static long score;
    public static int level = 0;
    public static int achievedLines = 0;
    public static char next = '\0';

    public static int getLevelSpeed() {
        switch (level) {
            case 0:
                return LEVEL_00_SPEED;
            case 1:
                return LEVEL_01_SPEED;
            case 2:
                return LEVEL_02_SPEED;
            case 3:
                return LEVEL_03_SPEED;
            case 4:
                return LEVEL_04_SPEED;
            case 5:
                return LEVEL_05_SPEED;
            case 6:
                return LEVEL_06_SPEED;
            case 7:
                return LEVEL_07_SPEED;
            case 8:
                return LEVEL_08_SPEED;
            case 9:
                return LEVEL_09_SPEED;
            case 10:
            case 11:
            case 12:
                return LEVEL_10_12_SPEED;
            case 13:
            case 14:
            case 15:
                return LEVEL_13_15_SPEED;
            case 16:
            case 17:
            case 18:
                return LEVEL_16_18_SPEED;
            case 29:
                return LEVEL_29_SPEED;
            default:
                return LEVEL_19_28_SPEED;
        }
    }

    public static void startMovement() {

        timer.cancel();

        block = new Block();
        if (next == '\0')
            block.setShape(block.getRandomShape());
        else
            block.setShape(next);

        next = block.getRandomShape();

        if (!block.appear()) {
            Movement.lose = true;
            Movement.timer.cancel();
        }
        timer = new Timer();
        if (lose) {
            timer.cancel();
            return;
        }
        block.updateGui();
        KeyHandler.b = false;

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (lose) {
                    timer.cancel();
                    return;
                }
                moveDown();
            }
        }, getLevelSpeed(), getLevelSpeed());
        lockMove = false;
        TetrisMain.pauseButton.setEnabled(true);
    }

    public static void moveRight() {

        if (lockMove)
            return;

        if (!(block.getSquare(0).getX() >= 9 || block.getSquare(1).getX() >= 9 || block.getSquare(2).getX() >= 9 || block.getSquare(3).getX() >= 9)) {

            if (!TetrisMain.fields[block.getSquare(0).getX() + 1][block.getSquare(0).getY()].isOccupied() && !TetrisMain.fields[block.getSquare(1).getX() + 1][block.getSquare(1).getY()].isOccupied() && !TetrisMain.fields[block.getSquare(2).getX() + 1][block.getSquare(2).getY()].isOccupied() && !TetrisMain.fields[block.getSquare(3).getX() + 1][block.getSquare(3).getY()].isOccupied()) {

                block.moveRight();
            }
        }
    }

    public static void moveLeft() {

        if (lockMove)
            return;

        if (!(block.getSquare(0).getX() <= 0 || block.getSquare(1).getX() <= 0 || block.getSquare(2).getX() <= 0 || block.getSquare(3).getX() <= 0)) {

            if (!TetrisMain.fields[block.getSquare(0).getX() - 1][block.getSquare(0).getY()].isOccupied() && !TetrisMain.fields[block.getSquare(1).getX() - 1][block.getSquare(1).getY()].isOccupied() && !TetrisMain.fields[block.getSquare(2).getX() - 1][block.getSquare(2).getY()].isOccupied() && !TetrisMain.fields[block.getSquare(3).getX() - 1][block.getSquare(3).getY()].isOccupied()) {

                block.moveLeft();
            }
        }
    }

    private static void blink(BlinkObject obj) {

        Color c = TetrisMain.fields[obj.getX()][obj.getY()].getColor();
        TetrisMain.fields[obj.getX()][obj.getY()].setColor(new Color(255, 254, 255));
        TetrisMain.fieldLabel.repaint();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                TetrisMain.fields[obj.getX()][obj.getY()].setColor(c);
                TetrisMain.fieldLabel.repaint();
            }
        }, 100);
    }

    public static void moveDown() {

        if (lockMove)
            return;

        if (block.getSquare(0).getY() + 1 < 20 && block.getSquare(1).getY() + 1 < 20 && block.getSquare(2).getY() + 1 < 20 && block.getSquare(3).getY() + 1 < 20) {

            if (temp && !isClearUnder()) {
                temp = false;
                timer.cancel();
                setFieldsOccupied();
                TetrisMain.fieldLabel.repaint();
            }
        }

        if (block.getSquare(0).getY() == 19 || block.getSquare(1).getY() == 19 || block.getSquare(2).getY() == 19 || block.getSquare(3).getY() == 19) {
            setFieldsOccupied();

        } else {
            if (isClearUnder()) {
                block.moveDown();
            } else {
                temp = true;
            }
        }
    }

    private static void setFieldsOccupied() {
        for (int i = 0; i < 4; i++) {
            TetrisMain.fields[block.getSquare(i).getX()][block.getSquare(i).getY()].setOccupied(true);
            lines[block.getSquare(i).getY()]++;
        }
        checkLines(true, new BlinkObject(block.getSquare(0).getX(), block.getSquare(0).getY()), new BlinkObject(block.getSquare(1).getX(), block.getSquare(1).getY()), new BlinkObject(block.getSquare(2).getX(), block.getSquare(2).getY()), new BlinkObject(block.getSquare(3).getX(), block.getSquare(3).getY()));
    }

    public static void skipDown() {

        if (lockMove)
            return;

        lockRotate = true;
        timer.cancel();

        Thread t1 = new Thread(() -> {

            while (true) {
                if (isClearUnder()) {
                    if (block.getSquare(0).getY() >= 18 || block.getSquare(1).getY() >= 18 || block.getSquare(2).getY() >= 18 || block.getSquare(3).getY() >= 18) {
                        timer.cancel();
                        block.moveDown();
                        setAnimFieldsOccupied();
                        return;
                    } else {
                        block.moveDown();
                    }

                } else {
                    timer.cancel();
                    setAnimFieldsOccupied();
                    return;
                }
            }
        });
        t1.start();
    }

    private static void setAnimFieldsOccupied() {

        for (int i = 0; i < 4; i++) {
            TetrisMain.fields[block.getSquare(i).getX()][block.getSquare(i).getY()].setOccupied(true);
            lines[block.getSquare(i).getY()]++;
        }
        checkLines(true);
        lockRotate = false;
    }

    public static void checkLines(boolean countPoints, BlinkObject... objects) {

        lockMove = true;
        TetrisMain.pauseButton.setEnabled(false);

        boolean doBlink = objects.length == 4;
        int n = 0;
        for (int i = 19; i >= 0; i--)
            if (lines[i] == 10) n++;
        if (countPoints) {
            if (n == 1) score += (long) POINTS_MULTIPLICATION_1_LINE * (level + 1);
            else if (n == 2) score += (long) POINTS_MULTIPLICATION_2_LINES * (level + 1);
            else if (n == 3) score += (long) POINTS_MULTIPLICATION_3_LINES * (level + 1);
            else if (n == 4) score += (long) POINTS_MULTIPLICATION_4_LINES * (level + 1);
        }

        int[] h = new int[n];
        int k = 0;

        for (int i = 0; i < 20; i++) {
            if (lines[i] == 10) {
                h[k++] = i;
                doBlink = false;
            }
        }

        if (k > 0)
            try {
                cleanLines(h);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        for (int i = 19; i >= 0; i--) {
            if (lines[i] == 10) {
                checkLines(false);
                break;
            }
        }
        if (doBlink)
            for (BlinkObject bo : objects)
                blink(bo);

        startMovement();
    }

    public static void cleanLines(int[] height) throws InterruptedException {

        Color[] colors = new Color[10];

        for (int j = 0; j < (height.length == 4 ? 9 : 2); j++) {

            // Lines get blinked
            for (int value : height) {

                // Colors get saved
                if (j == 0)
                    for (int k = 0; k < 10; k++)
                        colors[k] = TetrisMain.fields[j][value].getColor();

                // Colors set to white
                for (int k = 0; k < 10; k++) {
                    if (height.length == 4)
                        TetrisMain.fields[k][value].setColor(new Color(j % 3 * 127, (j + 1) % 3 * 127, (j + 2) % 3 * 127));
                    else
                        TetrisMain.fields[k][value].setColor(new Color(255, 254, 255));
                }
            }
            TetrisMain.fieldLabel.repaint();
            Thread.sleep(25L);

            for (int value : height)
                for (int k = 0; k < 10; k++)
                    TetrisMain.fields[k][value].setColor(colors[k]);

            TetrisMain.fieldLabel.repaint();
            Thread.sleep(25L);
        }
        // Adding score
        achievedLines += height.length;

        // Clearing lines
        for (int k : height) {

            // Setting lines to zero
            for (int j = 0; j < 10; j++) {
                TetrisMain.fields[j][k].setOccupied(false);
                TetrisMain.fields[j][k].setColor(Color.WHITE);
            }
            lines[k] = 0;

            // Match level
            if (achievedLines / 10 > level)
                level = achievedLines / 10;

            // Lines above down
            for (int j = k - 1; j >= 0; j--)
                lineDown(j);
        }
        TetrisMain.fieldLabel.repaint();
    }

    public static void lineDown(int height) {

        for (int i = 0; i < 10; i++) {
            if (TetrisMain.fields[i][height].isOccupied()) {
                lines[height]--;
                lines[height + 1]++;
            }
            TetrisMain.fields[i][height + 1].setColor(TetrisMain.fields[i][height].getColor());
            TetrisMain.fields[i][height + 1].setOccupied(TetrisMain.fields[i][height].getOccupied());
            TetrisMain.fields[i][height].setOccupied(false);
            TetrisMain.fields[i][height].setColor(Color.WHITE);
        }
    }
    private static boolean isClearUnder() {
        return !TetrisMain.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied() && !TetrisMain.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied() && !TetrisMain.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied() && !TetrisMain.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied();
    }
}

class BlinkObject {

    private final int x;
    private final int y;

    public BlinkObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}