import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static util.Constants.SCRWIDTH;

public abstract class Enemy extends Sprite{

    double maxhealth;
    double health;
    BufferedImage image1;
    BufferedImage image2;
    BufferedImage hitImg;
    int hitTick;
    int anim = 0;


    public Enemy(Game game) {
        super(game);
    }

    public abstract void collision(Game game);

    @Override
    public void update() {
        super.update();
        animate();

        if(!onScreenY((int) coords.y)){
            yVel *= -1;
        }

        if(coords.x<0){
            coords.set(SCRWIDTH, coords.y);
        } else if (coords.x>SCRWIDTH) {
            coords.set(0, coords.y);
        }

        transform.set(xVel, yVel);
        coords.add(transform);
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
                anim = updateCount + 10;
            }
        }
    }

    @Override
    public void drawSprite(Graphics2D graphics2D, ImageObserver a) {

        super.drawSprite(graphics2D, a);

        healthBar(graphics2D, (int)health, (int)maxhealth);
    }
}
