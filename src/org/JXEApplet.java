/**
 * JXEngine 
 * Engine that allows Java object’s to be define in XML format. 
 * Also act as plugging to any Java program, widget’s or toolkits.
 * 
 *  Copyright (C) 2013  Jwalin Patel
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/
 * 
 **/
package org;

/**
 *
 * @author Jwalin Patel
 */

public class JXEApplet extends javax.swing.JApplet {
 
    org.JXEngine x = new org.JXEngine();
    
   
    public JXEApplet() { 
        super();   
    }
    
    
    
    public void init() {
        try {
            x.add(this.getContentPane(), x.parse("/Demo/Main.xml")); 
            
           
        }
        catch (Exception ex){
          System.out.println(ex.getMessage());
        }
       
    }
    
    public void start() {
        // TODO:
    }
    
    public void stop() {
        // TODO:
      x.destroy();
    }
    
    public void destroy() {
        // TODO :
    }
    
    

    
    public static void main( String args[]) {
        int width, height;
        width = 550;
        height = 350;
        
        javax.swing.JFrame frame = new javax.swing.JFrame("JXEngine XML Demo");
        JXEApplet applet = new JXEApplet();

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent e) {}
            public void windowClosing(java.awt.event.WindowEvent we) {
                System.exit(0);
            }
            public void windowClosed(java.awt.event.WindowEvent e) {}
            public void windowIconified(java.awt.event.WindowEvent e) {}
            public void windowDeiconified(java.awt.event.WindowEvent e) {}
            public void windowActivated(java.awt.event.WindowEvent e) {}
            public void windowDeactivated(java.awt.event.WindowEvent e) {}
        });
        
        applet.setSize(width,height);
        
        applet.init();
        applet.start();
        
        frame.getContentPane().add(applet);
        
        frame.setSize(width,height);
        
        frame.setVisible(true);
 
    }
    
  
    
}
