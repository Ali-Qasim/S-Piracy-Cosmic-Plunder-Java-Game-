import util.Vector2D;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static util.Constants.*;
import static util.SoundManager.*;

public class Cannonball extends Sprite{

    public int powerLvl;

    BufferedImage base;
    BufferedImage empowered;
    Ship ship;

    Clip fired;

    public Cannonball(Double angle, Game game) {

        super(game);

        this.ship = game.ship;
        this.powerLvl = ship.flame? 2: 1;


        Vector2D origin = ship.center;

        coords.set(origin.x, origin.y);
        yVel = VEL*Math.sin(angle);
        xVel = VEL*Math.cos(angle);

        try {
            base = ImageIO.read(new File("images/bullet.png"));
            empowered = ImageIO.read(new File("images/bulletfire.png"));
            this.image = base;

            fired = getClip("sounds/Cannon2.wav");

        } catch (IOException e) {
            System.out.println("Cannonball assets missing!");
            //e.printStackTrace();
        }
        play(fired);
    }

    @Override
    public void update() {
        super.update();
        if(!onScreen()){
            game.dead.add(this);
        }
        if(powerLvl>1){
            image = empowered;
        }
        transform.set(xVel, yVel);
        coords.add(transform);
    }

    @Override
    public void animate() {}

    @Override
    public void collision(Game game) {}
}
