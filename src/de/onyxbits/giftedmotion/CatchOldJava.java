package de.onyxbits.giftedmotion;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.net.*;

/**
 * This class is a hack around for using fancy Java 6 API calls in 
 * giftedmotion and still be able to run the program on older JRE
 * installations with reduced fancyness.
 * The purpose of this class is to encapsulate exceptions, thrown due to
 * missing APIs
 */
public class CatchOldJava {

  /**
   * Try to open an URL in an external browser
   * @param url the URL to open
   */
  public static void openBrowser(String url) throws Exception {
    try {
      Desktop dt = Desktop.getDesktop();
      if (!dt.isSupported(Desktop.Action.BROWSE)) throw new Exception();
      dt.browse(new URI(url));
      
    }
    catch (java.lang.NoClassDefFoundError err) {
      // Likely ClassNotFoundError
      throw new Exception();
    }
  }
  
  /**
   * Try to put imageicons on the window
   * @param w the window to decorate
   */
  public static void decorateWindow(Window w) {
    try {
      ArrayList<Image> icolst = new ArrayList<Image>();
      icolst.add(new ImageIcon(ClassLoader.getSystemResource("resources/logo-32x32.png")).getImage());  
      icolst.add(new ImageIcon(ClassLoader.getSystemResource("resources/logo-48x48.png")).getImage());
      icolst.add(new ImageIcon(ClassLoader.getSystemResource("resources/logo-64x64.png")).getImage());
      icolst.add(new ImageIcon(ClassLoader.getSystemResource("resources/logo-96x96.png")).getImage());
      w.setIconImages(icolst);
    }
    catch (Throwable t) {
      // Really don't care. Its just deco
    }
  }
}
