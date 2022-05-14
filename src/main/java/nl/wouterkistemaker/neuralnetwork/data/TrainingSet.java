package nl.wouterkistemaker.neuralnetwork.data;

import nl.wouterkistemaker.neuralnetwork.util.NetworkUtils;

import java.util.ArrayList;
import java.util.Collections;
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
public class TrainingSet {

    private final List<TrainingSample> trainingSamples;

    public TrainingSet(List<TrainingSample> samples) {
        this.trainingSamples = samples;
    }

    public TrainingSet(int size) {
        this.trainingSamples = new ArrayList<>(size);
    }

    public void addSample(TrainingSample sample) {
        this.trainingSamples.add(sample);
    }

    public TrainingSample getSample(int index) {
        return trainingSamples.get(index);
    }

    public TrainingSet extractBatch(int batchSize) {
        final TrainingSet batch = new TrainingSet(batchSize);

        if (batchSize >= trainingSamples.size()) return this;

        for (int i = 0; i < batchSize; i++) {
            batch.addSample(trainingSamples.get(NetworkUtils.nextInt(trainingSamples.size())));
        }
        return batch;
    }

    public List<TrainingSet> extractBatches(int batchSize) {
        final LinkedList<TrainingSample> copy = new LinkedList<>(trainingSamples);

        final List<TrainingSet> result = new ArrayList<>();


        for (int i = 0; i < copy.size(); i++) {
            final TrainingSet set = new TrainingSet(batchSize);
            while (set.getSize() != batchSize) {
                set.addSample(copy.pop());
            }

            result.add(set);
        }

        return result;
    }

    public void shuffle() {
        Collections.shuffle(trainingSamples);
    }

    public int getSize() {
        return trainingSamples.size();
    }
}
