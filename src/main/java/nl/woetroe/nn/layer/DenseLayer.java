package nl.woetroe.nn.layer;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.neuron.Neuron;

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
public final class DenseLayer extends Layer {

    private static final long serialVersionUID = -4158063030455588345L;

    public DenseLayer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        super(size, withBias, activationFunction, errorFunction);
    }

    public DenseLayer(int size, boolean withBias) {
        super(size, withBias);
    }


    public DenseLayer(int size) {
        super(size);
    }

    public void fillFixed(double value) {
        for (int i = 0; i < getSize(); i++) {
            getNeurons().add(new Neuron(value));
        }
    }

    public void fillRandom() {
        for (int i = 0; i < getSize(); i++) {
            getNeurons().add(new Neuron());

        }
    }
}
