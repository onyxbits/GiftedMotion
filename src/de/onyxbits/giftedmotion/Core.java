package de.onyxbits.giftedmotion;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;

/**
 * Core class
 */
public class Core extends JFrame implements WindowListener, ActionListener, 
ComponentListener, MouseMotionListener, MouseListener {

  /**
   * Program version as shown in the title
   */
  public static final String VERSION="GiftedMotion "+Package.getPackage("de.onyxbits.giftedmotion").getImplementationVersion();
  
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
   * Export as animated GIF
   */
  private JMenuItem export = new JMenuItem(Dict.get("core.export"),KeyEvent.VK_S);
  
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
  private JButton record = new JButton(IO.createIcon("Tango/22x22/actions/media-record.png",Dict.get("core.record")));
  
  /**
   * Import (same as load)
   */
  private JButton open = new JButton(IO.createIcon("Tango/22x22/actions/document-open.png",Dict.get("core.open")));
  
  /**
   * Toggle displaying of the settings window
   */
  private JButton togglesettings = new JButton(IO.createIcon("Tango/22x22/categories/preferences-desktop.png",Dict.get("core.togglesettings")));
  
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
   * Construct a new instance of the program. There may only be one object
   * of this class present.
   */
  public Core() {
    // Wire listeners up
    load.addActionListener(this);
    extract.addActionListener(this);
    export.addActionListener(this);
    quit.addActionListener(this);
    handbook.addActionListener(this);
    license.addActionListener(this);
    open.addActionListener(this);
    play.addActionListener(this);
    pause.addActionListener(this);
    record.addActionListener(this);
    togglesettings.addActionListener(this);
    
    // Fancy stuff
    quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,ActionEvent.CTRL_MASK));
    load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,ActionEvent.CTRL_MASK));
    export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,ActionEvent.CTRL_MASK));
    handbook.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
    open.setToolTipText(((ImageIcon)open.getIcon()).getDescription());
    play.setToolTipText(((ImageIcon)play.getIcon()).getDescription());
    pause.setToolTipText(((ImageIcon)pause.getIcon()).getDescription());
    togglesettings.setToolTipText(((ImageIcon)togglesettings.getIcon()).getDescription());
    record.setToolTipText(((ImageIcon)record.getIcon()).getDescription());
    pause.setEnabled(false);
    
    // Build menus
    JMenu file = new JMenu(Dict.get("core.core.file"));
    file.add(load);
    file.add(extract);
    file.add(export);
    file.add(new JSeparator());
    file.add(quit);
    
    JMenu help = new JMenu(Dict.get("core.core.help"));
    help.add(handbook);
    help.add(license);
    
    JMenuBar mbar = new JMenuBar();
    mbar.add(file);
    mbar.add(Box.createHorizontalGlue());
    mbar.add(help);
    setJMenuBar(mbar);
    
    // Build toolbar
    JToolBar tbar = new JToolBar();
    tbar.setRollover(true);
    tbar.setFloatable(true);
    tbar.add(open);
    tbar.add(togglesettings);
    
    tbar.addSeparator();
    tbar.add(play);
    tbar.add(pause);
    tbar.add(record);
    tbar.addSeparator();
    
    // Put all together and display
    JPanel content = new JPanel();
    content.setLayout(new BorderLayout());
    content.add(tbar,BorderLayout.NORTH);
    content.add(workspace,BorderLayout.CENTER);
    content.add(status,BorderLayout.SOUTH);
    setContentPane(content);
    workspace.add(setedit);
    postStatus("");
  }
  
  /**
   ** Various event listeners interface implementations
   **/

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
    if (src == handbook) handleHandbook();
    if (src == license) handleLicense();
    if (src == play || src == pause ) handlePlayPause();
    if (src == togglesettings ) handleTogglesettings();
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
    Integer pos[] = {
      new Integer(seq.selected.position.x),
      new Integer(seq.selected.position.y)
    };
    postStatus(Dict.get("core.mousedragged",pos));
  }
  
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  
  public void mousePressed(MouseEvent e) {
    if (seq.selected==null) return;
    Integer pos[] = {
      new Integer(seq.selected.position.x),
      new Integer(seq.selected.position.y)
    };
    postStatus(Dict.get("core.mousepressed",pos));
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
  
  public void handleLoad() {
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
      setFrameSequence(new FrameSequence(frames));
      if (frames.length==1) {
        JOptionPane.showInternalMessageDialog(workspace,Dict.get("core.handleload.singlefile.txt"));
      }
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
      if (jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
      File dest=jfc.getSelectedFile();
      directory=jfc.getCurrentDirectory();
      IO.extract(seq,dest);
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
  
  public void handlePlayPause() {
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
    new Dict();
    app = new Core();
    app.setSize(new Dimension(800,600));
    app.setTitle(VERSION);
    CatchOldJava.decorateWindow(app);
    
    app.setVisible(true);
    app.addWindowListener(app);
    
    // If commandlinearguments are given, try to load them as files
    // This feature is intended for developer use and may change or go
    // away in future versions.
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

}