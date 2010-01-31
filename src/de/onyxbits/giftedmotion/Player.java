package de.onyxbits.giftedmotion;

/**
 * A thread, that steps through the animation.
 */
public class Player extends Thread {

  /**
   * The sequence to play
   */
  private FrameSequence seq;
  
  /**
   * How often to repeat the animation
   */
  private int repeat;
  
  /**
   * Construct a new Player
   * @seq the framesequence to play. 
   * @param repeat how often to repeat the animation (zero is infinite)
   */
  public Player(FrameSequence seq, int repeat) {
    this.seq=seq;
    this.repeat=repeat;
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
        seq.fireDataChanged();
        if (repeat!=0 && count<=0) return;
      }
    }
    catch (Exception exp) {}
  }
}