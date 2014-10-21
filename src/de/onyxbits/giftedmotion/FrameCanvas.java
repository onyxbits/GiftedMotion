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

		BufferedImage previous = new BufferedImage(size.width,size.height,BufferedImage.TYPE_INT_ARGB);

		for(int i=0;i<seq.frames.length;i++) {
			//Well, this is confusing.
			if ((!onionskinEnabled) || (onionskinEnabled && seq.selected != seq.frames[i])) //Paint normally
				seq.frames[i].paint(g);
			else if (i > 0) //Paint the onionskinned frame
			{
				Graphics2D g2d = (Graphics2D)g;
				seq.frames[i-1].paint(g2d);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.6f));
				seq.selected.paint(g2d);
			} 
			else seq.selected.paint(g); //No previous frame, just paint normally

			
			
			// Only draw the sequence up to the selected frame
			// FIXME: This is utterly inefficient!
			if (seq.frames[i]==seq.selected) break;

			// If the selected frame is not reached yet, dispose
			switch(seq.frames[i].dispose) {
				case 0: {break;} //Do not draw
				case 1: { //Draw
					Graphics pre_gr = previous.getGraphics();
					seq.frames[i].paint(pre_gr);
					pre_gr.dispose();
					break;
				}
				case 2: { //Clear
					g.clearRect(0,0,size.width,size.height);
					break;
				}
				case 3: { //Clear and draw
					g.clearRect(0,0,size.width,size.height);
					g.drawImage(previous,0,0,null);

					break;
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