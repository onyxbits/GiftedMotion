package de.onyxbits.giftedmotion;

import javax.swing.*;

/**
 * Wrapper around the actual canvas class, to glue it into the workspace
 */
public class FrameDisplay extends JInternalFrame {

  /**
   * The canvas to draw upon
   */
  private final FrameCanvas canvas;
  
  
  public FrameDisplay(FrameCanvas canvas) {
    super("",true,false,false,false);
    setFrameIcon(null);
    this.canvas=canvas;
    setContentPane(canvas);
    pack();
  }
  
  /**
   * Query the canvas
   * @return the canvas displayed
   */
  public FrameCanvas getCanvas() {
    return canvas;
  }
}
