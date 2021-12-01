package de.onyxbits.giftedmotion;

class PlayerHelper implements Runnable {

  private final FrameSequence seq;

  public PlayerHelper(FrameSequence seq) {
    this.seq = seq;
  }
  
  public void run() {
    seq.fireDataChanged();
  }
}