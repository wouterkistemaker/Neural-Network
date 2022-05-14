import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.data.TrainingSet;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.function.transfer.LeakyReLUTransfer;
import nl.wouterkistemaker.neuralnetwork.function.transfer.SigmoidTransfer;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.util.MnistTrainingSetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/*
  Copyright (C) 2020-2021, Wouter Kistemaker.
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published
  by the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU Affero General Public License for more details.
  You should have received a copy of the GNU Affero General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
public class MNISTTest {

    public static void main(String[] args) {

        final File trainImagesFile = new File("C:\\Development\\NeuralNetwork\\src\\test\\resources\\train-images.idx3-ubyte");
        final File trainLabelsFile = new File("C:\\Development\\NeuralNetwork\\src\\test\\resources\\train-labels.idx1-ubyte");

        final TrainingSet set = MnistTrainingSetLoader.loadTrainingSet(trainImagesFile, trainLabelsFile, 1000);

        final InputLayer inputLayer = new InputLayer(784, false, new XavierInitialization(), new LeakyReLUTransfer());
        final Layer hidden = new Layer(100, true, new XavierInitialization(), new SigmoidTransfer());
        final Layer hidden2 = new Layer(100, true, new XavierInitialization(), new SigmoidTransfer());
        final Layer hidden3 = new Layer(100, true, new XavierInitialization(), new SigmoidTransfer());
        final Layer output = new Layer(10, false, new XavierInitialization(), new LeakyReLUTransfer());

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hidden, hidden2, hidden3, output);
        network.train(set, 100, 5, 0.001);

        network.drawErrorCurve();
    }

    private static int[] extractLabels(File labelFile) {
        try (DataInputStream is = new DataInputStream(new FileInputStream(labelFile))) {
            is.skipBytes(4); // Magic Number, not needed.

            final int nItems = is.readInt();
            final int[] labels = new int[nItems];

            for (int i = 0; i < nItems; i++) {
                final int label = is.readUnsignedByte();

                // Store label together with the image.
                labels[i] = label;
            }
            return labels;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int[][] extractImagePixels(File imageFile) {
        return extractImagePixels(imageFile, -1);
    }

    private static int[][] extractImagePixels(File imageFile, int images) {
        try (DataInputStream is = new DataInputStream(new FileInputStream(imageFile))) {
            is.skipBytes(4); // Magic Number, not needed.

            final int nItems = is.readInt();
            final int nPixelsPerImage = 28 * 28;
            final int[][] imagePixels = new int[nItems][nPixelsPerImage];

            is.skipBytes(8);

            final int nLoops = images == -1 || images > nItems ? nItems : images;

            for (int image = 0; image < nLoops; image++) {
                for (int pixel = 0; pixel < nPixelsPerImage; pixel++) {
                    final int pixelValue = is.readUnsignedByte();
                    imagePixels[image][pixel] = pixelValue;
                }
            }

            return imagePixels;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void drawImage(int[] pixels, int label) {
        BufferedImage image = new BufferedImage(28, 28, Image.SCALE_DEFAULT);

        for (int column = 0; column < 28; column++) {
            for (int row = 0; row < 28; row++) {

                final int pixelValue = pixels[row * 28 + column];
                image.setRGB(column, row, new Color(pixelValue, pixelValue, pixelValue).getRGB());
            }
        }

        final BufferedImage rescaled = toBufferedImage(image.getScaledInstance(15 * 28, 15 * 28, 1));

        final Graphics graphics = rescaled.getGraphics();
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 150));
        graphics.setColor(new Color(255, 0, 0, 150));
        graphics.drawString(String.valueOf(label), rescaled.getWidth() / 2, rescaled.getHeight() / 2);

        JFrame frame = new JFrame();
        ImageIcon icon = new ImageIcon(rescaled);
        JLabel jlabel = new JLabel(icon);

        jlabel.setSize(new Dimension(3 * 28, 3 * 28));

        frame.add(jlabel);

        frame.setSize(new Dimension(1280, 720));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }

    private static void drawImages(int[][] images, int[] labels) {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(new Runnable() {

            int counter = 0;

            @Override
            public void run() {
                System.out.println("jo");
                drawImage(images[counter], labels[counter]);
                counter++;
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private static double[][] prepare(int[][] input) {
        final double[][] output = new double[input.length][];
        for (int i = 0; i < input.length; i++) {
            final int[] pixels = input[i];

            final double[] doubles = new double[pixels.length];

            for (int pixel = 0; pixel < pixels.length; pixel++) {
                doubles[pixel] = pixels[pixel] / 255.0;
            }

            output[i] = doubles;
        }

        return output;
    }

    private static double[] labelToTarget(int label) {
        final double[] target = new double[10];
        target[label] = 1;
        return target;
    }
}
