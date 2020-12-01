package nl.woetroe.nn.layer;

import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.activation.SigmoidFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.function.error.MeanSquaredFunction;
import nl.woetroe.nn.neuron.Neuron;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
/*
 * Copyright (C) 2020-2021, Wouter Kistemaker.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/**
 * Represents a Layer of {@link Neuron neurons} that is part
 * of the {@link NeuralNetwork}
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

    /**
     * Constructs a new {@link Layer}
     *
     * @param size               amount of {@link Neuron neurons} this {@link Layer} (will) contain(s)
     * @param withBias           whether or not a bias-{@link Neuron neuron} should be included
     * @param activationFunction activation function to be applied to the weighted sum
     *                           in the process of feedforwarding
     * @param errorFunction      error function that is used to measure the discrepancy between
     *                           the desired output and the actual output
     */
    public Layer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        this.size = size;
        this.withBias = withBias;
        this.neurons = new LinkedList<>();
        this.activationFunction = activationFunction == null ? DEFAULT_ACTIVATION_FUNCTION : activationFunction;
        this.errorFunction = errorFunction == null ? DEFAULT_ERROR_FUNCTION : errorFunction;
    }

    /**
     * Constructs a new {@link Layer}
     *
     * @param size     amount of {@link Neuron neurons} this {@link Layer} (will) contain(s)
     * @param withBias whether or not a bias-{@link Neuron neuron} should be included
     * @implNote This internally calls the {{@link #Layer(int, boolean, ActivationFunction, ErrorFunction)} main constructor}
     * with default activation- and errorfunction.
     */
    public Layer(int size, boolean withBias) {
        this(size, withBias, null, null);
    }

    /**
     * Constructs a new {@link Layer}
     *
     * @param size amount of {@link Neuron neurons} this {@link Layer} (will) contain(s)
     * @implNote This internally calls the {{@link #Layer(int, boolean, ActivationFunction, ErrorFunction)} main constructor}
     * without a bias-{@link Neuron} and default activation- and errorfunction.
     */
    public Layer(int size) {
        this(size, false);
    }

    /**
     * Connects all the {@link Neuron neurons} in this {@link Layer}
     * to all the {@link Neuron neurons} in the target {@link Layer}
     *
     * @param target layer to connect this layer with
     */
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

    /**
     * The amount of {@link Neuron neurons} in this layer
     *
     * @return the size of this {@link Layer}
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns a {@link List<Neuron>} of {@link Neuron neurons} in this {@link Layer}
     *
     * @return a {@link List<Neuron>} of {@link Neuron neurons} in this {@link Layer}
     */
    public List<Neuron> getNeurons() {
        return neurons;
    }

    /**
     * Returns whether or not this {@link Layer} has a bias-{@link Neuron neuron} or not
     *
     * @return whether or not this {@link Layer} has a bias-{@link Neuron neuron} or not
     */
    public boolean hasBias() {
        return withBias;
    }

    /**
     * Returns the bias-{@link Neuron neuron} in this {@link Layer}
     *
     * @return the bias-{@link Neuron neuron} in this {@link Layer}
     * @throws IllegalStateException - when this layer has no bias neuron
     */
    public Neuron getBias() {
        if (!hasBias()) throw new IllegalStateException();
        return BIAS;
    }

    /**
     * Returns the {@link ActivationFunction} of this {@link Layer}
     *
     * @return the {@link ActivationFunction} of this {@link Layer}
     */
    public ActivationFunction getActivationFunction() {
        return activationFunction;
    }

    /**
     * Returns the {@link ErrorFunction} of this {@link Layer}
     *
     * @return the {@link ErrorFunction} of this {@link Layer}
     */
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
