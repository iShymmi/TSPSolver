package main.java.testingApp;

import main.java.algorithm.TSPSolver;

import java.awt.*;

public class AppMain {

    public static void main(String... args){
        MainFrame mainFrame = new MainFrame();
        TSPSolver tspSolver = new TSPSolver();
        CanvaFrame canvaFrame = new CanvaFrame();
        AppController appController = new AppController(mainFrame, canvaFrame, tspSolver);

        EventQueue.invokeLater(appController::run);
    }
}
