package nl.wouterkistemaker.neuralnetwork;

import nl.wouterkistemaker.neuralnetwork.data.TrainingSample;
import nl.wouterkistemaker.neuralnetwork.data.TrainingSet;
import nl.wouterkistemaker.neuralnetwork.layer.InputLayer;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;
import nl.wouterkistemaker.neuralnetwork.neuron.NeuronConnection;
import nl.wouterkistemaker.neuralnetwork.util.MetricsUtils;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkFrame;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkPanel;

import java.io.*;
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
public final class NeuralNetwork implements Serializable {

    private static final long serialVersionUID = 3314104889598862062L;
    private static final String DEFAULT_FILE_NAME = "neuralnetwork.dat";

    private final UUID id;

    private final List<Layer> layers;
    private final InputLayer inputLayer;
    private final Layer outputLayer;

    private double[] targetOutput;
    private final List<Double> trainingCost;
    private final List<Double> testingLoss;
    private int epochCounter;

    private boolean connected; // boolean signalling whether or not the layers are yet interconnected

    private NeuralNetworkFrame frame;

    /**
     * Constructs a new NeuralNetwork instance containing the specified {@link Layer layers}
     *
     * @param layers array of {@link Layer layers} of this NeuralNetwork
     */
    public NeuralNetwork(Layer... layers) {

        this.id = UUID.randomUUID();
        this.connected = false;

        if (layers.length != 0) {
            final Layer outputLayer = layers[layers.length - 1];
            if (outputLayer.hasBias()) {
                layers[layers.length - 1] = new Layer(outputLayer.getSize(), false, outputLayer.getInitializationFunction(), outputLayer.getTransferFunction(), outputLayer.getCostFunction());
                System.out.println("The output-layer cannot have a bias-neuron, therefore it has been removed!");
            }

            final Layer inputLayer = layers[0];
            if (!(inputLayer instanceof InputLayer)) {
                layers[0] = new InputLayer(inputLayer.getSize(), inputLayer.hasBias(), inputLayer.getInitializationFunction(), inputLayer.getTransferFunction(), inputLayer.getCostFunction());
                System.out.println("The network must have an input-layer, therefore the first layer was transformed into an InputLayer");
            }

            this.layers = new LinkedList<>(Arrays.asList(layers));
            this.inputLayer = (InputLayer) this.layers.get(0);
            this.outputLayer = this.layers.get(layers.length - 1);
            this.layers.forEach(l -> l.setNetworkInstance(this));

            this.trainingCost = new LinkedList<>();
            this.testingLoss = new LinkedList<>();

            this.connect();

        } else {
            throw new IllegalStateException("Cannot create empty Neural Network");
        }
    }

    public final Layer getPreviousLayer(Layer current) {
        final int index = layers.indexOf(current);
        return (index - 1) < 0 ? current : layers.get(index - 1);
    }

    public final Layer getNextLayer(Layer layer) {
        final int index = layers.indexOf(layer);
        return (index + 1 >= layers.size()) ? layer : layers.get(index + 1);
    }

    public final boolean isOutputLayer(Layer layer) {
        return layers.indexOf(layer) == layers.size() - 1;
    }

    public final boolean isInputLayer(Layer layer) {
        return layers.indexOf(layer) == 0;
    }

    public final void feedforward() {
        for (int i = 0; i < layers.size(); i++) {
            if (i + 1 >= layers.size()) break;

            final Layer current = layers.get(i);
            final Layer next = layers.get(i + 1);

            current.feedforward(next);
        }

        if (this.frame != null) this.frame.update();
    }

    public final void propagateBackwards() {
        layers.get(layers.size() - 1).propagateBackwards();
        if (this.frame != null) this.frame.update();
    }

    public final void updateWeights(double learningRate) {
        for (int l = 0; l < layers.size(); l++) {
            if (l + 1 >= layers.size()) break;

            final Layer layer = layers.get(l);
            final Layer next = layers.get(l + 1);

            for (Neuron previousNeuron : layer.getNeurons()) {
                for (Neuron nextNeuron : next.getNeurons()) {

                    final NeuronConnection connection = previousNeuron.getConnectionWith(nextNeuron);
                    final double adjustment = -learningRate * nextNeuron.getDelta() * previousNeuron.getValue();

                    connection.adjustWeight(adjustment);
                }

                if (layer.hasBias(previousNeuron)) {
                    layer.getBias(previousNeuron).getConnectionWith(previousNeuron).adjustWeight(-learningRate * previousNeuron.getDelta());
                }
            }
        }
    }

    public final void train(double[] input, double[] target, double learningRate) {
        if (input.length != inputLayer.getSize() || target.length != outputLayer.getSize()) {
            throw new IllegalArgumentException("Invalid dimensions for " + (input.length == inputLayer.getSize() ? "target" : "input") + ", " + target.length + " does not match " + outputLayer.getSize());
        }

        this.inputLayer.setInput(input);
        this.targetOutput = target;

        this.feedforward();

        trainingCost.add(epochCounter++, outputLayer.getCost());

        this.propagateBackwards();
        this.updateWeights(learningRate);
    }

    public final void train(TrainingSet set, int batch_size, int epochs, double learningRate) {
        for (int i = 0; i < epochs; i++) {
            System.out.println("Epoch : " + (i+1));

            set.shuffle();
            List<TrainingSet> batches = set.extractBatches(batch_size);

            for (int b = 0; b < batches.size(); b++) {
                final TrainingSet batch = batches.get(b);

                if (batch.getSize() == batch_size) {
                    for (int j = 0; j < batch_size; j++) {
                        final TrainingSample sample = batch.getSample(j);

                        final double[] input = sample.getInput();
                        final double[] target = sample.getTarget();

                        train(input, target, learningRate);
                    }

                    // calculate cost for each sample and average it
                }

                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + (b+1) + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
        }
    }

    public final double[] predict(double[] input) {
        this.inputLayer.setInput(input);
        this.feedforward();
        return getOutput();
    }

    /**
     * Helper function that creates a comprehensible overview of this network's architecture
     */
    public final void visualize() {
        if (this.frame == null) {
            this.frame = new NeuralNetworkFrame(new NeuralNetworkPanel(this));
        } else {
            this.frame.update();
        }
    }

    public final void save() {
        this.save(null);
    }

    public final void save(File file) {
        if (file == null) {
            file = new File(DEFAULT_FILE_NAME);
        }

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file))) {
                os.writeObject(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NeuralNetwork load() {
        return load(null);
    }

    public static NeuralNetwork load(File file) {
        if (file == null) {
            file = new File(DEFAULT_FILE_NAME);
        }

        if (!file.exists()) {
            throw new IllegalStateException("File does not exist");
        }

        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream(file))) {

            final Object o = is.readObject();

            if (o instanceof NeuralNetwork) {
                return (NeuralNetwork) o;
            } else {
                throw new IllegalStateException("File does not contain NeuralNetwork data");
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @return a {@link LinkedList} of {@link Layer layers}
     */
    public final List<Layer> getLayers() {
        return layers;
    }

    public final double[] getOutput() {
        return layers.get(layers.size() - 1).getOutput();
    }

    public final double[] getTargetOutput() {
        return this.targetOutput;
    }

    public final void drawErrorCurve() {
        MetricsUtils.drawErrorCurve(trainingCost);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Function that initiates the process of connecting each individual
     * {@link Layer} to the next {@link Layer} in the network
     */
    private void connect() {
        if (connected) return;
        for (int i = 0; i < layers.size(); i++) {
            if (i + 1 >= layers.size()) break; // last layer

            final Layer layer = layers.get(i);
            final Layer next = layers.get(i + 1);

            layer.connect(next);
        }
        connected = true;
    }

}
