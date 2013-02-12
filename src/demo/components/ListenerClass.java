/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.components;

/**
 *
 * @author Patel
 */
public class ListenerClass implements java.awt.event.ActionListener,
                                      java.awt.event.ItemListener,
                                      javax.swing.event.ChangeListener,
                                      java.awt.event.MouseListener
                                      {
  public org.JXEngine jxe = null;
  public javax.swing.JTextArea output = null;
  
  private Object object = null;
  private String name = null;
  
  public ListenerClass() {}
  public ListenerClass(
          org.JXEngine jxe
          ) { 
    this.jxe = jxe;
  }
  
  
 
  
  public void actionPerformed(java.awt.event.ActionEvent e) {
    try {
      output = (javax.swing.JTextArea)jxe.find("output");
      object = e.getSource();
      name = ((java.awt.Container)object).getName();
      output.append( "\r\nClass Name : ListenerClass <=> Method Called : actionPerformed <=> Source Object : " + name );
    } catch (Exception exc) {
      
    } finally {
      output = null;
      object = null;
      name = null;
    }
    
	}
  
  
  public void itemStateChanged(java.awt.event.ItemEvent e) {
    try {
      output = (javax.swing.JTextArea)jxe.find("output");
      object = e.getSource();
      name = ((java.awt.Container)object).getName();
      output.append( "\r\nClass Name : ListenerClass <=> Method Called : itemStateChanged <=> Source Object : " + name );
    } catch (Exception exc) {
      
    } finally {
      output = null;
      object = null;
      name = null;
    }
  }
  
  
  
  public void stateChanged(javax.swing.event.ChangeEvent e) {
    try {
      output = (javax.swing.JTextArea)jxe.find("output");
      object = e.getSource();
      name = ((java.awt.Container)object).getName();
      output.append( "\r\nClass Name : ListenerClass <=> Method Called : stateChanged <=> Source Object : " + name );
    } catch (Exception exc) {
      
    } finally {
      output = null;
      object = null;
      name = null;
    }
  }
  
  
  
  
  public void mousePressed(java.awt.event.MouseEvent e) {

  }

  public void mouseReleased(java.awt.event.MouseEvent e) {

  }

  public void mouseEntered(java.awt.event.MouseEvent e) {

  }

  public void mouseExited(java.awt.event.MouseEvent e) {

  }

  public void mouseClicked(java.awt.event.MouseEvent e) {
   try {
      output = (javax.swing.JTextArea)jxe.find("output");
      object = e.getSource();
      name = ((java.awt.Container)object).getName();
      output.append( "\r\nClass Name : ListenerClass <=> Method Called : stateChanged <=> Source Object : " + name );
    } catch (Exception exc) {
      
    } finally {
      output = null;
      object = null;
      name = null;
    }
  }
  
  
  
}
