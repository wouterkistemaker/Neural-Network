package nl.wouterkistemaker.neuralnetwork.visualisation;

import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.BiasNeuron;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;
import nl.wouterkistemaker.neuralnetwork.neuron.NeuronConnection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashSet;
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
public final class NeuralNetworkPanel extends JPanel {

    private final Layer[] layers;
    private final Set<GraphicNeuron> graphicNeurons;
    private final Set<GraphicNeuronConnection> graphicNeuronConnections;

    private static final int DEFAULT_SIZE = 40; // 40 pixels height
    private int actualSize = DEFAULT_SIZE;

    private static final int paddingX = 20;
    private static final int paddingY = 20;

    public NeuralNetworkPanel(Layer... layers) {
        this(new NeuralNetwork(layers));
    }

    public NeuralNetworkPanel(NeuralNetwork network) {
        this.layers = network.getLayers().toArray(new Layer[0]);
        this.graphicNeurons = new HashSet<>();
        this.graphicNeuronConnections = new HashSet<>();

        setBorder(BorderFactory.createLineBorder(Color.black));
        setPreferredSize(new Dimension(1200, 720));
        setSize(new Dimension(1200, 720));

        this.calculateFittingSize();

        setVisible(true);
    }

    private void createGraphicNeurons() {
        graphicNeurons.clear();
        graphicNeuronConnections.clear();

        final int totalHeight = getHeight();
        final int height = totalHeight - (2 * paddingY);

        final int totalWidth = getWidth();
        final int width = totalWidth - (2 * paddingX);

        final int nRectanglesHorizontal = 2 + (layers.length - 1);
        final int xStep = width / nRectanglesHorizontal;

        int currentX = xStep;

        for (Layer layer : layers) {
            final int nRectanglesVertical = 2 + (layer.getSize() - 1);
            final int yStep = height / nRectanglesVertical;

            int currentY = yStep;

            for (Neuron n : layer.getNeurons()) {
                graphicNeurons.add(new GraphicNeuron(layer, n, currentX, currentY));
                currentY += yStep;
            }

            currentX += xStep;
        }

        Arrays.stream(layers).forEach(l-> l.getNeurons().forEach(n-> n.getConnections().forEach(con -> graphicNeuronConnections.add(new GraphicNeuronConnection(con)))));
    }

    private GraphicNeuron getGraphicNeuron(Neuron n) {
        return graphicNeurons.stream().filter(gn -> gn.neuron.equals(n)).findFirst().orElseThrow();
    }

    private void drawNeuron(Graphics graphic, GraphicNeuron neuron) {
        graphic.setColor(neuron.neuron instanceof BiasNeuron ? Color.CYAN : Color.PINK);
        graphic.fillOval(neuron.getX(), neuron.getY(), actualSize, actualSize);
        graphic.setColor(Color.BLACK);
        graphic.drawString(String.format("%.2f", neuron.getNeuron().getValue()), neuron.getCenterX() - 10, neuron.getCenterY() + 10);
    }

    private void drawConnection(Graphics graphic, GraphicNeuronConnection connection) {
        graphic.setColor(connection.from.neuron instanceof BiasNeuron ? Color.GREEN : Color.MAGENTA);
        graphic.drawLine(connection.from.getCenterX(), connection.from.getCenterY(), connection.to.getCenterX(), connection.to.getCenterY());
    }

    @Override
    public void paintComponent(Graphics graphic) {
        createGraphicNeurons();
        for (int i = 0; i < layers.length; i++) {
            if ((i + 1) >= layers.length) break;

            final Layer current = layers[i];
            final Layer next = layers[i + 1];

            graphicNeurons.forEach(gn -> this.drawNeuron(graphic, gn));
            graphicNeuronConnections.forEach(gnc -> this.drawConnection(graphic, gnc));
        }
        graphic.dispose();
    }

    private void calculateFittingSize() {
        int biggest = 0;

        for (Layer l : layers) {
            if (l.getSize() > biggest) biggest = l.getSize();
        }

        while (actualSize * biggest > (getHeight() - 2 * paddingY)) {
            actualSize--;
        }
    }

    private final class GraphicNeuron {
        private final Layer layer;
        private final Neuron neuron;
        private final int x;
        private final int y;

        public GraphicNeuron(Layer layer, Neuron neuron, int x, int y) {
            this.layer = layer;
            this.neuron = neuron;
            this.x = x;
            this.y = y;
        }

        public Layer getLayer() {
            return layer;
        }

        public Neuron getNeuron() {
            return neuron;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getCenterX() {
            return x + NeuralNetworkPanel.this.actualSize / 2;
        }

        public int getCenterY() {
            return y + NeuralNetworkPanel.this.actualSize / 2;
        }

        @Override
        public String toString() {
            return "GraphicNeuron{" +
                    "layer=" + layer +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    private final class GraphicNeuronConnection {

        private final NeuronConnection connection;

        private final GraphicNeuron from;
        private final GraphicNeuron to;

        public GraphicNeuronConnection(NeuronConnection connection) {
            this.connection = connection;

            this.from = getGraphicNeuron(connection.getFrom());
            this.to = getGraphicNeuron(connection.getTo());
        }

        public NeuronConnection getConnection() {
            return connection;
        }

        public GraphicNeuron getFrom() {
            return from;
        }

        public GraphicNeuron getTo() {
            return to;
        }
    }
}
