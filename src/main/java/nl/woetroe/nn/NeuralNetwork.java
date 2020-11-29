package nl.woetroe.nn;

import nl.woetroe.nn.layer.DenseLayer;
import nl.woetroe.nn.layer.InputLayer;
import nl.woetroe.nn.layer.Layer;
import nl.woetroe.nn.neuron.Neuron;
import nl.woetroe.nn.neuron.NeuronConnection;

import java.io.*;
import java.util.*;

public final class NeuralNetwork implements Serializable {

    private static final double DEFAULT_LEARNING_RATE = 0.5;
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

    public List<Neuron> getOutput() {
        return outputLayer.getNeurons();
    }

    /**
     *
     * @param duration duration in milliseconds
     * @return amount of epochs the training completed
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

    public void train() {
        feedForward();
        backPropagate();
        updateWeights();
    }

    public void saveNetwork(File file) {
        try (FileOutputStream stream = new FileOutputStream(file);
             ObjectOutputStream objectStream = new ObjectOutputStream(stream)) {

            objectStream.writeObject(this);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

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

    private void connect() {
        for (int i = 0; i < layers.size(); i++) {
            if (i + 1 >= layers.size()) return; // last layer

            final Layer layer = layers.get(i);
            final Layer next = layers.get(i + 1);

            layer.connectToLayer(next);
        }
    }

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

                    sum += bias.getConnectionWith(nextNeuron).getWeight(); // value is always 1 so we can just add the weight of the connection here
                }

                final double result = next.getActivationFunction().apply(sum);
                nextNeuron.setValue(result);
            }
        }
    }

    private void backPropagate() {
        for (int i = 0; i < outputLayer.getSize(); i++) {
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

    private void updateWeights() {
        this.updateWeights(true);
    }

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

    private void updateWeights(Layer layer, Layer previous) {
        for (Neuron n : previous.getNeurons()) {

            for (Neuron next : layer.getNeurons()) {
                final NeuronConnection connection = n.getConnectionWith(next);

                final double delta = next.getDelta();
                final double previousOutput = n.getValue();

                final double deltaWeight = -learningRate * delta * previousOutput;

                connection.adjustWeight(deltaWeight);
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

    public double[] getTargetOutput() {
        return targetOutput;
    }

    public String getTargetOutputString() {
        return Arrays.toString(getTargetOutput());
    }

    public String getInputString() {
        return inputLayer.getNeurons().toString();
    }

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
