package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MenuPanel extends JPanel implements ActionListener, ChangeListener {

    private JButton startButt;
    private JButton endButt;
    private boolean botPlays=false;

    private int difficulty=2;         //1 - easy, 2 - normal, 3 - hard
    private JButton diff;

    private JCheckBox bot;

    private int maxScore=10;

    //labele informacji
    JLabel info = new JLabel("Player 1: ");
    JLabel info3 = new JLabel("arrow keys");
    JLabel info4 = new JLabel("Player 2: ");
    JLabel info5 = new JLabel("w - up, s - down");
    JLabel info2 = new JLabel("p - pause");

    //
    private JSlider scoreLimitSlider;
    private JLabel limitInfo2;


    public MenuPanel(){
        super();

        setPreferredSize(new Dimension(190, 300));
        setLocation(500, 500);
//        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));


        //Przyciski główne obsługi gry
        startButt = new JButton("Play");
        endButt = new JButton("End");
        add(startButt);
        add(endButt);
        startButt.addActionListener(this);
        endButt.addActionListener(this);
        //.................................


        //Informacje o grze w menu głównym


        Font font = new Font("Arial", Font.ITALIC, 12);

        info2.setFont(font);
        info3.setFont(font);
        info5.setFont(font);


        add(info);
        add(info3);
        add(info4);
        add(info5);
        add(info2);
        //..............................

        //Checkbox czy gramy z botem
        bot = new JCheckBox("One player version");
        bot.addActionListener(this);
        add(bot);
        //..............................

        //Przycisk poziomu trudności
        diff = new JButton("level: Normal");
        add(diff);
        diff.addActionListener(this);
        diff.setVisible(false);
        //................................

        //slider ustawiający limit punktów
        JLabel limitInfo = new JLabel("Score limit:");
        add(limitInfo);
        scoreLimitSlider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);
        scoreLimitSlider.addChangeListener(this);
        add(scoreLimitSlider);
        limitInfo2 = new JLabel("10");
        add(limitInfo2);



    }

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == endButt)
            System.exit(0);
        if(source == startButt) {
            System.out.print("maxScore"+String.valueOf(maxScore));
            new FlyingFrame(botPlays, difficulty, maxScore);
        }
        if(source == bot){
            if(botPlays) {
                botPlays = false;
                diff.setVisible(false);
                info4.setVisible(true);
                info5.setVisible(true);
            }else{
                botPlays=true;
                diff.setVisible(true);
                info4.setVisible(false);
                info5.setVisible(false);
            }
        }
        if(source == diff){
            if(difficulty == 1){
                difficulty++;
                diff.setText("level: Normal");
            }
            else if(difficulty == 2){
                difficulty++;
                diff.setText("level: Hard");
            }
            else if(difficulty == 3){
                difficulty=1;
                diff.setText("level: Easy");
            }
        }

    }

    public void stateChanged(ChangeEvent e){
        JSlider source = (JSlider) e.getSource();
        limitInfo2.setText(String.valueOf(source.getValue()));
        if(!source.getValueIsAdjusting()){
            maxScore=source.getValue();
            System.out.print("przypisana wartosc: ");
            System.out.println(maxScore);
        }
    }
}
