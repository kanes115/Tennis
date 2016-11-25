package com.company;

import javax.swing.*;

public class FlyingFrame extends JFrame {

    public FlyingFrame(boolean botPlays, int difficulty, int maxScore){
        super("Flying Object");

        JPanel mainWindow = new FlyingPanel(botPlays, difficulty, maxScore);
        add(mainWindow);



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);
        pack();


        setVisible(true);


    }
}
