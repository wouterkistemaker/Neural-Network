package nl.wouterkistemaker.neuralnetwork.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

public final class PixelUtils {

    private PixelUtils() {
        throw new AssertionError("Instantiating Utils class is forbidden.");
    }

    public static int[][] getPixels(String filePath) {
        BufferedImage image;
        int width;
        int height;

        try {
            if (filePath.startsWith("resource:")) {
                image = ImageIO.read(Objects.requireNonNull(PixelUtils.class.getClassLoader().getResourceAsStream(filePath.substring(8))));
            } else {
                image = ImageIO.read(new File(filePath));
            }

            if (image == null) {
                throw new IllegalStateException("Could not read image.");
            }

            width = image.getWidth();
            height = image.getHeight();
            final int[][] data = new int[width][height];

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    data[x][y] = image.getRGB(x, y);
                }
            }

            return data;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Pixel parse(int rgba) {
        final int red = (rgba >> 16) & 0xFF;
        final int green = (rgba >> 8) & 0xFF;
        final int blue = rgba & 0xFF;
        final float alpha = (rgba >> 24) & 0xFF;

        return new Pixel(red, green, blue, alpha);
    }

    public static class Pixel {
        public static final int RGB_MIN = 0;
        public static final int RGB_MAX = 255;
        public static final float ALPHA_MIN = 0f;
        public static final float ALPHA_MAX = 1f;

        private final int red, green, blue;
        private final float alpha;

        public Pixel(int red, int green, int blue, float alpha) {
            if (Stream.of(red, green, blue).anyMatch(c -> c < RGB_MIN || c > RGB_MAX)) {
                throw new IllegalArgumentException("RGB is out of bounds.");
            }

            if (alpha < ALPHA_MIN || alpha > ALPHA_MAX) {
                throw new IllegalArgumentException("Alpha is out of bounds.");
            }

            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public Pixel(int red, int green, int blue) {
            this(red, green, blue, 1);
        }

        public int getRed() {
            return red;
        }

        public int getGreen() {
            return green;
        }

        public int getBlue() {
            return blue;
        }

        public float getAlpha() {
            return alpha;
        }

        public boolean isBlack() {
            return (red == RGB_MIN && green == RGB_MIN && blue == RGB_MIN && alpha == ALPHA_MAX);
        }

        public boolean isWhite() {
            return (red == RGB_MAX && green == RGB_MAX && blue == RGB_MAX && alpha == ALPHA_MAX) ||
                    (red == RGB_MIN && green == RGB_MIN && blue == RGB_MIN && alpha == ALPHA_MIN);
        }
    }
}
