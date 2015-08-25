package com.ZombieInvader.javafiles;
import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;


/* This class is designed to create a Hunter Sprite
 * This sprite is the only interactive sprite in the game
 * Loads hunter sprite image and uses the animation constructor method
 * HunterSprite image file is 16px wide, 38px tall (each frame) 5 frames total
*/

public class HunterSprite extends Sprite {

private static final int HUNTER_ANIME_FRAME_WIDTH = 16;
private static final int HUNTER_ANIME_FRAME_HEIGHT = 38;
private static final int HUNTER_START_POSITION_YAXIS = 138;

  public HunterSprite() throws IOException  {
      super(Image.createImage("/res/hunter.png"),
             HUNTER_ANIME_FRAME_WIDTH, HUNTER_ANIME_FRAME_HEIGHT);

    setPosition(145 / 2,HUNTER_START_POSITION_YAXIS );
 }
}