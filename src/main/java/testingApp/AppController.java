package testingApp;

import algorithm.TSPSolver;

import java.awt.*;

import static java.util.Arrays.stream;

public class AppController {

    private final MainFrame mainFrame;
    private final CanvaFrame canvaFrame;
    private final TSPSolver tspSolver;
    private final Canva canva;

    public AppController(MainFrame mainFrame, CanvaFrame canvaFrame, TSPSolver tspSolver) {
        this.mainFrame = mainFrame;
        this.canvaFrame = canvaFrame;
        this.tspSolver = tspSolver;

        canva = canvaFrame.getCanva();
        setupEvents();
    }

    public void run() {
        mainFrame.setVisible(true);
    }

    private void setupEvents() {
        mainFrame.getDrawPointsButton().addActionListener(e -> this.drawPoints());
        mainFrame.getSetButton().addActionListener(e -> this.setData());
        mainFrame.getRunButton().addActionListener(e -> this.runAlgorithm());
        canvaFrame.getSetButton().addActionListener(e -> this.setPoints());
    }

    public void drawPoints() {
        canvaFrame.setVisible(true);
        mainFrame.getDrawPointsButton().setEnabled(false);
    }

    public void setData() {
        tspSolver.setStartIndex((Integer) mainFrame.getStartPointIdSpinner().getValue());
        tspSolver.setPopulationSize((Integer) mainFrame.getPopulationSizeSpinner().getValue());
        tspSolver.setStopCondition((Integer) mainFrame.getStopConditionSpinner().getValue());
        tspSolver.setMutationPickProbability((Double) mainFrame.getSelectMutationPorbabilitySpinner().getValue());
        tspSolver.setCrossingPickProbability((Double) mainFrame.getSelectBreedPorbabilitySpinner().getValue());

        mainFrame.getSetButton().setEnabled(false);
        mainFrame.getRunButton().setEnabled(true);
    }

    public void setPoints() {
        double[][] distances = canva.getEuclideanDistances();

        tspSolver.setDistances(distances);
        mainFrame.exposeSettings();
        canvaFrame.getSetButton().setEnabled(false);
        stream(canva.getMouseListeners()).forEach(canva::removeMouseListener);
    }

    public void runAlgorithm() {
        canva.clearLines();
        tspSolver.run();

        EventQueue.invokeLater(() -> {
            canva.drawLine(tspSolver.getBestIndividual().getGenes());
        });
    }
}
