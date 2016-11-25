package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverPanel extends JPanel implements ActionListener {

    public static final int WIDTH=500;
    public static final int HEIGHT=300;
    private JButton endingButt;

    public GameOverPanel(int whoWon){

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        if(whoWon==1) setBackground(Color.MAGENTA);
        else setBackground(Color.BLUE);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        Font font = new Font("Arial", Font.BOLD, 40);


        JLabel tellingWinner = new JLabel("Player "+String.valueOf(whoWon)+ " wins!");
        tellingWinner.setFont(font);

        endingButt = new JButton("OK");
        add(endingButt);
        endingButt.setHorizontalAlignment(JLabel.CENTER);

        endingButt.addActionListener(this);


        add(tellingWinner);

    }
    @Override
    public void actionPerformed(ActionEvent evt){
        System.exit(0);
    }
}
