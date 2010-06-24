package de.onyxbits.giftedmotion;

class PlayerHelper implements Runnable {

  private FrameSequence seq;
  public PlayerHelper(FrameSequence seq) {
    this.seq = seq;
  }
  
  public void run() {
    seq.fireDataChanged();
  }
}