import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;

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
public class VisualisationTest {

    public static void main(String[] args) {
        final Layer inputLayer = new Layer(1,true);
        final Layer hiddenLayer = new Layer(2,true);
        final Layer hiddenLayer2 = new Layer(3,true);
        final Layer hiddenLayer3 = new Layer(13,true);
        final Layer hiddenLayer4 = new Layer(2,true);
        final Layer outputLayer = new Layer(2);

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, hiddenLayer2, hiddenLayer3, hiddenLayer4, outputLayer);
        network.visualize();
    }
}
