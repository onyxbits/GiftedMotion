package de.onyxbits.giftedmotion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

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
	 * Onionskin boolean
	 */
	private boolean onionskinEnabled = false;
	
	/**
	 * The tool used for transforming the selected image
	 */
	private TransformTool tool = new DragTool();
	
	/**
	 * Thread for flickering
	 */
	private FlickerThread flickerThread = new FlickerThread();
	
	/**
	 * Boolean to indicate which flickered image to show
	 */
	private boolean flickerShow = false;

	public FrameCanvas(FrameSequence seq) {
		this.seq = seq;
		addMouseListener(this);
		addMouseMotionListener(this);

		addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_SHIFT) 
					tool.setShiftPressed(true);
			}

			@Override
			public void keyReleased(KeyEvent ke)
			{
				if (ke.getKeyCode() == KeyEvent.VK_SHIFT)
					tool.setShiftPressed(false);
			}
		});
	}

	public void paintComponent(Graphics g) {
		//long time = System.currentTimeMillis();
		super.paintComponent(g);
		Dimension size = getSize();

		if (seq.selected==null) {
			g.clearRect(0,0,size.width,size.height);
			return;
		}

		//BufferedImage previous = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);

		for(int i = Util.getFirstNecessaryFrame(seq);i<seq.frames.length;i++) {
			if (onionskinEnabled) //Just draw the previous, and the onionskinned layer on top
			{
				if (seq.selected == seq.frames[i])
				{
					Graphics2D g2d = (Graphics2D)g;
					seq.frames[i-1].paint(g2d);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.6f));
					seq.selected.paint(g2d);
				}
			}
			else
			{
				//Draw the frame, if it won't be immediately erased, but not if it's the last frame.
				if (seq.frames[i].dispose != 2 && seq.frames[i].dispose != 3 && seq.frames[i] != seq.selected)
					seq.frames[i].paint(g);
				
				//Draw the frame, if it's the last frame
				if (seq.frames[i] == seq.selected)
					seq.frames[i].paint(g);
				
				//End the loop if we've encountered the last frame
    			if (seq.frames[i]==seq.selected) break;
    			
    			//"Dispose" of the drawn picture
    			switch(seq.frames[i].dispose) {
    				case 0: { //Undefined
    					//seq.frames[i].paint(g);
    					break;
    				}
    				case 1: { //None
    					//seq.frames[i].paint(g);
    					break;
    				}
    				case 2: { //Background
    					g.clearRect(0,0,size.width,size.height);
    					break;
    				}
    				case 3: { //Previous
    					g.clearRect(0,0,size.width,size.height);
    					if (i > 0) seq.frames[i-1].paint(g);
    					//g.drawImage(previous,0,0,null);
    					break;
    				}
    			}
    		}
		}
	}

	public Dimension getPreferredSize() { return seq.getExpansion(); }

	public void dataChanged(FrameSequence src) {
		repaint();
	}

	public void setTool(TransformTool t)
	{
		tool = t;
	}
	
	public void setOnionskin(boolean on)
	{
		onionskinEnabled = on;
		repaint();
	}
	
	public void setFlicker(boolean fli)
	{
		if (fli) 
		{
			flickerThread = new FlickerThread();
			flickerThread.start();
		}
		else 
		{
			flickerShow = false;
			flickerThread = null;
		}
	}

	public TransformTool getTool()
	{
		return tool;
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (seq.selected==null) return;
		Point pos = e.getPoint();
		tool.setOffset(new Point(pos.x-seq.selected.position.x,pos.y-seq.selected.position.y));
		tool.beginTransform(seq.selected, e.getPoint());
	}

	public void mouseReleased(MouseEvent e) {
		if (seq.selected==null) return;
		seq.fireDataChanged();
		tool.endTransform(seq.selected, e.getPoint());
	}

	public void mouseDragged(MouseEvent e) {
		if (seq.selected==null) return;
		tool.transform(seq.selected, e.getPoint());
		repaint();
	}


	public void mouseMoved(MouseEvent e) {}


}