import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.error.BinaryCrossEntropyCost;
import nl.wouterkistemaker.neuralnetwork.function.error.MeanSquaredCost;
import nl.wouterkistemaker.neuralnetwork.function.initialization.RandomInitialization;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.function.transfer.SigmoidTransfer;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.layer.OutputLayer;
import nl.wouterkistemaker.neuralnetwork.util.NetworkUtils;

import java.util.Arrays;

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
public class BackpropagationTests {

    public static void main(String[] args) {
//        execute1();
//        execute2();
        execute3();
    }

    private static void execute1() {
        final InputLayer inputLayer = new InputLayer(1, false, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer hiddenLayer = new Layer(15, false, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final OutputLayer outputLayer = new OutputLayer(4, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, outputLayer);

//        testNetwork(network);

        network.visualize();

        final double[] input = new double[]{0.4};
        final double[] targetOutput = new double[]{0.18,0.3, 0.65, 0.98};

        System.out.printf("Desired prediction for %s is: %s\n", Arrays.toString(input), Arrays.toString(targetOutput));
        System.out.printf("Actual prediction is: %s\n", Arrays.toString(network.predict(input)));

        for (int i = 0; i < 5000; i++) {
            network.train(input, targetOutput, 0.01);
        }

        System.out.printf("Actual prediction after backpropagation is: %s\n", Arrays.toString(network.predict(input)));

//        testNetwork(network);

        network.drawErrorCurve();
    }

    private static void execute2() {

        final InputLayer inputLayer = new InputLayer(1, false, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer hiddenLayer = new Layer(15, true, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer hiddenLayer2 = new Layer(8, true, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer outputLayer = new Layer(1, false, new RandomInitialization(), new SigmoidTransfer(), new MeanSquaredCost());

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, hiddenLayer2, outputLayer);

        for (int i = 0; i < 5000; i++) {
            double[] input = new double[]{NetworkUtils.nextDouble()};
            double[] target = new double[]{input[0] > 0.5 ? 0.999 : 0.001};

            network.train(input, target, 0.01);
        }

        final double[] input = new double[]{0.8};
        final double[] input2 = new double[]{0.2};

        System.out.printf("Prediction for (%s) is %s\n", input[0], Arrays.toString(network.predict(input)));
        System.out.printf("Prediction for (%s) is %s\n", input2[0], Arrays.toString(network.predict(input2)));

        network.visualize();
        network.drawErrorCurve();

    }

    private static void execute3(){

        final InputLayer inputLayer = new InputLayer(2, true);
        final Layer hiddenLayer = new Layer(2, true);
        final OutputLayer outputLayer = new OutputLayer(1);

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, outputLayer);

//        testNetwork(network,1);

        network.visualize();

    }

    private static void execute4() {

        final InputLayer inputLayer = new InputLayer(1, true);

    }

    private static void testNetwork(NeuralNetwork network, int a){
        for (int i = 0; i < a; i++) {
            double result = network.predict(new double[]{0.1 * i})[0];

            System.out.printf("Prediction for (%s) is %s\n", 0.1*i, result);
        }
    }

    private static void trainNetwork(NeuralNetwork network, double input, double target, double eta, double epochs){
        for (int i = 0; i < epochs; i++) {
            network.train(new double[]{input}, new double[]{target},eta);
        }
    }

}
