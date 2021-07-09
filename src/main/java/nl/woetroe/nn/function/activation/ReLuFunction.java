package nl.woetroe.nn.function.activation;

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

/**
 * Represents the ReLu activation function
 *
 * @deprecated class, this is not implemented and therefore useless for now
 */
@Deprecated
public final class ReLuFunction implements ActivationFunction {

    @Override
    public double applyDerivative(double d) {
        return d <= 0 ? 0 : 1;
    }

    @Override
    public Double apply(Double aDouble) {
        return Math.max(0, aDouble);
    }
}