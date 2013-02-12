// Example header
 /**
   * Parse xml and return root node component handle 
   *
   * @param xml <code>String</code> xml-file path info
   * @return <code>Object</code>- handle to a create java root object
   * <p/>
   * <p><b>Note:</b><br>
   *  This is the object used through introspection the actions and fileds are set.
   * </p>
   */ 

// JXBlend, JXMachine, JXPro
package org;

/**
 *
 * @author Patel
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
