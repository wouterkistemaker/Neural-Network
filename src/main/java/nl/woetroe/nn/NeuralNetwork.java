package nl.woetroe.nn;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;
import nl.woetroe.nn.layer.Layer;
import nl.woetroe.nn.neuron.Neuron;
import nl.woetroe.nn.neuron.NeuronConnection;

import java.io.*;
import java.util.*;

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
 * <p>
 */

/**
 * Class representing a multi-layer feedforward neural network.
 */
public final class NeuralNetwork implements Serializable {

    private static final double DEFAULT_LEARNING_RATE = 0.1;
    private static final long serialVersionUID = 2451752264370132969L;

    private final InputLayer inputLayer; // cannot be null
    private final Set<DenseLayer> denseLayers; // can be null
    private final DenseLayer outputLayer; // cannot be null

    private final double learningRate;
    private final double[] targetOutput;

    private final List<Layer> layers;

    private boolean ready;

    private NeuralNetwork(InputLayer inputLayer, DenseLayer outputLayer, double learningRate, double... targetOutput) {
        this.inputLayer = inputLayer;
        this.outputLayer = outputLayer;
        this.learningRate = learningRate;
        this.targetOutput = targetOutput;
        this.denseLayers = new HashSet<>();
        this.layers = new LinkedList<>();
    }

    private NeuralNetwork(InputLayer inputLayer, DenseLayer outputLayer, double... targetOutput) {
        this(inputLayer, outputLayer, DEFAULT_LEARNING_RATE, targetOutput);
    }

    /**
     * @return a list of all {@link Neuron neurons} from the {@link DenseLayer output-layer}
     */
    public List<Neuron> getOutput() {
        return outputLayer.getNeurons();
    }

    /**
     * @param duration duration in milliseconds
     * @return amount of epochs the training completed
     * @see #train()
     */
    public int train(long duration) {
        final long now = System.currentTimeMillis();
        final long end = now + duration;

        int i = 0;

        while (System.currentTimeMillis() < end) {
            train();
            i++;
        }

        return i;
    }

    /**
     * Runs a single training-cycle, which consists of three processes
     *
     * <ul>
     *     <li>feed-forward - the network computes the input to an output</li>
     *     <li>backpropagation - the network calculates the errors and signifies
     *      * how the weights should change to get a more accurate output</li>
     *     <li>updating the weights - the weights are actually adjusted</li>
     * </ul>
     *
     * @see #feedForward()
     * @see #backPropagate()
     * @see #updateWeights()
     */
    public void train() {
        feedForward();
        backPropagate();
        updateWeights();
    }

    /**
     * Only feedforwards the input and returns the output, does
     * not train the network but assumes that training has already
     * been done
     *
     * @return the output of the network
     */
    public double[] compute(double... input) {
        if (inputLayer.getSize() != input.length) {
            throw new IllegalArgumentException();
        }

        inputLayer.changeInputValues(input);
        feedForward();

        return getOutput().stream().mapToDouble(Neuron::getValue).toArray();
    }

    /**
     * Saves the entire network to a file, using the {@link java.io.Serializable}
     * framework
     *
     * @param file file to save this class to
     */
    public void saveNetwork(File file) {
        try (FileOutputStream stream = new FileOutputStream(file);
             ObjectOutputStream objectStream = new ObjectOutputStream(stream)) {

            objectStream.writeObject(this);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Loads an instance of {@link NeuralNetwork} from a {@link File file}
     *
     * @param file file where a {@link NeuralNetwork neural network} is stored
     * @return an instance of {@link NeuralNetwork}
     */
    public static NeuralNetwork loadNetwork(File file) {
        try (FileInputStream stream = new FileInputStream(file);
             ObjectInputStream objectStream = new ObjectInputStream(stream)) {

            Object obj = objectStream.readObject();
            if (!(obj instanceof NeuralNetwork)) {
                throw new IllegalArgumentException("This file does not contain a serialized NeuralNetwork");
            }
            return (NeuralNetwork) obj;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Internal function that prepares the network for usage
     * and signals that it is ready for action
     */
    private void ready() {
        if (ready) return;
        assert inputLayer != null;
        assert outputLayer != null;

        this.layers.add(inputLayer);
        this.layers.addAll(denseLayers);
        this.layers.add(outputLayer);

        connect();

        this.ready = true;
    }

    /**
     * Connects all each {@link Layer} with the next {@link Layer}, so
     * that all {@link Neuron neurons} are connected to
     * {@link Neuron neurons} in the next {@link Layer}
     */
    private void connect() {
        for (int i = 0; i < layers.size(); i++) {
            if (i + 1 >= layers.size()) return; // last layer

            final Layer layer = layers.get(i);
            final Layer next = layers.get(i + 1);

            layer.connectToLayer(next);
        }
    }

    /**
     * Process of feeding forward the input values
     * and computing an output value
     * <p>
     * This process takes the weighted sum of all previous {@link Neuron neurons} and
     * their weights to the current neuron, after which the
     * {@link ActivationFunction} of the current
     * {@link Layer} is applied to the weighted sum. The result of this application
     * is the final value of the current {@link Neuron}
     *
     * @see NeuronConnection
     * @see ActivationFunction
     */
    private void feedForward() {
        for (int i = 0; i < layers.size(); i++) {
            if (i + 1 >= layers.size()) return;

            final Layer current = layers.get(i);
            final Layer next = layers.get(i + 1);

            for (Neuron nextNeuron : next.getNeurons()) {
                double sum = 0.0D;
                for (Neuron previousNeuron : current.getNeurons()) {
                    final NeuronConnection connection = previousNeuron.getConnectionWith(nextNeuron);

                    sum += (previousNeuron.getValue() * connection.getWeight());
                }

                if (current.hasBias()) {
                    final Neuron bias = current.getBias();

                    sum += bias.getValue(); // value is always 1 so we can just add the weight of the connection here ... (?)
                }

                final double result = next.getActivationFunction().apply(sum);
                nextNeuron.setValue(result);
            }
        }
    }

    /**
     * Process of backpropagation. This is where the tough mathematics come in and
     * it goes a little too far to explain this.
     * <p>
     * The necessary computations are done and the outcomes are stored in fields
     * in the {@link Neuron neurons} because these outcomes are needed later in
     * the {@link #updateWeights()} function.
     * <p>
     * This function could as well have been put together into one big function,
     * but to preserve a clear overview, this has been split into two seperate methods.
     */
    private void backPropagate() {
        for (int i = 0; i < outputLayer.getSize(); i++) {

            // Output-layer backpropagation differs from the hidden layers

            final Neuron neuron = outputLayer.getNeurons().get(i);

            final double value = neuron.getValue();
            final double target = targetOutput[i];

            final double errorDerivative = outputLayer.getErrorFunction().applyDerivative(value, target);
            final double activationDerivative = inputLayer.getActivationFunction().applyDerivative(value);

            neuron.setDelta(errorDerivative * activationDerivative);
        }

        for (int i = layers.size() - 2; i > 0; i--) {
            final Layer next = layers.get(i + 1);
            final Layer current = layers.get(i);


            for (Neuron hidden : current.getNeurons()) {

                double sum = 0.0D;
                for (Neuron output : next.getNeurons()) {

                    final double weight = hidden.getConnectionWith(output).getWeight();
                    final double delta = output.getDelta();

                    sum += (weight * delta);
                }

                final double activationDerivative = current.getActivationFunction().applyDerivative(hidden.getValue());
                hidden.setDelta(activationDerivative * sum);
            }
        }
    }

    /**
     * Helper function that calls the {@link #updateWeights(boolean)} function
     * <p>
     * This is for testing purposes to see whether it would make a difference
     * to update the weights forwards or backwards. This method hasn't yet been
     * removed/simplified but this will probably happen soon in the future
     *
     * @see #updateWeights()
     */
    private void updateWeights() {
        this.updateWeights(true);
    }

    /**
     * Helper function that calls the {@link #updateWeights(Layer, Layer)} function
     * <p>
     * This is for testing purposes to see whether it would make a difference
     * to update the weights forwards or backwards. This method hasn't yet been
     * removed/simplified but this will probably happen soon in the future
     *
     * @param forward whether or not to update the weights forward (false=backward)
     * @see #updateWeights(Layer, Layer)
     */
    private void updateWeights(boolean forward) {
        if (forward) {
            for (int i = 1; i < layers.size(); i++) {
                final Layer layer = layers.get(i);
                final Layer previous = layers.get(i - 1);

                this.updateWeights(layer, previous);
            }
        } else {
            for (int i = layers.size() - 2; i > 0; i--) {
                final Layer layer = layers.get(i);
                final Layer next = layers.get(i + 1);

                this.updateWeights(next, layer);
            }

        }
    }

    /**
     * Process of updating the weights, based on the indications that
     * were given in the process of {@link #backPropagate() backpropagation}
     * <p>
     * Again, the mathematics are too complex to explain here and one will
     * just have to assume that this is correct
     *
     * @param layer    current layer that is involved in this process
     * @param previous previous layer that is involved in this process
     */
    private void updateWeights(Layer layer, Layer previous) {
        for (Neuron n : previous.getNeurons()) {

            for (Neuron next : layer.getNeurons()) {
                final NeuronConnection connection = n.getConnectionWith(next);

                final double delta = next.getDelta();
                final double previousOutput = n.getValue();

                final double deltaWeight = -learningRate * delta * previousOutput;

                connection.adjustWeight(deltaWeight);

                if (layer.hasBias()) { // might not work,
                    final double deltaBias = -learningRate * delta;
                    layer.getBias().adjustValue(deltaBias);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < layers.size(); i++) {
            builder.append(layers.get(i).toString());

            if (i + 1 < layers.size()) builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * @return a double array of the target output
     */
    public double[] getTargetOutput() {
        return targetOutput;
    }

    /**
     * @return a string of the double array of the target output
     */
    public String getTargetOutputString() {
        return Arrays.toString(getTargetOutput());
    }

    /**
     * @return a string of the input-neurons
     */
    public String getInputString() {
        return inputLayer.getNeurons().toString();
    }

    /**
     * Builder class to create an instance of {@link NeuralNetwork}, self-explanatory
     */
    public static class Builder {

        private final InputLayer inputLayer;
        private final Set<DenseLayer> hiddenLayers;
        private final DenseLayer outputLayer;

        private double learningRate;
        private double fixedStaringValue;
        private boolean randomStartingValues = true;

        private double[] input;
        private double[] targetOutput;

        public Builder(InputLayer inputLayer, DenseLayer outputLayer) {
            this.inputLayer = inputLayer;
            this.hiddenLayers = new HashSet<>();
            this.outputLayer = outputLayer;
        }

        public Builder withDenseLayer(DenseLayer denseLayer) {
            this.hiddenLayers.add(denseLayer);
            return this;
        }

        public Builder withLearningRate(double x) {
            this.learningRate = x;
            return this;
        }

        public Builder withInput(double... doubles) {
            if (inputLayer.getSize() != doubles.length) {
                throw new IllegalArgumentException();
            }
            this.input = doubles;
            return this;
        }

        public Builder withRandomStartValues() {
            this.randomStartingValues = true;
            return this;
        }

        public Builder withFixedStartValue(double d) {
            this.randomStartingValues = false;
            this.fixedStaringValue = d;
            return this;
        }

        public Builder withTargetOutput(double... doubles) {
            if (outputLayer.getSize() != doubles.length) {
                throw new IllegalArgumentException();
            }
            this.targetOutput = doubles;
            return this;
        }

        /**
         * Constructs an instance of {@link NeuralNetwork}
         *
         * @return an instance of {@link NeuralNetwork}
         * @throws IllegalStateException - when targetOutput or input is null
         */
        public NeuralNetwork build() {
            if (targetOutput == null || this.input == null) {
                throw new IllegalStateException();
            }

            final NeuralNetwork network = new NeuralNetwork(inputLayer, outputLayer, learningRate, targetOutput);
            network.inputLayer.setInput(this.input);
            network.denseLayers.addAll(hiddenLayers);

            if (randomStartingValues) {
                network.denseLayers.forEach(DenseLayer::fillRandom);
                network.outputLayer.fillRandom();
            } else {
                network.denseLayers.forEach(d -> d.fillFixed(fixedStaringValue));
                network.outputLayer.fillFixed(fixedStaringValue);
            }

            network.ready();
            return network;
        }
    }

}
