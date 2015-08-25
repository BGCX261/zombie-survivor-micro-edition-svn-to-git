package com.ZombieInvader.javafiles;
import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

/*
 * This class is designed to create a Hunter Sprite
 * This sprite is the only interactive sprite in the game
*/

public class DeathGhoulSprite extends Sprite {
private static final int DEATHGHOUL_FRAME_WIDTH = 80;
private static final int DEATHGHOUL_FRAME_HEIGHT = 69;
int xdelta = 0;
int ydelta = getY();
private static Random r;
int ghoulSequence[] = {7,8,9,10,11,12,13,14};

  public DeathGhoulSprite() throws IOException  {
      super(Image.createImage("/res/deathghoul.png"),
             DEATHGHOUL_FRAME_WIDTH, DEATHGHOUL_FRAME_HEIGHT);
     int gf[] = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
     setFrameSequence(gf);
  }
    //Moves the DeathGhoul Randomly rerunning position 0 everytime he spawns
    public void move() {
      nextFrame();
          if (r == null) {
          r = new Random();
      }
      int RandomXAxis = r.nextInt(60) + 1;
      if (ydelta < 30) {
          ydelta = ydelta + 2;
      } else if (ydelta == 30) {
          ydelta = -6;
          xdelta = xdelta +1;
          setPosition(RandomXAxis, -6);
          setFrame(0);
      }
      if (xdelta == 5) {
          xdelta = 0;
      }
      /*System.out.println(this.getX() + ", " + this.getY());*/
      move(xdelta, ydelta);
    }
}