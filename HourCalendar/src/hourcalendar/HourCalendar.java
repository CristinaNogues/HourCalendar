/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.BorderLayout;
import java.awt.Container;
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
        base = new Base();
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
}





