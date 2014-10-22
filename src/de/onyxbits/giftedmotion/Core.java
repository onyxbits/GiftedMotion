package de.onyxbits.giftedmotion;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.spi.IIORegistry;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Core class
 */
public class Core extends JFrame implements WindowListener, ActionListener, 
ComponentListener, MouseMotionListener, MouseListener, DropTargetListener {

	/**
	 * Program version as shown in the title
	 */
	public static final String VERSION="GiftedMotion "+((Package.getPackage("de.onyxbits.giftedmotion").getImplementationVersion()!=null) ? Package.getPackage("de.onyxbits.giftedmotion").getImplementationVersion() : "");

	/**
	 * Back reference to the running program
	 */
	public static Core app;

	/**
	 * Quit program
	 */
	private JMenuItem quit = new JMenuItem(Dict.get("core.quit"),KeyEvent.VK_Q);

	/**
	 * Load files
	 */
	private JMenuItem load = new JMenuItem(Dict.get("core.load"),KeyEvent.VK_L);
	
	/**
	 * Close project
	 */
	private JMenuItem close = new JMenuItem(Dict.get("core.close"),KeyEvent.VK_L);
	
	/**
	 * Export as animated GIF
	 */
	private JMenuItem export = new JMenuItem(Dict.get("core.export"),KeyEvent.VK_S);

	/**
	 * Export as deoptimized GIF
	 */
	private JMenuItem deoptimize = new JMenuItem(Dict.get("core.deoptimize"));
	
	/**
	 * Save the sequence as individual files
	 */
	private JMenuItem extract = new JMenuItem(Dict.get("core.extract"),KeyEvent.VK_E);

	/**
	 * Display license
	 */
	private JMenuItem license = new JMenuItem(Dict.get("core.license"));

	/**
	 * Go to homepage
	 */
	private JMenuItem handbook = new JMenuItem(Dict.get("core.handbook"));

	/**
	 * Go to FAQ
	 */
	private JMenuItem faq = new JMenuItem(Dict.get("core.faq"));

	/**
	 * Play animation
	 */
	private JButton play = new JButton(IO.createIcon("Tango/22x22/actions/media-playback-start.png",Dict.get("core.play")));

	/**
	 * Pause animation
	 */
	private JButton pause = new JButton(IO.createIcon("Tango/22x22/actions/media-playback-pause.png",Dict.get("core.pause")));

	/**
	 * Record (same as export)
	 */
	private JButton record = new JButton(IO.createIcon("Tango/22x22/actions/document-save.png",Dict.get("core.record")));

	/**
	 * Import (same as load)
	 */
	private JButton open = new JButton(IO.createIcon("Tango/22x22/actions/document-open.png",Dict.get("core.open")));
	
	/**
	 * Close project button
	 */
	private JButton closeButton = new JButton(IO.createIcon("Tango/22x22/actions/system-log-out.png", Dict.get("core.close")));

	/**
	 * Toggle displaying of the settings window
	 */
	private JButton togglesettings = new JButton(IO.createIcon("Tango/22x22/categories/preferences-desktop.png",Dict.get("core.togglesettings")));

	/**
	 * Drag tool
	 */
	private JToggleButton dragButton = new JToggleButton(IO.createIcon("Misc/Drag.png", Dict.get("core.dragtool")));
	
	/**
	 * Rotate tool
	 */
	private JToggleButton rotateButton = new JToggleButton(IO.createIcon("Misc/Rotate.png", Dict.get("core.rotatetool")));
	
	/**
	 * Resize tool
	 */
	private JToggleButton resizeButton = new JToggleButton(IO.createIcon("Misc/Resize.png", Dict.get("core.scaletool")));
	
	/**
	 * Onionskin button
	 */
	private JToggleButton onionButton = new JToggleButton(IO.createIcon("Misc/onion.png", Dict.get("core.onion")));
	
	/**
	 * Sequence Editor
	 */
	private SequenceEditor seqedit;

	/**
	 *  Frame Display
	 */
	private FrameDisplay display;

	/**
	 * Settings editor
	 */
	private SettingsEditor setedit = new SettingsEditor(); 

	/**
	 * The main workspace
	 */
	private JDesktopPane workspace = new JDesktopPane();

	/**
	 * The framesequence being worked upon
	 */
	private FrameSequence seq;

	/**
	 * Directory, to open filedialogs with
	 */
	private File directory = new File(System.getProperty("user.dir"));

	/**
	 * Used for doing an animation preview
	 */
	private Player player;

	/**
	 * For displaying status messages
	 */
	private JLabel status = new JLabel();

	/**
	 * Button group for tools
	 */
	private ButtonGroup toolGroup = new ButtonGroup();
	
	
	/**
	 * Construct a new instance of the program. There may only be one object
	 * of this class present.
	 */
	public Core() {
		//Mac OS compatibility things (fullscreen mode, icon setting)
		if (MacOSCompat.isMacOSX())
			MacOSCompat.enableFullScreenMode(this);
		
		//Create dragdrop stuff
		new DropTarget(this, this);

		// Wire listeners up
		load.addActionListener(this);
		extract.addActionListener(this);
		export.addActionListener(this);
		deoptimize.addActionListener(this);
		quit.addActionListener(this);
		faq.addActionListener(this);
		handbook.addActionListener(this);
		license.addActionListener(this);
		open.addActionListener(this);
		closeButton.addActionListener(this);
		play.addActionListener(this);
		pause.addActionListener(this);
		record.addActionListener(this);
		togglesettings.addActionListener(this);
		close.addActionListener(this);
		dragButton.addActionListener(this);
		rotateButton.addActionListener(this);
		resizeButton.addActionListener(this);
		onionButton.addActionListener(this);

		// Fancy stuff
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.CTRL_MASK));
		export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
		handbook.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
		open.setToolTipText(((ImageIcon)open.getIcon()).getDescription());
		closeButton.setToolTipText(((ImageIcon)closeButton.getIcon()).getDescription());
		play.setToolTipText(((ImageIcon)play.getIcon()).getDescription());
		pause.setToolTipText(((ImageIcon)pause.getIcon()).getDescription());
		togglesettings.setToolTipText(((ImageIcon)togglesettings.getIcon()).getDescription());
		record.setToolTipText(((ImageIcon)record.getIcon()).getDescription());
		dragButton.setToolTipText(((ImageIcon)(dragButton.getIcon())).getDescription());
		resizeButton.setToolTipText(((ImageIcon)(resizeButton.getIcon())).getDescription());
		rotateButton.setToolTipText(((ImageIcon)(rotateButton.getIcon())).getDescription());
		onionButton.setToolTipText(((ImageIcon)(onionButton.getIcon())).getDescription());
		
		pause.setEnabled(false);
		status.setBorder(new BevelBorder(BevelBorder.LOWERED));

		// Build menus
		JMenu file = new JMenu(Dict.get("core.core.file"));
		file.add(load);
		file.add(export);
		file.add(close);
		file.add(new JSeparator());
		file.add(extract);
		file.add(deoptimize);
		file.add(new JSeparator());
		file.add(quit);

		JMenu help = new JMenu(Dict.get("core.core.help"));
		help.add(handbook);
		help.add(faq);
		help.add(new JSeparator());
		help.add(license);

		JMenuBar mbar = new JMenuBar();
		mbar.add(file);
		mbar.add(Box.createHorizontalGlue());
		mbar.add(help);
		mbar.setBorder(new BevelBorder(BevelBorder.RAISED));
		setJMenuBar(mbar);

		// Build toolbar
		JToolBar tbar = new JToolBar();
		tbar.setRollover(true);
		tbar.setFloatable(true);
		tbar.add(open);
		tbar.add(record);
		tbar.add(closeButton);
		tbar.add(togglesettings);

		tbar.addSeparator();
		tbar.add(play);
		tbar.add(pause);
		tbar.addSeparator();

		toolGroup.add(dragButton);
		toolGroup.add(rotateButton);
		toolGroup.add(resizeButton);
		
		tbar.add(dragButton);
		tbar.add(rotateButton);
		tbar.add(resizeButton);
		tbar.addSeparator();

		tbar.add(onionButton);
		
		dragButton.setSelected(true);
		
		//Enable/disable buttons
		disableButtons();
		
		// Put all together and display
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		content.add(tbar,BorderLayout.NORTH);
		content.add(workspace,BorderLayout.CENTER);
		content.add(status,BorderLayout.SOUTH);
		setContentPane(content);
		workspace.add(setedit);
		workspace.setDesktopManager(new BoundedDesktopManager());
		postStatus("");
	}
	
	//UI Disabling/Enabling

	/**
	 ** Various event listeners interface implementations
	 **/
	
	public void enableButtons()
	{
		play.setEnabled(true);
		record.setEnabled(true);
		close.setEnabled(true);
		export.setEnabled(true);
		extract.setEnabled(true);
		togglesettings.setEnabled(true);
		dragButton.setEnabled(true);
		rotateButton.setEnabled(true);
		closeButton.setEnabled(true);
		resizeButton.setEnabled(true);
		onionButton.setEnabled(true);
		deoptimize.setEnabled(true);
	}
	
	public void disableButtons()
	{
		play.setEnabled(false);
		record.setEnabled(false);
		close.setEnabled(false);
		export.setEnabled(false);
		extract.setEnabled(false);
		togglesettings.setEnabled(false);
		dragButton.setEnabled(false);
		rotateButton.setEnabled(false);
		closeButton.setEnabled(false);
		resizeButton.setEnabled(false);
		onionButton.setEnabled(false);
		deoptimize.setEnabled(false);
	}

	public void windowClosing(WindowEvent e) { handleQuit(); }
	public void focusLost(FocusEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}


	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if (src == quit) handleQuit();
		if (src == load || src == open ) handleLoad();
		if (src == extract) handleExtract();
		if (src == export || src == record) handleExport();
		if (src == deoptimize) handleDeoptimize();
		if (src == handbook) handleHandbook();
		if (src == faq) handleFAQ();
		if (src == license) handleLicense();
		if (src == play || src == pause ) handlePlayPause();
		if (src == togglesettings ) handleTogglesettings();
		if (src == close || src == closeButton) handleClose();
		if (src == dragButton) display.getCanvas().setTool(new DragTool());
		if (src == rotateButton) display.getCanvas().setTool(new RotateTool());
		if (src == resizeButton) display.getCanvas().setTool(new ScaleTool());
		if (src == onionButton) display.getCanvas().setOnionskin(onionButton.isSelected());
	}

	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}

	public void componentResized(ComponentEvent e) {
		Component c = (Component)e.getSource();
		Integer size[] = {
				new Integer(c.getWidth()),
				new Integer(c.getHeight())
		};
		postStatus(Dict.get("core.componentresized",size));
	}

	public void mouseMoved(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
		if (seq.selected==null) return;
		postStatus(display.getCanvas().getTool().getStatus(seq.selected));
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (seq.selected==null) return;
		postStatus(display.getCanvas().getTool().getStatus(seq.selected));
	}

	public void mouseReleased(MouseEvent e) {
		postStatus("");
	}  

	/**
	 ** Handlers for events created by GUI elements
	 **/

	public void handleQuit() {
		System.exit(0);
	}

	public void handleLoad() { //TODO: This should be refactored into some load function or something.
		try {
			postStatus(Dict.get("core.handleload.hint"));
			LoadAccessory loadAccessory = new LoadAccessory();
			
			JFileChooser jfc = new JFileChooser(directory);
			jfc.setMultiSelectionEnabled(true);
			jfc.setAccessory(loadAccessory);
			jfc.addPropertyChangeListener(loadAccessory);
			jfc.addChoosableFileFilter(new ImageFileFilter(true));
			if (jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
				postStatus("");
				return;
			}
			postStatus("");

			directory=jfc.getCurrentDirectory();

			File[] selected = jfc.getSelectedFiles();
			if (selected.length==1 && selected[0].isDirectory()) {
				jfc.setCurrentDirectory(selected[0]);
				return;
			}

			SingleFrame[] frames = IO.load(selected);
			if (frames==null || frames.length==0) {
				postStatus(Dict.get("core.handleload.nothing"));
				return;
			}
			
			if (seq != null)
				for (int i = 0; i < frames.length; i++)
					seq.add(frames[i], seq.frames.length);
			
			else setFrameSequence(new FrameSequence(frames));
			
			enableButtons();
			
			dragButton.setSelected(true);
			onionButton.setSelected(false);
			display.getCanvas().setOnionskin(false);
		}
		catch (IllegalArgumentException exp) {
			postStatus(Dict.get("core.handleload.illegalargumentexception",exp.getMessage()));
		}
		catch (IOException exp) {
			postStatus(exp.getMessage());
		}
		catch (Exception exp) {
			postStatus(Dict.get("core.handleload.exception"));
			exp.printStackTrace();
		}
	}

	public void handleExtract() {
		if (seq==null || seq.frames.length==0) {
			postStatus(Dict.get("core.handleextract.nothing"));
			return;
		}

		try {
			JFileChooser jfc = new JFileChooser(directory);
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jfc.setAcceptAllFileFilterUsed(false);
			if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
			directory=jfc.getCurrentDirectory();
			IO.extract(seq, directory, display.getCanvas().getSize(), setedit.getSettings());
			postStatus(Dict.get("core.handleextract.saved"));
		}
		catch (IOException e) {
			postStatus(e.getMessage());
		}
	}

	public void handleExport() {
		try {
			postStatus("");
			if (seq==null) {
				postStatus(Dict.get("core.handleexport.nothing"));
				return;
			}
			JFileChooser jfc = new JFileChooser(directory);
			jfc.setSelectedFile(new File(Dict.get("core.handleexport.defaultname")));
			if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
			File dest=jfc.getSelectedFile();
			directory=jfc.getCurrentDirectory();

			Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
			setCursor(hourglassCursor);
			postStatus(Dict.get("core.handleexport.saving")); // No idea why this does not show!
			IO.export(dest,seq,display.getCanvas().getSize(),setedit.getSettings());
			postStatus(Dict.get("core.handleexport.finished"));
			hourglassCursor = new Cursor(Cursor.DEFAULT_CURSOR);
			setCursor(hourglassCursor);
		}
		catch (FileNotFoundException exp) {
			postStatus(Dict.get("core.handleexport.filenotfoundexception",exp.getMessage()));
		}
		catch(Exception exp) {
			postStatus(Dict.get("core.handleexport.exception"));
			exp.printStackTrace();
		}
	}
	
	public void handleDeoptimize()
	{
		Dimension size = display.getCanvas().getSize();
		BufferedImage outputBuf = new BufferedImage((int)size.getWidth(), (int)size.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int i=0;i<seq.frames.length;i++) {
			Graphics g = outputBuf.getGraphics();
			SingleFrame f = seq.frames[i];
			f.paint(g);
			f.raw = Util.copyImage(outputBuf);
			
			//Change all gif loaded settings to how it would be rendered
			f.position = new Point(0, 0);
			f.scaleX = (int)size.getWidth();
			f.scaleY = (int)size.getHeight();
		}
	}

	public void handleLicense() {
		try {
			JInternalFrame jif = new JInternalFrame("",false,true,false,false);
			JEditorPane txt = new JEditorPane(getClass().getClassLoader().getResource("resources/LICENSE"));
			txt.setEditable(false);
			jif.setContentPane(new JScrollPane(txt));
			workspace.add(jif);
			jif.setMaximum(true);
			jif.show();
		}
		catch (Exception exp) {
			// ?!
			exp.printStackTrace();
		}
	}

	public void handleHandbook() {
		String url = "http://www.onyxbits.de/giftedmotion/handbook";
		try {
			// Wrap this
			CatchOldJava.openBrowser(url);
		}
		catch (Exception exp) {
			JOptionPane.showInternalMessageDialog(workspace,Dict.get("core.handlehandbook.text",url),Dict.get("core.handlehandbook.title"), JOptionPane.ERROR_MESSAGE);
		}
	}


	public void handleFAQ() {
		String url = "http://www.onyxbits.de/faq/giftedmotion";
		try {
			// Wrap this
			CatchOldJava.openBrowser(url);
		}
		catch (Exception exp) {
			JOptionPane.showInternalMessageDialog(workspace,Dict.get("core.handlehandbook.text",url),Dict.get("core.handlehandbook.title"), JOptionPane.ERROR_MESSAGE);
		}
	}

	public void handlePlayPause() {
		onionButton.setSelected(false);
		display.getCanvas().setOnionskin(false);
		
		try {
			if (play.isEnabled()) {
				player = new Player(seq, 0);
				player.start();
				play.setEnabled(false);
				pause.setEnabled(true);
			}
			else {
				player.interrupt();
				player.join();
				play.setEnabled(true);
				pause.setEnabled(false);
			}
		}
		catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	public void handleTogglesettings() {
		if (setedit.isVisible()) setedit.setVisible(false);
		else setedit.setVisible(true);
	}
	
	public void handleClose()
	{
		seqedit.dispose();
		display.dispose();
		seq = null;
		disableButtons();
	}

	/**
	 **  Utility functions
	 **/

	/**
	 * Dispatch a new FrameSequence to the application
	 * @param seq the sequence to distribute to all gui elements
	 */
	public void setFrameSequence(FrameSequence seq) {
		if (seq==null) return;
		else this.seq=seq;

		if (seqedit!=null) seqedit.dispose();
		if (display!=null) display.dispose();

		seqedit = new SequenceEditor(seq);
		seq.addFrameSequenceListener(seqedit);
		workspace.add(seqedit);
		seqedit.setLocation(5,5);
		seqedit.show();

		FrameCanvas canvas = new FrameCanvas(seq);
		canvas.addComponentListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addMouseListener(this);
		seq.addFrameSequenceListener(canvas);
		display = new FrameDisplay(canvas);
		workspace.add(display);
		display.setLocation(seqedit.getSize().width+10,5);
		display.show();
	}

	/**
	 * Post a message to the status bar
	 * @param message message to post
	 */
	public void postStatus(String message) {
		if (message.equals("")) {
			status.setText(" ");
		}
		else {
			status.setText(message);
		}
	}


	public static void main(String args[]) {
		//Register TGA plugin
		//IIORegistry registry = IIORegistry.getDefaultInstance();
		//registry.registerServiceProvider(new com.realityinteractive.imageio.tga.TGAImageReaderSpi());
		
		if (MacOSCompat.isMacOSX())
		{
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			MacOSCompat.setAppIcon(new ImageIcon(ClassLoader.getSystemResource("resources/logo-96x96.png")).getImage());
		}
		
		/*try
		{
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		new Dict();
		app = new Core();
		app.setSize(new Dimension(800,600));
		app.setTitle(VERSION);
		app.setLocationRelativeTo(null);

		CatchOldJava.decorateWindow(app);

		app.setVisible(true);
		app.addWindowListener(app);

		// If commandlinearguments are given, try to load them as files. This isn't going away.
		if (args!=null && args.length!=0) {
			File[] f = new File[args.length];
			for (int i=0;i<args.length;i++) {
				f[i]=new File(args[i]);
			}
			try {
				SingleFrame[] frames = IO.load(f);
				app.setFrameSequence(new FrameSequence(frames));
			}
			catch (Exception exp) {
				exp.printStackTrace();
			}
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void drop(DropTargetDropEvent dtde)
	{
		dtde.acceptDrop(DnDConstants.ACTION_COPY);

		//Load dragged file
		Transferable transferable = dtde.getTransferable();
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		
		try 
		{

			for (DataFlavor flavor : flavors) {
				if (flavor.isFlavorJavaFileListType()) {
					@SuppressWarnings("unchecked")
					List<File> files = (List<File>)transferable.getTransferData(flavor);

					SingleFrame[] frames = IO.load(files.toArray(new File[files.size()]));
					if (frames==null || frames.length==0) {
						postStatus(Dict.get("core.handleload.nothing"));
						return;
					}
					
					if (seq != null)
						for (int i = 0; i < frames.length; i++)
							seq.add(frames[i], seq.frames.length);
					else setFrameSequence(new FrameSequence(frames));
					
				}
			}
			
			enableButtons();
			dragButton.setSelected(true);
			onionButton.setSelected(false);
			display.getCanvas().setOnionskin(false);

			dtde.dropComplete(true);
		}
		catch (IllegalArgumentException exp) {
			postStatus(Dict.get("core.handleload.illegalargumentexception",exp.getMessage()));
		}
		catch (IOException exp) {
			postStatus(exp.getMessage());
		}
		catch (Exception exp) {
			postStatus(Dict.get("core.handleload.exception"));
			exp.printStackTrace();
		}
	}
}