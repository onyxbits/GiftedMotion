package de.onyxbits.giftedmotion;
import javax.swing.*;

/**
 * A thread, that steps through the animation.
 */
public class Player extends Thread {

  /**
   * The sequence to play
   */
  private final FrameSequence seq;
  
  /**
   * How often to repeat the animation
   */
  private final int repeat;
  
  /**
   * Needed to fire events from the event dispatcher thread
   */
  PlayerHelper helper;
  
  /**
   * Construct a new Player
   * @param seq the framesequence to play.
   * @param repeat how often to repeat the animation (zero is infinite)
   */
  public Player(FrameSequence seq, int repeat) {
    this.seq=seq;
    this.repeat=repeat;
    helper = new PlayerHelper(seq);
  }
  
  public void run() {
    try {
      int count=repeat;
      
      while (true) {
        if (seq.selected==null) return;
        
        Thread.sleep(seq.selected.showtime);
        int idx=0;
        while(seq.frames[idx]!=seq.selected) idx++;
        idx++;
        if (idx>=seq.frames.length) {
          idx=0;
          count--;
        }
        seq.selected=seq.frames[idx];
        SwingUtilities.invokeAndWait(helper);
        if (repeat!=0 && count<=0) return;
      }
    }
    catch (Exception ignored) {}
  }
}