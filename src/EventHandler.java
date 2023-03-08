
import util.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static util.Constants.*;

public class EventHandler implements MouseListener, KeyListener {

    Game game;
    Houston houston;
    Ship ship;

    EventHandler(Game game) {
        this.game = game;
        this.houston = game.houston;
        this.ship = game.ship;
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON1 && houston.enter) {
            PointerInfo a = MouseInfo.getPointerInfo();
            Point point = new Point(a.getLocation());
            SwingUtilities.convertPointFromScreen(point, e.getComponent());
            int x=(int) point.getX();
            int y=(int) point.getY();

            Vector2D s = ship.center;
            double angle = Math.atan2(y-s.y, x-s.x);  //calc bearing of click from ship

            if(!houston.newlevel) {
                game.cannonballs.add(new Cannonball(angle, game));

                if (ship.triple) {
                    game.cannonballs.add(new Cannonball(angle - Math.PI / 16, game));
                    game.cannonballs.add(new Cannonball(angle + Math.PI / 16, game));

                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> ship.yAcc += -2.0/DT;//ship.yVel > -2 ? -2.0 / DT : .0;
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> ship.yAcc += 2.0/DT;//ship.yVel < 2 ? 2.0 / DT : .0;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> ship.xAcc += -2.0/DT;//ship.xVel > -2 ? -2.0 / DT : .0;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> ship.xAcc += 2.0/DT;//ship.xVel < 2 ? 2.0 / DT : .0;
            case KeyEvent.VK_SPACE -> {
                if(!houston.enter) {
                    houston.enter = true; //Start game
                }else if(game.houston.shields>0 && System.currentTimeMillis()>game.houston.levelStart+2000){    //2 seconds delay before shield can be activated
                    game.ship.shield = true;
                    game.ship.shieldActivation = System.currentTimeMillis();
                    game.houston.shields--;
                }
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_DOWN, KeyEvent.VK_S -> ship.yAcc = .0;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_RIGHT, KeyEvent.VK_D -> ship.xAcc = .0;
        }
    }

    //unused
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}