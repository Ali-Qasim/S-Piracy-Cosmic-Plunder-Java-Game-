import util.Vector2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static util.Constants.*;

public class AlienFire extends Sprite{

    BufferedImage base;
    Alien e;

    public AlienFire(Double angle, Game game, Alien e) {

        super(game);

        this.e = e;

        Vector2D origin = e.center;

        coords.set(origin.x, origin.y);

        yVel = VEL*Math.sin(angle);
        xVel = VEL*Math.cos(angle);

        try {
            base = ImageIO.read(new File("images/alienbullet.png"));
            this.image = base;

            this.radius = image.getHeight()/2;

        } catch (IOException a) {
            System.out.println("Enemy Image does not exist!");
            //e.printStackTrace();
        }
    }

    @Override
    public void update() {
        super.update();
        if(!onScreen()){
            game.dead.add(this);
        }
        transform.set(xVel, yVel);
        coords.add(transform);
    }

    @Override
    public void animate() {}

    @Override
    public void collision(Game game) {
        if (overlap(game.ship)){

            //damage ship
            game.ship.damage();
        }
    }
}
