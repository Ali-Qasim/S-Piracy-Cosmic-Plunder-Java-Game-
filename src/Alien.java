import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;

import util.Vector2D;
import static util.Constants.*;
import static util.SoundManager.*;

public class Alien extends Enemy{

    int id;
    long prevShot;
    Clip hit;
    Clip death;

    boolean onscreen;

    public Alien(int id, Game game) {
        super(game);
        this.id = id;

        onscreen = false;

        this.xVel = -1.0;
        this.yVel = .0;

        try {

            hit = getClip("sounds/enemyHit.wav");
            death = getClip("sounds/death.wav");

            switch (id) {
                case 1 -> {
                    this.image1 = ImageIO.read(new File("images/aliens/alien.png"));
                    image2 = ImageIO.read(new File("images/aliens/alien1.png"));
                    hitImg = ImageIO.read(new File("images/aliens/alienHit.png"));
                    this.image = image1;
                    health = maxhealth = ENEMYHEALTH;
                }
                case 2 -> {
                    this.image1 = ImageIO.read(new File("images/aliens/blue.png"));
                    image2 = ImageIO.read(new File("images/aliens/blue1.png"));
                    hitImg = ImageIO.read(new File("images/aliens/blueHit.png"));
                    this.image = image1;
                    health = maxhealth = ENEMY2HEALTH;
                }
                case 3 -> {
                    this.image1 = ImageIO.read(new File("images/aliens/jelly.png"));
                    image2 = ImageIO.read(new File("images/aliens/jelly1.png"));
                    hitImg = ImageIO.read(new File("images/aliens/jellyHit.png"));
                    this.image = image1;
                    health = maxhealth = ENEMY3HEALTH;
                }
                case 4 -> {
                    this.image1 = ImageIO.read(new File("images/aliens/red.png"));
                    image2 = ImageIO.read(new File("images/aliens/red1.png"));
                    hitImg = ImageIO.read(new File("images/aliens/redHit.png"));
                    this.image = image1;
                    health = maxhealth = ENEMY4HEALTH;
                }
                case 5 -> {
                    this.image1 = ImageIO.read(new File("images/aliens/yellow.png"));
                    image2 = ImageIO.read(new File("images/aliens/yellow1.png"));
                    hitImg = ImageIO.read(new File("images/aliens/yellowHit.png"));
                    this.image = image1;
                    health = maxhealth = ENEMY5HEALTH;
                }
                case 0 -> {
                    this.image1 = ImageIO.read(new File("images/aliens/bigship.png"));
                    image2 = ImageIO.read(new File("images/aliens/bigship1.png"));
                    hitImg = ImageIO.read(new File("images/aliens/bigshipHit.png"));
                    this.image = image1;
                    health = maxhealth = BOSSHEALTH;
                }
            }

        } catch (IOException e) {
            System.out.println("Enemy assets missing!");
        }
        this.radius = getRadius();
    }

    private void controller(){
        if (System.currentTimeMillis()> prevShot + ENEMYDELAY && onscreen){
            Vector2D s = game.ship.getCenter();

            switch (id){
                case 1: //basic saucer  //forward shots
                case 5: //yellow
                    double angle = -Math.PI;
                    game.alienFires.add(new AlienFire(angle, game, this));
                    break;
                case 2: //blue  //aiming bullets
                    angle = Math.atan2(s.y - center.y, s.x - center.y);  //calc bearing of click from ship
                    game.alienFires.add(new AlienFire(angle, game, this));
                    break;

                case 3: //jelly //rotating shots
                    angle = -Math.PI + updateCount*Math.PI/(6*90);  //rotates by PI/6 each time
                    game.alienFires.add(new AlienFire(angle, game, this));
                    break;

                case 4: //red   //3 shots spread by PI/6
                    angle = -Math.PI;
                    game.alienFires.add(new AlienFire(angle, game, this));
                    game.alienFires.add(new AlienFire(angle+(angle/6), game, this));
                    game.alienFires.add(new AlienFire(angle-(angle/6), game, this));
                    break;

                case 0: //BOSS  //moves toward ship with 4 rotating shots
                    angle = Math.atan2(s.y - center.y, s.x - center.y);  //calc bearing of click from ship
                    yVel = 2*Math.sin(angle);
                    xVel = 2*Math.cos(angle);

                    angle = -Math.PI + updateCount*Math.PI/(6*90);  //shots rotate by PI/6 each time
                    game.alienFires.add(new AlienFire(angle, game, this));
                    game.alienFires.add(new AlienFire(angle + Math.PI, game, this));
                    game.alienFires.add(new AlienFire(angle+Math.PI/2, game, this));
                    game.alienFires.add(new AlienFire(angle-Math.PI/2, game, this));
                    break;
            }
            prevShot = System.currentTimeMillis();
        }
    }

    @Override
    public void collision(Game game) {

        if (updateCount>hitTick && overlap(game.ship)){
            hitTick = updateCount+15;
            health--;
            if(health<=0)game.dead.add(this);

            Ship ship = game.ship;

            //push ship away
            /*if(ship.center.x<center.x){
                ship.xVel = -2.0;
            } else {
                ship.xVel = 2.0;
            }*/
            ship.yVel = 3.0*(ship.center.y<center.y?-1:1);
            ship.xVel = 3.0*(ship.center.x<center.x?-1:1);

            ship.transform.set(ship.xVel, ship.yVel);
            ship.coords.add(ship.transform);

            //damage ship
            ship.damage();
        }
        for (Cannonball s: game.cannonballs) {
            if (overlap(s)) {
                play(hit);
                hitTick = updateCount+7;
                game.dead.add(s);
                health-=s.powerLvl;
                if(health<=0)game.dead.add(this);
            }
        }
        if (game.dead.contains(this)) {
            play(death);
            houston.score += maxhealth;
            if(game.randy.nextBoolean())new Drop(this); //random chance of drop
        }
    }

    @Override
    public void update() {
        if(System.currentTimeMillis()>houston.levelStart+1000) {
            super.update();
            onscreen = onScreen();
            controller();
        }
    }

    @Override
    public void animate() {
        if(hitTick>updateCount){
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
}
