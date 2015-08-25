package com.ZombieInvader.javafiles;

import java.io.IOException;
import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class ZombieSprite extends Sprite {

  private static Random r;

  private static final int ZOMBIE_ANIME_FRAME_WIDTH = 27;
  private static final int ZOMBIE_ANIME_FRAME_HEIGHT = 47;

  /* Loads zombie sprite image and uses the animation constructor method */
    /*Zombiesprite image file is 54px wide  and 141 px tall */
  public ZombieSprite() throws IOException {
      super( Image.createImage("/res/zombiesprite.png"),
             ZOMBIE_ANIME_FRAME_WIDTH, ZOMBIE_ANIME_FRAME_HEIGHT);
      
              /*( ZOMBIE_ANIME_FRAME_WIDTH / 1,
                            ZOMBIE_ANIME_FRAME_HEIGHT / 1);*/
      if (r == null) {
          r = new Random();
      }
    int randomw = (r.nextInt(Bounds.WIDTH));
    int randomh = (24 - r.nextInt(24)-12);

    setPosition(randomw, randomh);
 }

  public void move() {
      nextFrame();
      int xdelta = r.nextInt(16) - 8;
      int ydelta = r.nextInt(2);
      if(((this.getX() + xdelta) < 0 ) ||
         ((this.getX() + xdelta) > Bounds.WIDTH )) {
          xdelta = 0;
      }
      if((this.getY() + ydelta) < 0 ) {
          ydelta = 1;
      }
      if(((this.getY() + ydelta) > Bounds.WIDTH )) {
          ydelta = 0;
      }
      move(xdelta, ydelta);

  }
  

}