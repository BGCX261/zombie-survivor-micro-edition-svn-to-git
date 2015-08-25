package com.ZombieInvader.javafiles;
import java.io.IOException;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;


public class BulletSprite extends Sprite {
    private static final int WIDTH = 7;
    private static final int HEIGHT = 11;
    public BulletSprite() throws IOException {
      super( Image.createImage("/res/bullet.png"),WIDTH, HEIGHT);
      defineReferencePixel( WIDTH / 1,
                            HEIGHT / 1);
  }
/* move() moves the bullet */
/* ydelta - moves the bullet ^ the y axis */
  public void move() {
      if ((this.getY() > 13 & this.getY() < 138)){
          /*System.out.println(this.getY());*/
      nextFrame();
      int xdelta = 0;
      int ydelta = -8;

      if((this.getY() + ydelta) <= 0 ) {
          ydelta = 0;
      }
      move(xdelta, ydelta);
      }
      else {
      //remove bullet
          this.setVisible(false);
      }
  }
}
  