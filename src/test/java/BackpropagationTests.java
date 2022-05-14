import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.error.MeanSquaredCost;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.function.transfer.SigmoidTransfer;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
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
        execute2();
    }

    private static void execute1() {
        final InputLayer inputLayer = new InputLayer(2, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer hiddenLayer = new Layer(15, true, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer outputLayer = new Layer(1, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, outputLayer);

        network.visualize();

        final double[] input = new double[]{0.4, 0.3};
        final double[] targetOutput = new double[]{0.18};

        System.out.printf("Desired prediction for %s is: %s\n", Arrays.toString(input), Arrays.toString(targetOutput));
        System.out.printf("Actual prediction is: %s\n", Arrays.toString(network.predict(input)));

        for (int i = 0; i < 500; i++) {
            network.train(input, targetOutput, 0.01);
        }

        System.out.printf("Actual prediction after backpropagation is: %s\n", Arrays.toString(network.predict(input)));

        network.drawErrorCurve();
    }

    private static void execute2() {

        final InputLayer inputLayer = new InputLayer(2, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer hiddenLayer = new Layer(15, true, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer outputLayer = new Layer(1, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, outputLayer);

        for (int i = 0; i < 1000; i++) {
            final double x = NetworkUtils.nextDouble(0, 0.5);
            final double y = NetworkUtils.nextDouble(0, 0.5);

            final double[] input = new double[]{x,y};
            final double[] target= new double[]{1.0};

            network.train(input, target, 0.01);
        }

        final double[] input = new double[]{0.3 , 0.2};
        final double[] input2 = new double[]{0.8 , 0.6};
        System.out.printf("Prediction for (%s, %s) is %s", input[0], input[1], Arrays.toString(network.predict(input)));
        System.out.printf("Prediction for (%s, %s) is %s", input2[0], input2[1], Arrays.toString(network.predict(input2)));


        network.drawErrorCurve();

    }

}
