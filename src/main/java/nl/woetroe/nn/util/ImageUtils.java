package nl.woetroe.nn.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class ImageUtils {

    public static Integer[] rgb(Image image) {
            final BufferedImage buf = toBufferedImage(image.getScaledInstance(128, 128, 0));
            final int[][] pixels = new int[buf.getWidth()][buf.getHeight()];

            for (int x = 0; x < buf.getWidth(); x++) {
                for (int y = 0; y < buf.getHeight(); y++) {
                    pixels[x][y] = buf.getRGB(x, y);
                }
            }

            final List<Integer> res = new LinkedList<>();

            for (int x = 0; x < pixels.length; x++) {
                for (int y = 0; y < pixels[x].length; y++) {
//                    final int rgb = pixels[x][y];
//                    int red = rgb >> 16 & 0xFF;
//                    int green = rgb >> 8 & 0xFF;
//                    int blue = rgb & 0xFF;

                    res.add(pixels[x][y]);
                }
            }

            return res.toArray(new Integer[res.size()]);
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

}
