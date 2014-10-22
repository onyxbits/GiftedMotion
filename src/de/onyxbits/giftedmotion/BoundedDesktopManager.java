package de.onyxbits.giftedmotion;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

public class BoundedDesktopManager extends DefaultDesktopManager {

	@Override
	public void beginDraggingFrame(JComponent f) {
		// Don't do anything. Needed to prevent the DefaultDesktopManager setting the dragMode
	}

	@Override
	public void beginResizingFrame(JComponent f, int direction) {
		// Don't do anything. Needed to prevent the DefaultDesktopManager setting the dragMode
	}

	@Override
	public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
		boolean didResize = (f.getWidth() != newWidth || f.getHeight() != newHeight);

		if (!didResize)
			if (newY < 0) newY = 0;

		f.setBounds(newX, newY, newWidth, newHeight);

		if(didResize) 
			f.validate();
	}
}