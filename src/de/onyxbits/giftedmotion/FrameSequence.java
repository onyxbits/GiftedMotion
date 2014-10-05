package de.onyxbits.giftedmotion;
import java.awt.*;
import java.util.*;


/**
 * A Sequence of SingleFrames
 */
public class FrameSequence {

  /**
   * The frames, this sequence consists of.
   */
  protected SingleFrame[] frames;
  
  /**
   * The frame, that is currently subject to editing;
   */
  protected SingleFrame selected;
  
  /**
   * Eventlisteners
   */
  private Vector<FrameSequenceListener> listeners = new Vector<FrameSequenceListener>();
  
  /**
   * Create a new FrameSequence
   * @param frames the frames of the sequence. This must contain at least one
   * element.
   */
  public FrameSequence(SingleFrame[] frames) {
    this.frames = frames;
    selected=frames[0];
  }
  
  /**
   * Add a frame to the sequence
   * @param frame frame to add
   * @param index where to add
   */
  public void add(SingleFrame frame, int index) {
    if (index>=0 && index<=frames.length) {
      SingleFrame[] bigger = new SingleFrame[frames.length+1];
      // Copy the first few old ones over
      for(int i=0;i<index;i++)
        bigger[i] = frames[i];
      bigger[index]=frame; // Add the new frame
      // Copy the rest of the old ones over
      for(int i=index+1;i<bigger.length;++i)
        bigger[i] = frames[i-1];
      frames=bigger;
      fireDataChanged();
    }
    else
      throw new IndexOutOfBoundsException();
  }

  /**
   * Returns the index of the currently selected frame
   */
  public int getSelectedIndex() {
    for(int i=0;i<frames.length;++i)
      if(frames[i]==selected)
        return i;
    return -1;
  }
  
  /**
   * Remove a frame from the sequence
   * @param frame the frame to remove
   */
  public void remove(SingleFrame frame) {
    if (frames.length==1 && frame==frames[0]) {
      frames = new SingleFrame[0];
      fireDataChanged();
      return;
    }
    if (frames.length==0) return;
    Vector<SingleFrame> tmp = new Vector<SingleFrame>();
    for(int i=0;i<frames.length;i++) tmp.add(frames[i]);
    tmp.remove(frame);
    frames = new SingleFrame[tmp.size()];
    tmp.copyInto(frames);
    if (frames.length>0) selected=frames[0];
    else selected=null;
    fireDataChanged();
  }
  
  /**
   * Query the required dimension of a canvas able to display all frames
   * without cutting anything of (assumed, they are all offesetted at 0,0).
   * @return the biggest x and y dimension found among all frames of the
   * sequence.
   */
  public Dimension getExpansion() {
    Dimension ret = new Dimension(1,1);
    for (int i=0;i<frames.length;++i) {
      Dimension d = frames[i].getSize();
      if (d.width>ret.width) ret.width=d.width;
      if (d.height>ret.height) ret.height=d.height;
    }
    return ret;
  }
  
  /**
   * Move a frame in the sequence to a sooner or later position
   * @param frame the frame to move
   * @param sooner if true, move frame to a sooner position
   */
  public void move (SingleFrame frame, boolean sooner) {
    try {
      int idx=0;
      while(frames[idx]!=frame) idx++;
      SingleFrame tmp;
      if (sooner) {
        tmp=frames[idx-1];
        frames[idx-1]=frames[idx];
        frames[idx]=tmp;
      }
      else {
        tmp=frames[idx+1];
        frames[idx+1]=frames[idx];
        frames[idx]=tmp;
      }
      fireDataChanged();
    }
    catch (Exception e) {
      // Lazy way
    }
  }
  
  /**
   * Register with this FrameSequence to be notified of datachanges
   * @param fsl Object ot be notifed of datachange events
   */
  public void addFrameSequenceListener(FrameSequenceListener fsl) {
    listeners.add(fsl);
  }
  
  /**
   * Deregister listener
   * @param fsl listener to remove
   */
  public void removeFrameSequenceListener(FrameSequenceListener fsl) {
    listeners.remove(fsl);
  }
  
  /**
   * Notify Framesequencelisteners, that the data changed
   
   */
  protected void fireDataChanged() {
    int size= listeners.size();
    for(int i=0;i<size;i++) {
      (listeners.get(i)).dataChanged(this);
    }
  }
  
}
