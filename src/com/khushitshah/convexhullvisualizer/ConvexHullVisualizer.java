package com.khushitshah.convexhullvisualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class ConvexHullVisualizer {

    public static final int WIDTH = 720, HEIGHT = 640;

    ConvexHullSolver solver =  new ConvexHullSolver();

    private final int pointsLength = 20;
    private boolean isSelectingPoint = false;

    ConvexHullVisualizer() {
        JFrame jFrame = new JFrame("ConvexHull Visualizer");
        jFrame.setLayout(new BorderLayout());

        jFrame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        jFrame.setMinimumSize(new Dimension(WIDTH, HEIGHT));


        JPanel northPanel, southPanel, eastPanel, westPanel;
        northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        eastPanel = new JPanel();
        eastPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        westPanel = new JPanel();
        westPanel.setLayout(new FlowLayout(FlowLayout.LEFT));


        JButton pointSelection = new JButton("Start Selecting Point");
        pointSelection.addActionListener(actionEvent -> {
            if (actionEvent.getActionCommand().equals("Stop Selection")) {
                isSelectingPoint = false;
                pointSelection.setText("Start Selecting Point");
            } else {
                isSelectingPoint = true;
                pointSelection.setText("Stop Selection");
            }
        });

        JButton randomPoints = new JButton("Random Points");
        randomPoints.addActionListener(actionEvent -> {
            solver.points.clear();
            Random r = new Random();
            for (int i = 0; i < pointsLength; i ++) {
                solver.points.add(new Point(r.nextInt(600), r.nextInt(600)));
            }
            solver.repaint();
            jFrame.repaint();
        });
        JButton startAnimation = new JButton("Start");
        startAnimation.addActionListener(actionEvent -> {
            if (actionEvent.getActionCommand().equals("Start")) {
                randomPoints.setEnabled(false);
                pointSelection.setEnabled(false);
                solver.start();
                startAnimation.setText("Stop");

                solver.repaint();
                jFrame.repaint();
            } else {
                randomPoints.setEnabled(true);
                pointSelection.setEnabled(true);
                solver.stop();
                startAnimation.setText("Start");
                solver.repaint();
                jFrame.repaint();
            }

        });
        northPanel.add(startAnimation);

        JSlider animationSpeed = new JSlider();
        animationSpeed.setMinimum(1);
        animationSpeed.setMaximum(100);
        animationSpeed.addChangeListener(changeEvent -> solver.ticks = animationSpeed.getValue());
        northPanel.add(pointSelection);
        northPanel.add(randomPoints);

        southPanel.add(new JLabel("Select Animation Speed: "));
        southPanel.add(animationSpeed);

        solver.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isSelectingPoint) {
                    solver.points.add(e.getPoint());
                    solver.repaint();
                    jFrame.repaint();
                }
            }
        });

        jFrame.add(northPanel, BorderLayout.NORTH);
        jFrame.add(westPanel, BorderLayout.WEST);
        jFrame.add(eastPanel ,BorderLayout.EAST);
        jFrame.add(southPanel ,BorderLayout.SOUTH);
        jFrame.add(solver, BorderLayout.CENTER);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new ConvexHullVisualizer();
    }
}