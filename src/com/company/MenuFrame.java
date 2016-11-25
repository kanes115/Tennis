package com.company;


import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuFrame extends JFrame{



    public MenuFrame(){
        super("Ping Pong Game!");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocation(1920/2, 1080/2);

        JPanel mainWindow = new MenuPanel();
        add(mainWindow);
        pack();

        setVisible(true);

    }

}
