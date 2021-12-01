package de.onyxbits.giftedmotion;
import javax.swing.*;
import java.awt.*;

/**
 * An Icon of arbitrary size, showing a solid color
 */
public class ColorIcon implements Icon {
  
  private final Color color;
  private final int width;
  private final int height;
  
  public ColorIcon(Color c, int w, int h) {
    color=c;
    width=w;
    height=h;
  }
  
  public int getIconWidth() { return width; }
  public int getIconHeight() { return height; }
  
  public Color getColor() { return color;}
  
  public void paintIcon(Component c, Graphics g, int x, int y) {
    Color tmp = g.getColor();

    if (color==null) {
      IO.createIcon("Tango/16x16/actions/process-stop.png","").paintIcon(c,g,x,y);
    }
    else {
      g.setColor(color);
      g.fillRect(x,y,width,height);
    }

    g.setColor(tmp);
  }
}