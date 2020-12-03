/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clock;

import solitaire.GUI;
import view.*;

public class SecondThread extends Thread {

    private GUI tf;
    private MinuteThread mtd;
    private int count;
    private  int finsh;
    public SecondThread(GUI tf, MinuteThread mtd,int count) {
        super();
        this.tf = tf;
        this.mtd = mtd;
        this.count = count;
        finsh =-1;
    }

    public int getFinsh() {
        return finsh;
    }

    public void setFinsh(int finsh) {
        this.finsh = finsh;
    }

    public void run() {
        while (true) {
            try {
                this.sleep(1000);
                count--;
                if (count == 0 && mtd.getCount()>0) {
                    count = 60;
                    mtd.DescCrease();
                 //   System.out.println("minute--");
                }
               else if (mtd.getCount()==0 && count==0) {
                   mtd.stop();
                  break;
                }
                tf.setSecond(count);
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
        System.out.println("Stop Second......!!!");
        tf.setSecond(0); 
        tf.msg("Het Gio Hoa bạn được cộng 0.5 điểm");
        tf.sendResult("Moi ben duoc them 0.5");
        tf.checkRequestRepeat();
       
        
       // System.exit(0);
    }

    
}
