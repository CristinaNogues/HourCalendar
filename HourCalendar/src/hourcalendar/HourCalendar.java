/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hourcalendar;

import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author angelsafira1988
 */
public class HourCalendar {
    private static Base base;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        base = new Base();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
	}
    
    public static Base getBase() {
        return base;
    }
}





