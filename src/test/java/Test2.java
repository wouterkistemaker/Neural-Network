import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.function.activation.SigmoidFunction;
import nl.woetroe.nn.function.error.MeanSquaredFunction;
import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;

public class Test2 {

    // without file

    public static void main(String[] args) {

        NeuralNetwork network = new NeuralNetwork.Builder(
                new InputLayer(3, false, new SigmoidFunction(), new MeanSquaredFunction()),
                new DenseLayer(2, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withDenseLayer(new DenseLayer(10, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withDenseLayer(new DenseLayer(5, false, new SigmoidFunction(), new MeanSquaredFunction()))
                .withLearningRate(0.01)
                .withTargetOutput(0.3, 0.8)
                .withRandomStartValues()
                .withInput(0.1, 0.2, 0.9).build();

        final int resultEpochs = network.train(1000 * 60); // 1 minute

        System.out.println("==================================================");
        System.out.println("Results of Neural Network Training Session, Sigmoid/MeanSquared, epochs=" + resultEpochs);
        System.out.printf("Target -> %s\n", network.getTargetOutputString());
        System.out.printf("Output -> %s\n", network.getOutput());
        System.out.println("==================================================");

    }

}
