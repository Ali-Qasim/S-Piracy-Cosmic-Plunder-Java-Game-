import util.Vector2D;


import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;

import static util.Constants.*;
import static util.SoundManager.*;

public class Drop extends Sprite {

    int id;
    BufferedImage image1;

    Clip collect;

    public Drop(Alien e) {
        super(e.game);

        this.id = e.id;

        switch (id) {
            case 1, 5 -> this.image = D1;   //triple
            case 2 -> this.image = D2;      //shield
            case 3 -> this.image = D3;      //flame
            case 4 -> this.image = D4;      //health
            case 0 -> this.image = D0;      //chest
        }

        collect = getClip("sounds/collect.wav");

        Vector2D origin = e.coords;

        coords.set(origin.x, origin.y);

        game.drops.add(this);
    }

    @Override
    public void animate() {}

    @Override
    public void collision(Game game) {
        if (overlap(game.ship)){
            Ship ship = game.ship;

            ship.drop(id);
            houston.score += 5;
            play(collect);
            game.dead.add(this);
        }
    }
}
