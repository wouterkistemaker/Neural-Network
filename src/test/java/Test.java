import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.function.activation.SigmoidFunction;
import nl.woetroe.nn.function.error.MeanSquaredFunction;
import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;

import java.io.File;
import java.util.Arrays;

public class Test {

    // kinda crappy .. ?

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
       exec1();
//        exec2();
    }

    private static void exec1() {
        NeuralNetwork network = new NeuralNetwork.Builder(
                new InputLayer(3),
                new DenseLayer(2))
                .withDenseLayer(new DenseLayer(10, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withDenseLayer(new DenseLayer(5, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withLearningRate(0.1)
                .withTargetOutput(0.3, 0.8)
                .withRandomStartValues()
                .withInput(0.1, 0.2, 0.9).build();

        for (int i = 0; i < 10000; i++) {
            network.train();
        }

        System.out.println(network.getOutput());

        network.saveNetwork(file);
    }

    private static void exec2() {

        NeuralNetwork network = NeuralNetwork.loadNetwork(file);

        final double[] output = network.compute(0.6, 0.6, 0.1);

        System.out.println(Arrays.toString(output));
    }

//    public static void main(String[] args) {
//
//
//        NeuralNetwork neuralNetwork = new NeuralNetwork.Builder(new InputLayer(5, true), new DenseLayer(1, true))
//                .withLearningRate(0.001)
//                .withRandomStartValues()
//                .withInput(0.4, 0.6, .8, .9, .9)
//                .withTargetOutput(0.5).build();
//
//        final int nEpochs = 10000000;
//        for (int i = 0; i < nEpochs; i++) {
//            neuralNetwork.train();
//        }
//
//        neuralNetwork.saveNetwork(file);
//
//        System.out.println(neuralNetwork);
//    }

    private static void runNetwork(NeuralNetwork network) {
        final int nCyclus = 10000;

        for (int i = 0; i < nCyclus; i++) {
            network.train();
        }

        network.saveNetwork(file);

        System.out.println(network.getTargetOutputString());
        System.out.println(network.getOutput());
    }
}
