package de.onyxbits.giftedmotion;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Util
{
	public static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	public static BufferedImage convertIndexed(BufferedImage source)
	{
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}
	
	/**
	 * Return a 
	 * @param frames
	 * @return The index of the last frame actually necessary to paint.
	 */
	public static int getFirstNecessaryFrame(FrameSequence seq)
	{
		SingleFrame[] frames = seq.frames;
		for (int i = getFrameNumber(seq, seq.selected); i > 0; i--)
		{
			if (frames[i].dispose == 2 || frames[i].dispose == 3)
				return i;
		}
		
		return 0;
	}
	
	public static int getFrameNumber(FrameSequence seq, SingleFrame frame)
	{
		for (int i = 0; i < seq.frames.length; i++)
			if (seq.frames[i] == frame) return i;
		return -1;
	}
}
