/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RunMain;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import view.LoginView;

/**
 *
 * @author Administrator
 */
public class ClientRun {
       public static void main(String[] args) throws IOException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 6666);
        control.ClientControl clientControl = new control.ClientControl(socket);
          
        clientControl.getLoginview().setVisible(true);

    }
}
