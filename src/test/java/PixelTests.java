import nl.wouterkistemaker.neuralnetwork.util.PixelUtils;
import nl.wouterkistemaker.neuralnetwork.util.PixelUtils.Pixel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class PixelTests {

    public static void main(String[] args) {
        final File in = new File("in.png");
        final File out = new File("out.txt");

        try (FileWriter writer = new FileWriter(out)) {
            int[][] pixels = PixelUtils.getPixels(in);

            for (Map.Entry<String, Integer> entry : compress(pixels).entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toCollection(LinkedHashSet::new))) {
                writer.append(entry.getKey()).append(" x ").append(String.valueOf(entry.getValue())).append('\n');
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

                map.put(pixel.toString(), map.getOrDefault(pixel.toString(), 0) + 1);
            }
        }

        return map;
    }
}
