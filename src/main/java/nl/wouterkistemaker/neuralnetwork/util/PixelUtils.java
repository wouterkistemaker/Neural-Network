package nl.wouterkistemaker.neuralnetwork.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.stream.Stream;

public final class PixelUtils {

    private PixelUtils() {
        try {
            throw new InstantiationException("Util class cannot be instantiated");
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves pixels from the given image.
     *
     * @param filePath The path to the image.
     * @return The RGB values in a two-dimensional array (X, Y).
     * @throws IllegalStateException When the image cannot be read.
     */
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
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public static int[][] getPixels(File file) {
        return PixelUtils.getPixels(file.getPath());
    }

    /**
     * Parses the given RGBA value and wraps it into a Pixel object.
     *
     * @param rgba The given RGBA value.
     * @return The wrapped pixel.
     * @throws IllegalArgumentException When the RGB value is less than {@link Pixel#RGB_MIN } or bigger than {@link Pixel#RGB_MAX}.
     * @throws IllegalArgumentException When the Alpha value is less than {@link Pixel#RGB_MIN} or bigger than {@link Pixel#RGB_MAX}.
     */
    public static Pixel parse(int rgba) {
        final int red = (rgba >> 16) & 0xFF;
        final int green = (rgba >> 8) & 0xFF;
        final int blue = rgba & 0xFF;
        final int alpha = (rgba >> 24) & 0xFF;

        return new Pixel(red, green, blue, alpha);
    }

    public static class Pixel {
        public static final int RGB_MIN = 0;
        public static final int RGB_MAX = 255;

        private final int red, green, blue, alpha;

        public Pixel(int red, int green, int blue, int alpha) {
            if (Stream.of(red, green, blue, alpha).anyMatch(c -> c < RGB_MIN || c > RGB_MAX)) {
                throw new IllegalArgumentException("RGB is out of bounds.");
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
            return (red == RGB_MIN && green == RGB_MIN && blue == RGB_MIN && alpha == RGB_MIN);
        }

        public boolean isWhite() {
            return (red == RGB_MAX && green == RGB_MAX && blue == RGB_MAX && alpha == RGB_MAX) ||
                    (red == RGB_MIN && green == RGB_MIN && blue == RGB_MIN && alpha == RGB_MIN);
        }

        @Override
        public String toString() {
            return "Pixel{" +
                    "red=" + red +
                    ", green=" + green +
                    ", blue=" + blue +
                    ", alpha=" + alpha +
                    '}';
        }
    }
}
