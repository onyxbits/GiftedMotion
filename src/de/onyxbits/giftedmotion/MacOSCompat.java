package de.onyxbits.giftedmotion;

import java.awt.Image;
import java.awt.Window;
import java.lang.reflect.Method;

//import com.apple.eawt.Application;

public class MacOSCompat
{
	public static boolean isMacOSX() {
        return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
    }
	
	public static void enableFullScreenMode(Window window) {
        String className = "com.apple.eawt.FullScreenUtilities";
        String methodName = "setWindowCanFullScreen";
 
        try {
            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, new Class<?>[] {
                    Window.class, boolean.class });
            method.invoke(null, window, true);
        } catch (Throwable t) {
            System.err.println("Full screen mode is not supported");
            t.printStackTrace();
        }
    }
	
	public static void setAppIcon(Image i)
	{
		//Leave it out for now.
		//Application application = Application.getApplication();
		//application.setDockIconImage(i);
	}
	
	
}
