package nl.wouterkistemaker.neuralnetwork;

import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkFrame;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkPanel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    private final List<Layer> layers;

    private boolean connected; // boolean signalling whether or not the layers are yet interconnected

    private NeuralNetworkFrame frame;

    /**
     * Constructs a new NeuralNetwork instance containing the specified {@link Layer layers}
     *
     * @param layers array of {@link Layer layers} of this NeuralNetwork
     */
    public NeuralNetwork(Layer... layers) {
        this.connected = false;
        this.layers = new LinkedList<>(Arrays.asList(layers));
        this.layers.forEach(l -> l.setNetworkInstance(this));
        this.connect();
    }

    public Layer getPreviousLayer(Layer current) {
        final int index = layers.indexOf(current);
        return (index - 1) < 0 ? current : layers.get(index - 1);
    }

    public void feedforward() {
        for (int i = 0; i < layers.size(); i++) {
            if (i + 1 >= layers.size()) break;

            final Layer current = layers.get(i);
            final Layer next = layers.get(i + 1);

            current.feedforward(next);
        }

        if (this.frame != null) this.frame.update();
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

    /**
     * @return a {@link LinkedList} of {@link Layer layers}
     */
    public List<Layer> getLayers() {
        return layers;
    }
}
