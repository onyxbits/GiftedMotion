package de.onyxbits.giftedmotion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

/**
 * The canvas on which to draw SingleFrames
 */
public class FrameCanvas extends JPanel implements FrameSequenceListener,
MouseListener, MouseMotionListener {

  /**
   * The sequence to draw
   */
  private FrameSequence seq;
  
  /**
   * Used for dragging -> Where the mousecursor is relative to the position
   */
  private Point offset;
  
  
  public FrameCanvas(FrameSequence seq) {
    this.seq = seq;
    addMouseListener(this);
    addMouseMotionListener(this);
  }
  
  public void paintComponent(Graphics g) {
    //long time = System.currentTimeMillis();
    super.paintComponent( g );
    Dimension size = getSize();
    
    if (seq.selected==null) {
      g.clearRect(0,0,size.width,size.height);
      return;
    }
    
    BufferedImage previous = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);
    
    for(int i=0;i<seq.frames.length;i++) {
      
      
      seq.frames[i].paint(g);
      // Only draw the sequence up to the selected frame
      // FIXME: This is utterly inefficient!
      if (seq.frames[i]==seq.selected) break;
      
      // If the selected frame is not reached yet, dispose
      switch(seq.frames[i].dispose) {
        case 0: {break;}
        case 1: {
          Graphics pre_gr = previous.getGraphics();
          seq.frames[i].paint(pre_gr);
          pre_gr.dispose();
          break;
        }
        case 2: {
          g.clearRect(0,0,size.width,size.height);
          break;
        }
        case 3: {
          g.clearRect(0,0,size.width,size.height);
          g.drawImage(previous,0,0,null);
          
          break;
        }
      }
    }
    //System.err.println("Time: "+(System.currentTimeMillis()-time));
  }
  
  public Dimension getPreferredSize() { return seq.getExpansion(); }
  
  public void dataChanged(FrameSequence src) {
    repaint();
  }
  
  public void mouseClicked(MouseEvent e) {}
  
  public void mouseEntered(MouseEvent e) {}
  
  public void mouseExited(MouseEvent e) {}
  
  public void mousePressed(MouseEvent e) {
    if (seq.selected==null) return;
    Point pos = e.getPoint();
    offset= new Point(pos.x-seq.selected.position.x,pos.y-seq.selected.position.y);
  }

  public void mouseReleased(MouseEvent e) {
    offset=null;
    seq.fireDataChanged();
  }
  
  public void mouseDragged(MouseEvent e) {
    if (seq.selected==null) return;
    Point pos = e.getPoint();
    seq.selected.position.x=pos.x-offset.x;
    seq.selected.position.y=pos.y-offset.y;
    repaint();
  }
  
  
  public void mouseMoved(MouseEvent e) {}
  

}