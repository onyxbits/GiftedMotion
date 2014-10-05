package de.onyxbits.giftedmotion;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

/**
 * Preview an image file
 */
public class Preview extends JPanel {

  /**
   * Preferredsize of the previewcanvas
   */
  private Dimension preferred = new Dimension(1,1);
  
  /**
   * Image to show
   */
  private BufferedImage img;
  

  public Preview () {}
  
  /**
   * Display an image
   * @param file the file to display. If it cannot be loaded, nothing will be
   * displayed.
   */
  public void show(File f) {
    try {
      img = ImageIO.read(f);
      preferred = new Dimension(img.getWidth(),img.getHeight());
    }
    catch (Exception e) {
      //e.printStackTrace(); // Debug only
      img=null;
    }
    //repaint();
    revalidate();
    repaint();
  }
  
  // Overridden
  public Dimension getPreferredSize() {
    return preferred;
  }
  
  // Overriden
  public void paint(Graphics gr) {
    Dimension size = getSize();
    gr.clearRect(0,0,size.width,size.height);
    if (img!=null) {
      gr.drawImage(img,0,0,null);
    }
  }
}