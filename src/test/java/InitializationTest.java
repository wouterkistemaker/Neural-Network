import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.function.initialization.RandomInitialization;
import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;

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
*/public class InitializationTest {

    public static void main(String[] args) {
        final InputLayer inputLayer = new InputLayer(2, false);
        final DenseLayer outputLayer = new DenseLayer(1, false);

        final NeuralNetwork network = new NeuralNetwork.Builder(inputLayer, outputLayer)
            .withLearningRate(0.1)
            .withInput(0.5, 0.3)
            .withTargetOutput(0.8)
            .withInitType(new RandomInitialization())
            .withDenseLayer(new DenseLayer(20, true))
            .withDenseLayer(new DenseLayer(10, true))
            .withDenseLayer(new DenseLayer(10, true)).build();


    }
}
