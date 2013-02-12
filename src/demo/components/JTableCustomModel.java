/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.components;

/**
 *
 * @author Patel
 */
public class JTableCustomModel extends javax.swing.table.AbstractTableModel {
       
        private Object[][] data = new java.lang.Object[][] {
									{1, "Bob", "Smith", 1948},
									{2, "John", "Doe", 1969},
									{3, "Alan", "Smith", 1998},
								};
         private String[] columnNames = new String[] {
                  "ID", "First", "Last", 
                  "Year"
								};
         
         
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            
        }

       
    }

