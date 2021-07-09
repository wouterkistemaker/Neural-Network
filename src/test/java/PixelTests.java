import nl.wouterkistemaker.neuralnetwork.util.PixelUtils;
import nl.wouterkistemaker.neuralnetwork.util.PixelUtils.Pixel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PixelTests {

    public static void main(String[] args) {
        final File in = new File("in.png");
        final File out = new File("out.txt");

        try (FileWriter writer = new FileWriter(out)) {
            int[][] pixels = PixelUtils.getPixels(in);

<<<<<<< HEAD
            for (Map.Entry<String, Integer> entry : compress(pixels).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toCollection(LinkedHashSet::new))) {
                writer.append(entry.getKey()).append(" x ").append(String.valueOf(entry.getValue())).append('\n');
=======
            for (int x = 0; x < pixels[0].length; x++) {
                for (int y = 0; y < pixels[x].length; y++) {
                    writer.append(String.valueOf(x+1)).append(", ").append(String.valueOf(y+1)).append(": ").append(String.valueOf(PixelUtils.parse(pixels[x][y]))).append('\n');
                }
>>>>>>> 21c68496c983cd04eb8609ac027b369fd799e271
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Integer> compress(int[][] data) {
        final Map<String, Integer> map = new LinkedHashMap<>();

        for (int x = 0; x < data[0].length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                final Pixel pixel = PixelUtils.parse(data[x][y]);

                if (map.containsKey(pixel.toString())) {
                    map.replace(pixel.toString(), map.get(pixel.toString()) + 1);
                } else {
                    map.put(pixel.toString(), 1);
                }
            }
        }

        return map;
    }
}
