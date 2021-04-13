package main.java.testingApp;

import javax.swing.*;
import java.awt.*;

public class CanvaFrame extends JFrame {

    private final Canva canva;
    private JButton setButton;

    public CanvaFrame(){
        JPanel navPanel = createNavPanel();
        canva = new Canva();

        add(navPanel,BorderLayout.NORTH);
        add(canva,BorderLayout.CENTER);

        setMinimumSize(new Dimension(800,800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JPanel createNavPanel() {
        JPanel navPanel = new JPanel();

        setButton = new JButton("Set");

        navPanel.add(setButton);
        navPanel.setBackground(new Color(66, 62, 68));

        return navPanel;
    }

    public JButton getSetButton() {
        return setButton;
    }

    public Canva getCanva() {
        return canva;
    }
}
