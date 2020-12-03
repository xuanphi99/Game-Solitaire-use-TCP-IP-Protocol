/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RunMain;

import control.ServerControl;
import java.io.IOException;
import view.MessageView;

/**
 *
 * @author Administrator
 */
public class ServerRun {
     public static void main(String[] args) throws IOException {
        MessageView view = new MessageView();
        ServerControl sc = new ServerControl(view);
        sc.listenning();

    }
}
