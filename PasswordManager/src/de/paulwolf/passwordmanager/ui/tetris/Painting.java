package de.paulwolf.passwordmanager.ui.tetris;

import de.paulwolf.passwordmanager.Main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serial;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Painting extends JLabel {

	@Serial
	private static final long serialVersionUID = 1L;

	BufferedImage field;
	BufferedImage grid;

	protected void paintComponent(Graphics g) {

		try {
			field = ImageIO.read(Objects.requireNonNull(getClass().getResource("/empty_field.png")));
			grid = ImageIO.read(Objects.requireNonNull(getClass().getResource("/grid.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Graphics2D graphics = field.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for(int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {

				graphics.setPaint(Main.fields[x][y].getColor());
				graphics.fillRect(0, 0, 32, 32);

				g.drawImage(field, x*32, y*32, 32, 32, null);
				g.drawImage(grid, x*32, y*32, 32, 32, null);

			}
		}
	}
}