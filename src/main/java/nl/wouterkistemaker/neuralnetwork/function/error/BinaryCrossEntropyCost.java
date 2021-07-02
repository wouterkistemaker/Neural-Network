package nl.wouterkistemaker.neuralnetwork.function.error;

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
public final class BinaryCrossEntropyCost implements CostFunction {

    @Override
    public double computeError(double output, double target) {
        // https://youtu.be/gIx974WtVb4?t=173
        // L = t(lnO) + (1-t)ln(1-O)

        if (output == 1 || output == 0){
            throw new IllegalStateException("Error cannot be computed for this output");
        }

        // Where output is variable and target is a constant
        return target * Math.log(output) + (1 - target) * Math.log(1 - output);
    }
}
