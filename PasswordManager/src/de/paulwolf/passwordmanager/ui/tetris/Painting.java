package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Configuration;
import de.paulwolf.passwordmanager.TetrisMain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Painting extends JLabel {

    private static final long serialVersionUID = 1L;

    static BufferedImage field;
    static BufferedImage emptyField;
    static BufferedImage grid;
    public static BufferedImage pause, play;
    static BufferedImage[] blocks = new BufferedImage[7];
    static BufferedImage[] darkBlocks = new BufferedImage[7];

    public static void loadImages() throws IOException {
        field = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/empty_field.png")));
        emptyField = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/empty_field.png")));
        grid = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/grid.png")));
        pause = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/pause.png")));
        play = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/play.png")));

        blocks[0] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/blue.png")));
        blocks[1] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/cyan.png")));
        blocks[2] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/green.png")));
        blocks[3] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/mustard.png")));
        blocks[4] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/purple.png")));
        blocks[5] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/red.png")));
        blocks[6] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/yellow.png")));
        darkBlocks[0] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/blue_dark.png")));
        darkBlocks[1] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/cyan_dark.png")));
        darkBlocks[2] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/green_dark.png")));
        darkBlocks[3] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/mustard_dark.png")));
        darkBlocks[4] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/purple_dark.png")));
        darkBlocks[5] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/red_dark.png")));
        darkBlocks[6] = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/yellow_dark.png")));
    }

    protected BufferedImage colorToImage(Color c, boolean dark) {

        if (c == Color.BLUE) return dark ? darkBlocks[0] : blocks[0];
        if (c == Color.CYAN) return dark ? darkBlocks[1] : blocks[1];
        if (c == Color.GREEN) return dark ? darkBlocks[2] : blocks[2];
        if (c == Color.ORANGE) return dark ? darkBlocks[3] : blocks[3];
        if (c == Color.MAGENTA) return dark ? darkBlocks[4] : blocks[4];
        if (c == Color.RED) return dark ? darkBlocks[5] : blocks[5];
        if (c == Color.YELLOW) return dark ? darkBlocks[6] : blocks[6];
        return null;
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        Graphics2D graphics = field.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 20; y++) {

                BufferedImage blockImg = colorToImage(TetrisMain.fields[x][y].getColor(), false);
                if (blockImg != null && !Movement.pause) {
                    g.drawImage(blockImg, x * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, y * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                } else {
                    if (TetrisMain.fields[x][y].getColor() != Color.WHITE)
                        graphics.fillRect(0, 0, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION);
                    g.drawImage(field, x * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, y * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    if (TetrisMain.fields[x][y].getColor() == Color.WHITE || Movement.pause)
                        g.drawImage(emptyField, x * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, y * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    else g.drawImage(grid, x * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, y * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                }
            }
        }
        g.setColor(Color.WHITE);
        g.setFont(Configuration.TETRIS_FONT);

        g.drawString("SCORE:", Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_ELEMENT_MARGIN);
        g.drawString(String.valueOf(Movement.score), Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_ELEMENT_MARGIN + Configuration.SCALED_TETRIS_TEXT_MARGIN);
        g.drawString("LINES:", Configuration.SCALED_TETRIS_WIDTH_MARGIN, 3 * Configuration.SCALED_TETRIS_ELEMENT_MARGIN);
        g.drawString(String.valueOf(Movement.achievedLines), Configuration.SCALED_TETRIS_WIDTH_MARGIN, 3 * Configuration.SCALED_TETRIS_ELEMENT_MARGIN + Configuration.SCALED_TETRIS_TEXT_MARGIN);
        g.drawString("LEVEL:", Configuration.SCALED_TETRIS_WIDTH_MARGIN, 5 * Configuration.SCALED_TETRIS_ELEMENT_MARGIN);
        g.drawString(String.valueOf(Movement.level), Configuration.SCALED_TETRIS_WIDTH_MARGIN, 5 * Configuration.SCALED_TETRIS_ELEMENT_MARGIN + Configuration.SCALED_TETRIS_TEXT_MARGIN);
        g.drawString("NEXT:", Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_BOTTOM_ELEMENT);

        g.setColor(Field.getColor(Movement.next));

        if (!Movement.pause)
            switch (Movement.next) {
                case 'I':
                    g.drawImage(blocks[1], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[1], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[1], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[1], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 3, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
                case 'J':
                    g.drawImage(blocks[0], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[0], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[0], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[0], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
                case 'L':
                    g.drawImage(blocks[3], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[3], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[3], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[3], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
                case 'O':
                    g.drawImage(blocks[6], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[6], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[6], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[6], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
                case 'S':
                    g.drawImage(blocks[2], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[2], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[2], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[2], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
                case 'T':
                    g.drawImage(blocks[4], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[4], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[4], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[4], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
                case 'Z':
                    g.drawImage(blocks[5], Configuration.SCALED_TETRIS_WIDTH_MARGIN, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[5], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[5], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    g.drawImage(blocks[5], Configuration.SCALED_TETRIS_WIDTH_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION * 2, Configuration.SCALED_TETRIS_HEIGHT_MARGIN + Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
                    break;
            }

        int minReach = 20;
        int reach;

        for (int i = 0; i < 4; i++) {
            reach = Movement.block.getSquare(i).getY();
            while (reach + 1 < 20 && !TetrisMain.fields[Movement.block.getSquare(i).getX()][reach + 1].isOccupied()) reach++;
            if (reach - Movement.block.getSquare(i).getY() < minReach)
                minReach = reach - Movement.block.getSquare(i).getY();
        }
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3F);
        ((Graphics2D) g).setComposite(ac);
        if (Movement.block.exists() && !Movement.pause)
            for (int i = 0; i < 4; i++)
                g.drawImage(colorToImage(TetrisMain.fields[Movement.block.getSquare(0).getX()][Movement.block.getSquare(0).getY()].getColor(), true), Movement.block.getSquare(i).getX() * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, (minReach + Movement.block.getSquare(i).getY()) * Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, Configuration.SCALED_TETRIS_BLOCK_DIMENSION, null);
    }
}