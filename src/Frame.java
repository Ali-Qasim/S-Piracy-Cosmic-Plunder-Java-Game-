import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;

import static util.Constants.*;

public class Frame extends JComponent {

    private final Game game;
    private final Houston houston;
    JFrame window;

    BufferedImage background;

    String message;

    boolean gameOver = false;

    public Frame(Game game) {

        this.game = game;
        this.houston = game.houston;

        //case 4: //GAME OVER
        switch (houston.level) {
            case 1 -> {
                background = L1;
                message = "LEVEL 1";
            }
            case 2 -> {
                background = L2;
                message = "LEVEL 2";
            }
            case 3 -> {
                background = L3;
                message = "LEVEL 3";
            }
        }

        window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(SCRWIDTH, SCRHEIGHT));
        window.setResizable(false);
        window.setTitle("S'Piracy: Cosmic Plunder! 1902914");

        window.add(this);

        EventHandler e = new EventHandler(game);

        window.addMouseListener(e);
        window.addKeyListener(e);

        window.pack();
        window.setVisible(true);

    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (houston.level < 4 && houston.lives > 0) {
            if (!houston.enter) {  //start screen
                gameStart(g2);
            } else {
                if (houston.newlevel) {
                    //case 4: //GAME OVER
                    switch (houston.level) {
                        case 1 -> {
                            g2.setColor(Color.BLACK);
                            background = L1;
                            message = "LEVEL 1";
                        }
                        case 2 -> {
                            g2.setColor(new Color(50, 168, 82));
                            background = L2;
                            message = "LEVEL 2";
                        }
                        case 3 -> {
                            g2.setColor(new Color(122, 83, 58));
                            background = L3;
                            message = "LEVEL 3";
                        }
                    }
                    g2.fillRect(0, 0, SCRWIDTH, SCRHEIGHT);
                    g2.setFont(BASE);
                    g2.setColor(Color.white);
                    g2.drawString(message, SCRWIDTH / 2 - 30, SCRHEIGHT / 2);
                } else {
                    levels(g2);
                }
            }
        } else {
            try {
                gameOver(g2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void drawUI(Graphics2D g2){
        g2.setFont(UI);
        message ="Plunder: "+houston.score+"   Lives: "+houston.lives+"   Shields: "+houston.shields;
        g2.drawString(message,10, (SCRHEIGHT - 50));


        if (houston.shields>0 && !houston.game.ship.shield) {    //prompt to activate shields if have, but not active
            Color color = g2.getColor();

            g2.setColor(Color.orange);
            g2.setFont(UI);
            message = "Press [SPACE] to activate shield";
            g2.drawString(message, SCRWIDTH/2-80, 15);

            g2.setColor(color); //reset color
        }
    }

    private void levels(Graphics2D g2){
        //Create background
        g2.drawImage(background, 0, 0, null);


        for (Sprite s: game.cannonballs){   //ship bullets
            s.drawSprite(g2,null);
            s.update();
        }

        for (Alien s: game.enemies){   //enemies
            s.drawSprite(g2,null);
            s.update();
        }

        for (AlienFire s: game.alienFires){   //enemies
            s.drawSprite(g2,null);
            s.update();
        }

        Ship ship = game.ship;
        ship.drawSprite(g2, null); //ship
        ship.update();

        for (Drop s: game.drops){   //drops
            s.drawSprite(g2, null);
            s.update();
        }

        g2.setColor(Color.WHITE);
        drawUI(g2);
    }

    private void gameStart(Graphics2D g2){
        String s1;
        String s2;

        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCRWIDTH, SCRHEIGHT);
        message = "S'Piracy: Cosmic Plunder!";
        s1 = "There be treasure in these starry seas!";
        s2 = "Use [WASD] or [Arrows] to move, and [Mouse] to aim and shoot";

        g2.setColor(Color.orange);
        g2.setFont(TITLE);
        g2.drawString(message,SCRWIDTH/2-225, SCRHEIGHT/2-200);
        g2.setColor(Color.white);
        g2.setFont(BASE);
        g2.drawString(s1,SCRWIDTH/2-170, SCRHEIGHT/2-130);
        g2.setFont(DESC);
        g2.drawString(s2,SCRWIDTH/2-210, SCRHEIGHT/2-100);

        g2.drawImage(D1, SCRWIDTH/2-D1.getWidth()-100, SCRHEIGHT/2-70, null);
        g2.drawString("Triple shot",SCRWIDTH/2-60, SCRHEIGHT/2-70+ D1.getHeight()/2);

        g2.drawImage(D2, SCRWIDTH/2-D2.getWidth()-100, SCRHEIGHT/2-20, null);
        g2.drawString("+1 shield",SCRWIDTH/2-60, SCRHEIGHT/2-20+ D2.getHeight()/2);

        g2.drawImage(D3, SCRWIDTH/2-D3.getWidth()-100, SCRHEIGHT/2+30, null);
        g2.drawString("Double damage",SCRWIDTH/2-60, SCRHEIGHT/2+30+ D3.getHeight()/2);

        g2.drawImage(D4, SCRWIDTH/2-D4.getWidth()-100, SCRHEIGHT/2+80, null);
        g2.drawString("Restores health",SCRWIDTH/2-60, SCRHEIGHT/2+80+ D4.getHeight()/2);

        g2.drawImage(D0, SCRWIDTH/2-D0.getWidth()-100, SCRHEIGHT/2+130, null);
        g2.drawString("+200 plunder",SCRWIDTH/2 - 60, SCRHEIGHT/2+130+ D0.getHeight()/2);

        g2.setColor(Color.orange);
        g2.drawString("Press [Space] to start", SCRWIDTH/2-75, SCRHEIGHT - 70);
    }

    private void gameOver(Graphics2D g2) throws FileNotFoundException {
        String s1;
        int s1x;
        String s2;
        int s2x;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, SCRWIDTH, SCRHEIGHT);
        message = "GAME OVER";

        Color theme = houston.lives <= 0? Color.red: Color.green;

        if (houston.lives <= 0){
            s1 = "YOU DIED";
            s1x = 50;
        }
        else {
            s1 = "A GOOD DAY'S HAUL!";
            s1x = 110;
        }

        s2 = "Your Score: "+houston.score;
        s2x = 70;

        g2.setColor(theme);
        g2.setFont(TITLE);
        g2.drawString(message,SCRWIDTH/2-115, SCRHEIGHT/2-70);
        g2.setFont(BASE);
        g2.drawString(s1,SCRWIDTH/2-s1x, SCRHEIGHT/2-25);
        g2.drawString(s2,SCRWIDTH/2-s2x, SCRHEIGHT/2+10);

        if(!gameOver) {
            houston.updateHighScores();
            gameOver = true;
        }

        Integer[] highScores = Houston.getHighScores(); // populate array from file

        boolean hs = true;
        for (int i = 0; i < highScores.length; i++) { //display list of high-scores
            if (highScores[i] == houston.score && hs) {
                g2.setColor(theme);
                hs = false;
            } else g2.setColor(Color.white);
            g2.drawString(Integer.toString(highScores[i]), SCRWIDTH/2-4, (SCRHEIGHT / 2)  +20*i + 50);
        }
        g2.setColor(Color.white);
        g2.drawString("Press [Space] to reset", SCRWIDTH/2-80, 40);
    }
}
