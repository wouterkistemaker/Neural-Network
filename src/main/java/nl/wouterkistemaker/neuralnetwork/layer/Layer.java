package nl.wouterkistemaker.neuralnetwork.layer;

import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.function.error.CostFunction;
import nl.wouterkistemaker.neuralnetwork.function.error.MeanSquaredCost;
import nl.wouterkistemaker.neuralnetwork.function.initialization.InitializationFunction;
import nl.wouterkistemaker.neuralnetwork.function.initialization.RandomInitialization;
import nl.wouterkistemaker.neuralnetwork.function.transfer.SigmoidTransfer;
import nl.wouterkistemaker.neuralnetwork.function.transfer.TransferFunction;
import nl.wouterkistemaker.neuralnetwork.neuron.BiasNeuron;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;
import nl.wouterkistemaker.neuralnetwork.neuron.NeuronConnection;

import java.io.Serializable;
import java.util.*;

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
public class Layer implements Serializable {

    private static final long serialVersionUID = -8895510521099509056L;

    private NeuralNetwork network;

    private static final InitializationFunction DEFAULT_INITIALIZATION_FUNCTION = new RandomInitialization();
    private static final TransferFunction DEFAULT_ACTIVATION_FUNCTION = new SigmoidTransfer();
    private static final CostFunction DEFAULT_ERROR_FUNCTION = new MeanSquaredCost();

    private final boolean bias;
    private final List<Neuron> neurons;
    private final Map<Neuron, BiasNeuron> biasNeurons;

    private final InitializationFunction initializationFunction;
    private final TransferFunction transferFunction;
    private final CostFunction costFunction;

    public Layer(int size, boolean bias, InitializationFunction initializationFunction, TransferFunction transferFunction, CostFunction costFunction) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }
        this.bias = bias;
        this.neurons = new LinkedList<>();
        this.biasNeurons = new HashMap<>();

        this.initializationFunction = initializationFunction == null ? DEFAULT_INITIALIZATION_FUNCTION : initializationFunction;
        this.transferFunction = transferFunction == null ? DEFAULT_ACTIVATION_FUNCTION : transferFunction;
        this.costFunction = costFunction == null ? DEFAULT_ERROR_FUNCTION : costFunction;

        for (int i = 0; i < size; i++) {
            this.neurons.add(new Neuron(true));
        }

        if (bias) {
            this.neurons.forEach(n -> biasNeurons.put(n, new BiasNeuron()));
        }
    }

    public Layer(int size, boolean bias, InitializationFunction initializationFunction, TransferFunction transferFunction) {
        this(size, bias, initializationFunction, transferFunction, DEFAULT_ERROR_FUNCTION);
    }

    public Layer(int size, boolean bias, InitializationFunction initializationFunction) {
        this(size, bias, initializationFunction, DEFAULT_ACTIVATION_FUNCTION);
    }

    public Layer(int size, boolean bias) {
        this(size, bias, DEFAULT_INITIALIZATION_FUNCTION);
    }

    public Layer(int size) {
        this(size, false);
    }

    public void connect(Layer target) {
        this.checkNetworkInstance();

        neurons.forEach(n -> {

            if (hasBias(n)) {
                final BiasNeuron bias = this.getBias(n);
                bias.connect(n);
                this.initializationFunction.initialize(network.getPreviousLayer(this), bias.getConnectionWith(n));
            }

            target.neurons.forEach(t -> {
                if (!(t instanceof BiasNeuron)) {
                    n.connect(t);
                    initializationFunction.initialize(network.getPreviousLayer(this), n.getConnectionWith(t));
                }
            });
        });
    }

    public void feedforward(Layer next) {
        this.checkNetworkInstance();
        for (Neuron nextNeuron : next.getNeurons()) {

            double sum = next.hasBias(nextNeuron) ? next.getBias(nextNeuron).getConnectionWith(nextNeuron).getWeight() : 0;

            /*
            This ensures that no NPE can occur, since previous neurons are not connected to the next Bias Neurons
            The Bias Neurons of the current layer, however, are very much included in the process of feeding forward.
             */
            if (nextNeuron instanceof BiasNeuron) continue;

            for (Neuron current : neurons) {
                final NeuronConnection connection = current.getConnectionWith(nextNeuron); // Connection of current neuron with next neuron
                sum += (current.getValue() * connection.getWeight());
            }

            nextNeuron.setValue(transferFunction.apply(sum));
        }
    }

    public void propagateBackwards(double learningRate) {
        if (network.isFirstLayer(this)) {
            return;
        }

        final Layer previousLayer = network.getPreviousLayer(this);
        // do some stuff

        for (int k = 0; k < neurons.size(); k++) {
            final Neuron neuron = neurons.get(k);
            for (int j = 0; j < previousLayer.getNeurons().size(); j++) {
                final Neuron previousNeuron = previousLayer.getNeurons().get(j);

                // We compare the output of neuron K with target output for neuron K and apply the derivative of the cost function
                final double costDerivative = costFunction.applyDerivative(neuron.getValue(), network.getTargetOutput()[k]);

                // We apply the derivative of the transfer function to the weighted sum that determines the output of neuron K
                final double transferDerivative = transferFunction.applyDerivative(neuron.getValue());

                /* This is the adjustment that needs to made (after being multiplied with the learning-rate) to
                the weight that connects the previous neuron J with the current neuron K
                */
                final double deltaWeight = costDerivative * transferDerivative * previousNeuron.getValue();

                previousNeuron.getConnectionWith(neuron).adjustWeight(learningRate * deltaWeight);
            }
        }
        previousLayer.propagateBackwards(learningRate);
    }

    public final int getSize() {
        return neurons.size();
    }

    /**
     * @return whether or not the neurons in this layer have a bias-term
     * @deprecated For now this function works because if the layer has a bias-term enabled, this
     * means that each neuron in the layer has a bias-term but this will likely be
     * changed in the future, where specific amounts of neurons can be biased rather
     * than the whole layer.
     */
    @Deprecated(forRemoval = true)
    public final boolean hasBias() {
        return bias;
    }

    public final List<Neuron> getNeurons() {
        return Collections.unmodifiableList(this.neurons);
    }

    public final CostFunction getCostFunction() {
        return costFunction;
    }

    public final TransferFunction getTransferFunction() {
        return transferFunction;
    }

    public final InitializationFunction getInitializationFunction() {
        return initializationFunction;
    }

    public final double getCost() {

        return getNeurons().stream().mapToDouble(Neuron::getError).sum();
    }

    public final double[] getOutput() {
        double[] output = new double[this.neurons.size()];

        for (int i = 0; i < output.length; i++) {
            output[i] = neurons.get(i).getValue();
        }

        return output;
    }

    private boolean hasBias(Neuron n) {
        return this.biasNeurons.containsKey(n);
    }

    private BiasNeuron getBias(Neuron n) {
        if (!hasBias(n)) {
            throw new IllegalStateException("This neuron does not have a bias");
        }
        return this.biasNeurons.get(n);
    }

    @Deprecated
    public void setNetworkInstance(NeuralNetwork network) {
        if (this.network != null) {
            throw new IllegalStateException("The instance can only be set once");
        }
        this.network = network;
    }

    private void checkNetworkInstance() {
        if (network == null) {
            throw new IllegalStateException("NeuralNetwork instance is missing");
        }
    }
}
