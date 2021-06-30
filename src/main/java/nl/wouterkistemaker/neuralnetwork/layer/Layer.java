package nl.wouterkistemaker.neuralnetwork.layer;

import nl.wouterkistemaker.neuralnetwork.function.initialization.InitializationFunction;
import nl.wouterkistemaker.neuralnetwork.function.initialization.RandomInitialization;
import nl.wouterkistemaker.neuralnetwork.neuron.BiasNeuron;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;

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

    private static final InitializationFunction DEFAULT_INITIALIZATION_FUNCTION = new RandomInitialization();

    private final boolean bias;
    private final Set<Neuron> neurons;
    private BiasNeuron biasNeuron;

    private final InitializationFunction initializationFunction;

    public Layer(int size, boolean bias, InitializationFunction initializationFunction) {
        this.bias = bias;
        this.neurons = new LinkedHashSet<>(size + (bias ? 1 : 0));
        this.initializationFunction = initializationFunction;

        for (int i = 0; i < size; i++) {
            this.neurons.add(new Neuron());
        }

        if (bias) this.neurons.add((biasNeuron = new BiasNeuron()));
    }

    public Layer(int size, boolean bias) {
        this(size, bias, DEFAULT_INITIALIZATION_FUNCTION);
    }

    public Layer(int size) {
        this(size, false);
    }

    public void connect(Layer target) {
        neurons.forEach(n -> target.neurons.forEach(t -> {
            if (!(t instanceof BiasNeuron)) {
                n.connect(t);
                initializationFunction.initialize(n.getConnectionWith(t));
            }
        }));
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
}
