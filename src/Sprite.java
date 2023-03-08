import util.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import static util.Constants.*;

public abstract class Sprite extends Image {

    Houston houston;

    int updateCount = 0;

    BufferedImage image;

    Double xAcc = .0;
    Double xVel = .0;
    Double yAcc = .0;
    Double yVel = .0;
    Vector2D coords;
    Vector2D transform;
    Vector2D center = new Vector2D(0, 0);
    int radius;
    boolean hit;
    boolean dead;

    Game game;

    public Sprite(Game game){
        this.coords = new Vector2D(0, 0);
        this.transform = new Vector2D(0, 0);
        this.game = game;
        houston = game.houston;

        hit = false;
        dead = false;
    }



    public void drawSprite(Graphics2D graphics2D, ImageObserver a){graphics2D.drawImage(image, (int)coords.x, (int)coords.y, null);}

    public void update() {

        updateCount++;
        collision(game);
        animate();
        this.center = getCenter();

    }

    public abstract void animate();

    public abstract void collision(Game game);

    public boolean onScreen(){
        return onScreenX((int) coords.x)&&onScreenY((int) coords.y);
    }

    public boolean onScreenX(int x){

        return (x >= 0 && x <= (SCRWIDTH - (image.getWidth() + 10)));
    }

    public boolean onScreenY(int y){
        return y >= 0 && y < (SCRHEIGHT - (image.getHeight() + 35));
    }

    public void centerAt(int x, int y){
        this.coords.set(x- image.getWidth()/2, y - image.getHeight()/2);

        getCenter();
    }

    public Vector2D getCenter(){
        return new Vector2D((coords.x + image.getWidth()/2),(coords.y + image.getHeight()/2));
    }

    protected int getRadius(){
        radius = (int) (Math.hypot(image.getHeight(), image.getWidth())/2);
        return radius;
    }

    public double dist(Sprite other){

        return center.dist(other.center);
    }

    public boolean overlap(Sprite other){return dist(other)<(this.radius+other.radius);}

    public void healthBar(Graphics2D graphics2D, int health, int maxHealth){
        double healthBar = 20*health;
        graphics2D.setColor(healthBar < maxHealth*8 ? Color.RED: Color.GREEN);  //if health less than 40%
        graphics2D.drawRect((int)(coords.x+(image.getWidth()/2)-25),(int)(coords.y - 10), 50, 3);
        graphics2D.fillRect((int)(coords.x+(image.getWidth()/2)-25),(int)(coords.y - 10), (int)(((double) health /maxHealth)*50), 3);
        graphics2D.setFont(HEALTHBAR);
        graphics2D.drawString(Integer.toString((int) healthBar), (int)(coords.x+(image.getWidth()/2)-4), (int)(coords.y - 11));

    }

    @Override
    public int getWidth(ImageObserver observer) {
        return image.getWidth();
    }

    @Override
    public int getHeight(ImageObserver observer) {
        return image.getHeight();
    }

    @Override
    public ImageProducer getSource() {
        return image.getSource();
    }

    @Override
    public Graphics getGraphics() {
        return image.getGraphics();
    }

    @Override
    public Object getProperty(String name, ImageObserver observer) {
        return image.getProperty(name, observer);
    }
}
