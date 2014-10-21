package de.onyxbits.giftedmotion;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

/**
 * A container for holding one frame of animation
 */
public class SingleFrame  {

  /**
   * The raw BufferedImage, as loaded from disk
   */
  protected BufferedImage raw;
  
  /**
   * only x and y are used to specified where to draw the raw image on the
   * canvas.
   */
  protected Point position;
  
  /**
   * An amount of rotation degrees to rotate the image by
   * 0 = Correct direction
   */
  protected double rotationDegrees = 0;
  
  /**
   * Scale amount (basically just scaled dimensions)
   */
  protected float scaleX, scaleY;
  
  /**
   * How long to show this frame in the final animation (in ms).
   */
  protected int showtime=100;
  
  /**
   * What to do with the previous frame. According to the GIF spec: <br>
   * 0 - undefined<br>
   * 1 - Do not dispose between frames.<br>
   * 2 - Overwrite the image area with the background color.<br>
   * 3 - Overwrite the image area with what was there prior to rendering<br>
   *     the image.
   */
  protected int dispose = 3;
  
  /**
   * For identifying purposes
   */
  private String name;
  
  
  /**
   * Create a new Frame
   * @param raw the raw iamge as loaded from disk
   * @param name A name for identification purposes
   */
  public SingleFrame(BufferedImage raw, String name) {
    this.raw=raw;
    this.name=name;
    position=new Point(0,0);
    
    scaleX = raw.getWidth();
    scaleY = raw.getHeight();
  }

  /**
   * Create a copy of a Frame
   * @param frame Frame to be copied
   */
  public SingleFrame(SingleFrame frame) {
    this.raw=frame.raw;
    this.name=frame.name;
    rotationDegrees = frame.rotationDegrees;
    position=new Point(frame.position);
    showtime=frame.showtime;
    scaleX = frame.scaleX;
    scaleY = frame.scaleY;
  }
  
  /**
   * Query the dimension of the raw image
   */
  public Dimension getSize() { 
    return new Dimension(raw.getWidth(), raw.getHeight());
  }
  
  /**
   * Specify position of the raw image on the canvas
   * @param r the x and y coordinate of where to draw the raw image on the
   * canvas.
   */
  public void setPosition(Point p) { position=p; }
  
  /**
   * Draw this frame on a canvas
   * @param g the grpahics object to render to
   */
  public void paint(Graphics g) {
		  Graphics2D g2 = (Graphics2D)g;
		  AffineTransform at = new AffineTransform();
		  at.translate(scaleX/2, scaleY/2);
		  at.rotate(rotationDegrees);
		  at.translate(-scaleX/2, -scaleY/2);
		  
		  at.scale(scaleX/raw.getWidth(), scaleY/raw.getHeight());
		  g2.drawImage(raw, new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR), position.x, position.y);
  }
  
  /**
   * Produce an image, that can directly be assembled into an animated GIF
   * @param size Size of the final frame in pixels.
   * @param trans the color to replace transparent pixels with.
   * @return a BufferedImage of the desired size with the transparent pixels
   * replaced by the specified solid color.
   */
  public BufferedImage exportFrame(Dimension size, Color trans) {
    BufferedImage ret = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);
    Graphics gr = ret.createGraphics();
    gr.setColor(trans);
    gr.fillRect(0,0,size.width,size.height);
    paint(gr);
    //gr.drawImage(raw,position.x,position.y,null);
    return ret;
  }
  
  public String toString() { return name; }
}
