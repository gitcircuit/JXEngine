/**
 * JXEngine 
 * Engine that allows Java object’s to be define in XML format. 
 * Also act as plugging to any Java program, widget’s or toolkits.
 * 
 * The MIT License
 *
 * Copyright © 2013 Jwalin Patel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this 
 * software and associated documentation files (the “Software”), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge, 
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons 
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * 
 */
package org;


/**
 * Class that converts XML description into Java objects.
 * @version $Revision: 1.01 $
 */
public class JXEngine {
  /**
   * Resource Bundle
   */
  private transient java.util.ResourceBundle resourcebundle = null;
  /**
   * Handle to Root Panel i.e. Application main frame.
   */
  private Object rootpane = null;
 
  /**
   * Constructor for JXEngine
   * @param resource <code>String</code> TODO:
   */
  public JXEngine() { 
    resourcebundle = null;
    rootpane = null;
  }
  
  /**
   * Destructor for cleaning up resource held by JXEngine
   */
  public void destroy () {
    resourcebundle = null; //GC
    rootpane = null; //GC
  }
  
  
  
  
////////////////////////////////////////////////////////////////////////////////  
  
  private void renderTreeNode ( 
          Object oparent,
          Object ochild,
          Object otree
          ) {
 
  javax.swing.tree.DefaultMutableTreeNode parent =
                            ((javax.swing.tree.DefaultMutableTreeNode)oparent);
  javax.swing.tree.DefaultMutableTreeNode child =
                            ((javax.swing.tree.DefaultMutableTreeNode)ochild);
  
  javax.swing.JTree tree = ((javax.swing.JTree)otree);
  
  javax.swing.tree.DefaultTreeModel model = 
                          ((javax.swing.tree.DefaultTreeModel)tree.getModel());
  javax.swing.tree.DefaultMutableTreeNode node = 
                    ((javax.swing.tree.DefaultMutableTreeNode)model.getRoot());
  
  
  if (parent == null) parent = node;
  
  model.insertNodeInto(  child, parent, parent.getChildCount());
  tree.scrollPathToVisible (new javax.swing.tree.TreePath(child.getPath()));
  
  
  parent = null; //GC
  child = null; //GC
  tree = null; //GC
  model = null; //GC
  node = null; //GC
  oparent = null; //GC
  ochild = null; //GC
  
  }
  

////////////////////////////////////////////////////////////////////////////////
  
  public Object find (
          String name
          ) {
     return find(rootpane, name);
  } 
  
  public Object find (
          Object rootpane, 
          String name 
          ) {

    Object object = null;
    
    java.awt.Container parent = (java.awt.Container)rootpane;
    java.awt.Component [] children = parent.getComponents();
    java.awt.Container child = null; 
     
    for (int i=0; i<children.length; i++) {
      
      if (object == null) {
        child = (java.awt.Container)children[i];

        String childname = child.getName();
        if (childname != null) {
            if (childname.equals(name)) object = child;
        }
        
        if (object == null) {
          int childchildrens = child.getComponentCount();
          if ( childchildrens !=0) object = find(child, name);
        }
        
      }
       
      child = null; //GC
      children[i] = null; //GC
    }
    
    parent = null; //GC
    children = null; //GC
    rootpane = null; //GC
    name = null; //GC
    
    
    return object;
  }
  
  
  
  
////////////////////////////////////////////////////////////////////////////////
   
  
  public void add(
          Object parent,
          org.w3c.dom.Node nhandle
          ) 
          throws Exception {
    Object oparent = null;
    try {
        if (rootpane == null) rootpane = parent;
        oparent = create(parent, nhandle, null);
        
    } catch (Exception exc) {
      throw exc;
    } finally {
      oparent = null; //GC
      parent = null; //GC
      nhandle = null; //GC
    }
    
  }
  

 
  
  
  private Object create (
          Object parent,
          org.w3c.dom.Node nhandle,
          Object object
          ) 
         throws Exception {
    org.w3c.dom.Node node = null;
    org.w3c.dom.Node attribute = null;
    Object child = null;
    
    String tag = null;
    String method = null;
    String parameters = null;
    JXInfo jxinfo = null;
    boolean layoutcontraints = false;
  try {
     
    org.w3c.dom.NodeList list = nhandle.getChildNodes();
    for (int i=0; i< list.getLength(); i++) {
      
      node = list.item(i);

      if (node.getNodeName().equals("#text")) continue; 
      if (node.getNodeName().equals("#comment")) continue;
      
      
      tag = node.getNodeName();
      org.w3c.dom.NamedNodeMap nmap = node.getAttributes();
      

      attribute = nmap.getNamedItem((getString(28) + getString(29)));
      if (attribute != null) {
        
        method = attribute.getNodeName().trim();
        parameters = attribute.getNodeValue().trim();
        nmap.removeNamedItem((getString(28) + getString(29)));
        
        if (parameters.startsWith(getString(1))) {  // NEW
          jxinfo = new JXInfo();
          jxinfo.org = parent;
          jxinfo.s = parameters;
          jxinfo = process(jxinfo);
         
          child = jxinfo.o;
          jxinfo.destroy(); //GC
          jxinfo = null; //GC
          
        } else {
          if (parameters.startsWith(getString(27))) {
            child = create(null, 
                            parse( (parameters.substring(4).trim()) ), null); 
          }
          else if (parameters.startsWith(getString(32))) {
            child = find(parameters.substring(4).trim());
          }
          else {
            throw new Exception (
                        "\r\n  setTagObjectError -::- @create "       + 
                        "\r\n  Error String -::- " + parameters       +
                        "\r\n  Must start with \"new objectname()\""
                        );
          }
          
        }

      } 
      else if (getPattern(5).matcher(tag).matches()) {
        
        child = (Object)(Class.forName( tag ).newInstance());

        if (child instanceof javax.swing.JTree) {
          javax.swing.tree.DefaultMutableTreeNode tnode = 
                                 new javax.swing.tree.DefaultMutableTreeNode();
          javax.swing.tree.DefaultTreeModel tmodel = 
                                  new javax.swing.tree.DefaultTreeModel(tnode);
          javax.swing.JTree tree = ((javax.swing.JTree)child);
          tree.setModel(tmodel);
          object = tree;
          tnode = null; //GC
          tmodel = null; //GC
          tree = null; //GC
        }
      } 
      
      
      
      
      for (int k=0; k<nmap.getLength(); k++) {
        attribute = nmap.item(k);
        method = attribute.getNodeName().trim();
        parameters = attribute.getNodeValue().trim();
       
        if (method.startsWith(getString(28), 0)) {
          // 3 = jxe
          if (method.startsWith(getString(30), 3)) {
            // setListener(child, parameters);
            continue;
          }
        }
        
        
        
     
        if (method.equals((getString(28) + getString(31)))) {
          
          if (parent != null) {
            jxinfo = new JXInfo();
            jxinfo.org = child;
            jxinfo.s  = parameters;
            jxinfo = process(jxinfo);

            ((java.awt.Container)parent).add(
                                          (java.awt.Container)child, jxinfo.o);
            layoutcontraints = true;
            jxinfo.destroy();
            jxinfo = null; //GC
          } 
          else {
            throw new Exception (
              "\r\n  jxeSetConstraints -::- @create "       + 
              "\r\n  Error String -::- " + parameters       +
              "\r\n  Note: jxeSetConstraints(..) is define at wrong place."
              );
          }

					
        }
        else {
          invokeMethod(child, method, parameters);
        }
        
       
        method = null; //GC
        parameters = null; //GC
        attribute = null; //GC
        
      }
      
      for (int k=0; k<nmap.getLength(); k++) {
        attribute = nmap.item(k);
        method = attribute.getNodeName().trim();
        parameters = attribute.getNodeValue().trim();
       
        if (method.startsWith(getString(28), 0)) {
          // 3 = jxe
          if (method.startsWith(getString(30), 3)) {
            setListener(child, parameters);
          }
        }
        method = null; //GC
        parameters = null; //GC
        attribute = null; //GC
      }
      
      
      
      if (parent == null) {
        parent = child;
      }
      else {
        if ( child instanceof javax.swing.tree.DefaultMutableTreeNode ) {

          if (parent instanceof javax.swing.JTree) {
            object = parent;
            renderTreeNode(null, child, object);
          }
          else {
            renderTreeNode(parent, child, object);
          }
        }
        else if ( parent instanceof javax.swing.JScrollPane) {
          ((javax.swing.JScrollPane)parent).
                                setViewportView((java.awt.Container)child);
        }
        else {
          if (layoutcontraints == false) {
            ((java.awt.Container)parent).add((java.awt.Container)child);
              object = null; //GC
          }
        }
      }
      layoutcontraints = false;
      
      if ( node.hasChildNodes() == true) create(child, node, object);
    }

  return parent;
   
  } catch (Exception exc) {
     throw exc;
  } finally {
    node = null; //GC
    child = null; //GC
    nhandle = null; //GC
    object = null; //GC
    if (jxinfo != null) jxinfo.destroy();
    jxinfo = null; //GC
    method = null; //GC
    tag = null; //GC
    parameters = null; //GC
    attribute = null; //GC
    object = null; //GC
    parent = null; //GC
  }
 
  }

////////////////////////////////////////////////////////////////////////////////
  
  
  public void setResourceBundle(java.util.ResourceBundle resourcebundle) {
		this.resourcebundle = resourcebundle;
	} 
  
////////////////////////////////////////////////////////////////////////////////
  

  
  
  public org.w3c.dom.Node parse (
         String xml
         )
         throws Exception {
        
    java.io.InputStream is = null;  
  try {

    is =  getClass().getResourceAsStream(xml);
    if (is == null) {
       is = new java.net.URL(xml).openStream();    
    }
    if (is == null) throw new Exception (
                          "\r\n  Exception -::- @parse \r\n"           + 
                          "\r\n  Error File -::- " + xml
                          );
   
    return parse(is);

  } catch (Exception exc) {
     throw exc;
  } finally {
     if (is != null) is.close();
     is = null; //GC
     xml = null; //GC
  }
     
  }
  
  
  private static org.w3c.dom.Node parse (
          java.io.InputStream is
          )
          throws Exception {
    javax.xml.parsers.DocumentBuilderFactory factory = null;
    javax.xml.parsers.DocumentBuilder builder = null; 
    org.w3c.dom.Document document = null; 
         
  try {

    factory =  javax.xml.parsers.DocumentBuilderFactory.newInstance();
    factory.setValidating(false);
    builder = factory.newDocumentBuilder();
    document = builder.parse(is);
    return ((org.w3c.dom.Node)document);
    
  } catch (javax.xml.parsers.FactoryConfigurationError fce) {
    throw new Exception (
      "\r\n  FactoryConfigurationError -::- @parse \r\n"           +
        fce.getMessage()
        );
  } catch (javax.xml.parsers.ParserConfigurationException pcex) {
    throw new Exception (
      "\r\n  ParserConfigurationException -::- @parse \r\n"        +
        pcex.getMessage()
        );
  } catch (org.xml.sax.SAXException sex) {
    throw new Exception (
      "\r\n  SAXException -::- @parse  \r\n"                       +
        sex.getMessage()
        );
  } catch (java.io.IOException ioex) {
    throw new Exception (
      "\r\n  IOException -::- @parse  \r\n"                        +
        ioex.getMessage()
        );
  } finally {
    if (is != null) is.close();
    is = null; //GC
    factory = null; //GC
    builder = null; //GC
  }
  
  } 
   
   
////////////////////////////////////////////////////////////////////////////////
/*  
  jxeSetListener=
          "addActionListener,
          java.awt.event.ActionListener, 
          actionPerformed,
          org.DynamicExternalMethodCall.methodP1_B1(Did you see this Message)"
 */
  private void setListener (
          Object object,
          String parameters
          ) throws Exception {
     String [] array = null;  
     java.lang.reflect.Method objectmethod = null;
     
     Class listenerclass = null;
     
     java.lang.reflect.Method listenermethod = null;
     Class listenermethodclass = null;
     ClassLoader listenermethodclassloader = null;
     
     java.lang.reflect.Method jxemethod = null;
     
     String [] temparray = null;
     String [] targetarray = null;
     Class targetclass = null; 
     Object targetobject = null;
     String [] targetmethodarray = null;

     Object proxy = null;
     
     String sobject = null;
     String smethod = null;
     JXInfo tjxinfo = null;
     
     String targetmethod = null;
     String targetparameters = null;
     java.lang.reflect.Method [] m = null;
     
     int i = 0;
  try {

      array = parseString(parameters, ",", 0);
      array[1] = array[1].replaceAll(getString(8), "");
      listenerclass = Class.forName(array[1]);
      objectmethod = object.getClass().getMethod(array[0], listenerclass);
      
      m = listenerclass.getMethods(); 
      for (i = 0; i < m.length; i++) {
          if (!array[2].equals(m[i].getName())) {
            m[i] = null;  //GC
            continue;
          } 
          listenermethod = m[i];
          m[i] = null; //GC
          break;
      } 
      for (; i < m.length; i++) { m[i] = null; } //GC
      m = null; //GC
      if (listenermethod == null) throw new NoSuchMethodException ();

      listenermethodclass = listenermethod.getDeclaringClass();
      listenermethodclassloader = listenermethodclass.getClassLoader();

      
      jxemethod = this.getClass().getMethod(
                      "invokeMethod",
                      new Class []{ Object.class, String.class, String.class } 
                      );

      
      
      if (array[3].startsWith(getString(1))) {  // NEW
        
        int index = array[3].indexOf(")");
        sobject = array[3].substring(0, (index + 1)).trim();
        smethod = array[3].substring( (index+1), (array[3].length()) ).trim();
        // Remove . form .method
        smethod = smethod.substring(1, (smethod.length())).trim();
        
        tjxinfo = new JXInfo();
        tjxinfo.s = sobject;
        tjxinfo = process(tjxinfo);

        temparray = parseMethod(smethod);
        
        targetobject = tjxinfo.o;
        targetmethod = temparray[0];
        targetparameters = temparray[1];
        
      }
      else {
        temparray = parseMethod(array[3]);
        temparray[0] = temparray[0].replaceAll(getString(8), "");
        targetarray = parseConstantFieldValue(temparray[0]);
        targetclass = Class.forName(targetarray[0]);
        
        targetobject = targetclass.newInstance();
        targetmethod = targetarray[1];
        targetparameters = temparray[1];
      }

      proxy = java.lang.reflect.Proxy.newProxyInstance (
                    listenermethodclassloader,
                    new Class[]{ listenermethodclass },
                    new JXInvoker (
                                    this, 
                                    jxemethod, 
                                    new Object [] { 
                                          (Object)targetobject,
                                          (Object)targetmethod, 
                                          (Object)targetparameters 
                                          }
                                    ) 
                                    );
    
      objectmethod.invoke(object, proxy);
    
  }
  catch (NoSuchMethodException nsmexc) {
     throw new Exception (
           "\r\n  NoSuchMethodException -::- @jxeSetListener  \r\n"   +
           "\r\n  Error Parameter -::- " + parameters  
         );
  }
  catch (Exception exc) {
    throw new Exception (
           "\r\n  Exception -::- @jxeSetListener  \r\n"               +
           "\r\n  Error Parameters -::- " + parameters  
         );
  } finally {
    if (array != null) {
      for (i=0; i< array.length; i++) array[i] = null; //GC
    }
    array = null; //GC
    objectmethod = null; //GC
    listenerclass = null; //GC
    
    listenermethod = null; //GC
    listenermethodclass = null; //GC
    listenermethodclassloader = null; //GC
    
    jxemethod = null; //GC
    if (temparray != null ) {
      for (i=0; i<temparray.length;i++) temparray[i] = null; //G
    }
    temparray = null; //GC
    if (targetarray != null) {
      for (i=0; i<targetarray.length;i++) targetarray[i] = null; //GC
    }
    targetarray = null; //GC
    targetclass = null; //GC
    targetobject = null; //GC
    if (targetmethodarray != null) {
      for (i=0; i<targetmethodarray.length;i++) targetmethodarray[i] = null;//GC
    }
    targetmethodarray = null; //GC
    proxy = null; //GC
    
    object = null; //GC
    parameters = null; //GC
    sobject = null; //GC
    smethod = null; //GC
    if (tjxinfo != null) { tjxinfo.destroy(); tjxinfo = null; } //GC
    
    targetmethod = null; //GC
    targetparameters = null; //GC
    if (m != null) { 
      for (i=0; i<m.length;i++) m[i] = null;//GC
    }
    m = null; //GC
    i = 0;
  }
    
  }
  
  
  
  
  
////////////////////////////////////////////////////////////////////////////////
   
 public JXInfo invokeMethod (
          Object object,
          String method,
          String parameters
          )
          throws Exception {

    boolean success = false;
    int i = 0;
    java.lang.reflect.Method omethod = null;  
    JXInfo jxinfo = new JXInfo();
    jxinfo.org = object;
    jxinfo.o = object;
    jxinfo.c = null;
    jxinfo.s = null;
  try {

    if (method.startsWith(getString(28), 0)) {
      if (method.startsWith(getString(30), 3)) {
        setListener(object, parameters);
      }
      else {
        throw new Exception (
          "\r\n  NoSuchMethodException -::- @invokeMethod  \r\n"             +
          "\r\n  Error Method -::- " + method                                + 
          "\r\n  Error Parameters -::- " + parameters 
          ); 
      }
    }
    else {
      
      if ( parameters.equals("") || parameters.equals(null) ) {
        omethod = object.getClass().getMethod(method , null);
        jxinfo.c = omethod.getReturnType();
        jxinfo.o = omethod.invoke(object, null);
      }
      else {

        String [] aparameter = parseString(parameters, getString(20), 0);
        Object [] aobject = new Object[aparameter.length];
        Class  [] aclass = new Class[aparameter.length];

        for (i=0; i<aparameter.length; i++) {
          jxinfo.org = object;
          jxinfo.o = object;
          jxinfo.c = null;
          jxinfo.s = aparameter[i];
          jxinfo = process(jxinfo);
          aclass[i] = jxinfo.c;
          aobject[i] = jxinfo.o;
   //       System.out.println(aparameter[i]);
        }

        try {
          omethod = object.getClass().getMethod(method , aclass);
          jxinfo.c = omethod.getReturnType();
          jxinfo.o = omethod.invoke(object, aobject);
          success = true;
        } catch (Exception exc) {
          success = false;
        }

        if (!success) {
          java.lang.reflect.Method [] amethods = 
                                                object.getClass().getMethods();
          for (i=0; i< amethods.length; i++) {
            if (!success) {
              if (amethods[i].getName().equals(method)) {
                aclass = amethods[i].getParameterTypes(); 
                if (aclass.length == aobject.length) {
                  java.lang.reflect.Method ocmethod = null;
                  try {
                    ocmethod = object.getClass().getMethod(method , aclass);

                    if (!(ocmethod.equals(omethod))) {
                      jxinfo.c = ocmethod.getReturnType();
                      jxinfo.o = ocmethod.invoke(object, aobject);
                      success = true;
                    }
                  } catch (Exception exc) {
                    success = false;
                  } finally {
                    ocmethod = null; //GC
                  }
                } // End of if
              } // End of if 
            } // End of (!success)
            amethods[i] = null; //GC
          } // End of for loop 
          amethods = null; //GC
        }

        for (i=0; i<aparameter.length; i++) {  //GC
          aparameter[i] = null;
          aobject[i] = null;
          aclass[i] = null;
        }
        aparameter = null; //GC
        aobject = null; //GC
        aclass = null;  //GC
        if (!success) throw new NoSuchMethodException();            
      }
    }
    return jxinfo;
    
  } catch (NoSuchMethodException nsme) {
    throw new Exception (
      "\r\n  NoSuchMethodException -::- @invokeMethod  \r\n"                 +
      "\r\n  Error Method -::- " + method                                    + 
      "\r\n  Error Parameters -::- " + parameters 
      );
  } catch (IllegalAccessException iae) {
    throw new Exception (
      "\r\n  IllegalAccessException -::- @invokeMethod  \r\n" 	             +
      "\r\n  Error Method -::- " + method                                    + 
      "\r\n  Error Parameters -::- " + parameters 
      );
  } catch (java.lang.reflect.InvocationTargetException ite) {
    throw new Exception (
      "\r\n  InvocationTargetException -::- @invokeMethod  \r\n"             +
      "\r\n  Error Method -::- " + method                                    + 
      "\r\n  Error Parameters -::- " + parameters 
      );
  } catch (Exception exc) {
    throw exc;
  }  finally {
    omethod = null; //GC
    object = null; //GC
    method = null; //GC
    parameters = null; //GC
  }

    
  }


  
////////////////////////////////////////////////////////////////////////////////  
  
  
  
  
  
  private JXInfo process (
          JXInfo jxinfo
          ) 
          throws Exception {
      
  try {

//  Pass or create instance of object that start with syntax "new "
//  Format: 
//  new com.company.project.object(...)
//  new com.company.project.object(...){{ setmethod(..); setmethod(..); }}
//  new com.company.project.object(...).getmethod(..).method(..)~
//  new onedarray[]{ 1,2,3}
//  new onedarray[10]
//  new twodarray[][]{ {1,1}, {2,2} }
//  new twodarray[2][4]
//  For Java release 1.5+ 
//  Support for Generics
//  new java.util.HashMap<String, int>() {{ put(one,1); put(two,2); }}
//  
//  Example:
//  <object setmethod="new java.util.HashMap<String, int>() 
//                    {{ put(one,1); put(two,2); }}" />
//  Prototype: setmethod(java.util.Map<String,int> map)
    if (jxinfo.s.startsWith(getString(1))) {  // NEW
      jxinfo.s = jxinfo.s.substring(3, (jxinfo.s.length())).trim();

      if (!(getPattern(5).matcher(jxinfo.s).find(0))) {
        throw new Exception (
                      "\r\n  ClassPathSyntaxError -::- @process \r\n"       + 
                      "\r\n  Error String -::- " + jxinfo.s
                      );
      }

      String replacement = jxinfo.s.split(getString(5), 2)[1];
      String classpath = jxinfo.s.replace(replacement, "");
      jxinfo.s = jxinfo.s.substring(classpath.length(), jxinfo.s.length());
      classpath = classpath.replaceAll(getString(8), "");
/*       
      System.out.println("=====  OBJECT   ====");
      System.out.println(classpath);
      System.out.println("--------------------------------------");
      System.out.println(jxinfo.s);
      System.out.println("=====  OBJECT END  ====");
*/
      char cc = jxinfo.s.charAt(0);

      if (cc == '<') {
        jxinfo.s = jxinfo.s.substring(1, (jxinfo.s.length()));
        String [] array = parseString(jxinfo.s, ">", 0);
        // array[0] = <...>
        jxinfo.s = array[1].trim();
        for (int i=0; i< array.length; i++){ array[i] = null;  } //GC
        array = null; //GC
      }
      cc = jxinfo.s.charAt(0);
      switch (cc) {
        case '[':
        {
          int rows = 0;
          int columns = 0;
          String[] avalue = null;
          byte choice = 0;
          char atype = classpath.charAt(0);
          boolean found = false;
          
          String append = "";

          if (getPattern(9).matcher(jxinfo.s).matches()) {
            choice = 9;
            avalue = parse2DArray(9, jxinfo.s);
            rows = Integer.parseInt(avalue[0]);
            columns = Integer.parseInt(avalue[1]);
            avalue = null;
          }
          else if (getPattern(10).matcher(jxinfo.s).matches()) {
            choice = 10;
            avalue = parse2DArray(10, jxinfo.s);
            rows = Integer.parseInt(avalue[0]);
            columns = Integer.parseInt(avalue[1]);
          }
          else if (getPattern(2).matcher(jxinfo.s).matches()) {
            choice = 2;
            rows = Integer.parseInt(parse1DArray(2, jxinfo.s));
            avalue = null;
          }
          else if (getPattern(3).matcher(jxinfo.s).matches()) {
            choice = 3;
            String values = parse1DArray(3, jxinfo.s);
            avalue = parseString(values, getString(20), 0);
            rows = avalue.length;

            values = null;  //GC
          }

          switch (atype) 
          {
            case 'c':
            {

              if (classpath.equals("char")) {
                found = true;
                jxinfo.c = char.class;
              }

            } break;
            case 's':
            case 'S':
            {

              if (classpath.equals("short")) {
                found = true;
                jxinfo.c = short.class;
                append = "s";
              }
              else if (classpath.equals("String")) {
                found = true;
                jxinfo.c = String.class;
              }

            } break;
            case 'i':
            {

              if (classpath.equals("int")) {
                found = true;
                jxinfo.c = int.class;
                append = "i";
              }

            } break;
            case 'f':
            {

              if (classpath.equals("float")) {
                found = true;
                jxinfo.c = float.class;
                append = "f";
              }

            } break;
            case 'b':
            {

              if (classpath.equals("byte")) {
                found = true;
                jxinfo.c = byte.class;
                append = "b";
              }
              else if (classpath.equals("boolean")) {
                found = true;
                jxinfo.c = boolean.class;
              }

            } break;
            case 'd':
             {

              if (classpath.equals("double")) {
                found = true;
                jxinfo.c = double.class;
                append = "d";
              }

            } break;
            case 'O':
            {

              if (classpath.equals("Object")) {
                found = true;
                jxinfo.c = Object.class;
              }

            } break;
            case 'l':
            {

              if (classpath.equals("long")) {
                found = true;
                jxinfo.c = long.class;
                append = "l";
              }

            } break;
            default:  break;

          } // END of switch (atype) 

          if (!found) {
            jxinfo.c = Class.forName(classpath);
          }

          switch (choice) {
            case 9:
            {
              int[] dimensions =
                new int[]{rows, columns};
              Object oarray = java.lang.reflect.Array.newInstance(
                                                    jxinfo.c, dimensions);
              jxinfo.c = oarray.getClass();                                      
              jxinfo.o = oarray;

              dimensions = null;  //GC
              oarray = null; //GC
            } break;
            case 10:
            {
// TODO 2D array
              int[] dimensions =  new int[]{rows, columns};
              Object oarray = java.lang.reflect.Array.newInstance(
                                                 jxinfo.c, dimensions);

              JXInfo tjxinfo = new JXInfo();
              tjxinfo.org = oarray; // jxinfo.org;
              for (int r = 0; r < rows; r++) {
                Object row = java.lang.reflect.Array.get(oarray, r);
                String[] columnparam = 
                              parseString(avalue[2 + r], getString(20), 0);
                for (int c = 0; c < columns; c++) {
                  tjxinfo.c = null;
                  tjxinfo.o = null;
                  if (columnparam[c].startsWith(append)) {
                    tjxinfo.s = columnparam[c];
                  } else {
                    tjxinfo.s = append + columnparam[c]; 
                  }  
                  tjxinfo = process(tjxinfo);
                  java.lang.reflect.Array.set( row,c, tjxinfo.o);
                  columnparam[c] = null; //GC
                }

                row = null; //GC
                columnparam = null; //GC
              }

              jxinfo.c = oarray.getClass();
              jxinfo.o = oarray;

              tjxinfo.destroy();  //GC
              tjxinfo = null; //GC
              dimensions = null;  //GC 
              oarray = null;  //GC

            } break;
            case 2:
            {

              Object oarray = java.lang.reflect.Array.newInstance(
                                                        jxinfo.c, rows);
              jxinfo.c = oarray.getClass();                                          
              jxinfo.o = oarray;

              oarray = null;  //GC

            } break;
            case 3:
            {


              Object oarray = java.lang.reflect.Array.newInstance(
                                                        jxinfo.c, rows);

              JXInfo tjxinfo = new JXInfo();
              tjxinfo.org = oarray; // Patel : jxinfo.org;
              for (int r = 0; r < rows; r++) {
                tjxinfo.c = null;
                tjxinfo.o = null;
                if (avalue[r].startsWith(append)) {
                  tjxinfo.s = avalue[r];
                } else {
                  tjxinfo.s = append + avalue[r]; 
                }
                tjxinfo = process(tjxinfo);
                java.lang.reflect.Array.set(oarray,r, tjxinfo.o);
              }

              jxinfo.c = oarray.getClass();
              jxinfo.o = oarray;

              tjxinfo.destroy();  //GC
              tjxinfo = null; //GC
              oarray = null;  //GC
            } break;

          }

          for (int i=0; i<avalue.length; i++) { avalue[i] = null; } //GC
          avalue = null;  //GC
          append = null;  //GC

        } break;
        case '(': 
        {
          jxinfo.c = Class.forName(classpath);
          String [] array = null;
          int index = -1;
          
          if (getPattern(11).matcher(jxinfo.s).matches()) {
            array = parseObject(11, jxinfo.s);
            index = 11;
          }
          else if (getPattern(12).matcher(jxinfo.s).matches()) {
            array = parseString(jxinfo.s, getString(22), 0);
            index = 12;
          }
          else if (getPattern(7).matcher(jxinfo.s).matches()) {
            array  = new String[1];
            array[0] = jxinfo.s;
          }

          String[] oparam = parseObject(7, array[0]);


          if (oparam[0].equals("") || oparam[0].equals(null)) {
            jxinfo.o = jxinfo.c.getConstructor().newInstance();
          }
          else {

            java.lang.reflect.Constructor constructor = null;
            Class[]  aclass = new Class[oparam.length];
            Object[] aobject = new Object[oparam.length];
            boolean success = false;
            JXInfo tjxinfo = new JXInfo();

            tjxinfo.org = jxinfo.org;
            for (int i = 0; i < oparam.length; i++) {
                tjxinfo.c = null;
                tjxinfo.o = null;
                tjxinfo.s = oparam[i];
                tjxinfo = process(tjxinfo);
                aclass[i] = tjxinfo.c;
                aobject[i] = tjxinfo.o;
            }

            try {
              constructor = jxinfo.c.getConstructor(aclass);
              // Patel : added jxinfo.org =
              jxinfo.org = jxinfo.o = constructor.newInstance(aobject); 
              success = true;
            } catch (Exception exc) {
              success = false;
            }

            if (!success) {              
              java.lang.reflect.Constructor [] constructors =
                                           jxinfo.c.getDeclaredConstructors();

              for (int i=0; i< constructors.length; i++) {
                if (!success) {
                  aclass = constructors[i].getParameterTypes();
                  if ( aclass.length == oparam.length) {
                    try {                   
                      constructor = jxinfo.c.getConstructor(aclass);
                      // Patel : jxinfo.org =
                      jxinfo.org = jxinfo.o = constructor.newInstance(aobject); 
                      success = true;
                    } catch (Exception exc) {
                      success = false;
                    }
                  }
                  for(int k=0; k<aclass.length; k++) { aclass[k] = null; }//GC
                }
                constructors[i] = null; //GC
              }
              constructors = null; // GC

            }
            if (!success) throw new IllegalAccessException(); //DEBUG

            for (int i=0; i<aclass.length; i++) { aclass[i] = null; } //GC
            aclass = null; //GC
            for (int i=0; i<aobject.length; i++) { aobject[i] = null; } //GC
            aobject = null;  //GC
            success = false;
            tjxinfo.destroy();  //GC
            tjxinfo = null;   //GC
            constructor = null; //GC
          }

          switch (index) {
            case 12:
            {

              for (int i = 1; i < array.length; i++) {
              // call's 13 - below
                jxinfo.s = array[i];
                jxinfo = process(jxinfo);
                array[i] = null; //GC
              }
              array[0] = null; //GC
              array = null; //GC
              for (int i=0; i<oparam.length; i++) { oparam[i] = null; } //GC
              oparam = null;  //GC
            } break;
            case 11:
            {
              oparam = parseString(array[1], getString(21), 0);
              JXInfo tjxinfo = new JXInfo();
              tjxinfo.org = jxinfo.org;
              for (int i=0; i<oparam.length; i++) {
                tjxinfo.c = jxinfo.c;
                tjxinfo.o = jxinfo.o;
                tjxinfo.s = oparam[i];
                process(tjxinfo);
                oparam[i] = null; //GC
              }
              oparam = null; //GC
              tjxinfo.destroy();  //GC
              tjxinfo = null; //GC
              for (int i=0; i<array.length; i++) { array[i] = null; } //GC
              array = null; //GC

            } break;
            default:  break;
          }

        }
        default: break;
      }

      replacement = null; //GC
      classpath = null; //GC


    } // NEW end
//  Matches primitive data type values
//  PLEASE see the document for allowed possible syntaxes
//  All primitive data type can be passed in.
//  !!Infinite Possibilities!! 
//  Example:
//  <object setmethod(1, b01, d-0x423578, f#234fabc, s23) />
//  Prototype:  setmethod(int,byte,decimal,float,short) 
    else if (getPattern(16).matcher(jxinfo.s).matches()) {

      char cc = jxinfo.s.charAt(0);
      int length = jxinfo.s.length();

      char binary = '@';
      if (length > 1) {
        binary = jxinfo.s.charAt(1);
      }

      switch (binary) {
        case 'b':
        case 'B':
        {
          jxinfo.s = jxinfo.s.substring(2, length);
        } break;
        default:
        {
          switch (cc) {
            case 'f':
            case 'F':
            case 'd':
            case 'D':
            case 'b':
            case 'B':
            case 's':
            case 'S':
            case 'l':
            case 'L':
            case 'i':
            case 'I':
            {
              jxinfo.s = jxinfo.s.substring(1, length);
            } break;
            default:  break;
          }

        } break;
      }
      switch (cc) {
        case 'f':
        case 'F':
        {
          jxinfo.c = float.class;
          switch (binary) {
            case 'b':
            case 'B':
            {
              long longvalue = Long.valueOf(jxinfo.s, 2);
              int intvalue = (int) longvalue;
              jxinfo.o = Float.intBitsToFloat(intvalue);
            } break;
            default:
            {
              long longvalue = Long.decode(jxinfo.s);
              int intvalue = (int) longvalue;
              jxinfo.o = Float.intBitsToFloat(intvalue);
            } break;
          }

        } break;
        case 'd':
        case 'D':
        {
          jxinfo.c = double.class;
          switch (binary) {
            case 'b':
            case 'B':
            {
              long longvalue = Long.valueOf(jxinfo.s, 2);
              jxinfo.o = Double.longBitsToDouble(longvalue);
            } break;
            default:
            {
              long longvalue = Long.decode(jxinfo.s);
              jxinfo.o = Double.longBitsToDouble(longvalue);
            } break;
          }
        } break;
        case 'b':
        case 'B':
        {
          jxinfo.c = byte.class;
          switch (binary) {
            case 'b':
            case 'B':
            {
              jxinfo.o = Byte.valueOf(jxinfo.s, 2);
            } break;
            default:
            {
              jxinfo.o = Byte.decode(jxinfo.s);
            } break;
          }

        } break;
        case 's':
        case 'S':
        {
          jxinfo.c = short.class;
          switch (binary) {
            case 'b':
            case 'B':
            {
              jxinfo.o = Short.valueOf(jxinfo.s, 2);
            } break;
            default:
            {
              jxinfo.o = Short.decode(jxinfo.s);
            } break;
          }

        } break;
        case 'l':
        case 'L':
        {
          jxinfo.c = long.class;
          switch (binary) {
            case 'b':
            case 'B':
            {
              jxinfo.o = Long.valueOf(jxinfo.s, 2);
            } break;
            default:
            {
              jxinfo.o = Long.decode(jxinfo.s);
            } break;
          }

        } break;
        default:
        case 'i':
        case 'I':
        {
          jxinfo.c = int.class;
          switch (binary) {
            case 'b':
            case 'B':
            {
              jxinfo.o = Integer.valueOf(jxinfo.s, 2);
            } break;
            default:
            {
              jxinfo.o = Integer.decode(jxinfo.s);
            } break;
          }
        } break;
      }
    }
    else if (getPattern(17).matcher(jxinfo.s).matches()) {
      char cc = jxinfo.s.charAt(0);
      jxinfo.s = jxinfo.s.substring(1, jxinfo.s.length());
      switch (cc) {
        case 'f':
        case 'F':
        {
          jxinfo.c = float.class;
          jxinfo.o = Float.valueOf(jxinfo.s);
        } break;
        default:
        case 'd':
        case 'D':
        {
          jxinfo.c = double.class;
          jxinfo.o = Double.valueOf(jxinfo.s);
        } break;
      }

    }
//  Matches boolean character
//  Parameter value is of type boolean data type 
//  Format: true or TRUE or false or FALSE
//  Example:
//  <object setmethod="1, true"/>
//  Prototype: setmethod(int, boolean) 
    else if (getPattern(15).matcher(jxinfo.s).matches()) {
      jxinfo.c = boolean.class;
      jxinfo.o = Boolean.parseBoolean(jxinfo.s);
    }
//  Matches char character as parameter i.e. in single quote 
//  Parameter value is of type char data type
//  Format: 'c'
//  <object setmethod="1, 'c'"/>
//  Prototype: setmethod(int, char) 
    else if (getPattern(14).matcher(jxinfo.s).matches()) {
      jxinfo.c = char.class;
      jxinfo.o = jxinfo.s.charAt(1);
    }
//  Matches anything with "..." as String
//  Format: &quot;String within quotation&quot;
//  Example 
//  <object setmethod="1, &quot;(TEST)&quot;"/>
//  Prototype: setmethod(int, String) 
    else if (getPattern(18).matcher(jxinfo.s).matches()) {
      jxinfo.c = String.class;
      jxinfo.o = jxinfo.s.substring(1, (jxinfo.s.length() - 1));
    }
//  Instance of current xml tag object
//  Format: this
//  Example:
//  <com.company.project.myobject setmethod="this" />
//  would pass instance of "com.company.project.myobject" to its own
//  Prototype: setmethod(com.company.project.myobject cthis){} 
    else if ((jxinfo.s).startsWith(getString(6))) {
     
      if (jxinfo.org == null) {
        throw new Exception( 
          "\r\n this  -::- @getParameterObject  \r\n" 	+
          "\r\n Error Parameter -::- " + jxinfo.s       +
          "\r\n Note -::- reference to \"this\" cannot be resolved" 
        );
      }
      if ((jxinfo.s).equals(getString(6))) {
        jxinfo.c = jxinfo.org.getClass();
        jxinfo.o = jxinfo.org;
      } 
      else if ( getPattern(4).matcher(jxinfo.s).matches()) {
        jxinfo.c = jxinfo.org.getClass();
        jxinfo.o = jxinfo.org;
        jxinfo.s = jxinfo.s.substring(4).trim();
        jxinfo.s = jxinfo.s.substring(1).trim();
        String[] array = parseMethod(jxinfo.s);
        jxinfo = invokeMethod(jxinfo.o, array[0], array[1]);
        
        for (int i=0; i<array.length; i++) { array[i] = null; } //GC
        array = null; //GC
      } 
      else {
        throw new Exception( 
          "\r\n this  -::- @getParameterObject  \r\n" 	+
          "\r\n Error Parameter -::- " + jxinfo.s       +
          "\r\n Note -::- Please check syntex, " +
                        " only \"this\" or \"this.method(...)\" is allowed"
        );
      }
      
    }
//  Instance of JXEngine
//  Format: JXEngine or org.JXEngine
//  Return instance i.e. this
//  Within XML you can pass handle to JXEngine as parameter to any method that
//  requires it.
    else if (getPattern(19).matcher(jxinfo.s).matches()) {
      jxinfo.c = this.getClass(); // JXEngine.class
      jxinfo.o = this;
    }
//  Absolute values i.e. constant values
//  Format: classpath.CONSTANT_VALUES
//  Example:
//  java.awt.GridBagConstraints.SOUTHWEST
//  would return 16 as int object
//  javax.swing.JSplitPane.BOTTOM
//  would return "bottom" as string object
    else if (getPattern(5).matcher(jxinfo.s).matches()) {
      jxinfo.s = jxinfo.s.replaceAll(getString(8), "");
      String[] array = parseConstantFieldValue(jxinfo.s);
      Class cclass = Class.forName(array[0]);
      java.lang.reflect.Field field = cclass.getField(array[1]);
      jxinfo.c = field.getType();
      jxinfo.o = field.get(jxinfo.c);
      
      for (int i=0; i<array.length; i++) { array[i] = null; } //GC
      array = null; //GC
      cclass = null; //GC
      field = null; //GC
    }
//  Directly calling public static method i.e. with out using "new" operator
//  Format: classpath.method(...)
//  Example:
//  Let say you have a class with public static method i.e. staticmethod
//
//  package com.company.project;
//  public class example {
//    public static int staticmethod(int a, int b) { return a*b; }
//  }
//
//  Within xml file, you can do something like this
//  othermethod( com.company.method.example.staticmethod(2,3) )
//  would pass value 6 as parameter to othermethod(int)
    else if (getPattern(24).matcher(jxinfo.s).matches()) {
      String [] array = parseMethod(jxinfo.s);
      String parameters = array[1];
      String path = array[0].replaceAll(getString(8), "");

      for (int i=0; i<array.length; i++) { array[i] = null; } //GC
      array = null; //GC

      array = parseConstantFieldValue(path);
      Class cclass = Class.forName(array[0]);
      String method = array[1];
      
      for (int i=0; i<array.length; i++) { array[i] = null; } //GC
      array = null; //GC
      path = null; //GC
      
      jxinfo.o = cclass.getConstructor().newInstance();
      cclass = null; //GC
      
      jxinfo = invokeMethod(jxinfo.o, method, parameters);

      method = null; //GC
      parameters = null; //GC

     
    } 
//  Invoke a method for a given object and return the resulting value 
//  Format: method( param1, param2, param3, ...)
//  Process all parameter and calls the method for a given object and return 
//  result (as object)
    else if (getPattern(13).matcher(jxinfo.s).matches()) {
      String[] array = parseMethod(jxinfo.s);
      jxinfo = invokeMethod(jxinfo.o, array[0], array[1]);
      for (int i=0; i<array.length; i++) { array[i] = null; } //GC
      array = null; //GC
    }
//  Internationalization support using resource bundle
//  Format: @rb=key
//  Provided the key using "@rb=key" string, replace it with value found in
//  the resource bundle
//  If give key is not found then, "?*?" is displayed
    else if (jxinfo.s.startsWith(getString(25))) {
      if (resourcebundle != null) {
        jxinfo.s = resourcebundle.getString(jxinfo.s.substring(4).trim());
      } else {
        jxinfo.s = getString(26); 
      }
      jxinfo.c = String.class;
      jxinfo.o = jxinfo.s;
    }
//  XML Link
//  Format: @xl=/file.xml
//  Parse the xml file, and return the object reference of the tree root
//  Let say you have xml file: example.xml that contains int array
//  <array jxeSetObject="new int[]{1,2,3}/>
//  You can load and pass int array by doing @xl=/example.xml 
//  would return int array.
//  NOTE: 
//  External file can contain any kind of object hashmap, JPanel, JComponent etc
//  i.e.
//  <javax.swing.JPanel>
//     <javax.swing.JButton>
//  </javax.swing.JPanel>
//  Would return reference to javax.swing.JPanel that contains JButton within it
    else if (jxinfo.s.startsWith(getString(27))) {
     jxinfo.o = create(null, 
                        parse( (jxinfo.s.substring(4).trim()) ), null); 
     jxinfo.c = jxinfo.o.getClass();
    }
//  Find Object
//  Format: @fo=name
//  Look for object with a given name, and return its reference i.e. handle
//  Let say you have JButton with a name "button" 
//  @fo=button would return button reference i.e. handle
    else if (jxinfo.s.startsWith(getString(32))) {
     jxinfo.s = jxinfo.s.substring(4).trim();
     jxinfo.o = find(jxinfo.s);
     jxinfo.c = jxinfo.o.getClass();
    }
//  Format: (object).method(...).method(...)~
//  Only ONE parameter between "(" and ")" and must be of type object
//  !!Possibilities are infinite!! 
//  (@xl=/file.xml                      ).method(...)~
//  (@fo=name                           ).method(...)~
//  (new myobject(...)                  ).method(...)~
//  (new myobject(...){{ method(..); }} ).method(...)~
//  Example:
//  (new JButton() ).setName( button ).toString().trim()
//  (@xl=/JButton.xml).getName().substring(..)
//  NOTE:
//  Let say if you want to pass "(TEST)" as string, you can either do following
//  new java.lang.String(&quot;(TEST)&quot;) or &quot;(TEST)&quot;
    else if (getPattern(12).matcher(jxinfo.s).matches()) {
      String [] array = parseString(jxinfo.s, getString(22), 0);
      String[] oparam = parseObject(7, array[0]);
            
      if (oparam[0].equals("") || oparam[0].equals(null) ) {
        throw new Exception( 
          "\r\n NoParameterFound  -::- @getParameterObject  \r\n" 	+
          "\r\n Error Parameter -::- " + jxinfo.s
        );
      }
      if  (oparam.length > 1) {
        throw new Exception( 
        "\r\n TooManyParameterFound  -::- @getParameterObject  \r\n" 	+
        "\r\n Error Parameter -::- " + jxinfo.s
        );
      }
      JXInfo tjxinfo = new JXInfo();
      tjxinfo.org = jxinfo.org;
      tjxinfo.c = null;
      tjxinfo.o = null;
      tjxinfo.s = oparam[0];
      tjxinfo = process(tjxinfo);
      
      jxinfo = tjxinfo;

      for (int i = 1; i < array.length; i++) {
        // call's 13 - below
        jxinfo.s = array[i];
        jxinfo = process(jxinfo);
        array[i] = null; //GC
      }
     
      array[0] = null; //GC
      array = null; //GC
      for (int i=0; i<oparam.length; i++) { oparam[i] = null; } //GC
      oparam = null;  //GC
      tjxinfo.destroy();  //GC
      tjxinfo = null;   //GC

    }
//  Last resort, every thing else is consider as string
// Check for image path or xml file path
// If you want following  "/   something /" as string
// Then you must put &quot;/   something /&quot; in xml file
    else {
         jxinfo.c = String.class;
         if (jxinfo.s.contains("/")) {
           jxinfo.o = jxinfo.s.replaceAll(getString(8), "");
         } 
         else {
           jxinfo.o = jxinfo.s;
         }
         
    }
    
    
     return jxinfo;
  }
  catch (NumberFormatException nfe) {
    throw new Exception( 
      "\r\n NumberFormatException -::- @getParameterObject  \r\n" 	+
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (ClassNotFoundException cnfe) {
    throw new Exception (
      "\r\n ClassNotFoundException -::- @getParameterObject  \r\n" 	+
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (NoSuchFieldException nsfe) {
     throw new Exception (
      "\r\n NoSuchFieldException -::- @getParameterObject  \r\n" 	+
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (IllegalAccessException iae) {
    throw new Exception (
      "\r\n IllegalAccessException -::- @getParameterObject  \r\n" 	+
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (NoSuchMethodException nsme) {
    throw new Exception (
      "\r\n NoSuchMethodException -::- @getParameterObject  \r\n" 	+
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (InstantiationException ie) {
    throw new Exception (
      "\r\n InstantiationException -::- @getParameterObject  \r\n" 	+
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (java.lang.reflect.InvocationTargetException ite) {
    throw new Exception (
      "\r\n NoSuchFieldException -::- @getParameterObject  \r\n" 	 +
      "\r\n Error Parameter -::- " + jxinfo.s
      );
  }
  catch (Exception exc) {
    throw exc;
  } 
   
    
    
  }


    
////////////////////////////////////////////////////////////////////////////////
  
  
  
  
  private static String [] parseObject (
          final int index,
          String string
          ) {

//   regexstring = regexstring.trim();
    switch (index) {
// Return parameter with ( ... )
      case 7: 
      {
// TODO       if (index == -1) throw new Exception("missing ) bracket" );
        string = string.substring(1, (string.length() - 1)).trim();
        return parseString(string, getString(20), 0);
      }
      case 11: 
      {
// Return parameter within (...) {{  .... }} i.e. methods
// Remove }}
        string = string.substring(0, (string.length() - 1)).trim();   
        string = string.substring(0, (string.length() - 1)).trim();
        
        return parseString(string, getString(23), 2);
      }
      default: return null;
    }
  }
  
  private static final String parse1DArray (
          final int index,
          final String string
          ) {

    switch (index) {
      case 3: 
      {
        return string.substring(
          ((string.indexOf("{")) + 1), (string.length() - 1)).trim();
      }
      case 2: 
      {
        return string.substring(
          ((string.indexOf("[")) + 1), (string.length() - 1)).trim();
      }
      default: return null;
    }

  }

  private static String [] parse2DArray (
          final int index,
          final String string
          ) {

    switch (index) {
      case 9: 
      {
        String[] array = string.split("]");
        for (int i = 0; i < array.length; i++) {
          array[i] = array[i].replace("[", "").trim();
        }
        return array;
      }
      case 10: 
      {
        int firstbracket = string.indexOf("{");
        String remaining = string.substring(
                        (firstbracket + 1), (string.length() - 1)).trim();
        String[] rows = parseString(remaining, getString(20), 0);
        int totalrows = rows.length;
        String[] array = new String[2 + totalrows];
        
        for (int i = 2; i < (totalrows + 2); i++) {
          array[i] = rows[i - 2].substring(1,(rows[i - 2].length() - 1)).trim();
        }
        String[] columns = parseString(array[2], getString(20), 0);
        int totalcolumns = columns.length;

        array[0] = Integer.toString(totalrows);
        array[1] = Integer.toString(totalcolumns);
        
        for (int i=0; i<rows.length; i++) { rows[i] = null; } //GC
        rows = null;  //GC
        for (int i=0; i<columns.length; i++) { columns[i] = null; } //GC
        columns = null; //GC
        return array;
      }
      default: return null;
    }
  }

  private static String [] parseConstantFieldValue ( 
          final String string
          ) {
    
    String[] array = new String[2];
    String[] sarray = string.split("\\.");
    int length = sarray.length;

    array[1] = sarray[--length];
    array[0] = string.replace("." + array[1], "").trim();

    for (int i=0; i<sarray.length; i++) { sarray[i] = null; } //GC
    sarray = null; //GC
    return array;

  }

  private static String [] parseMethod (
          final String string
          ) {
    
    String[] array = new String[2];
    int openbracket = string.indexOf("(");

    array[0] = string.substring(0, openbracket).trim();
    array[1] = string.substring((openbracket + 1),(string.length() - 1)).trim();
    
    return array;
  }

  




////////////////////////////////////////////////////////////////////////////////    
   
 

  
  
  
  private static String [] parseString (
          final String string,
          final String regex,
          final int splitlimit
          ) {
   
    java.util.regex.Matcher m = getPattern(0).matcher(string);
    java.util.ArrayList list = new java.util.ArrayList();
    StringBuffer sb = new StringBuffer();

    boolean flag = false;
    int ob = 0;
    int cb = 0;
    int sp = 0;
    int ep = 0;
    int ip = 0;

    while (m.find()) {

      switch (string.charAt(m.start())) {

        case '(':
        case '<':
        case '{':
        {
          if (ob == 0) { sp = m.start(); flag=false; } 
          ob++;
        } break;
        case '"':
        {
            if (ob == 0) { sp = m.start();  ob++; flag=true; }
            else if (flag) { cb++; flag=false; }
        } break;
        default:
        {
          cb++;
        } break;

      }


      if (cb == ob) {
        ep = m.end();
        cb = 0;
        ob = 0;
        flag = false;
        String key = "____yek___" + ip + "__";
        String value = string.substring( sp, ep);

        list.add(key);            
        list.add(value);
//    System.out.println(key + "  :  " + value);

        if (sp != 0) {
          String sub = string.substring( ip, sp);
          sb = sb.append(sub + key);
          ip = ep;
        } else {
          sb = sb.append(key);
          ip = ep;
        }
      }

    } // End of While


      // Reuse ob as length        
    ob = string.length();      
    if (ip != ob) {
      String sub = string.substring( ip, ob);
      sb = sb.append(sub);
      sub = null; //GC
    }

//  System.out.println(sb.toString());
    String [] array = sb.toString().split(regex, splitlimit);

    // Reuse ob as counter
    for (ob = 0; ob < array.length; ob++) {   
      java.util.Iterator it = list.iterator();
      while (it.hasNext()) {
        String key = it.next().toString();
        String value = it.next().toString();
        if (array[ob].contains(key)) {
          array[ob] = array[ob].replace(key, value);
        } 
      }
      array[ob] = array[ob].trim();
    }


    list.clear(); //GC
    list = null; //GC
    sb = null; //GC
    m = null; //GC
    return array;

  }
    
    
////////////////////////////////////////////////////////////////////////////////
    
    
  private static final java.util.regex.Pattern getPattern (
          final int index
          ) {
    return java.util.regex.Pattern.compile( getString(index) );
  }
    
    
    
  private static final String getString (
          final int index
          ) {

    switch (index) {

// Use by parseParameters
      case 0: return  "(?!')[<({})>\"](?!')";
      
// new[space]
     // case 1: return  "(new)[\\p{Space}]+";
      case 1: return "new";
      
// java.lang.Integer [ 5 ]
      case 2: return  "\\[" +
                      "[\\p{Space}]*" +
                      "[\\p{Digit}]+" +
                      "[\\p{Space}]*" +
                      "\\]";
      
// java.lang.Integer [ ] { 1 , 3, 4 }
      case 3: return  "\\[" +
                      "[\\p{Space}]*" +
                      "\\]" +
                      "[\\p{Space}]*" +
                      "[{]" +
                      "[\\p{Graph}\\p{Space}]*" +
                      "[}]";

      case 4: return getString(6) +
                     getString(8) +
                     getString(22) +
                     getString(8) +
                     getString(13);
                     
      
// Object Path -> java.lang.Character 
// Absolute Values -> java.awt.GridBagConstraints.SOUTHWEST
// or com.mycompany.division.project
      case 5: return  "[^\\p{Punct}]" +
                      "[\\p{Alpha}]" +
                      "[\\p{Alnum}_\\{Space}]+" +
                      "[.]" +
                      "[\\p{Alnum}._\\p{Space}]+";
      
// Instance of object itself
      case 6: return "this";

// new com.mycompany.division.project (...)
      case 7: return  "\\(" +
                      "[\\p{Graph}\\p{Space}]*" +
                      "\\)";
      
// Zero or more spaces
      case 8: return "[\\p{Space}]*";
      
// 2D Array : new java.lang.String [3][2]
      case 9: return  "\\[" +
                      "[\\p{Space}]*" +
                      "[\\p{Digit}]+" +
                      "[\\p{Space}]*" +
                      "\\]" +
                      "[\\p{Space}]*" +
                      "\\[" +
                      "[\\p{Space}]*" +
                      "[\\p{Digit}]+" +
                      "[\\p{Space}]*" +
                      "\\]";
// 2D Array : new java.lang.Integer [] [] { {1,2,3}, {4,5,6} }               
      case 10:  return  "\\[" +
                        "[\\p{Space}]*" +
                        "\\]" +
                        "[\\p{Space}]*" +
                        "\\[" +
                        "[\\p{Space}]*" +
                        "\\]" +
                        "[\\p{Space}]*" +
                        "[{]" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "[}]";

// new com.mycompany.division.project (...) {{ method(..); }}
      case 11:  return  "\\(" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "\\)" +
                        "[\\p{Space}]*" +
                        "[{]" +
                        "[\\p{Space}]*" +
                        "[{]" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "[}]" +
                        "[\\p{Space}]*" +
                        "[}]";


// new com.mycompany.division.project (...).method(..).method(..)
      case 12:  return  "\\(" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "\\)" +
                        "[\\p{Space}]*" +
                        "[.]" +
                        "[\\p{Graph}\\p{Space}]*";

// Method : methodname (param1, param2, param3,...)
      case 13:  return  "[\\p{Alpha}]+" +
                        "[\\p{Graph}]*" +
                        "[\\p{Space}]*" +
                        "\\(" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "\\)";
      
// Matches single char : 'c'
      case 14:  return  "['][\\p{Graph}][']";
      
// Matches boolean value : true or false
      case 15:  return  "(true)|(false)|(TRUE)|(FALSE)";

// Matches Whole value
      case 16:  return  "([bB]|[sS]|[lL]|[fF]|[dD]|[iI]?)" +
                        "(" +
                        "(" +
                        "[bB][-]?[01]+" +
                        ")" +
                        "|" +
                        "(" +
                        "[-]?" +
                        "(0[xX]|#)" +
                        "[\\p{XDigit}]+" +
                        ")" +
                        "|" +
                        "(" +
                        "[-]?" +
                        "0([0-7]|[0-7][0-7]|[0-3][0-7][0-7])" +
                        ")" +
                        "|" +
                        "(" +
                        "[-]?" +
                        //                       "[1-9]"     
                        "[\\p{Digit}]+" +
                        ")" +
                        ")";
      
      
// Matches real number i.e. (fraction) (decimal)
      case 17:  return  "([fF]|[dD]?)" +
                        "[-]?" +
                        "[\\p{Digit}]*" +
                        "[.]" +
                        "[\\p{Digit}]+" +
                        "([eE]?[+-]?[\\p{Digit}]+)?";

// Matches String in double quotes
      case 18:  return  "\"" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "\"";

// Instance of JXEngine
      case 19:  return  "(JXEngine)|(org.JXEngine)";

      case 20:  return  ",";
      
      case 21:  return  ";";
      
      case 22:  return  "\\.";
      
      case 23:  return  "[{][\\p{Space}]*[{]";

// Calling Static Method - Classpath.Method(...)
      case 24:  return  "[^\\p{Punct}]" +
                        "[\\p{Alpha}]+" +
                        "[\\p{Alnum}_\\{Space}]+" +
                        "[.]" +
                        "[\\p{Alnum}._\\p{Space}]+" +
                        /*"[\\p{Space}]*" + */
                        "\\(" +
                        "[\\p{Graph}\\p{Space}]*" +
                        "\\)";
      
// Resource Bundle key      
      case 25 : return "@rb="; // @rb.
      
      case 26 : return "?*?";

// xml link - Parse and create object from the xml file and return root object
      case 27 : return "@xl="; // ".xml";

      case 28 : return "jxe";
// Only one is allowed      
      case 29 : return "SetObject";

// Setup Event Listener
//Allow multiple i.e. SetListener[number starting with zero] 
      case 30 : return "SetListener";

// Setup Layout Constraints
//Only one is allowed
      case 31 : return "SetConstraints";

// Find object with a give name i.e. @fo=Name of the object
// Returns handle to an object
      case 32 : return "@fo=";
    
      default:  return  null;
        
    } // End of switch

  }

 
////////////////////////////////////////////////////////////////////////////////
  
  
  private static class JXInfo {
		
    private Object org = null;
    private Object o = null;
    private Class  c = null;
    private String s = null;
    private JXInfo() { org=null; o=null; c=null; s=null;  }
    private void destroy() { org=null; o=null; c=null; s=null;  }
//    protected void finalize() throws Throwable { destroy(); }
  }
  
  
 
  private class JXInvoker
                implements java.lang.reflect.InvocationHandler {
    Object target = null;
    java.lang.reflect.Method method = null;
    Object [] parameters = null;
    
    private JXInvoker (
          Object target,
          java.lang.reflect.Method method,
          Object [] parameters
          ) {
      this.target = target;
      this.method = method;
      this.parameters = parameters;
    }
    
    public Object invoke(
          Object proxy, 
          java.lang.reflect.Method proxymethod, 
          Object[] proxyargs
          ) throws Throwable {
     
      proxy = null; //GC
      proxymethod = null; //GC
      try {
      if (proxyargs != null) {
        for(int i=0; i< proxyargs.length; i++) proxyargs[i] = null; //GC
      }
      proxyargs = null; //GC
      return method.invoke(target, parameters);
      } catch (Exception exc) {
        throw exc;
      } finally {
        if (rootpane != null) ((javax.swing.JComponent)rootpane).repaint();
      }
    }

  }
  
  
  
} //End of JXEngine
