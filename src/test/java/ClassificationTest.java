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

import nl.woetroe.nn.NeuralNetwork;
import nl.woetroe.nn.data.DataSet;
import nl.woetroe.nn.data.Label;
import nl.woetroe.nn.data.TrainingDataSet;
import nl.woetroe.nn.data.type.Coordinate;
import nl.woetroe.nn.util.NetworkUtils;

public class ClassificationTest {

    private static final Label ORANGE = new Label("orange", 0); // if neuron 0 output > neuron 1 output it is categorized as orange
    private static final Label BLUE = new Label("blue", 1); // vice versa

    private static final TrainingDataSet<Coordinate> trainingSet = new TrainingDataSet<>(20);
    private static final DataSet<Coordinate> testSet = new TrainingDataSet<>(10);

    public static void main(String[] args) {
        for (int i = 0; i < trainingSet.getSize(); i++) {
            trainingSet.add(new Coordinate(NetworkUtils.nextDouble(-6, 6), NetworkUtils.nextDouble(-6, 6)));
        }

        for (int i = 0; i < testSet.getSize(); i++) {
            testSet.add(new Coordinate(NetworkUtils.nextDouble(-6, 6), NetworkUtils.nextDouble(-6, 6)));
        }

        trainingSet.classify(obj -> obj.getYAsDouble() >= 0 ? BLUE : ORANGE);

        final NeuralNetwork network = new NeuralNetwork.Builder(null, null)
            .withLearningRate(0.001).build();


    }


}
