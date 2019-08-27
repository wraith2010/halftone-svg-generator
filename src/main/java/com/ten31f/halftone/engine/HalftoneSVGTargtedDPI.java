package com.ten31f.halftone.engine;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.apache.sanselan.ImageInfo;

public class HalftoneSVGTargtedDPI extends HalftoneAbstract {

	private int targetDPI = 10;
	private boolean noBorder = false;

	public HalftoneSVGTargtedDPI(int targetDPI, boolean noBorder) {
		setTargetDPI(targetDPI);
		setNoBorder(noBorder);
	}

	public String halftone(BufferedImage image, ImageInfo imageInfo) {

		collectDPI(imageInfo);

		int step = caculateStep();

		StringBuilder stringBuilder = new StringBuilder(startSVG(image.getWidth(), image.getHeight(), isNoBorder()));

		for (int x = 0; x < image.getWidth(); x += step) {
			for (int y = 0; y < image.getHeight(); y += step) {
				stringBuilder.append(addCircle(x, y, sample(x, y, image)));
			}
		}

		if (!isNoBorder()) {
			stringBuilder.append(svgRec(image.getWidth(), image.getHeight()));
		}
		stringBuilder.append(stopSVG());

		return stringBuilder.toString();
	}

	private String addCircle(int x, int y, float sample) {

		float segment = (float) ((1.0 / getTargetDPI()) - 0.000000001);

		float border = (float) ((isNoBorder()) ? 0.0 : 0.1);

		float xCordinates = (float) ((((float) x) / (float) getWidthDPI()) + border);
		float yCordinates = (float) ((((float) y) / (float) getHeightDPI()) + border);

		float radius = (float) ((sample * (segment * 0.70)) / 2.0);

		return svgCircle(xCordinates, yCordinates, radius);
	}

	protected float sample(int x, int y, BufferedImage image) {
		int rgb = image.getRGB(x, y);
		float[] hsb = Color.RGBtoHSB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF, null);

		return (float) (1.0 - hsb[2]);
	}

	protected int caculateStep() {
		return getWidthDPI() / getTargetDPI();
	}

	protected int getTargetDPI() {
		return targetDPI;
	}

	private void setTargetDPI(int targetDPI) {
		this.targetDPI = targetDPI;
	}

	public boolean isNoBorder() {
		return noBorder;
	}

	public void setNoBorder(boolean noBorder) {
		this.noBorder = noBorder;
	}

}
