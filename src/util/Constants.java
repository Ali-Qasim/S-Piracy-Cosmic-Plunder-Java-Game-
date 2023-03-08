package util;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import  static util.SoundManager.*;

public class Constants {
    public static final int FPS = 60;// frames per second
    public static final long DT = 1000/FPS;  // delay in milliseconds
    public static final int SCRWIDTH = 1000;
    public static final int SCRHEIGHT = 600;

    public static final Double VLIM = 3.0;  //spaceship speed limit
    public static final Double VEL = 10.0;  //ship and enemy cannon speed
    public static final long ENEMYDELAY = 1500;   //Frequency of enemy shots

    public static final int MAXSHIELDS = 3;     //Max shields at a time

    public static final long SHIELDURATION = 10000;
    public static final long FLAMEDURATION = 5000;
    public static final long TRIPLEDURATION = 3000;

    public static final int SHIPHEALTH = 5;    //Max ship health

    public static final int ENEMYHEALTH = 4;
    public static final int ENEMY2HEALTH = 8;
    public static final int ENEMY3HEALTH = 7;
    public static final int ENEMY4HEALTH = 2;
    public static final int ENEMY5HEALTH = 15;
    public static final int BOSSHEALTH = 25;

    public static final int HIT = 3;    //Score penalty for getting hit
    public static final int DIFFICULTY = 3; //Controls difficulty of spawn patterns

    public static final Font HEALTHBAR = new Font(Font.SERIF, Font.PLAIN, 8);
    public static final Font BASE = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    public static final Font TITLE = new Font(Font.SANS_SERIF, Font.ITALIC, 40);
    public static final Font DESC = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
    public static final Font UI = new Font(Font.SANS_SERIF, Font.BOLD, 10);

    public static BufferedImage L1;
    public static BufferedImage L2;
    public static BufferedImage L3;

    public static BufferedImage D1;
    public static BufferedImage D2;
    public static BufferedImage D3;
    public static BufferedImage D4;
    public static BufferedImage D0;

    public static Clip START;
    public static Clip LOSE;
    public static Clip WIN;
    public static Clip LEV1;
    public static Clip LEV2;
    public static Clip LEV3;
    public static Clip LEVUP;
    static {
        try {
            L1 = ImageIO.read(new File("images/space.png"));
            L3 = ImageIO.read(new File("images/marslevel.png"));
            L2 = ImageIO.read(new File("images/greenlevel.png"));

            D1 = ImageIO.read(new File("images/coin.png"));
            D2 = ImageIO.read(new File("images/shield.png"));
            D3 = ImageIO.read(new File("images/flame.png"));
            D4 = ImageIO.read(new File("images/health.png"));
            D0 = ImageIO.read(new File("images/chest.png"));

            START = getClip("sounds/start.wav");
            LOSE = getClip("sounds/lose.wav");
            WIN = getClip("sounds/win.wav");
            LEV1 = getClip("sounds/level1.wav");
            LEV2 = getClip("sounds/level2.wav");
            LEV3 = getClip("sounds/level3.wav");
            LEVUP = getClip("sounds/levelup.wav");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
