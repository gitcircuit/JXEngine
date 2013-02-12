/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.components;

/**
 *
 * @author Patel
 */
public class JTreeInfo  {

  public String name; 
  public Object xmlobject; 

  public JTreeInfo() {
  }

  public JTreeInfo(String name, Object xmlobject) {

    this.name = name;
    this.xmlobject = xmlobject;
  }

  public String toString() {
    return name;
  }
}
