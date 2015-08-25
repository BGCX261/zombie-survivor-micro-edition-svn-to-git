package com.ZombieInvader.javafiles;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import java.util.Timer;
import java.util.Vector;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class ZIGameCanvas extends GameCanvas implements Runnable, CommandListener, ItemCommandListener{

  private volatile boolean gameRunningBoolean;/*boolean value to determine when to start and stop the game */
  private HunterSprite hunter;
  private ZombieDeathSprite zombiedeath;
  public DeathGhoulSprite deathghoul;
  private TiledLayer gameBoard;
  private LayerManager layerManager;
  private Clock timerClock;
  private Vector bulletCollection;
  private Vector zombieCollection;
  private boolean winlose;
  private int totalscore = 0;
  private static int highScore;
  private int GameLevel = 1;
  private String msg;
  private static final int HUNTER_START_POSITION_YAXIS = 138;
  private int durabilityDeathGhoul;
  private long TimeLastShot;
  private long TimeSinceLastShot;
  private final int SHOOT_BULLET_DELAY = 500;

  private static final Command CMD_PRESS = new Command ("Press", Command.ITEM, 1); //button
    //Creates a DeathGhoul
    public void createDeathGhoul() {
        try {
            deathghoul = new DeathGhoulSprite();
        } catch (Exception e) {
            System.out.println("Cannot create Death Ghoul.");
        }
        durabilityDeathGhoul = 20;
    }

    //Creates a Hunter
    public void createHunter() {
        try {
            hunter = new HunterSprite();
        } catch (Exception e) {
            System.out.println("Cannot create Hunter.");
        }
    }

    //Creates a Zombie
    public void createZombies( int size ) {
        if (zombieCollection == null) {
            zombieCollection = new Vector();
        }
        try {
            for(int i = 0; i < size; i++) {
            zombieCollection.addElement(new ZombieSprite());
            }
        } catch (Exception e) {
        System.out.println("Cannot create zombies.");
        }
    }

    public void createZombieDeath() {
        try {
        zombiedeath = new ZombieDeathSprite();
        } catch (Exception e) {
            System.out.println("Cannot create Zombie Death!");
        }
    }

    //Creates a Bullet
    public void createBullet( ) {
        if (bulletCollection == null) {
            bulletCollection = new Vector();
        }
        try {
            bulletCollection.addElement(new BulletSprite());
        } catch (Exception e) {
            System.out.println("Cannot create bullet.");
        }
    }


    //Creates a gameboard using TiledLayer
    private TiledLayer CreateBoard() throws IOException {
        Image image = Image.createImage("/res/canvas.png");
        TiledLayer tiledLayer = new TiledLayer(10, 11, image, 16, 16);

        int[] map = {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
            61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
            71, 72, 73, 74, 75, 76, 77, 78, 79, 80,
            81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
            91, 92, 93, 94, 95, 96, 97, 98, 99, 100,
            101, 102, 103, 104, 105, 106, 107, 108, 109, 110
        };

        for (int i = 0; i < map.length; i++) {
          int column = i % 10;
          int row = (i - column) / 10;
          tiledLayer.setCell(column, row, map[i]);
        }
        return tiledLayer;
      }

    public ZIGameCanvas() throws IOException {
        super(true);
        winlose = true;
        bulletCollection = new Vector();
        createZombies(5);
        createHunter();
        gameBoard = CreateBoard();
        hunter.setPosition((145/2) , 138);
        layerManager = new LayerManager();
        layerManager.append(hunter);

        for(int i = 0; i < this.zombieCollection.size(); i++) {
            layerManager.append((ZombieSprite)zombieCollection.elementAt(i));
        }
        layerManager.append(gameBoard);
    }

      // Starts the game
      public void start() {
        gameRunningBoolean = true;
        Thread t = new Thread(this);
        t.start();
      }
      // Stops the game
      public void stop() {
        gameRunningBoolean = false;
        return;
      }
      // Creates a run cycle within the game
      public void run() {
        Graphics g = getGraphics();
        /* creates a new timer clock to begin running the game */
        timerClock = new Clock(100);
        new Timer().schedule(timerClock, 0, 1000);
        while (gameRunningBoolean) {

          
                try {
                    input();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
          verifyGameState();
          render(g);
          flushGraphics();
          try { Thread.sleep(80); }
            catch (InterruptedException ie) { stop(); }
          } // end of while

        // Start a new level or generate an end of game screen
        StringItem item = new StringItem ("Button ", "Button", Item.BUTTON);
        item.setDefaultCommand (CMD_PRESS);
        item.setItemCommandListener (this);
          //
        String finalsay;
          if (totalscore > highScore) {
              highScore = totalscore;
              finalsay = "New High Score!!" + "\r\n" + "Total Score:" + totalscore;
          } else {
              finalsay = "Game Over!!" + "\r\n" + "Total Score:" + totalscore;
          }
          //String finalsay = "Game Over!" + "\r\n" + "Total Score:" + totalscore;
          //String finalsay = this.msg;
          if (winlose) {
              finalsay = "YOU WIN!" + "\r\n" + "Total Score:" + totalscore;
          }
        g.setColor(0x000000);
        g.fillRect( 25, 45, 125, 50);
        g.setColor(0xffffff);
        Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
        g.setFont(font);
        g.drawString(finalsay, 50, 50, 0);
        repaint();
      }

       private void showGameLevel(Graphics g){
          g.setColor(0xffffff);
          Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
          g.setFont(font);
          String level = "Level:" + GameLevel;
          g.drawString(level, getWidth()-60, 0,0);
          
       }

    private void showHighScore(Graphics g) {
          g.setColor(0xffffff);
          Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
          g.setFont(font);
          String hs = "High:" + highScore;
          g.drawString(hs, getWidth()-80, 10,0);
         
    }

       //Shows the total score
       private void showTotalScore(Graphics g) {
          g.setColor(0xffffff);
          Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
          g.setFont(font);
          String score = "Score:" + totalscore;
          g.drawString(score, 0, 10,0);
          
       }

       //Shows the time leftshowt
       private void showTimeLeft(Graphics g) {

          // what does the clock say
          int timeLeft = timerClock.getTimeLeft();

          // if less than 15 seconds left
          // flicker time with red and black
          if(timeLeft <= 15)
              g.setColor(0xff0000);//red
          else
              g.setColor(0xffffff);//black

          // draw the time left string
          String totalTime = "Time:" + timeLeft;
          Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
          g.setFont(font);
          g.drawString(totalTime, 0, 0, 0);

          // reset the color
          g.setColor(0xffffff);
          
        }
    private void verifyGameState() {
        if(timerClock.getTimeLeft() == 0) {
            stop();
            return;
      }
    }
    private void input() throws IOException {
        int keyStates = getKeyStates();
        int position = hunter.getX();
        int MOVE_AMOUNT = 8;

       // Moves the hunter left
       if ((keyStates & LEFT_PRESSED) != 0) {
           position -= MOVE_AMOUNT;
           if (position >= 0) {
           hunter.prevFrame();
           hunter.setPosition(position, HUNTER_START_POSITION_YAXIS);
           }
       }
        // Moves th e hunter right
        else if ((keyStates & RIGHT_PRESSED) != 0) {
           position += MOVE_AMOUNT;
            if (position < 145) {
           hunter.nextFrame();
           hunter.setPosition(position, HUNTER_START_POSITION_YAXIS);
            }
        }
        // Shoots a bullet
        else if ((keyStates & UP_PRESSED) !=0) {
            TimeSinceLastShot = (System.currentTimeMillis() - TimeLastShot);
            if (TimeSinceLastShot > SHOOT_BULLET_DELAY) {

        try {
           
            InputStream gunSound = getClass().getResourceAsStream("/res/gun.wav");
            Player p = Manager.createPlayer(gunSound, "audio/X-wav");
            //Click the MMAPI link in this post to view supported audio formats.
            p.start();
            gunSound = null;
            p = null;

        } catch (IOException ioe) {
        } catch (MediaException me) {
        }



                TimeLastShot = System.currentTimeMillis();
                System.out.println("Time Last Shot " + TimeLastShot);
                BulletSprite bullet = new BulletSprite();
                
                bulletCollection.addElement(bullet);
                
                bullet.setPosition((hunter.getX() + 10), (hunter.getY() - 5));
                layerManager.insert(bullet, 0);
           
            }
        }
    }

  private void render(Graphics g) {

    g.setColor(0x000000);/* sets background color */
    g.fillRect(0, 0, getWidth(), getHeight()); //sets full screen rectangle

/* -------------------  THIS IS WHERE LEVELS ARE IMPLEMENTED BASED ON timerClock --------------- */
   if (zombieCollection.isEmpty() && GameLevel == 1) { // if all the zombies are dead then end game or level.
        createZombies(10);
        GameLevel ++;
            for(int i = 0; i < this.zombieCollection.size(); i++) {
            layerManager.append((ZombieSprite)zombieCollection.elementAt(i));
            layerManager.append(gameBoard);
            }
   } else if(zombieCollection.isEmpty() && GameLevel == 2) {
        createZombies(15);
        GameLevel ++;
            for(int i = 0; i < this.zombieCollection.size(); i++) {
            layerManager.append((ZombieSprite)zombieCollection.elementAt(i));
            layerManager.append(gameBoard);
            }
   } else if(zombieCollection.isEmpty() && GameLevel == 3) {
       createZombies(20);
       GameLevel ++;
            for(int i = 0; i < this.zombieCollection.size(); i++) {
            layerManager.append((ZombieSprite)zombieCollection.elementAt(i));
            layerManager.append(gameBoard);
            }
   } else if (zombieCollection.isEmpty() && GameLevel == 4 && deathghoul == null) {
            createDeathGhoul();
            deathghoul.setPosition(getWidth() / 2, -60);
            layerManager.insert(deathghoul, 0);
    }// end level loops and decisions
      


    moveZombies(); //moves Zombies
    moveDeathghoul(); //moves Deathghoul
    moveBullets(); //moves Bullets when fired
    checkCollisions(); // checks if a Zombie or Deathghoul collided with the hunter
    ZombieKilled(); // When a Hunter kills a Zombie
    DeathghoulKill();
    int x = (getWidth() - Bounds.WIDTH) / 2; //creates 5px left & right border
    int y = (getHeight() - Bounds.HEIGHT); //starts at 0, very bottom of screen
    layerManager.paint(g, x, y);

    /* sets background color */
    g.setColor(y);
    g.drawRect(x, y, Bounds.WIDTH, Bounds.HEIGHT); /* draws rectangle for GameCanvas */
    
    flushGraphics(); /* resets graphics for new frame to be painted */
    showTimeLeft(g);
    showTotalScore(g);
    showGameLevel(g);
    showHighScore(g);
    /*showDeathGhoulHealth(g);*/
    
  }
    /*private void showDeathGhoulHealth(Graphics g){
          g.setColor(0xff0000);
          String level = "Health:" + "xxx";
          Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
          g.setFont(font);
          g.drawString(level, getWidth()-85, 10,0);
    }*/
    public void GameOver() {
       
        stop();
        winlose = false;
    }

    public void moveZombies() {
      if (this.zombieCollection != null) {
            for(int i = 0; i < this.zombieCollection.size(); i++) {
                ((ZombieSprite)zombieCollection.elementAt(i)).move();
            }
        }
    }

    public void moveDeathghoul() {
        if (deathghoul != null) {
            deathghoul.move();
        }
    }
    
    public void moveBullets() {
        for(int i = 0; i < this.bulletCollection.size(); i++) {
        ((BulletSprite)bulletCollection.elementAt(i)).move();
        }
    }

    public void checkCollisions() {
        if (this.zombieCollection != null) {
            for(int i = 0; i < this.zombieCollection.size(); i++) {
                if (((ZombieSprite)zombieCollection.elementAt(i)).collidesWith(hunter, true)) {
                    GameOver();
                    //show gameover splash screen
                }
            }
        }
        if ((deathghoul != null) && (deathghoul.collidesWith(hunter, true))) {
            GameOver();
        }
    }

    public void ZombieKilled() {
        // creates a stack to purge all bullets and mark them for deletion
        Stack bulletPurgeStack = new Stack();
        Stack zombiePurgeStack = new Stack();
            for(int i = 0; i < bulletCollection.size(); i++) {
               if (this.zombieCollection != null) {
                    for(int j = 0; j < zombieCollection.size(); j++) {
                        if (((BulletSprite)bulletCollection.elementAt(i)).collidesWith(
                            (ZombieSprite)zombieCollection.elementAt(j), true)) {
                            // mark zombies and bullets for later deletion

//////////uncomment the following try statement to get zombie sounds... this will throw an OutOfMemory error later on in the game
                              /*try {

                            InputStream zombieDie = getClass().getResourceAsStream("/res/zombieDie.wav");
                            Player p = Manager.createPlayer(zombieDie, "audio/X-wav");
                            p.start();
                            zombieDie = null;
                            p = null;

                            } catch (IOException ioe) {
                            } catch (MediaException me) {
                            }*/
                            bulletPurgeStack.push(bulletCollection.elementAt(i));
                            zombiePurgeStack.push(zombieCollection.elementAt(j));
                            // add to score
                            totalscore += 100;
                            totalscore = totalscore + (timerClock.getTimeLeft() * 10);
                        }
                    }
                }
            }
            while(!bulletPurgeStack.empty()) {
                BulletSprite b = (BulletSprite)bulletPurgeStack.pop();
                layerManager.remove(b);
                bulletCollection.removeElement(b);
        }
            while(!zombiePurgeStack.empty()) {
                ZombieSprite z = (ZombieSprite)zombiePurgeStack.pop();
                layerManager.remove(z);
                zombieCollection.removeElement(z);
        }
    }

    public void DeathghoulKill() {
        if ((deathghoul != null)) {
            Stack bulletPurgeGhoulStack = new Stack();
            for (int k = 0; k < this.bulletCollection.size(); k++) {
                if (((BulletSprite)bulletCollection.elementAt(k)).collidesWith(deathghoul, true)) {
                    durabilityDeathGhoul -= 1;
                    totalscore += 10;
                    bulletPurgeGhoulStack.push(bulletCollection.elementAt(k));
                    System.out.println(durabilityDeathGhoul);
                        if (durabilityDeathGhoul == 0) {
                            totalscore += 500;
                            layerManager.remove(deathghoul);
                            winlose = true;
                            stop();
                        }
                }

            }
            while(!bulletPurgeGhoulStack.empty()) {
                BulletSprite b = (BulletSprite)bulletPurgeGhoulStack.pop();
                layerManager.remove(b);
                bulletCollection.removeElement(b);
            }

        }
    }

    /*private void PlaySound(String soundLoc) {
    try {
    InputStream iStreamSound = getClass().getResourceAsStream(soundLoc);
    Player p = Manager.createPlayer(iStreamSound, "audio/X-wav");
    //Click the MMAPI link in this post to view supported audio formats.
    p.start();

    } catch (IOException ioe) {
    } catch (MediaException me) {
    }

    }*/

    public void commandAction(Command c, Displayable d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void commandAction(Command c, Item item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
} //End of ZIGameCanvas