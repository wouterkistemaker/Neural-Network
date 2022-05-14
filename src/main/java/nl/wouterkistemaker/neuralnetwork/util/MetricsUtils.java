package nl.wouterkistemaker.neuralnetwork.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
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
public class MetricsUtils {

    public static void drawErrorCurve(List<Double> cost) {
        final XYSeries series = getSeries("Error Curve", cost);
        setupFrame("Error Curve", series);
    }

    public static void drawLearningCurve(List<Double> trainingCost, List<Double> testingCost) {
        final XYSeries trainingSeries = getSeries("Training Loss", trainingCost);
        final XYSeries testingSeries = getSeries("Testing Loss", testingCost);

        setupFrame("Learning Curve", trainingSeries, testingSeries);
    }

    private static void setupFrame(String title, XYSeries... series){
        final XYSeriesCollection collection = new XYSeriesCollection();
        Arrays.stream(series).forEach(collection::addSeries);

        final JFreeChart chart = ChartFactory.createXYLineChart(title, "Epoch (n)", "Cost", collection);
        final ChartPanel panel = new ChartPanel(chart);

        final JFrame frame = new JFrame();
        frame.setSize(new Dimension(1280, 720));
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static XYSeries getSeries(String name, List<Double> input) {
        XYSeries series = new XYSeries(name);
        for (int i = 0; i < input.size(); i++) {
            series.add(i, input.get(i));
        }
        return series;
    }
}
