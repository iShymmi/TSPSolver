package testingApp;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private JButton drawPointsButton;
    private JButton setButton;
    private JButton runButton;
    private final JPanel settingsPanel;

    private JSpinner populationSizeSpinner;
    private JSpinner stopConditionSpinner;
    private JSpinner startPointIdSpinner;
    private JSpinner selectMutationPorbabilitySpinner;
    private JSpinner selectBreedPorbabilitySpinner;

    public MainFrame() {
        JPanel titlePanel = drawPointsPanel();
        settingsPanel = createSettingsPanel();

        settingsPanel.setVisible(false);
        add(titlePanel, BorderLayout.NORTH);
        add(settingsPanel, BorderLayout.CENTER);

        pack();
        setAlwaysOnTop(true);
        setMinimumSize(new Dimension(400,100));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel drawPointsPanel (){
        JPanel drawPointsPanel = new JPanel();
        drawPointsButton = new JButton("Draw points");

        drawPointsPanel.add(drawPointsButton);
        drawPointsPanel.setSize(500,200);
        drawPointsPanel.setBorder(BorderFactory.createTitledBorder("Draw points"));

        return drawPointsPanel;
    }

    private JPanel createSettingsPanel(){
        JPanel settingsPanel = new JPanel(new GridLayout(6, 2, 5, 10));

        JLabel populationSizeLabel = new JLabel("Population size: ");
        JLabel stopConditionLabel = new JLabel("Stop contions ( milis ): ");
        JLabel startPointLabel = new JLabel("Start point: ");
        JLabel mutationProbabilityLabel = new JLabel("Mutation probability: ");
        JLabel breedProbabilityLabel = new JLabel("Breed probability: ");

        populationSizeSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));
        stopConditionSpinner = new JSpinner(new SpinnerNumberModel(1,1,Integer.MAX_VALUE,1));
        startPointIdSpinner = new JSpinner(new SpinnerNumberModel(0,0,Integer.MAX_VALUE,1));
        selectMutationPorbabilitySpinner = new JSpinner(new SpinnerNumberModel(0,0,1,0.01));
        selectBreedPorbabilitySpinner = new JSpinner(new SpinnerNumberModel(0,0,1,0.01));

        setButton = new JButton("Set");
        runButton = new JButton("Run");

        runButton.setEnabled(false);

        settingsPanel.add(populationSizeLabel);
        settingsPanel.add(populationSizeSpinner);

        settingsPanel.add(stopConditionLabel);
        settingsPanel.add(stopConditionSpinner);

        settingsPanel.add(startPointLabel);
        settingsPanel.add(startPointIdSpinner);

        settingsPanel.add(mutationProbabilityLabel);
        settingsPanel.add(selectMutationPorbabilitySpinner);

        settingsPanel.add(breedProbabilityLabel);
        settingsPanel.add(selectBreedPorbabilitySpinner);

        settingsPanel.add(setButton);
        settingsPanel.add(runButton);

        settingsPanel.setBorder(BorderFactory.createTitledBorder("Setup"));

        return settingsPanel;
    }

    public void exposeSettings(){
        settingsPanel.setVisible(true);
        this.setSize(400,400);
    }

    public JButton getDrawPointsButton() {
        return drawPointsButton;
    }

    public JButton getSetButton() {
        return setButton;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JSpinner getPopulationSizeSpinner() {
        return populationSizeSpinner;
    }

    public JSpinner getStopConditionSpinner() {
        return stopConditionSpinner;
    }

    public JSpinner getStartPointIdSpinner() {
        return startPointIdSpinner;
    }

    public JSpinner getSelectMutationPorbabilitySpinner() {
        return selectMutationPorbabilitySpinner;
    }

    public JSpinner getSelectBreedPorbabilitySpinner() {
        return selectBreedPorbabilitySpinner;
    }
}
