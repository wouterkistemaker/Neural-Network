package nl.wouterkistemaker.neuralnetwork.function.error;/*
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

import java.io.Serializable;

/**
 * Function that determines the offset between the output of a neuron
 * and the expected output of that same neuron in a implementation-specific fashion
 */
public interface CostFunction extends Serializable {

    double apply(double output, double target);

    double applyDerivative(double output, double target);

}
