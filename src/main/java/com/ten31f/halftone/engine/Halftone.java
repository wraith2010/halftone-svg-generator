package com.ten31f.halftone.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Halftone {

	public enum DotShape {
		DOT, BLOCK
	}

	private final int size;
	private final int spacing;
	private final int scale;
	private final DotShape shape;
	private final Color bg;
	private final Color fg;
	private final int minimum;

	/**
	 * Create a new Halftone image processor, with the provided settings.
	 *
	 * @param size
	 *            size of the halftone dots, in pixels
	 * @param spacing
	 *            spacing between the dots, in pixels
	 * @param scale
	 *            to ensure accurate rendering of dots, the output image may need to
	 *            be rendered at a larger scale, then scaled back down. the higher
	 *            this value, the more memory will be used, but more luscious dots
	 *            will be produced
	 * @param shape
	 *            of the halftone dots
	 * @param bg
	 *            background colour, or null to use the source image itself as the
	 *            background
	 * @param fg
	 *            foreground (dot) colour, or null to use the colour of the pixels
	 *            under the dot
	 */
	public Halftone(int size, int spacing, int scale, int minimum, DotShape shape, Color bg, Color fg,
			boolean negative) {
		this.size = size;
		this.spacing = spacing;
		this.scale = scale;
		this.shape = shape;
		this.minimum = minimum;
		if (negative) {
			this.bg = fg;
			this.fg = bg;
		} else {
			this.bg = bg;
			this.fg = fg;
		}

	}

	/**
	 * Process an image to generate a halftone effect.
	 * <p>
	 * A new image is created, and the original image is scanned at pixel intervals
	 * defined by the
	 * 
	 * <pre>
	 * size
	 * </pre>
	 * 
	 * option, and dots are drawn on the new image, scaled according to the
	 * brightness value of the pixel on the original image.
	 *
	 * @param image
	 *            input image
	 * @return halftone effect copy of the input image
	 */
	public BufferedImage halftone(BufferedImage image) {
		BufferedImage bigImage = new BufferedImage(image.getWidth() * scale, image.getHeight() * scale,
				BufferedImage.TYPE_INT_RGB);

		float maxDotSize = (float) (size * scale * 1.0);

		float minimumDotSize = (float) ((size * scale * 1.0) * (minimum / 10.0));

		Graphics2D graphics = bigImage.createGraphics();
		graphics.setColor(bg);
		graphics.fillRect(0, 0, bigImage.getWidth(), bigImage.getHeight());

		if (bg == null) {
			graphics.drawImage(image.getScaledInstance(bigImage.getWidth(), bigImage.getHeight(), Image.SCALE_SMOOTH),
					0, 0, null);
		}

		if (fg != null)
			graphics.setColor(fg);

		for (int x = 0; x < image.getWidth(); x += size + spacing) {
			for (int y = 0; y < image.getHeight(); y += size + spacing) {
				int rgb = image.getRGB(x, y);
				float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF, null);

				if (fg == null)
					graphics.setColor(new Color(rgb));

				float dotSize = (float) (size * scale * hsb[2]);
				dotSize = (maxDotSize - dotSize < minimumDotSize) ? minimumDotSize : maxDotSize - dotSize;

				if (shape == DotShape.DOT) {
					graphics.drawOval((x - ((size - spacing) / 2)) * scale, (y - ((size - spacing) / 2)) * scale,
							Math.round(dotSize), Math.round(dotSize));
					// graphics.fillOval((x - ((size - spacing) / 2)) * scale, (y - ((size -
					// spacing) / 2)) * scale,
					// Math.round(dotSize), Math.round(dotSize));
				} else if (shape == DotShape.BLOCK) {
					graphics.fillRect((x - ((size - spacing) / 2)) * scale, (y - ((size - spacing) / 2)) * scale,
							Math.round(dotSize), Math.round(dotSize));
				}
			}
		}

		BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		result.createGraphics().drawImage(
				bigImage.getScaledInstance(result.getWidth(), result.getHeight(), Image.SCALE_SMOOTH), 0, 0, null);

		return result;
	}

}
