package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

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
    public static int[] lines = new int[20];

    public static long score;
    public static int level = 0;
    public static int achievedLines = 0;
    public static char next = '\0';

    private static int getLevelSpeed() {
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
        if (next == '\0') {
            block.setShape(block.getRandomShape());
        } else {
            block.setShape(next);
        }

        next = block.getRandomShape();

        if (!block.appear()) {
            Movement.lose = true;
            Movement.timer.cancel();
        }
        timer = new Timer();
        if (lose) {
            System.out.println("u lose");
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

    }

    public static void moveRight() {
        if (!(block.getSquare(0).getX() >= 9 || block.getSquare(1).getX() >= 9 || block.getSquare(2).getX() >= 9 || block.getSquare(3).getX() >= 9)) {

            if (!Main.fields[block.getSquare(0).getX() + 1][block.getSquare(0).getY()].isOccupied() && !Main.fields[block.getSquare(1).getX() + 1][block.getSquare(1).getY()].isOccupied() && !Main.fields[block.getSquare(2).getX() + 1][block.getSquare(2).getY()].isOccupied() && !Main.fields[block.getSquare(3).getX() + 1][block.getSquare(3).getY()].isOccupied()) {

                block.moveRight();
            }
        }
    }

    public static void moveLeft() {
        if (!(block.getSquare(0).getX() <= 0 || block.getSquare(1).getX() <= 0 || block.getSquare(2).getX() <= 0 || block.getSquare(3).getX() <= 0)) {

            if (!Main.fields[block.getSquare(0).getX() - 1][block.getSquare(0).getY()].isOccupied() && !Main.fields[block.getSquare(1).getX() - 1][block.getSquare(1).getY()].isOccupied() && !Main.fields[block.getSquare(2).getX() - 1][block.getSquare(2).getY()].isOccupied() && !Main.fields[block.getSquare(3).getX() - 1][block.getSquare(3).getY()].isOccupied()) {

                block.moveLeft();
            }
        }
    }

    private static void blink(BlinkObject obj) {

        Color c = Main.fields[obj.getX()][obj.getY()].getColor();
        Main.fields[obj.getX()][obj.getY()].setColor(new Color(255, 254, 255));
        Main.fieldLabel.repaint();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Main.fields[obj.getX()][obj.getY()].setColor(c);
                Main.fieldLabel.repaint();
            }
        }, 100);
    }

    public static void moveDown() {

        if (block.getSquare(0).getY() + 1 < 20 && block.getSquare(1).getY() + 1 < 20 && block.getSquare(2).getY() + 1 < 20 && block.getSquare(3).getY() + 1 < 20) {

            if (temp && !(!Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied() && !Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied() && !Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied() && !Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied())) {
                temp = false;
                timer.cancel();
                for (int i = 0; i < 4; i++) {
                    Main.fields[block.getSquare(i).getX()][block.getSquare(i).getY()].setOccupied(true);
                    lines[block.getSquare(i).getY()]++;
                }
                checkLines(true, new BlinkObject(block.getSquare(0).getX(), block.getSquare(0).getY()), new BlinkObject(block.getSquare(1).getX(), block.getSquare(1).getY()), new BlinkObject(block.getSquare(2).getX(), block.getSquare(2).getY()), new BlinkObject(block.getSquare(3).getX(), block.getSquare(3).getY()));
                Main.fieldLabel.repaint();
                startMovement();
            }
        }

        if (block.getSquare(0).getY() == 19 || block.getSquare(1).getY() == 19 || block.getSquare(2).getY() == 19 || block.getSquare(3).getY() == 19) {
            for (int i = 0; i < 4; i++) {
                Main.fields[block.getSquare(i).getX()][block.getSquare(i).getY()].setOccupied(true);
                lines[block.getSquare(i).getY()]++;
            }
            checkLines(true, new BlinkObject(block.getSquare(0).getX(), block.getSquare(0).getY()), new BlinkObject(block.getSquare(1).getX(), block.getSquare(1).getY()), new BlinkObject(block.getSquare(2).getX(), block.getSquare(2).getY()), new BlinkObject(block.getSquare(3).getX(), block.getSquare(3).getY()));
            startMovement();

        } else {
            if (!Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied() && !Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied() && !Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied() && !Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied()) {
                block.moveDown();
            } else {
                temp = true;
            }
        }
    }

    public static void skipDown() {

        timer.cancel();

        Thread t1 = new Thread(() -> {

            while (true) {
                if (!Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY() + 1].isOccupied() && !Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY() + 1].isOccupied() && !Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY() + 1].isOccupied() && !Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY() + 1].isOccupied()) {

                    if (block.getSquare(0).getY() >= 18 || block.getSquare(1).getY() >= 18 || block.getSquare(2).getY() >= 18 || block.getSquare(3).getY() >= 18) {
                        timer.cancel();
                        block.moveDown();
                        Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY()].setOccupied(true);
                        Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY()].setOccupied(true);
                        Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY()].setOccupied(true);
                        Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY()].setOccupied(true);
                        for (int i = 0; i < 4; i++) {
                            lines[block.getSquare(i).getY()]++;
                        }
                        checkLines(true);
                        startMovement();
                        return;
                    } else {
                        block.moveDown();
                    }

                } else {
                    timer.cancel();
                    Main.fields[block.getSquare(0).getX()][block.getSquare(0).getY()].setOccupied(true);
                    Main.fields[block.getSquare(1).getX()][block.getSquare(1).getY()].setOccupied(true);
                    Main.fields[block.getSquare(2).getX()][block.getSquare(2).getY()].setOccupied(true);
                    Main.fields[block.getSquare(3).getX()][block.getSquare(3).getY()].setOccupied(true);
                    for (int i = 0; i < 4; i++) {
                        lines[block.getSquare(i).getY()]++;
                    }
                    checkLines(true);
                    startMovement();
                    return;
                }
            }
        });
        t1.start();
    }

    public static void checkLines(boolean countPoints, BlinkObject... objects) {

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
        System.out.println("Score: " + score);

        for (int i = 19; i >= 0; i--) {
            if (lines[i] == 10) {
                cleanLines(i);
                doBlink = false;
            }
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
    }

    public static void cleanLines(int height) {

        for (int k = 0; k < 2; k++) {
            Color[] colors = new Color[10];

            for (int i = 0; i < 10; i++) {
                colors[i] = Main.fields[i][height].getColor();
                Main.fields[i][height].setColor(new Color(255, 254, 255));
            }

            Main.fieldLabel.repaint();
            try {
                Thread.sleep(25L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; i++)
                Main.fields[i][height].setColor(colors[i]);
            Main.fieldLabel.repaint();
            Main.fieldLabel.repaint();
            try {
                Thread.sleep(25L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        achievedLines++;
        for (int x = 0; x < 10; x++) {
            Main.fields[x][height].setOccupied(false);
            Main.fields[x][height].setColor(Color.WHITE);
        }
        lines[height] = 0;

        if (achievedLines % 10 == 0) {
            level++;
            System.out.println("Level up: " + level);
        }
        for (int i = height - 1; i >= 0; i--) {
            lineDown(i);
            Main.fieldLabel.repaint();
        }
    }

    public static void lineDown(int height) {

        for (int i = 0; i < 10; i++) {
            if (Main.fields[i][height].isOccupied()) {
                lines[height]--;
                lines[height + 1]++;
            }
            Main.fields[i][height + 1].setColor(Main.fields[i][height].getColor());
            Main.fields[i][height + 1].setOccupied(Main.fields[i][height].getOccupied());
            Main.fields[i][height].setOccupied(false);
            Main.fields[i][height].setColor(Color.WHITE);
        }
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