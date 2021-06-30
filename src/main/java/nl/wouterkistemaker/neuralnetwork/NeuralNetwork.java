package nl.wouterkistemaker.neuralnetwork;

import nl.wouterkistemaker.neuralnetwork.layer.Layer;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkFrame;
import nl.wouterkistemaker.neuralnetwork.visualisation.NeuralNetworkPanel;

import java.io.Serializable;
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
    private final Set<Layer> layers;

    private boolean connected; // boolean signalling whether or not the layers are yet interconnected

    /**
     * Constructs a new NeuralNetwork instance containing the specified {@link Layer layers}
     *
     * @param layers array of {@link Layer layers} of this NeuralNetwork
     */
    public NeuralNetwork(Layer... layers) {
        this.connected = false;
        this.layers = new LinkedHashSet<>(Arrays.asList(layers));
        this.layers.forEach(l -> l.setNetworkInstance(this));
        this.connect();
    }

    public Layer getPreviousLayer(Layer current) {
        List<Layer> list = new LinkedList<>(layers);
        final int index = list.indexOf(current);
        return (index - 1) < 0 ? current : list.get(index - 1);
    }

    /**
     * Function that initiates the process of connecting each individual
     * {@link Layer} to the next {@link Layer} in the network
     */
    private void connect() {
        if (connected) return;

        final Layer[] layerArray = layers.toArray(new Layer[0]);
        for (int i = 0; i < layerArray.length; i++) {
            Layer current = layerArray[i];
            if ((i + 1) >= layerArray.length) return;
            Layer next = layerArray[i + 1];
            current.connect(next);
        }
        connected = true;
    }

    /**
     * Helper function that creates a comprehensible overview of this network's architecture
     */
    public final void visualize() {
        new NeuralNetworkFrame(new NeuralNetworkPanel(this));
    }

    /**
     * @return a {@link LinkedHashSet} of {@link Layer layers}
     */
    public Set<Layer> getLayers() {
        return layers;
    }
}
