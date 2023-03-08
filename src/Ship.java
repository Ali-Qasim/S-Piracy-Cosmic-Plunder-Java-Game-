import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import static util.SoundManager.*;
import static util.Constants.*;

public class Ship extends Sprite{

    int health;

    BufferedImage image1;
    BufferedImage image2;
    BufferedImage hitImg;
    BufferedImage shieldImg;

    boolean shield;
    boolean flame;
    boolean triple;

    Clip hit;

    Clip shielded;

    int hitTick;
    int anim = 0;

    long delay = 0;
    long shieldActivation = 0;
    long flameActivation = 0;
    long tripleActivation = 0;

    public Ship(Game game) {
        super(game);
        houston = game.houston;

        try {
            image1 = ImageIO.read(new File("images/pship.png"));
            image2 = ImageIO.read(new File("images/pship2.png"));
            hitImg = ImageIO.read(new File("images/pshipHit.png"));
            shieldImg = ImageIO.read(new File("images/pshipShield.png"));

            image = image1;

            hit = getClip("sounds/shipHit.wav");
            shielded = getClip("sounds/shielded.wav");

            health = houston.lives;

            radius = getRadius();
            center = getCenter();
        } catch (IOException e) {
            System.out.println("Ship assets missing!");
            e.printStackTrace();
        }
        coords.set(image.getWidth(), SCRHEIGHT/2-image.getHeight());
    }

    public void drop(int id){
        switch (id) {
//yellow
            case 1, 5 -> {
                triple = true;
                tripleActivation = System.currentTimeMillis();
            }
//blue  //shield
            case 2 -> {
                houston.shields += houston.shields < MAXSHIELDS ? 1 : 0;   //
            }
//jelly //flame shot
            case 3 -> {
                flame = true;
                flameActivation = System.currentTimeMillis();
            }
//red   //health
            case 4 -> health += health < SHIPHEALTH? SHIPHEALTH/4: 0;
//BOSS  //Treasure
            case 0 -> houston.score += 200;
        }
    }

    @Override
    public void update(){
        super.update();
        center = getCenter();
        if(System.currentTimeMillis()>flameActivation+FLAMEDURATION){
            flame = false;
        }

        if(System.currentTimeMillis()>tripleActivation+TRIPLEDURATION){
            triple = false;
        }

        if(System.currentTimeMillis()>shieldActivation+SHIELDURATION){
            shield = false;
        }

        houston.lives = health;

        animate();

        if(Math.abs(xVel+xAcc) > VLIM){    //if ship is faster than speed limit
            if(Math.abs(xVel+xAcc)<Math.abs(xVel)) {  //if acceleration is slowing down ship
                xVel+= xAcc;
            }
        } else {    //if ship within allowed speed bounds
            xVel+= xAcc;
        }
        if(Math.abs(yVel+yAcc) > VLIM){    //if ship is faster than speed limit
            if(Math.abs(yVel+yAcc)<Math.abs(yVel)) {  //if acceleration is slowing down ship
                yVel+= yAcc;
            }
        } else {    //if ship within allowed speed bounds
            yVel+= yAcc;
        }

        transform.set(xVel, yVel);

        coords.add(transform);

        if(coords.x<0){
            coords.x = 0;
            xVel = 1.1;
        } else if (coords.x > SCRWIDTH-image.getWidth()-10) {
            coords.x = SCRWIDTH-image.getWidth()-10;
            xVel = 1.1;
        }
        if(coords.y<0){
            coords.y = 0;
            yVel = 1.1;

        } else if (coords.y > SCRHEIGHT-image.getHeight()-35) {
            coords.y = SCRHEIGHT-image.getHeight()-35;
            yVel = -1.1;
        }

    }

    @Override
    public void animate() {
        if(shield){
            image = shieldImg;
        } else if(hitTick>updateCount){
            image = hitImg;
        } else {
            if(anim<updateCount) {
                if (image == image1) {
                    image = image2;
                } else {
                    image = image1;
                }
                anim = updateCount + 5;
            }
        }
    }

    @Override
    public void collision(Game game){}

    public void damage(){
        if(System.currentTimeMillis()>houston.levelStart+3000) { //3 second immunity at level start
            if (shield) {
                play(shielded);
            } else if (System.currentTimeMillis() > delay + 2000) { //2 second immunity after hit
                play(hit);
                hitTick = updateCount + FPS;
                houston.score -= HIT;
                health--;
                delay = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void drawSprite(Graphics2D graphics2D, ImageObserver a) {

        super.drawSprite(graphics2D, a);

        healthBar(graphics2D, health, SHIPHEALTH);
    }
}
