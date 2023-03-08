import javax.sound.sampled.Clip;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static util.Constants.*;
import static util.SoundManager.play;

public class Houston {  //What else would the control center for a space-themed game be called?

    Game game;
    Boolean started;
    int score;
    int level;
    int lives;
    boolean newlevel;
    int shields;
    boolean enter;

    long levelStart;
    Clip levTheme;
    JFrame window;

    public Houston(){

        shields = 2;
        started = true;
        score = 0;
        lives = SHIPHEALTH;
        level = 1;
        newlevel = true;

        window = new JFrame();
    }

    public void initialise() throws IOException, InterruptedException {
        enter = level != 1;

        game.cannonballs = new ArrayList<>();
        game.enemies = new ArrayList<>();
        game.dead = new ArrayList<>();
        game.alienFires = new ArrayList<>();
        game.drops = new ArrayList<>();
        game.reserves = new ArrayList<>();

        game.ship = new Ship(game);


        int[] lev;
        switch (level) {
            case 1 -> {
                lev = new int[]{1, 5, 2};
                levTheme = LEV1;
            }
            case 2 -> {
                lev = new int[]{2, 3, 5};
                levTheme = LEV2;
            }
            default -> {
                lev = new int[]{2, 3, 4};
                levTheme = LEV3;
            }
        }
        int id;

        for (int i = 0; i < DIFFICULTY*level-(level-1); i++) {
            id = lev[game.randy.nextInt(lev.length)];
            Alien al = new Alien(id, game);
            al.centerAt(750+game.randy.nextInt(100), 70+game.randy.nextInt(SCRHEIGHT-200));
            game.reserves.add(al);
        }
        if(level>1) {
            for (int i = 0; i < level - 1; i++) {
                id = 0;
                Alien al = new Alien(id, game);
                al.centerAt(750 + game.randy.nextInt(100), 70 + game.randy.nextInt(SCRHEIGHT - 200));
                game.reserves.add(al);
            }
        }

        for (int i = 0; i < DIFFICULTY; i++) { //wave 1

            Alien en = new Alien(1+game.randy.nextInt(5), game);
            en.centerAt(700+game.randy.nextInt(100), 70+game.randy.nextInt(SCRHEIGHT-200));
            game.enemies.add(en);
        }

        game.frame = new Frame(game); //FRAME, NOT JFRAME
    }

    public void control() throws IOException, InterruptedException {

        play(START);

        if(level == 1){
            while (!enter){
                game.frame.repaint();
                TimeUnit.MILLISECONDS.sleep(DT);
            }
        }
        START.stop();
        play(levTheme);
        do {
            TimeUnit.MILLISECONDS.sleep(DT);

            for (Sprite ded : game.dead) {
                if (game.cannonballs.contains(ded)) game.cannonballs.remove(ded);
                else if (game.enemies.contains(ded)) game.enemies.remove(ded);
                else if (game.alienFires.contains(ded)) game.alienFires.remove(ded);
                else if (game.drops.contains(ded)) game.drops.remove(ded);
                else game.ship.dead = true; //ship dies in dead?
            }
            game.dead.clear();

            if(game.enemies.size()<DIFFICULTY){
                if (!game.reserves.isEmpty()){
                    for (int i = 0; i < DIFFICULTY; i++) {
                        game.enemies.add(game.reserves.get(0));
                        game.reserves.remove(0);
                    }
                }
            }

            game.frame.repaint();

            if (newlevel) {
                play(LEVUP);
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                    enter = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                newlevel = false;
                levelStart = System.currentTimeMillis();
                if(level>1) {
                    game.frame.window.dispose();
                    new Game(this);
                }
            }

            if (allEmpty()) {
                if(game.drops.size()>0){
                    for(Drop d: game.drops){
                        game.ship.drop(d.id);
                        score += 2;
                        play(d.collect);
                        game.dead.add(d);
                    }
                } else {
                    newlevel = true;
                    levTheme.stop();
                    level++;
                }
            }

        } while (level < 4 && lives>0);

        levTheme.stop();

        enter = false;
        Clip play = level==4 ? WIN : LOSE;
        play(play);    //play win or lose sound depending on what happened
        do {
            game.frame.repaint();
            TimeUnit.MILLISECONDS.sleep(100);
        } while (!enter);
        play.stop();
        game.frame.window.dispose();
        new Game(new Houston());
    }

    public boolean allEmpty(){
        return game.enemies.size() == 0 && game.dead.size() ==0;
    }


    //High score methods are the same as a previous assignment because they are meant to do the same thing
    public void updateHighScores(){
        try {
            Integer[] scores = getHighScores(); //Populate array from file

            for (int i = 0; i < 5  ; i++) {

                Arrays.sort(scores); //sort array in ascending order so that lowest high-score is replaced first
                if (score > scores[i]) {
                    scores[i] = score;
                    break;
                }
            }
            Arrays.sort(scores, Collections.reverseOrder()); // rearrange scores by highest-first

            FileWriter writer = new FileWriter("src/util/highscores.txt");

            for(int highSc: scores){
                writer.write("\n"+highSc);
            }
            writer.close();

        } catch (Exception e) { // if file not found

            File highScores = new File("src/util/highscores.txt");

            try {

                FileWriter writer = new FileWriter(highScores);

                writer.write("\n"+score);
                writer.close();

            } catch (IOException ioException) {

                ioException.printStackTrace();

            }
        }
    }

    static Integer[] getHighScores() throws FileNotFoundException {

        Integer[] scores = {0,0,0,0,0}; // initialise array so that empty slots are 0
        Scanner input = new Scanner(new File("src/util/highscores.txt"));

        input.nextLine(); // skip first line

        for (int i = 0; input.hasNext(); i++) {
            scores[i] = Integer.parseInt(input.nextLine());
        }
        input.close();

        return scores;
    }
}
