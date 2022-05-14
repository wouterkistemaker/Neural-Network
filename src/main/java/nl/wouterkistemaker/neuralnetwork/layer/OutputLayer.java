package nl.wouterkistemaker.neuralnetwork.layer;

import nl.wouterkistemaker.neuralnetwork.function.error.CostFunction;
import nl.wouterkistemaker.neuralnetwork.function.error.MeanSquaredCost;
import nl.wouterkistemaker.neuralnetwork.function.initialization.InitializationFunction;
import nl.wouterkistemaker.neuralnetwork.function.transfer.TransferFunction;

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
public final class OutputLayer extends Layer {

    private static final CostFunction DEFAULT_COST_FUNCTION = new MeanSquaredCost();

    private final CostFunction costFunction;

    public OutputLayer(int size, boolean bias, InitializationFunction initializationFunction, TransferFunction transferFunction, CostFunction costFunction) {
        super(size, bias, initializationFunction, transferFunction, costFunction);
        this.costFunction = costFunction == null ? DEFAULT_COST_FUNCTION : costFunction;
    }

    public OutputLayer(int size, boolean bias, InitializationFunction initializationFunction, TransferFunction transferFunction) {
        this(size, bias, initializationFunction, transferFunction, null);
    }

    public OutputLayer(int size, boolean bias, InitializationFunction initializationFunction) {
        this(size, bias, initializationFunction, null);
    }

    public OutputLayer(int size, boolean bias) {
        this(size, bias, null);
    }

    public OutputLayer(int size) {
        this(size, false);
    }

    @Override
    public void propagateBackwards() {
        
    }


}
