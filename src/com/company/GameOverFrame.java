package com.company;


import javax.swing.*;

public class GameOverFrame extends JFrame {

    public GameOverFrame(int whoWon){
        super("Game Over!");

        JPanel mainWindow = new GameOverPanel(whoWon);
        add(mainWindow);
        setLocation(1920/2, 1080/2);



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);
        pack();


        setVisible(true);


    }
}
