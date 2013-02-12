/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo.components;

/**
 *
 * @author Family
 */
public class Tree<V> {
    // These fields hold the value and the branches
    V value;
    java.util.List<Tree<? extends V>> branches = new java.util.ArrayList<Tree<? extends V>>();

    public Tree() {}
    // Here's a constructor
    public Tree(V value) { 
        this.value = value; 
    }

    // These are instance methods for manipulating value and branches
    public V getValue() { return value; }
    public void setValue(V value) { this.value = value; }
    public int getNumBranches() { return branches.size(); }
    public Tree<? extends V> getBranch(int n) { return branches.get(n); }
    public void addBranch(Tree<? extends V> branch) { 
        branches.add(branch);
    }
    
    public static double sum1(Tree<? extends Number> t) {
        double total = t.value.doubleValue();
        for(Tree<? extends Number> b : t.branches) total += sum1(b);
        return total;
    }
    
    public static <N extends Number> double sum2(Tree<N> t) {
        N value = t.value;
        double total = value.doubleValue();
        for(Tree<? extends N> b : t.branches) total += sum2(b);
        return total;
    }
    
    public static <N extends Number> Tree<N> max1(Tree<N> t, Tree<N> u) {
        double ts = sum1(t);
        double us = sum1(u);
        if (ts > us) return t;
        else return u;
    }
    
    public static Tree<? extends Number> max2(Tree<? extends Number> t,
                                         Tree<? extends Number> u) {
        double ts = sum2(t);
        double us = sum2(u);
        if (ts > us) return t;
        else return u;
        
    }
}
