package nl.woetroe.nn.layer;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.activation.SigmoidFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.function.error.MeanSquaredFunction;
import nl.woetroe.nn.neuron.Neuron;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
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
public abstract class Layer implements Serializable {

    private static final ActivationFunction DEFAULT_ACTIVATION_FUNCTION = new SigmoidFunction();
    private static final ErrorFunction DEFAULT_ERROR_FUNCTION = new MeanSquaredFunction();

    private static final Neuron BIAS = new Neuron(true);
    private static final long serialVersionUID = -7022757345368501174L;

    private final int size;
    private final boolean withBias;
    private final List<Neuron> neurons;

    // functions
    private final ActivationFunction activationFunction;
    private final ErrorFunction errorFunction;

    public Layer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        this.size = size;
        this.withBias = withBias;
        this.neurons = new LinkedList<>();
        this.activationFunction = activationFunction == null ? DEFAULT_ACTIVATION_FUNCTION : activationFunction;
        this.errorFunction = errorFunction == null ? DEFAULT_ERROR_FUNCTION : errorFunction;
    }

    public Layer(int size, boolean withBias) {
        this(size, withBias, null, null);
    }

    public Layer(int size) {
        this(size, false);
    }

    public void connectToLayer(Layer target) {
        for (Neuron t : target.getNeurons()) {
            for (Neuron n : neurons) {
                n.connect(t);
            }

            if (hasBias()) {
                getBias().connect(t);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public List<Neuron> getNeurons() {
        return neurons;
    }

    public boolean hasBias() {
        return withBias;
    }

    public Neuron getBias() {
        return BIAS;
    }

    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    public ErrorFunction getErrorFunction() {
        return errorFunction;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "size=" + size +
                ", withBias=" + withBias +
                (withBias ? (", bias=" + BIAS.toString()) : "") +
                ", neurons=" + neurons +
                ", activationFunction=" + activationFunction +
                '}';
    }
}
