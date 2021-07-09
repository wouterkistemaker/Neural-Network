package nl.woetroe.nn.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (C) 2020-2021, Wouter Kistemaker.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
