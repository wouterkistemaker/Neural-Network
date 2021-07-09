import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.initialization.RandomInitialization;
import nl.wouterkistemaker.neuralnetwork.function.initialization.XavierInitialization;
import nl.wouterkistemaker.neuralnetwork.function.transfer.LeakyReLUTransfer;
import nl.wouterkistemaker.neuralnetwork.function.transfer.ReLUTransfer;
import nl.wouterkistemaker.neuralnetwork.function.transfer.SigmoidTransfer;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.BiasNeuron;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

public class ObjectTests {
    @Test
    public void testConnections() {
        final Neuron neuron = new Neuron(0.1);
        final Neuron bias = new BiasNeuron();

        Assertions.assertFalse(neuron.isConnectedTo(bias));
        Assertions.assertFalse(bias.isConnectedTo(neuron));

        neuron.connect(bias);

        Assertions.assertTrue(neuron.isConnectedTo(bias));
        Assertions.assertFalse(bias.isConnectedTo(neuron));
    }

    @Test
    public void testLayers() {
        final Layer first = new Layer(2);
        final Layer last = new Layer(1);

        final Neuron lastNeuron = (Neuron) last.getNeurons().toArray()[0];

        Assertions.assertTrue(first.getNeurons().stream().noneMatch(n -> n.isConnectedTo(lastNeuron)));

        new NeuralNetwork(first, last);

        Assertions.assertFalse(first.getNeurons().stream().noneMatch(n -> n.isConnectedTo(lastNeuron)));
        Assertions.assertTrue(first.getNeurons().stream().noneMatch(lastNeuron::isConnectedTo));
    }

    @Test
    public void testSaveAndLoad() {
        final Layer input = new Layer(2, false, new XavierInitialization());
        final Layer output = new Layer(1, false, new XavierInitialization());

        final NeuralNetwork network = new NeuralNetwork(input, output);

        network.save();

        final NeuralNetwork copy = NeuralNetwork.load();

        Assertions.assertEquals(network.hashCode(), copy.hashCode());

        final Neuron outputNeuron = (Neuron) output.getNeurons().toArray()[0];
        final Neuron outputNeuronCopy = (Neuron) copy.getLayers().get(copy.getLayers().size() - 1).getNeurons().toArray()[0];

        Assertions.assertEquals(outputNeuron, outputNeuronCopy);
    }

    @Test
    public void testFeedForward() {

        final InputLayer input = new InputLayer(2, false, new RandomInitialization());
        final Layer output = new Layer(1, false, new XavierInitialization());

        final NeuralNetwork network = new NeuralNetwork(input, output);

        input.setInput(new double[]{0.1, 0.6});
        network.feedforward();

        System.out.println(Arrays.toString(input.getOutput()));
        System.out.println(Arrays.toString(output.getOutput()));
        System.out.println(Arrays.toString(network.getOutput()));
    }

    @Test
    public void testSigmoidFunction() {
        SigmoidTransfer sigmoid = new SigmoidTransfer();
        ReLUTransfer relu = new ReLUTransfer();
        LeakyReLUTransfer leakyRelu = new LeakyReLUTransfer();

        final double weightedSum = 0.562;

        final double sigmoidValue = sigmoid.apply(weightedSum);
        final double reluValue = relu.apply(weightedSum);
        final double leakyReluValue = leakyRelu.apply(weightedSum);

        Assertions.assertNotEquals(weightedSum, sigmoid.unapply(sigmoidValue));
        Assertions.assertEquals(weightedSum, relu.unapply(reluValue));
        Assertions.assertEquals(weightedSum, leakyRelu.unapply(leakyReluValue));
    }

    @Test
    public void testBackPropagate() {

        final InputLayer input = new InputLayer(2, false, new XavierInitialization());
        final Layer hiddenLayer = new Layer(5, false, new XavierInitialization());
        final Layer output = new Layer(1, false, new XavierInitialization());

        final NeuralNetwork network = new NeuralNetwork(input, hiddenLayer, output);

        final Neuron inputNeuron = input.getNeurons().get(0);
        final Neuron hiddenNeuron = hiddenLayer.getNeurons().get(3);

        final double weight = inputNeuron.getConnectionWith(hiddenNeuron).getWeight();

        network.feedforward();
        network.propagateBackwards();
        network.updateWeights(0.01);

        Assertions.assertTrue(output.getNeurons().stream().allMatch(n -> n.getDelta() != 0));
        Assertions.assertTrue(weight != inputNeuron.getConnectionWith(hiddenNeuron).getWeight());
    }
}
