package nl.wouterkistemaker.neuralnetwork.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
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
        XYSeriesCollection collection = new XYSeriesCollection();

        XYSeries series = new XYSeries("Error Curve");

        for (int i = 0; i < cost.size(); i++) {
            series.add(i, cost.get(i));
        }

        collection.addSeries(series);

        final JFreeChart chart = ChartFactory.createXYLineChart("Error Curve", "Epoch", "Cost", collection);
        final ChartPanel panel = new ChartPanel(chart);

        final JFrame frame = new JFrame();
        frame.setSize(new Dimension(1280, 720));
        frame.add(panel);
        frame.setVisible(true);
    }
}
