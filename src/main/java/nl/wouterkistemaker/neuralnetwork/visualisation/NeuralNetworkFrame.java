package nl.wouterkistemaker.neuralnetwork.visualisation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
public final class NeuralNetworkFrame extends JFrame {

    private static final long serialVersionUID = 8389964237793525241L;

    public NeuralNetworkFrame(NeuralNetworkPanel panel) {
        super("Neural Network Visualisation by Wouter Kistemaker");
        setSize(new Dimension(1200, 720));
        setPreferredSize(new Dimension(1200, 720));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        add(panel);
        setVisible(true);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getComponent() instanceof NeuralNetworkFrame) {
                    final Dimension updated = e.getComponent().getSize();
                    panel.setSize(updated);
                }
            }
        });
    }

    public NeuralNetworkFrame() {
        this(new NeuralNetworkPanel());
    }


}
