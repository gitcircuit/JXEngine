/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.components;

/**
 *
 * @author Patel
 */
public class ListenerMethods {
  
  
  public void clearOutput (
        org.JXEngine jxe,
        javax.swing.JTextArea output
          ) {
 
   output.setText("");
  }

  // Called when you click and select tree node
   public void onclickJTreeNode(
     org.JXEngine jxe,
     javax.swing.JTree tree,
     javax.swing.JPanel display) {

    javax.swing.tree.DefaultMutableTreeNode selected = null;
    Object selectedinfo = null;
    javax.swing.tree.DefaultMutableTreeNode node = null;
    Object nodeinfo = null;

    demo.components.JTreeInfo treenodeinfo = null;
    Object xmlobject = null;

    try {
      selected = (javax.swing.tree.DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

      if (selected == null) {
        return;
      }

      selectedinfo = selected.getUserObject();

      node = (javax.swing.tree.DefaultMutableTreeNode) selectedinfo;
      nodeinfo = node.getUserObject();

      if (nodeinfo instanceof demo.components.JTreeInfo) {
        treenodeinfo = (demo.components.JTreeInfo ) node.getUserObject();

        xmlobject = treenodeinfo.xmlobject;
        if (xmlobject == null) {
          return;
        }


// IMPORTANT NOTE: For any layout out i.e. java.awt.BorderLayout
// You must, since BorderLayout only allows one component per area!
//  1. removeAll() component from a container
//  2. add component to a container
//  3. validate() 
        display.removeAll();
        display.add(((java.awt.Container) xmlobject));
      //     display.validate(); 
      } else {
        jxe.add(display, jxe.parse("/demo/Components/JPanelEmpty.xml"));
      }



    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      tree = null;
      display = null;
      selected = null;
      selectedinfo = null;
      node = null;
      nodeinfo = null;
      treenodeinfo = null;
      xmlobject = null;
    }
  }
   
   
   
   
   
   // Called when you hit enter key in JTextField
   public void onenterJTextField(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JTextField txtfield) {

    try {
      text.append("\r\nActionListener :" + txtfield.getName() + "  =>  " + txtfield.getText());
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      txtfield = null;
    }
  }
   
   // Called when you hit enter key in JPasswordField
   public void onenterJPasswordField(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     char[] password) {

    try {
      text.append("\r\nPassword is  : " + new String(password));
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
    }
  }
   
   
   // Called when you click JButton
  public void onclickJButton(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JButton button) {

    try {
      text.append("\r\nActionListener :" + button.getText());
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      button = null;
    }
  } 
  
  
  public void onclickJButtonGenericExample( 
          org.JXEngine jxe,
          javax.swing.JTextArea text,
          Tree<Integer> z
          ) {
      try {
        int branches = z.getNumBranches();
        text.append("\r\nMax Tree Total Branches :" + branches);
      } catch (Exception exc) {
          
      } finally {
          //GC
        jxe = null;
        text = null;
      }
  }
  
  
  // Called when you click JCheckBox
  public void onclickJCheckBox(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JCheckBox checkbox) {

    try {

      text.append("\r\nItemListener :" + checkbox.getText());
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      checkbox = null;
    }
  } 
  
  // Called when you click JRadioButton
  public void onclickJRadioButton(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JRadioButton radiobutton) {

    try {

      text.append("\r\nItemListener :" + radiobutton.getText());
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      radiobutton = null;
    }
  } 
  
  // Called when you click JComboBox
  public void onselectJComboBox(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JComboBox combobox) {

    try {
      String name = combobox.getName();
      String selected = (String)combobox.getSelectedItem();
      text.append("\r\nActionListener : Name : " + name + "  => " + selected);
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      combobox = null;
    }
  } 
  
  
  
  // Called when you click JSlider
  public void onchangeJSlider(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JSlider slider) {

    try {
      int value = (int)slider.getValue();
      text.append("\r\nChangeListener :  Moved @" + Integer.toString(value));
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      slider = null;
    }
  } 
  
  
  
  // Called when you click JList
  public void onclickJList(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JList list) {

    try {
      String value = (String)(list.getSelectedValue());
      text.append("\r\nMouseListener :  " + value);
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      list = null;
    }
  } 
  
  
  // Called when you click JSpinner
  public void onchangeJSpinner(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JSpinner spinner) {
     Object o = null;
    try {
      javax.swing.SpinnerModel model = spinner.getModel();
      if (model instanceof javax.swing.SpinnerListModel) {
         o = ((javax.swing.SpinnerListModel)model).getValue();
      }
      if (o != null) text.append(
                    "\r\nJSpinner : ChangeListener : Value = " + o.toString());
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      spinner = null;
      o = null;
    }
  } 
  
  
  
  // Called when you click JSpinner
  public void onclickJTable(
     org.JXEngine jxe,
     javax.swing.JTextArea text,
     javax.swing.JTable table) {
     String name = null;
    try {
      name = table.getName();
      text.append(
                    "\r\nJTable : MouseListener : Value = " + name);
    } catch (Exception exc) {

    } finally {
      //GC
      jxe = null;
      text = null;
      table = null;
      name = null;
    }
  }
}
