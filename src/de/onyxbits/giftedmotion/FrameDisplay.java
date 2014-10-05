package de.onyxbits.giftedmotion;
import javax.swing.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Wrapper around the actual canvas class, to glue it into the workspace
 */
public class FrameDisplay extends JInternalFrame implements MouseListener {

  /**
   * The canvas to draw upon
   */
  private FrameCanvas canvas;
  
  public FrameDisplay(FrameCanvas canvas) {
    super("",true,false,false,false);
    setFrameIcon(null);
    this.canvas=canvas;
    setContentPane(canvas);
    
    addMouseListener(this);
    
    pack();
  }
  
  /**
   * Query the canvas
   * @return the canvas displayed
   */
  public FrameCanvas getCanvas() {
    return canvas;
  }

@Override
public void mouseClicked(MouseEvent e){}

@Override
public void mousePressed(MouseEvent e){}

@Override
public void mouseReleased(MouseEvent e)
{
	if (getY() < 0)
		setLocation(getX(), 0);
}

@Override
public void mouseEntered(MouseEvent e){}

@Override
public void mouseExited(MouseEvent e){}
}
