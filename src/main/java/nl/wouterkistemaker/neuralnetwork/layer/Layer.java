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
import java.util.LinkedHashSet;
import java.util.Set;

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
    private final Set<Neuron> neurons;
    private BiasNeuron biasNeuron;

    private final InitializationFunction initializationFunction;
    private final TransferFunction transferFunction;
    private final CostFunction costFunction;

    public Layer(int size, boolean bias, InitializationFunction initializationFunction, TransferFunction transferFunction, CostFunction costFunction) {
        if (size < 0) {
            throw new IllegalArgumentException("Size must be > 0");
        }
        this.bias = bias;
        this.neurons = new LinkedHashSet<>(size + (bias ? 1 : 0));

        this.initializationFunction = initializationFunction == null ? DEFAULT_INITIALIZATION_FUNCTION : initializationFunction;
        this.transferFunction = transferFunction == null ? DEFAULT_ACTIVATION_FUNCTION : transferFunction;
        this.costFunction = costFunction == null ? DEFAULT_ERROR_FUNCTION : costFunction;

        for (int i = 0; i < size; i++) {
            this.neurons.add(new Neuron(true));
        }

        if (bias) this.neurons.add((biasNeuron = new BiasNeuron()));
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
        neurons.forEach(n -> target.neurons.forEach(t -> {
            if (!(t instanceof BiasNeuron)) {
                n.connect(t);
                initializationFunction.initialize(network.getPreviousLayer(this), n.getConnectionWith(t));
            }
        }));
    }

    public void feedforward(Layer next) {
        this.checkNetworkInstance();
        for (Neuron nextNeuron : next.getNeurons()) {
            double sum = 0.0D;

            /*
            This ensures that no NPE can occur, since previous neurons are not connected to the next Bias Neurons
            The Bias Neurons of the current layer, however, are very much included in the process of feeding forward.
             */
            if (nextNeuron instanceof BiasNeuron) continue;

            for (Neuron current : neurons) {
                final NeuronConnection connection = current.getConnectionWith(nextNeuron);
                sum += (current.getValue() * connection.getWeight());
            }
            nextNeuron.setValue(transferFunction.activate(sum));
        }
    }

    public final int getSize() {
        return neurons.size();
    }

    public final boolean hasBias() {
        return bias;
    }

    public BiasNeuron getBiasNeuron() {
        return biasNeuron;
    }

    public final Set<Neuron> getNeurons() {
        return neurons;
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
