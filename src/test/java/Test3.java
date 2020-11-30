import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;

public class Test3 {

    public static void main(String[] args) {

        final InputLayer inputLayer = new InputLayer(2);
        final DenseLayer outputLayer = new DenseLayer(1);

        final NeuralNetwork network = new NeuralNetwork.Builder(inputLayer, outputLayer)
                .withInput(0.3, 0.5)
                .withTargetOutput(0.1).build();
    }
}
