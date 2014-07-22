package de.onyxbits.giftedmotion;

/**
 * Implementors of this class can request to be notified of changes in a
 * specific FrameSequence
 */
public interface FrameSequenceListener {

  /**
   * Called, when the data of a FrameSequence changes
   * @param src the source FrameSequence
   */
  public void dataChanged(FrameSequence src);
}