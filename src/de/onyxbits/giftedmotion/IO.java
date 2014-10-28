package de.onyxbits.giftedmotion;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

import java.net.*;

import javax.imageio.*;

import java.awt.image.*;

import javax.imageio.metadata.*;

import org.w3c.dom.*;

import com.sun.imageio.plugins.gif.GIFImageReader;

/**
 * Responsible for doing all the disk IO related things
 */
public class IO {


	private IO() {
	}

	/**
	 * Load a bunch of files and convert them into frames
	 * @param files the files to load images from.
	 * @return the frames constructed from the files. A file not containing an
	 * image in a valid format will cause an InvalidArguementException to be
	 * thrown.
	 * be loaded will be represented by an errorshape.
	 */
	public static SingleFrame[] load(File[] files) throws IOException, FileNotFoundException, IllegalArgumentException {
		Vector<SingleFrame> tmp = new Vector<>();
		for(int i=0;i<files.length;i++) {
			Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix(getSuffix(files[i]));
			if (!it.hasNext()) throw new IllegalArgumentException(files[i].getPath());
			ImageReader reader = (ImageReader)it.next();
			if (reader instanceof GIFImageReader) reader = new PatchedGIFImageReader(null);
			reader.setInput(ImageIO.createImageInputStream(new FileInputStream(files[i])));
			int ub = reader.getNumImages(true);

			for (int x=0;x<ub;x++) {
				BufferedImage img = reader.read(x);
				//if (img.getType() == 4) img = Util.convertIndexed(img);
				if (ub==1) tmp.add(new SingleFrame(img,files[i].getName()));
				else {
					SingleFrame sf = new SingleFrame(img,x+"_"+files[i].getName());

					// Getting meta info from an animated GIF is a bit complicated...
					// ... try the quick and dirty method.
					try {
						IIOMetadata meta = reader.getImageMetadata(x);
						NodeList nl = meta.getAsTree("javax_imageio_gif_image_1.0").getChildNodes();
						for (int count=0;count<nl.getLength();count++) {
							IIOMetadataNode node = (IIOMetadataNode) nl.item(count);
							if (node.getNodeName().equals("GraphicControlExtension")) {
								sf.showtime=10*(Integer.parseInt(node.getAttribute("delayTime")));
								String dispose=node.getAttribute("disposalMethod");
								if (dispose.equals("none")) sf.dispose=0;
								if (dispose.equals("doNotDispose")) sf.dispose=1;
								if (dispose.equals("restoreToBackgroundColor")) sf.dispose=2;
								if (dispose.equals("restoreToPrevious")) sf.dispose=3;
							}
							if (node.getNodeName().equals("ImageDescriptor")) {
								int off_x=(Integer.parseInt(node.getAttribute("imageLeftPosition")));
								int off_y=(Integer.parseInt(node.getAttribute("imageTopPosition")));
								sf.position=new Point(off_x,off_y);
							}
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					
					tmp.add(sf);
				}
			}
		}

		SingleFrame[] ret = new SingleFrame[tmp.size()];
		tmp.copyInto(ret);
		return ret;
	}

	/**
	 * Write an animated GIF
	 * @param dest File to save to
	 * @param seq the FrameSequence to turn into an animation
	 * @param size height and width of the animated GIF
	 * @param opt other options.
	 */
	public static void export(File dest, FrameSequence seq, Dimension size, Settings opt) throws IOException {
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		e.start(new FileOutputStream(dest));
		e.setRepeat(opt.repeat);
		e.setQuality(opt.quality);
		e.setTransparent(opt.transparent);
		for (int i=0;i<seq.frames.length;i++) {
			e.setDelay(seq.frames[i].showtime);
			e.setDispose(seq.frames[i].dispose);
			e.addFrame(seq.frames[i].exportFrame(size,opt.transparent));
		}
		e.finish();
	}
	
	/**
	 * Deoptimize an animated GIF and save it
	 * @param dest File to save to
	 * @param seq the FrameSequence to turn into an animation
	 * @param size height and width of the animated GIF
	 * @param opt other options.
	 */
	public static void exportDeoptimized(File dest, FrameSequence seq, Dimension size, Settings opt) throws IOException
	{
		AnimatedGifEncoder e = new AnimatedGifEncoder();
		e.start(new FileOutputStream(dest));
		e.setRepeat(opt.repeat);
		e.setQuality(opt.quality);
		e.setTransparent(opt.transparent);
		BufferedImage outputBuf = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = outputBuf.getGraphics();
		for (int i=0;i<seq.frames.length;i++) {
			e.setDelay(seq.frames[i].showtime);
			e.setDispose(seq.frames[i].dispose); //Redraw background
			seq.frames[i].paint(g);
			e.addFrame(outputBuf);
		}
		g.dispose();
		e.finish();
	}

	/**
	 * Extract frames from the sequence and save them as single files
	 * @param seq the frame sequence
	 * @param dir base directory
	 * @throws IOException on error
	 */
	public static void extract(FrameSequence seq, File dir, Dimension size, Settings opt) throws IOException {
		for (int i=0;i<seq.frames.length;i++) {
			File f = new File(dir,seq.frames[i].toString());
			String[] tmp = seq.frames[i].toString().split("\\.");
			String format = tmp[tmp.length-1].toLowerCase();
			//ImageIO.write(seq.frames[i].raw,format,f);
			ImageIO.write(seq.frames[i].exportFrame(size,opt.transparent),format,f);
		}
	}

	/**
	 * Locate an image in the resources folder and build an icon from it.
	 * @param fname filename (relative to resources/icons/)
	 * @param desc Icon description
	 * @return the loaded icon
	 */
	public static ImageIcon createIcon(String fname,String desc) {
		URL imgURL = new Object().getClass().getResource("/resources/icons/"+fname);
		return new ImageIcon(imgURL,desc);
	}

	/**
	 * Helper function to determine filetype
	 * @param the file to look at
	 * @return the suffix (lowercase) or null
	 */
	private static String getSuffix(File f) {
		String[] tmp = f.getName().split("\\.");
		return tmp[tmp.length-1].toLowerCase();
	}


}