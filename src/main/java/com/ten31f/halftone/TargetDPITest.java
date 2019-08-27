package com.ten31f.halftone;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import com.ten31f.halftone.engine.HalftoneSVGTargtedDPI;

public class TargetDPITest {

	private static final String OPTION_TARGETDPI = "--dpi=";
	private static final String OPTION_NO_BORDER = "--no-border";

	public static void main(String... args) throws IOException, ImageReadException {

		if (args.length < 1) {
			printHelp();
		}

		Path inPath = Paths.get(args[args.length - 1]).toAbsolutePath();

		if (!Files.exists(inPath)) {
			System.out.printf("File does not exist: %s%n", inPath);
			System.exit(2);
		}

		int targetDPI = parseTargetDPI(args);

		Path outPath = getOutputPath(inPath, targetDPI);

		HalftoneSVGTargtedDPI halftone = new HalftoneSVGTargtedDPI(targetDPI, parseNoBorder(args));

		Files.write(Paths.get(outPath.toString()),
				halftone.halftone(ImageIO.read(inPath.toFile()), Sanselan.getImageInfo(inPath.toFile())).getBytes());

		System.out.println(String.format("Output: %s @ %s dpi", outPath, targetDPI));

	}

	private static int parseTargetDPI(String... args) {

		for (String arg : args) {
			if (arg.startsWith(OPTION_TARGETDPI)) {
				try {
					return Integer.parseInt(arg.substring(OPTION_TARGETDPI.length()));
				} catch (NumberFormatException formatException) {
					System.err.println("Unable to parse (arg.substring(OPTION_TARGETDPI.length() + 1)) as a number.");
					printHelp();
				}
			}
		}

		return 10;
	}

	private static boolean parseNoBorder(String... args) {

		for (String arg : args) {
			if (arg.startsWith(OPTION_NO_BORDER)) {
				return true;
			}
		}

		return false;
	}

	private static Path getOutputPath(Path inPath, int dpi) {
		String filePath = inPath.toString();
		int lastDot = filePath.lastIndexOf('.');
		filePath = filePath.substring(0, lastDot) + String.format("-halftone(%s).svg", dpi);

		return Paths.get(filePath).toAbsolutePath();
	}

	private static void printHelp() {
		System.out.println("usage: java -jar halftone.jar [options] \"path to image\"");
		System.out.println("--dpi=        Dots per inch. I recomend 10-15(default 10)");
		System.out.println("--no-border   No border cut line will be added");

		System.exit(1);
	}

}
