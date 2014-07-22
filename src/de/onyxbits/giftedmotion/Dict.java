package de.onyxbits.giftedmotion;
import java.util.*;
import java.text.*;

/**
 * This class is responsible for i18n
 */
public class Dict  {

  /**
   * The "classname" of the resource containing the translations
   */
  public static final String RSRCNAME = "resources.i18n";
  
  /**
   * Contains the translations
   */
  private static ResourceBundle trans;
  
  
  /**
   * Load the bundle
   */
  public Dict() {
    trans = ResourceBundle.getBundle(RSRCNAME);
  }
  
  /**
   * Fetch a key from the i18n file
   * @return the translation for key
   */
  public static String get(String key) {
    return get(key,null);
  }
  
  /**
   * Fetch a key from the i18n file and replace its variables with values
   * @param args the replacement values of the variables in the string
   */
  public static String get(String key, Object[] args) {
    String val=null;
    try {
      val = trans.getString(key);
    }
    catch (Exception e) {
      System.err.println("Missing translation: "+key);
      val="";
    }
    
    if (args==null) return val;
    else {
      MessageFormat formatter = new MessageFormat(val);
      return formatter.format(args);
    }
  }
  
  /**
   * Fetch a key from the i18n file and replace its variable with a value
   * @param args the replacement value of the variable in the string
   */
  public static String get(String key, Object arg) {
    Object[] tmp = {arg};
    return get(key,tmp);
  }
    
}