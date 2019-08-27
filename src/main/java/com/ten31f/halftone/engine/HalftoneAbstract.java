package com.ten31f.halftone.engine;

import org.apache.sanselan.ImageInfo;

public class HalftoneAbstract {

	private static final String SVG_START = "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\"%sin\" height=\"%sin\">\n";
	private static final String SVG_END = "</svg>";
	private static final String SVG_CIRCLE = "<circle cx=\"%sin\" cy=\"%sin\" r=\"%sin\" stroke=\"red\" stroke-width=\"1\" fill=\"red\" />\n";
	private static final String SVG_REC = "<rect x=\"0.0in\"  width=\"%sin\" height=\"%sin\" style=\"stroke-width:1;stroke:red\" fill-opacity=\"0.0\" />\n";

	private int widthDPI;
	private int heightDPI;

	public HalftoneAbstract() {

	}

	protected void collectDPI(ImageInfo imageInfo) {
		setWidthDPI(imageInfo.getPhysicalWidthDpi());
		setHeightDPI(imageInfo.getPhysicalHeightDpi());
	}

	protected String startSVG(int width, int height, boolean noBorder) {

		float border = (float) ((noBorder)? 0.0 : 0.2);
		
		float widthInches = (float) ((width / getWidthDPI()) + border);
		float heightInches = (float) ((height / getHeightDPI()) + border);

		return String.format(SVG_START, widthInches, heightInches);
	}

	protected String svgRec(int width, int height) {

		float widthInches = (float) ((width / getWidthDPI()) + 0.2);
		float heightInches = (float) ((height / getHeightDPI()) + 0.2);

		return String.format(SVG_REC, widthInches, heightInches);
	}

	protected String stopSVG() {
		return SVG_END;
	}

	protected String svgCircle(float xCordinates, float yCordinates, float radius) {
		return String.format(SVG_CIRCLE, xCordinates, yCordinates, radius);
	}

	protected int getWidthDPI() {
		return widthDPI;
	}

	protected void setWidthDPI(int widthDPI) {
		this.widthDPI = widthDPI;
	}

	protected int getHeightDPI() {
		return heightDPI;
	}

	protected void setHeightDPI(int heightDPI) {
		this.heightDPI = heightDPI;
	}

}
