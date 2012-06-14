package org.zeroturnaround;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Akt2 {
  private static final Logger log = Logger.getLogger(Akt2.class);
  // most simple logging atm
  static {
    BasicConfigurator.configure();
  }

  private static final String lcOSName = System.getProperty("os.name").toLowerCase();
  public static final boolean IS_LINUX = lcOSName.startsWith("linux");
  public static final boolean IS_MAC = lcOSName.startsWith("mac os x");

  public static void main(String[] args) {
    log.debug("Starting app");
    init();
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        new MainWindow();
      }
    });
  }

  private static void init() {
    if (IS_MAC) {
      try {
        log.debug("Applying Mac OS specific settings");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "akt2");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {
        log.debug("Problem setting MAC specific settings", e);
      }
    } else if (IS_LINUX) {
      try {
        log.debug("Applying Linux specific settings");
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (Exception e) {
        log.debug("Problem setting Linux specific settings", e);
      }
    }
  }
}
