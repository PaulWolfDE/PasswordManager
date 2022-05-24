package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

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
    static BufferedImage[] blocks = new BufferedImage[7];
    static BufferedImage[] darkBlocks = new BufferedImage[7];

    public static void loadImages() throws IOException {
        field = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/empty_field.png")));
        emptyField = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/empty_field.png")));
        grid = ImageIO.read(Objects.requireNonNull(Painting.class.getResource("/grid.png")));

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

                // graphics.setPaint(Main.fields[x][y].getColor());
                BufferedImage blockImg = colorToImage(Main.fields[x][y].getColor(), false);
                if (blockImg != null && !Movement.pause) {
                    g.drawImage(blockImg, x * 32, y * 32, 32, 32, null);
                } else {
                    if (Main.fields[x][y].getColor() != Color.WHITE)
                        graphics.fillRect(0, 0, 32, 32);
                    g.drawImage(field, x * 32, y * 32, 32, 32, null);
                    if (Main.fields[x][y].getColor() == Color.WHITE || Movement.pause)
                        g.drawImage(emptyField, x * 32, y * 32, 32, 32, null);
                    else g.drawImage(grid, x * 32, y * 32, 32, 32, null);
                }
            }
        }
        g.setColor(Color.WHITE);

        if (!Main.windows) g.setFont(new Font("Nimbus Mono PS", Font.BOLD, 40));
        else g.setFont(new Font("Consolas", Font.BOLD, 40));

        g.drawString("SCORE:", 350, 50);
        g.drawString(String.valueOf(Movement.score), 350, 90);
        g.drawString("LINES:", 350, 150);
        g.drawString(String.valueOf(Movement.achievedLines), 350, 190);
        g.drawString("LEVEL:", 350, 250);
        g.drawString(String.valueOf(Movement.level), 350, 290);
        g.drawString("NEXT:", 350, 520);

        g.setColor(Field.getColor(Movement.next));

        switch (Movement.next) {
            case 'I':
                g.drawImage(blocks[1], 350, 544, 32, 32, null);
                g.drawImage(blocks[1], 350 + 32, 544, 32, 32, null);
                g.drawImage(blocks[1], 350 + 32 * 2, 544, 32, 32, null);
                g.drawImage(blocks[1], 350 + 32 * 3, 544, 32, 32, null);
                break;
            case 'J':
                g.drawImage(blocks[0], 350, 544, 32, 32, null);
                g.drawImage(blocks[0], 350, 544 + 32, 32, 32, null);
                g.drawImage(blocks[0], 350 + 32, 544 + 32, 32, 32, null);
                g.drawImage(blocks[0], 350 + 32 * 2, 544 + 32, 32, 32, null);
                break;
            case 'L':
                g.drawImage(blocks[3], 350, 544 + 32, 32, 32, null);
                g.drawImage(blocks[3], 350 + 32, 544 + 32, 32, 32, null);
                g.drawImage(blocks[3], 350 + 32 * 2, 544 + 32, 32, 32, null);
                g.drawImage(blocks[3], 350 + 32 * 2, 544, 32, 32, null);
                break;
            case 'O':
                g.drawImage(blocks[6], 350, 544, 32, 32, null);
                g.drawImage(blocks[6], 350 + 32, 544, 32, 32, null);
                g.drawImage(blocks[6], 350, 544 + 32, 32, 32, null);
                g.drawImage(blocks[6], 350 + 32, 544 + 32, 32, 32, null);
                break;
            case 'S':
                g.drawImage(blocks[2], 350, 544 + 32, 32, 32, null);
                g.drawImage(blocks[2], 350 + 32, 544 + 32, 32, 32, null);
                g.drawImage(blocks[2], 350 + 32, 544, 32, 32, null);
                g.drawImage(blocks[2], 350 + 32 * 2, 544, 32, 32, null);
                break;
            case 'T':
                g.drawImage(blocks[4], 350, 544 + 32, 32, 32, null);
                g.drawImage(blocks[4], 350 + 32, 544 + 32, 32, 32, null);
                g.drawImage(blocks[4], 350 + 32, 544, 32, 32, null);
                g.drawImage(blocks[4], 350 + 32 * 2, 544 + 32, 32, 32, null);
                break;
            case 'Z':
                g.drawImage(blocks[5], 350, 544, 32, 32, null);
                g.drawImage(blocks[5], 350 + 32, 544, 32, 32, null);
                g.drawImage(blocks[5], 350 + 32, 544 + 32, 32, 32, null);
                g.drawImage(blocks[5], 350 + 32 * 2, 544 + 32, 32, 32, null);
                break;
        }

        int minReach = 20;
        int reach;

        for (int i = 0; i < 4; i++) {
            reach = Movement.block.getSquare(i).getY();
            while (reach + 1 < 20 && !Main.fields[Movement.block.getSquare(i).getX()][reach + 1].isOccupied()) reach++;
            if (reach - Movement.block.getSquare(i).getY() < minReach)
                minReach = reach - Movement.block.getSquare(i).getY();
        }
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3F);
        ((Graphics2D) g).setComposite(ac);
        if (Movement.block.exists() && !Movement.pause)
            for (int i = 0; i < 4; i++)
                g.drawImage(colorToImage(Main.fields[Movement.block.getSquare(0).getX()][Movement.block.getSquare(0).getY()].getColor(), true), Movement.block.getSquare(i).getX() * 32, (minReach + Movement.block.getSquare(i).getY()) * 32, 32, 32, null);

    }
}