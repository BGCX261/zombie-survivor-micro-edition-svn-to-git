 package com.ZombieInvader.javafiles;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class ZombieDeathSprite extends Sprite {

  private static final int ZOMBIEDEATH_ANIME_FRAME_WIDTH = 35;
  private static final int ZOMBIEDEATH_ANIME_FRAME_HEIGHT = 50;
  int deathSequence[] = {0,0,0,0,0,1,1,1,1,1,1,2,3,4,5,6,7,8,9};
  /* Loads zombie sprite image and uses the animation constructor method */
    /*Zombiesprite image file is 54px wide  and 141 px tall */
  public ZombieDeathSprite() throws IOException {
      super( Image.createImage("/res/zombiedeathsprite.png"),
             ZOMBIEDEATH_ANIME_FRAME_WIDTH, ZOMBIEDEATH_ANIME_FRAME_HEIGHT);
      /*setFrameSequence(deathSequence);*/
 }

  public void executedeath() {
      nextFrame();
  }
}