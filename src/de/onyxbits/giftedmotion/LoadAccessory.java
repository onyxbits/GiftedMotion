package de.onyxbits.giftedmotion;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.beans.*;

/**
 * Accessory for the Loading dialog
 */
public class LoadAccessory extends JPanel implements PropertyChangeListener {

  /**
   * Lets the user pick a default showtime for each frame
   */
  //private JSpinner showtime = new JSpinner (new SpinnerNumberModel(100,0,1000000,10));
  
  /**
   * Image preview canvas
   */
  private Preview preview = new Preview();
  
  public LoadAccessory() {
    setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    JScrollPane cont1 = new JScrollPane(preview);
    cont1.setPreferredSize(new Dimension(200,150));
    add(cont1);
    cont1.setBorder(BorderFactory.createTitledBorder(Dict.get("loadaccessory.loadaccessory.preview")));
  }
  
  // Interface implemented
  public void propertyChange(PropertyChangeEvent event) {
    if(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(event.getPropertyName())) {
      File f = (File) event.getNewValue();
      preview.show(f);
    }
    if(JFileChooser.SELECTED_FILES_CHANGED_PROPERTY.equals(event.getPropertyName())) {
      File[] f = (File[]) event.getNewValue();
      if (f==null || f.length==0) preview.show(null);
      else preview.show(f[f.length-1]);
    }
  }
}