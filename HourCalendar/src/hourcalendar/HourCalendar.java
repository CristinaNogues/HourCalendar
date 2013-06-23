/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author angelsafira1988
 */
public class HourCalendar {
    private static Base base;
    private static MainFrame mainFrame;
    private static FormulariInici formulariInici;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HourCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(HourCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HourCalendar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(HourCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        formulariInici = new FormulariInici();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                formulariInici.setLocation(dim.width/2-formulariInici.getSize().width/2, dim.height/2-formulariInici.getSize().height/2);
				formulariInici.setSize(470, 256);    //(435,206);
                formulariInici.setResizable(false);
				formulariInici.setVisible(true);
            }
        });
	}
    
    public static void inicialitza(int conjuntDeDades) {
        base = new Base(conjuntDeDades);
        mainFrame = new MainFrame();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainFrame.setVisible(true);
            }
        });
    }
    
    public static Base getBase() {
        return base;
    }
    
    public static MainFrame getMainFrame() {
        return mainFrame;
    }
    
    public static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        System.out.println(sourceLocation.getAbsolutePath().concat(" > ").concat(targetLocation.getAbsolutePath()));
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {

            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}





