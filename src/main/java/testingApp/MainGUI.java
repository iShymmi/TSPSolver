package main.java.testingApp;

import main.java.algorithm.TSPSolver;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class MainGUI extends JFrame{

    private final Canva canva;
    private final TSPSolver tspSolver;
    private JButton runButton;

    public MainGUI(){
        canva = new Canva();
        JPanel navBar = new JPanel();

        tspSolver = new TSPSolver();

        JPanel settings = new JPanel(new GridLayout(20,1,15,2));

        JLabel populationSize = new JLabel("Population size");
        JSpinner populationSizeSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));

        JLabel stopCondition = new JLabel("Stop condition");
        JSpinner stopConditionSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));

        JLabel startPointId = new JLabel("Start point id");
        JSpinner startPointIdSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));

        JLabel selectMutationPorbability = new JLabel("Select mutation probability");
        JSpinner selectMutationPorbabilitySpinner = new JSpinner(new SpinnerNumberModel(0,0,1,0.01));

        JLabel selectCrossingPorbability = new JLabel("Select crossing probability");
        JSpinner selectCrossingPorbabilitySpinner = new JSpinner(new SpinnerNumberModel(0,0,1,0.01));

        JButton set = new JButton("Set");

        settings.add(populationSize);
        settings.add(populationSizeSpinner);
        settings.add(stopCondition);
        settings.add(stopConditionSpinner);
        settings.add(startPointId);
        settings.add(startPointIdSpinner);
        settings.add(selectMutationPorbability);
        settings.add(selectMutationPorbabilitySpinner);
        settings.add(selectCrossingPorbability);
        settings.add(selectCrossingPorbabilitySpinner);
        settings.add(Box.createVerticalBox());
        settings.add(set);


        set.addActionListener((e -> {
            tspSolver.setDistances(getDistances(canva.getPoints()));

            tspSolver.setStartIndex((Integer) startPointIdSpinner.getValue());

            tspSolver.setPopulationSize((Integer) populationSizeSpinner.getValue());

            tspSolver.setStopCondition((Integer) stopConditionSpinner.getValue());

            tspSolver.setMutationPickProbability((Double) selectMutationPorbabilitySpinner.getValue());


            tspSolver.setCrossingPickProbability((Double) selectCrossingPorbabilitySpinner.getValue());

            runButton.setEnabled(true);
        }));

        JLabel times = new JLabel("Times");
        JSpinner timesSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));

        runButton = new JButton("RUN");
        runButton.setEnabled(false);
        runButton.addActionListener((e -> {
            canva.clearLines();
            for(int i = 0; i < (int) timesSpinner.getValue(); i++) {
                EventQueue.invokeLater(() -> {
                    tspSolver.run();
                    System.out.println(tspSolver.getBestIndividual());

                    canva.drawLine(tspSolver.getBestIndividual().getGenes());
                });
            }
        }));

        JButton clear = new JButton("CLEAR");

        navBar.add(times);
        navBar.add(timesSpinner);
        navBar.add(runButton);
        navBar.add(clear);

        add(navBar,BorderLayout.NORTH);
        add(canva,BorderLayout.CENTER);
        add(settings,BorderLayout.WEST);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
    }

    public double[][] getDistances(ArrayList<Point> points){
        double[][] distances = new double[points.size()][points.size()];
        double distance;
        double x1;
        double x2;

        double y1;
        double y2;

        for(int i = 0; i < (points.size() / 2) + 1; i++){
            distances[i][i] = 0;
            for(int j = i + 1; j < points.size(); j++){
                x1 = points.get(i).getX();
                x2 = points.get(j).getX();

                y1 = points.get(i).getY();
                y2 = points.get(j).getY();

                distance = sqrt(pow((x2 - x1),2) + pow((y2 - y1),2));
                distances[i][j] = distances[j][i] = distance;
            }
        }

        return distances;
    }

    public static void main(String[] args) {
        MainGUI testApp = new MainGUI();
        EventQueue.invokeLater(()->
                testApp.setVisible(true)
        );
    }
}
