package de.onyxbits.giftedmotion;
import java.io.File;

/**
 * FileFilter, that only allows imagefiles and optionally directories
 */
public class ImageFileFilter extends javax.swing.filechooser.FileFilter 
implements java.io.FileFilter {

  private String ext[] = {"PNG","JPG","JPEG","GIF","BMP"};
  private boolean diraccept;
  
  /**
   * Create a new FileFilter
   * @param diraccept whether or not to also accept directories
   */
  public ImageFileFilter(boolean diraccept) {
    this.diraccept=diraccept;
  }
  
  public String getDescription() {
    return Dict.get("imagefilefilter.desc");
  }
  
  public boolean accept(File f) {
    if (diraccept && f.isDirectory()) return true;
    for (int i=0;i<ext.length;i++) {
      if (f.getName().toUpperCase().endsWith(ext[i])) return true;
    }
    return false;
  }
}
