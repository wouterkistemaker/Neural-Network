import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.error.MeanSquaredCost;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.function.transfer.SigmoidTransfer;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;

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
        execute1();
    }

    private static void execute1() {
        final InputLayer inputLayer = new InputLayer(2, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer hiddenLayer = new Layer(15, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());
        final Layer outputLayer = new Layer(1, false, new XavierInitialization(), new SigmoidTransfer(), new MeanSquaredCost());

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, outputLayer);

        network.visualize();

        final double[] input = new double[]{0.4, 0.3};
        final double[] targetOutput = new double[]{0.1};

        System.out.printf("Desired prediction for %s is: %s\n", Arrays.toString(input), Arrays.toString(targetOutput));
        System.out.printf("Actual prediction is: %s\n", Arrays.toString(network.predict(input)));

        for (int i = 0; i < 500000; i++) {
            network.train(input, targetOutput, 0.01);
        }

        System.out.printf("Actual prediction after backpropagation is: %s\n", Arrays.toString(network.predict(input)));
    }

}
