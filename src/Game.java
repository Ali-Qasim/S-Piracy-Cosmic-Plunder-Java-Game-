import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Game {

    Random randy = new Random();
    Houston houston;
    Ship ship;
    List<Alien> reserves;
    List<Alien> enemies;
    List<Cannonball> cannonballs;
    List<AlienFire> alienFires;
    List<Drop> drops;

    List<Sprite> dead;

    Frame frame;

    public Game(Houston houston) throws InterruptedException, IOException {
        this.houston = houston;
        this.houston.game = this;

        houston.initialise();
        houston.control();
    }
    public static void main(String[] args) throws InterruptedException, IOException {
        new Game(new Houston());
    }
}
