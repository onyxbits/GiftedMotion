package de.onyxbits.giftedmotion;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static de.onyxbits.giftedmotion.IO.*;

/**
 * Core class
 */
public class Core extends JFrame implements WindowListener, ActionListener,
        ComponentListener, MouseMotionListener, MouseListener {

  /** Program version as shown in the title */
  public static final String VERSION="GiftedMotion "+ Core.class.getClassLoader().getDefinedPackage("de.onyxbits.giftedmotion").getImplementationVersion();

  /** Quit program */
  private final JMenuItem quit = new JMenuItem(Dict.get("core.quit"),KeyEvent.VK_Q);
  
  /** Load files */
  private final JMenuItem load = new JMenuItem(Dict.get("core.load"),KeyEvent.VK_L);
  
  /** Export as animated GIF */
  private final JMenuItem export = new JMenuItem(Dict.get("core.export"),KeyEvent.VK_S);
  
  /** Save the sequence as individual files */
  private final JMenuItem extract = new JMenuItem(Dict.get("core.extract"),KeyEvent.VK_E);
  
  /** Display license */
  private final JMenuItem license = new JMenuItem(Dict.get("core.license"));
  
  /** Go to homepage */
  private final JMenuItem handbook = new JMenuItem(Dict.get("core.handbook"));
  
  /** Go to FAQ */
  private final JMenuItem faq = new JMenuItem(Dict.get("core.faq"));
  
  /** Play animation */
  private final JButton play = new JButton(createIcon("Tango/22x22/actions/media-playback-start.png",Dict.get("core.play")));
  
  /** Pause animation */
  private final JButton pause = new JButton(createIcon("Tango/22x22/actions/media-playback-pause.png",Dict.get("core.pause")));
  
  /** Record (same as export) */
  private final JButton record = new JButton(createIcon("Tango/22x22/actions/media-record.png",Dict.get("core.record")));
  
  /** Import (same as load) */
  private final JButton open = new JButton(createIcon("Tango/22x22/actions/document-open.png",Dict.get("core.open")));
  
  /** Toggle displaying of the settings window */
  private final JButton togglesettings = new JButton(createIcon("Tango/22x22/categories/preferences-desktop.png",Dict.get("core.togglesettings")));
  
  /** Sequence Editor */
  private SequenceEditor seqedit;
  
  /** Frame Display */
  private FrameDisplay display;
  
  /** Settings editor */
  private final SettingsEditor setedit = new SettingsEditor();
  
  /** The main workspace */
  private final JDesktopPane workspace = new JDesktopPane();
  
  /** The framesequence being worked upon */
  private FrameSequence seq;
  
  /** Directory, to open filedialogs with */
  private File directory = new File(System.getProperty("user.dir"));
  
  /** Used for doing an animation preview */
  private Player player;
  
  /** For displaying status messages */
  private final JLabel status = new JLabel();
  
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
    faq.addActionListener(this);
    handbook.addActionListener(this);
    license.addActionListener(this);
    open.addActionListener(this);
    play.addActionListener(this);
    pause.addActionListener(this);
    record.addActionListener(this);
    togglesettings.addActionListener(this);
    
    // Fancy stuff
    quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
    load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
    export.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
    handbook.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
    open.setToolTipText(((ImageIcon)open.getIcon()).getDescription());
    play.setToolTipText(((ImageIcon)play.getIcon()).getDescription());
    pause.setToolTipText(((ImageIcon)pause.getIcon()).getDescription());
    togglesettings.setToolTipText(((ImageIcon)togglesettings.getIcon()).getDescription());
    record.setToolTipText(((ImageIcon)record.getIcon()).getDescription());
    pause.setEnabled(false);
    status.setBorder(new BevelBorder(BevelBorder.LOWERED));
    
    // Build menus
    JMenu file = new JMenu(Dict.get("core.core.file"));
    file.add(load);
    file.add(extract);
    file.add(export);
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
    if (src == faq) handleFAQ();
    if (src == license) handleLicense();
    if (src == play || src == pause ) handlePlayPause();
    if (src == togglesettings ) handleTogglesettings();
  }
  
  public void componentHidden(ComponentEvent e) {}
  public void componentMoved(ComponentEvent e) {}
  public void componentShown(ComponentEvent e) {}
  
  public void componentResized(ComponentEvent e) {
    Component c = (Component)e.getSource();
    Integer[] size = {
            c.getWidth(),
            c.getHeight()
    };
    postStatus(Dict.get("core.componentresized",size));
  }
  
  public void mouseMoved(MouseEvent e) {}
  public void mouseDragged(MouseEvent e) {
    if (seq.selected==null) return;
    Integer[] pos = {
            seq.selected.position.x,
            seq.selected.position.y
    };
    postStatus(Dict.get("core.mousedragged",pos));
  }
  
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}
  
  public void mousePressed(MouseEvent e) {
    if (seq.selected==null) return;
    Integer[] pos = {
            seq.selected.position.x,
            seq.selected.position.y
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
      
      SingleFrame[] frames = load(selected);
      if (frames.length == 0) {
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
      extract(seq,dest);
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
      export(dest,seq,display.getCanvas().getSize(),setedit.getSettings());
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
    setedit.setVisible(!setedit.isVisible());
  }
  

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
  

  public static void main(String[] args) {
    Dict.init();

    EventQueue.invokeLater(() -> startApp(args));
  }

  private static void startApp(String[] args) {
    Core app = new Core();
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
        SingleFrame[] frames = load(f);
        app.setFrameSequence(new FrameSequence(frames));
      }
      catch (Exception exp) {
        exp.printStackTrace();
      }
    }
  }

}