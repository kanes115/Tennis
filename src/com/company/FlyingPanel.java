package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import static java.lang.Math.abs;

public class FlyingPanel extends JPanel implements KeyListener {

    public static final int WIDTH=800;          //rozmiary panelu
    public static final int HEIGHT=600;

    private volatile boolean ifPause=true;
    JLabel pause = new JLabel("PAUSE");         //tekst pauzy

    //piłka i rakietki  ----------------------------------------
    Ball ball = new Ball();
    Racket racket1 = new Racket(60, HEIGHT/2, Color.PINK);
    Racket racket2 = new Racket(WIDTH-60, HEIGHT/2, Color.BLUE);
    //-----------------------------------------------------------


    //punktacja   ---------------------------------
    private int maxScore;
    private int actScorePl1=0, getActScorePl2=0;
    //---------------------------------------------

    //boole mówiące o tym, co jest wciśnięte --------------------
    boolean r1down=false, r1up=false, r2down=false, r2up=false;
    //-----------------------------------------------------------

    //Wyświetlanie wyniku
    private JLabel scorePl1, scorePl2;


    public FlyingPanel(boolean botPlays, int difficulty, int maxScore){
        super();

        setFocusable(true);
        addKeyListener(this);           //panel słucha akcji

        setPreferredSize(new Dimension(WIDTH, HEIGHT));     //wielkość panelu i kolor
        setBackground(Color.BLACK);

        this.maxScore=maxScore;         //Przypisanie maksymalnej wartości punktów

        System.out.print(maxScore);

        add(pause);                     //tekst pauzy
        pause.setVisible(true);

        new BallAnimator().start();         //wątki poruszające piłką i rakietkami
        new RacketAnimator(botPlays).start();
        if(botPlays) new RacketAnimatorBoot(botPlays, difficulty).start();

        //wyświetlanie wyniku
        Font scoreFont = new Font("Arial", Font.BOLD, 15);
        scorePl1 = new JLabel("0");
        scorePl2 = new JLabel("0");
        scorePl1.setLocation(WIDTH/4, 50);
        scorePl2.setLocation(3*WIDTH/4, 50);
        add(scorePl1);
        add(scorePl2);

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        racket1.paint(g);   //rysujemy rakietkę
        racket2.paint(g);   //rysujemy rakietkę
        ball.paint(g);      //rysujemy piłkę
    }

    public void keyTyped(KeyEvent e){}

    public void keyPressed(KeyEvent e){

        if(e.getKeyCode()==KeyEvent.VK_UP) r1up=true;
        if(e.getKeyCode()==KeyEvent.VK_DOWN) r1down=true;

        if(e.getKeyCode()==KeyEvent.VK_W) r2up=true;
        if(e.getKeyCode()==KeyEvent.VK_S) r2down=true;

        if(e.getKeyCode()==KeyEvent.VK_ESCAPE) System.exit(0);

    }
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_UP) r1up=false;
        if(e.getKeyCode()==KeyEvent.VK_DOWN) r1down=false;

        if(e.getKeyCode()==KeyEvent.VK_W) r2up=false;
        if(e.getKeyCode()==KeyEvent.VK_S) r2down=false;
    }



    //RAKIETA-----------
    class Racket{
        private int x;
        private int y;
        static final int SIZE = 20;     //połowy rozmiarów rakiety
        static final int THICK = 6;
        Color racketColor;

        public Racket(int x, int y, Color color){
            this.x=x;
            this.y=y;
            racketColor=color;
        }

        public int getX(){return x;}
        public int getY(){return y;}


        public void moveUp(){ y-=20;}

        public void moveDown(){ y+=20;}

        void paint(Graphics g){
            g.setColor(racketColor);
            g.fillRect(x-THICK, y-SIZE, 2*THICK, 2*SIZE);
        }
    }
    //----RAKIETA--------


    //PIŁKA-------------
    class Ball{
        static final int RADIUS=8;
        private int x=FlyingPanel.WIDTH/2, y=FlyingPanel.HEIGHT/2;
        private int vx=3; //Racket.THICK;
        private int vy=1;     //Racket.THICK;

        public void accelerate(){

            if(vx>0) vx+=1;
            else vx-=1;
            if(vy>0) vy+=1;
            else vy-=1;
        }

        public void moveX(){
            x+=vx;
        }

        public void moveY(){
            y+=vy;
        }

        public int getX(){ return x;}
        public int getY(){ return y;}

        public void setX(int x){this.x = x;}
        public void setY(int y){this.y = y;}

        public void reverseSpeedX(){vx*=-1;}
        public void reverseSpeedY(){vy*=-1;}

        public void setRandomV(){
            Random randGen = new Random();

            vy= abs(randGen.nextInt()%3)+2;
            vx= vy + abs(randGen.nextInt()%6)+2;

            if(randGen.nextBoolean()) vx*=-1;
            if(randGen.nextBoolean()) vy*=-1;

        }

        void paint(Graphics g){
            g.setColor(Color.WHITE);
            g.fillOval(x-RADIUS, y-RADIUS, 2*RADIUS, 2*RADIUS);
            g.setColor(Color.BLACK);
            g.drawOval(x-RADIUS, y-RADIUS, 2*RADIUS, 2*RADIUS);
        }

    }
    //-----PIŁKA-------

//ANIMACJA PIŁKI





    class BallAnimator extends Thread implements KeyListener {


        public BallAnimator(){
            addKeyListener(this);
        }

        private void gameReset(){
            ball.setX(WIDTH/2);
            ball.setY(HEIGHT/2);
            ball.setRandomV();
        }


        private void wait(int time){
            try {
                sleep(time);
            }catch(InterruptedException e) {
                System.out.print("Error while moving the ball");
            }
        }

        @Override
        public void run(){
            while(true){
                if(ifPause) continue;

                wait(12);
                ball.moveX();
                ball.moveY();

                Dimension fieldSize = getSize();
                //ODBICIA
                //od lewej ściany - koniec gry
                if(ball.getX()<Ball.RADIUS){
                  if(getActScorePl2==maxScore){
                      new GameOverFrame(2);
                      break;
                  }else{
                      getActScorePl2++;
                      //TO DO trzeba zrobić reset piłki w tym miejscu
                      gameReset();

                  }

                }
                //od prawej ściany - koniec gry
                if(ball.getX()>fieldSize.width-Ball.RADIUS){
                    if(actScorePl1==maxScore){
                        new GameOverFrame(1);
                        break;
                    }else{
                        actScorePl1++;
                        //TO DO trzeba zrobić reset piłki w tym miejscu
                        gameReset();
                    }

                }
                //od górnej ściany
                if(ball.getY()<Ball.RADIUS){
                    ball.setY(Ball.RADIUS);
                    ball.reverseSpeedY();
                }
                //od dolnej ściany
                if(ball.getY()>fieldSize.height-Ball.RADIUS){
                    ball.setY(fieldSize.height-Ball.RADIUS);
                    ball.reverseSpeedY();
                }
                //Odbijanie od rakietek
                //racket1
                if(ball.getX()-Ball.RADIUS<=racket1.getX()+Racket.THICK && ball.getX()-Ball.RADIUS>=racket1.getX()-Racket.THICK && ball.getY()<=racket1.getY()+Racket.SIZE && ball.getY()>=racket1.getY()-Racket.SIZE){
                    ball.setX(racket1.getX()+Ball.RADIUS+Racket.THICK);
                    ball.reverseSpeedX();
                }
                //racket2
                if(ball.getX()+Ball.RADIUS>=racket2.getX()-Racket.THICK && ball.getX()+Ball.RADIUS>=racket2.getX()+Racket.THICK && ball.getY()<=racket2.getY()+Racket.SIZE && ball.getY()>=racket2.getY()-Racket.SIZE){
                    ball.setX(racket2.getX()-Ball.RADIUS-Racket.THICK);
                    ball.reverseSpeedX();
                }

                updateUI();
                repaint();
            }
        }

        //KEYLISTENER--------------
        public void keyTyped(KeyEvent e){}

        public void keyPressed(KeyEvent e){
            if(e.getKeyCode()==KeyEvent.VK_P){
                if(ifPause){
                    ifPause=false;
                    pause.setVisible(false);
                }
                else{
                    ifPause=true;
                    pause.setVisible(true);

                }
            }
        }
        public void keyReleased(KeyEvent e){

        }
        //KEYLISTENER--------------
    }

    class RacketAnimator extends Thread {

        private boolean botPlays;

        public RacketAnimator(boolean botPlays){
            super();
            this.botPlays=botPlays;
        }

        private void wait(int time){
            try {
                sleep(time);
            }catch(InterruptedException e) {
                System.out.print("Error while moving the ball");
            }
        }

        @Override
        public void run(){
            while(true){
                if(ifPause) continue;
                wait(50);
                if(r1up) racket1.moveUp();
                if(r1down) racket1.moveDown();
                if(r2up && !botPlays) racket2.moveUp();
                if(r2down && !botPlays) racket2.moveDown();
                updateUI();
                repaint();
            }
        }

    }

    class RacketAnimatorBoot extends RacketAnimator {

        private int maxReactionTime;
        private Random randGen = new Random();

        public RacketAnimatorBoot(boolean botPlays, int difficulty){
            super(botPlays);
            if(difficulty == 1) maxReactionTime = 140;
            if(difficulty == 2) maxReactionTime = 50;
            if(difficulty == 3) maxReactionTime = 20;
        }

        @Override
        public void run(){
            while(true){
                if(ifPause) continue;
                super.wait(abs(randGen.nextInt()%maxReactionTime));

                int yBall = ball.getY();
                int yRacket = racket2.getY();
                if(abs(yRacket-yBall)<Racket.SIZE+15) continue;
                if(yBall-yRacket<0) racket2.moveUp();
                if(yBall-yRacket>0) racket2.moveDown();

                updateUI();
                repaint();
            }
        }
    }


}
