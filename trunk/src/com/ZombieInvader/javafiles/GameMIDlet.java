package com.ZombieInvader.javafiles;

import java.io.IOException;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class GameMIDlet extends MIDlet implements CommandListener {
  private ZIGameCanvas ZIGameCanvas;

  public void startApp() {
    if (ZIGameCanvas == null) {
      try {
        ZIGameCanvas = new ZIGameCanvas();
        ZIGameCanvas.start();
        Command exitCommand = new Command("Exit", Command.EXIT, 0);
        Command restartCommand = new Command("Restart", Command.OK, 0);
        ZIGameCanvas.addCommand(exitCommand);
        ZIGameCanvas.addCommand(restartCommand);
        ZIGameCanvas.setCommandListener(this);
      }
      catch (IOException ioe) {
        System.out.println(ioe);
      }
    }

    Display.getDisplay(this).setCurrent(ZIGameCanvas);
  }

  public void pauseApp() {}

  public void destroyApp(boolean unconditional) {
    if (ZIGameCanvas != null)
      ZIGameCanvas.stop();
  }

  public void commandAction(Command c, Displayable s) {
    if (c.getCommandType() == Command.EXIT) {
      destroyApp(true);
      notifyDestroyed();
    }
        if (c.getCommandType() == Command.OK) {
      //destroyApp(true);
      //notifyDestroyed();
      this.ZIGameCanvas = null;
      this.startApp();
    }

  }
}
