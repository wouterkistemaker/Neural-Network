import nl.wouterkistemaker.neuralnetwork.util.PixelUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PixelTests {

    public static void main(String[] args) {
        final File in = new File("in.png");
        final File out = new File("out.txt");

        try (FileWriter writer = new FileWriter(out)) {
            int[][] pixels = PixelUtils.getPixels(in);

            for (int x = 0; x < pixels[0].length; x++) {
                for (int y = 0; y < pixels[x].length; y++) {
                    writer.append(String.valueOf(x)).append(", ").append(String.valueOf(y)).append(": ").append(String.valueOf(PixelUtils.parse(pixels[x][y]))).append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
