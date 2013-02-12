/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo.components;

import java.awt.*;
import java.util.Random;

/**
 *
 * @author Patel
 */
public class JProgressBarListener implements Runnable {
  
 public org.JXEngine jxe = null;
  public javax.swing.JProgressBar progressbar = null;
  public javax.swing.JTextArea output = null;
  
  public JProgressBarListener(
     org.JXEngine jxe 
     ) {
    this.jxe = jxe;
  }
  
  
  public void run() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            progressbar.setValue(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                progressbar.setValue(Math.min(progress, 100));
                output.append(String.format( progressbar.getName() +
                    "  => Completed %d%% of task.\n", progressbar.getValue()));
            }    
  }

  /**
     * Invoked when the user presses the start button.
     */
  public void onclickJProgressButton(
     javax.swing.JProgressBar progressbar,
     javax.swing.JTextArea output
              ) {
   
    this.progressbar = progressbar;
    this.output = output;
    
    progressbar.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    new Thread(this).start();
    
  }
 
  
}
