import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;

import java.util.Timer;
import java.util.TimerTask;

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
        final Layer inputLayer = new Layer(2,false, new XavierInitialization());
        final Layer hiddenLayer = new Layer(10, false, new XavierInitialization());
        final Layer hiddenLayer2 = new Layer(25, false, new XavierInitialization());
        final Layer hiddenLayer3 = new Layer(8, true, new XavierInitialization());
        final Layer hiddenLayer4 = new Layer(8, false, new XavierInitialization());
        final Layer hiddenLayer5 = new Layer(8, true, new XavierInitialization());
        final Layer hiddenLayer6 = new Layer(8, true, new XavierInitialization());
        final Layer hiddenLayer7 = new Layer(2, false, new XavierInitialization());
        final Layer outputLayer = new Layer(1);

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, hiddenLayer2, hiddenLayer3, hiddenLayer4, hiddenLayer5, hiddenLayer6, hiddenLayer7, outputLayer);
        network.visualize();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("About to draw again");
                network.feedforward();
            }
        }, 5000);
    }
}
