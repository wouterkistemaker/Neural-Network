package nl.woetroe.nn.layer;

import nl.woetroe.nn.function.activation.ActivationFunction;
import nl.woetroe.nn.function.error.ErrorFunction;
import nl.woetroe.nn.neuron.Neuron;

import java.util.Arrays;

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
public final class InputLayer extends Layer {

    private static final long serialVersionUID = -6512736351539200069L;

    public InputLayer(int size, boolean withBias, ActivationFunction activationFunction, ErrorFunction errorFunction) {
        super(size, withBias, activationFunction, errorFunction);
    }

    public InputLayer(int size, boolean withBias){
        super(size, withBias);
    }

    public InputLayer(int size){
        super(size);
    }

    public void setInput(double... input) {
        Arrays.stream(input).forEach(i -> this.getNeurons().add(new Neuron(i)));
    }

    public void changeInputValues(double... input){
        for (int i = 0; i < input.length; i++) {
            getNeurons().get(i).setValue(input[i]);
        }
    }


}
