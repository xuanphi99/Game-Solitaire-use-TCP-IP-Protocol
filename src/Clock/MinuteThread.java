/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clock;

import solitaire.GUI;
import view.*;

public class MinuteThread extends Thread {

    private GUI tf;
   
    private int count;
    // true = dung
   // private boolean flag = false;
    
    public int getCount( ) {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public MinuteThread(GUI tf, int count) {
        super();
        this.tf = tf;
       
        this.count = count;
    }


    public void run() {
        while (true) {
            try {
                tf.setMinute(count);
             //   System.out.println(count +" minute");
      
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
       
        
    }

  public  void DescCrease() {
        count--;
        if (count==0) {
        //  flag=true; // dung dong ho
      }
         tf.setMinute(count);
    }
}
