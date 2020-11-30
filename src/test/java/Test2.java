import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.function.activation.SigmoidFunction;
import nl.woetroe.nn.function.error.MeanSquaredFunction;
import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;

import java.io.File;

public class Test2 {

    private static File file;

    static {
        file = new File("C:\\Development\\NeuralNetwork\\src\\test\\resources\\network.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static void main(String[] args) {
//        exec1();
        exec2();
    }

    private static void exec1() {
        NeuralNetwork network = new NeuralNetwork.Builder(
                new InputLayer(3, false, new SigmoidFunction(), new MeanSquaredFunction()),
                new DenseLayer(2, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withDenseLayer(new DenseLayer(10, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withDenseLayer(new DenseLayer(5, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withLearningRate(0.01)
                .withTargetOutput(0.3, 0.8)
                .withRandomStartValues()
                .withInput(0.1, 0.2, 0.9).build();

        final int resultEpochs = network.train(1000 * 30); // 30 seconds

        printData(network, resultEpochs);

        network.saveNetwork(file);
    }

    private static void exec2() {
        final NeuralNetwork network = NeuralNetwork.loadNetwork(file);

        network.compute(0.1, 0.2, 0.9);

        printData(network, 1);
    }

    private static void printData(NeuralNetwork network, int resultEpochs) {
        System.out.println("==================================================");
        System.out.println("Results of Neural Network Training Session, Sigmoid/MeanSquared, epochs=" + resultEpochs);
        System.out.printf("Target -> %s\n", network.getTargetOutputString());
        System.out.printf("Output -> %s\n", network.getOutput());
        System.out.println("==================================================");
    }

}
