package nl.wouterkistemaker.neuralnetwork.visualisation;

import nl.wouterkistemaker.neuralnetwork.NeuralNetwork;
import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.neuron.BiasNeuron;
import nl.wouterkistemaker.neuralnetwork.neuron.Neuron;
import nl.wouterkistemaker.neuralnetwork.neuron.NeuronConnection;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
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

    private static final long serialVersionUID = -5206286198194216255L;
    private final List<Layer> layers;
    private final NeuralNetwork network;
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
        this.network = network;
        this.layers = network.getLayers();
        this.graphicNeurons = new HashSet<>();
        this.graphicNeuronConnections = new HashSet<>();

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(1280, 720));
        setSize(new Dimension(1280, 720));

        setVisible(true);
    }

    private void createGraphicNeurons() {
        graphicNeurons.clear();
        graphicNeuronConnections.clear();

        final int totalHeight = getHeight();
        final int height = totalHeight - (2 * paddingY);

        final int totalWidth = getWidth();
        final int width = totalWidth - (2 * paddingX);

        final int nRectanglesHorizontal = 2 + (layers.size() - 1);
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

        layers.forEach(l -> l.getNeurons().forEach(n -> n.getConnections().forEach(con -> graphicNeuronConnections.add(new GraphicNeuronConnection(con)))));
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

        int dX = connection.from.getCenterX() - connection.to.getCenterX();
        int dY = connection.from.getCenterY() - connection.to.getCenterY();

        int weightX = dX / 2;
        int weightY = dY / 2;

        if (getConnectionCount(connection.from.layer, connection.to.layer) <= 8) {
            graphic.setColor(Color.blue);
            graphic.drawString(String.format("%.2f", connection.connection.getWeight()),
                    connection.from.getCenterX() - weightX,
                    connection.from.getCenterY() - weightY - 10);
        }
    }

    @Override
    public void paintComponent(Graphics graphic) {
        this.calculateFittingSize();
        createGraphicNeurons();

        graphic.setColor(Color.WHITE);
        graphic.fillRect(getWidth() - 125, getY(), 125, 25 + layers.size() * 25);
        graphic.setColor(Color.RED);
        graphic.drawRect(getWidth() - 125, getY(), 125, 25 + layers.size() * 25);

        for (int i = 0; i < layers.size(); i++) {
            graphic.setColor(Color.BLACK);
            graphic.drawString(String.format("Layer %s, size=%s", i + 1, layers.get(i).getSize()), getWidth() - 115, 25 + i * 25);

            if ((i + 1) >= layers.size()) break;

            graphicNeuronConnections.forEach(gnc -> this.drawConnection(graphic, gnc));
            graphicNeurons.forEach(gn -> this.drawNeuron(graphic, gn));
        }
        graphic.dispose(); // Not sure if this is necessary so can be removed if this causes breaks of the system.
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

    private int getConnectionCount(Layer from, Layer to) {
        int sum = 0;

        sum += from.getSize() * to.getSize();
        if (to.hasBias()) {
            sum -= from.getSize();
        }

        return sum;
    }

    private final class GraphicNeuron implements Serializable {
        private static final long serialVersionUID = -5124805450106215780L;
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

    private final class GraphicNeuronConnection implements Serializable {

        private static final long serialVersionUID = -3427888148405163514L;
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
