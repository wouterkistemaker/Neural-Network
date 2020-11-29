import nl.woetroe.nn.NeuralNetwork;

import java.io.File;

public class Test {

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
        NeuralNetwork network = NeuralNetwork.loadNetwork(file);

        network.train();
        System.out.println(network);
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
