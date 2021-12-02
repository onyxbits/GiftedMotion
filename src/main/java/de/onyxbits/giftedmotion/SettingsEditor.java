package de.onyxbits.giftedmotion;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Display settings to be used for exporting the frames into the actual
 * animated GIF.
 */
public class SettingsEditor extends JInternalFrame implements ChangeListener,
ActionListener {

  /**
   * How often to repeat
   */
  private final JSpinner repeat = new JSpinner(new SpinnerNumberModel(0,-1,10000,1));
  
  /**
   * Quality of the dithering
   */
  private final JSpinner quality = new JSpinner(new SpinnerNumberModel(1,1,256,1));
  
  /**
   * The transparency color
   */
  private final JButton trans = new JButton(new ColorIcon(Color.MAGENTA,16,16));
  
  
  /**
   * The colorchooser for the transparency color
   */
  private final JColorChooser chooser = new JColorChooser(Color.MAGENTA);

  public SettingsEditor() {
    super(Dict.get("settingseditor.settingseditor.title"),false,true,false,false);
    setContentPane(getContent());
    chooser.setPreviewPanel(new JPanel());
    chooser.getSelectionModel().addChangeListener(this);
    trans.addActionListener(this);
    setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
    
    trans.setToolTipText(Dict.get("settingseditor.settingseditor.trans"));
    repeat.setToolTipText(Dict.get("settingseditor.settingseditor.repeat"));
    quality.setToolTipText(Dict.get("settingseditor.settingseditor.quality"));
    pack();
  }
  
  /**
   * Build the contentpane
   */
  private JPanel getContent() {
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content,BoxLayout.Y_AXIS));
    content.add(chooser);
    
    JPanel ctrl = new JPanel();
    ctrl.setLayout(new GridLayout(3,2));
    ctrl.add(new JLabel(Dict.get("settingseditor.getcontent.trans")));
    ctrl.add(trans);
    ctrl.add(new JLabel(Dict.get("settingseditor.getcontent.dither")));
    ctrl.add(quality);
    ctrl.add(new JLabel(Dict.get("settingseditor.getcontent.repeat")));
    ctrl.add(repeat);
    
    content.add(ctrl);
    return content;
  }
  
  /**
   * Query user preferences
   * @return a settings object, reflecting the user preferences
   */
  public Settings getSettings() {
    Settings ret = new Settings();
    ret.transparent=((ColorIcon)trans.getIcon()).getColor();
    ret.quality= (Integer) quality.getValue();
    ret.repeat= (Integer) repeat.getValue();
    return ret;
  }
  
  
  public void stateChanged(ChangeEvent evt) {
    trans.setIcon(new ColorIcon(chooser.getColor(),16,16));
  }
  
  public void actionPerformed(ActionEvent e) {
    trans.setIcon(new ColorIcon(null,16,16));
    chooser.setColor(null);
  }
  
}