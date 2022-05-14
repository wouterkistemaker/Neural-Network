import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.layer.OutputLayer;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

public class VisualisationTests {

    public static void main(String[] args) {
//        execute0();
//        execute1();
        execute2();
    }

    private static void execute0(){
        final Layer inputLayer = new Layer(2, false, new XavierInitialization());
        final Layer hiddenLayer = new Layer(10, false, new XavierInitialization());
        final Layer hiddenLayer2 = new Layer(25, true, new XavierInitialization());
        final Layer hiddenLayer3 = new Layer(8, false, new XavierInitialization());
        final Layer hiddenLayer4 = new Layer(8, false, new XavierInitialization());
        final Layer hiddenLayer5 = new Layer(8, false, new XavierInitialization());
        final Layer hiddenLayer6 = new Layer(8, false, new XavierInitialization());
        final Layer hiddenLayer7 = new Layer(2, false, new XavierInitialization());
        final Layer outputLayer = new Layer(1,true);

        final NeuralNetwork network = new NeuralNetwork(inputLayer, hiddenLayer, hiddenLayer2, hiddenLayer3, hiddenLayer4, hiddenLayer5, hiddenLayer6, hiddenLayer7, outputLayer);
        network.visualize();

        final ScheduledExecutorService service = Executors.newScheduledThreadPool(8);
        service.scheduleAtFixedRate(network::feedforward, 3000,200, TimeUnit.MILLISECONDS);
    }

    private static void execute1(){
        final Layer input = new Layer(2, false, new XavierInitialization());
        final Layer output = new Layer(1, false, new XavierInitialization());
        final NeuralNetwork network = new NeuralNetwork(input, output);

        network.save();
        network.visualize();

        final NeuralNetwork copy = NeuralNetwork.load();

        final Layer outputCopy = copy.getLayers().get(copy.getLayers().size() - 1);
        final Neuron outputNeuron = (Neuron) outputCopy.getNeurons().toArray()[0];

        System.out.println("Output of the copy is " + outputNeuron.getValue()); // This is the same as the output of the actual output-neuron so saving & loading works ! :)
    }

    private static void execute2() {

        final Layer input = new InputLayer(2, false, new XavierInitialization());
        final Layer hidden = new Layer(3, true, new XavierInitialization());
        final Layer output = new OutputLayer(1, false, new XavierInitialization());
        final NeuralNetwork network = new NeuralNetwork(input, hidden, output);

        network.visualize();

        final ScheduledExecutorService service = Executors.newScheduledThreadPool(8);

        service.schedule(network::feedforward, 2, TimeUnit.SECONDS);
    }
}
