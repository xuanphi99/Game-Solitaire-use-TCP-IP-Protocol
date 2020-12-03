/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import model.Ranking;
import model.User;
import solitaire.Engine;
import solitaire.GUI;
import solitaire.Game;
import view.HomeView;
import static view.HomeView.listUser;
import view.LoginView;

/**
 *
 * @author Administrator
 */
public class ClientControl {

    public static Socket mySocket;
    private String serverHost = "localhost";
    private int serverPort = 6666;
    //-------
    static String result;
    private DataInputStream dis;
    private DataOutputStream dos;
    private JOptionPane jop;
    private Component component;
    private LoginView loginview;
    private HomeView homeView;
    private Game game;
    private GUI gui;
    private int chon;
    public  static User userResult;
    public static Ranking ranking;
    final double TongThoiGian = 5*60;
    public LoginView getLoginview() {
        return loginview;
    }

    public void setLoginview(LoginView loginview) {
        this.loginview = loginview;
    }

    public ClientControl() {
    }

    public ClientControl(Socket s) throws IOException {
        chon = -1;
        loginview = new LoginView(this);
        homeView = new HomeView(this);
        this.mySocket = s;
        dis = new DataInputStream(s.getInputStream());
        dos = new DataOutputStream(s.getOutputStream());
        jop = null;

        receiveRequest(component);

    }

    public void sendRequest(String msg, Component component) throws IOException {

        dos.writeUTF(msg);

        dos.flush();

        this.component = component;

        //  return true;
    }

    public void sendData(String str) {
        try {
            dos.writeUTF(str);
            dos.flush();

        } catch (Exception e) {

        }

    }

    public User getUserResult() {
        return userResult;
    }

    public void setUserResult(User userResult) {
        this.userResult = userResult;
    }

    public boolean closeConnection() {
        try {
            mySocket.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // nhan dl
    public void receiveRequest(Component component) {
        Thread thread = new Thread() {

            @Override
            public void run() {

                while (true) {
                    try {
                        String str = dis.readUTF();
                        // phan hoi loi moi
                        if (str != null && str.contains("Nguoi choi ten la")) {
                            // phan hoi cho nguoi duoc moi 0 La Yes
                            chon = jop.showConfirmDialog(component, str, "Loi moi choi co ", JOptionPane.YES_NO_OPTION);
                            //   System.out.println("ban y chon"+ chon);
                            //  if (chon == jop.YES_OPTION) {
                            //   homeView.setChonChoi(chon);
                            //}
// gui lua chon cua nguoi dc moi len server
                            try {
                                dos.writeUTF("LuaChonChoi-"+chon);
                                dos.flush();

                            } catch (Exception e) {

                            }

                            // phan hoi cho nguoi moi
                        } else if (str != null && str.contains("gui yeu cau thanh cong dang doi phan hoi")) {
                            jop.showMessageDialog(component, str);
                        } // phan hoi dang nhap
// phan hoi loi moi : tu choi 
                        else if (str != null && str.contains("Nguoi choi tu choi loi moi")) {
                            jop.showMessageDialog(component, str);
                            
                        }
// phan hoi loi moi :dong y 
                        else if (str != null && str.contains("play game")) {
                            
                            System.out.println(str);
                            String arr[] = str.split("\\-");
                            homeView.setVisible(false);
                         //   game = new Game(Integer.parseInt(arr[1]),ClientControl.this);
                         gui = new GUI(new Engine(arr[1]), ClientControl.this,homeView);
                         gui.setTitle("Giao diện của :"+ userResult.getTen()  );
                         gui.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                            gui.addWindowListener(new WindowAdapter() {
                                  public void windowClosing(WindowEvent e) {
                                    if (JOptionPane.showConfirmDialog(gui, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                        try {
                                            //     System.out.println(result.getId());
                                          //  sendRequest(String.valueOf(userResult.getId()), gui);
                                          gui.CheckUserOff(String.valueOf(userResult.getId()));
                                        System.out.println(gui.getTime()+"---------------------------");
                                        double tgian = 0;
                                        String timeFinish[] = gui.getTime().split("\\:");
                                           tgian = Double.parseDouble(timeFinish[1])*60 + Double.parseDouble(timeFinish[2]);
                                            System.out.println("tgian hoan thanh  == "+ (TongThoiGian- tgian));
                                        
                                        } catch (Exception ex) {
                                            Logger.getLogger(ClientControl.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                       
                                        System.exit(0);
                                    }

                                }
});
                        }
                        else if (str != null && str.contains("dang nhap that bai")) {
                            jop.showMessageDialog(component, str);
                        } else if ((str != null && str.contains("loginSucess")) || (str != null && str.contains("exit"))) {
                            // dang nhap thanh cong
                            System.out.println("Dang nhap thanh cong ");
                            loginview.dispose();
                            // khi dang nhap thanh cong chuyen ve trang home
                            homeView = new HomeView(ClientControl.this);

                            homeView.setVisible(true);
                            String userArr[] = str.split("\\-");
                             userResult = new User();
                            userResult.setId(Integer.parseInt(userArr[0]));
                            userResult.setTen(userArr[1]);
                            userResult.setTrangthai(Integer.parseInt(userArr[2]));
                            userResult.setTongdiem(Double.parseDouble(userArr[3]));

                            homeView.setUser(userResult);
                            homeView.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                            homeView.setLocationRelativeTo(null);

                            homeView.addWindowListener(new WindowAdapter() {

                                public void windowClosing(WindowEvent e) {
                                    if (JOptionPane.showConfirmDialog(homeView, "Are you sure you want to close this window?", "Close Window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                                        //     System.out.println(result.getId());
                                        
                                        homeView.CheckUserOff(String.valueOf(userResult.getId()));
                                        System.exit(0);
                                    }

                                }
                            });
                            //end dn thanh cong

                        } // het phan hoi dang nhap  
                        // check user online
                        else if (str != null && str.contains("checkuser")) {
                            JButton jButton3 = homeView.getjButton3();
                            User user = homeView.getUser();
                            JTable jTable1 = homeView.getjTable1();
                            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
                            dtm.setNumRows(0);
                            listUser = new ArrayList<>();
                            //  System.out.println("hihi");

                            try {
                                String listOnline = str;
                                System.out.println(listOnline);
                                String userOnArr[] = listOnline.split("\\;");
                                for (int j = 0; j < userOnArr.length - 1; j++) {
                                    if (j != 0) {
                                        User userResult = new User();
                                        //  System.out.println("item " + userOnArr[j]);

                                        String userArr[] = userOnArr[j].split("\\-");

                                        for (int i = 0; i < userArr.length; i++) {
                                            if (i != 0) {
                                                if (i == 1) {
                                                    userResult.setId(Integer.parseInt(userArr[i]));
                                                    //  System.out.println(userArr[i]);
                                                } else if (i == 2) {
                                                    //    System.out.println(userArr[i]);
                                                    userResult.setTen(userArr[i]);
                                                } else if (i == 3) {
                                                    //  System.out.println(userArr[i]);
                                                    userResult.setTrangthai(Integer.parseInt(userArr[i]));
                                                } else {
                                                    //  System.out.println(userArr[i]);
                                                    userResult.setTongdiem(Double.parseDouble(userArr[i]));
                                                }

                                            }
//
                                        }
                                        //    System.out.println(userResult.getId()+" - "+ userResult.getTen()+" "+ userResult.getTongdiem());
                                        listUser.add(userResult);
                                    }
                                }

                            } catch (Exception ex) {
                                Logger.getLogger(HomeView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (listUser != null && listUser.size() > 1) {

                                jButton3.setEnabled(true);

                                for (User i : listUser) {
                                    if (i.getId() != user.getId()) {
                                        if (i.getTrangthai() == 0) {
                                            dtm.addRow((new Object[]{i.getTen(), "Đang rảnh", i.getTongdiem()}));

                                        } else {
                                            dtm.addRow((new Object[]{i.getTen(), "Đang bận", i.getTongdiem()}));
                                        }
                                    }
                                }

                            } else {
                                homeView.showMessage("khong co ai dang online");
                                jButton3.setEnabled(false);
                            }

                        }

                        // het check user ONline1
   // xem ban choi
                        else if (str != null && str.contains("Danh sach cac ban choi")) {
                            // Danh sach cac ban choi ;a-b $ phi
                            System.out.println(str);
                            String dsBc[] = str.split("\\$");
                            
                            if (dsBc.length>1) {

                            String arrMaBc[] = dsBc[0].split("\\;");
                              JTable jTable2 = homeView.getjTable2();
                            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                            dtm.setNumRows(0);
                            
                            if (arrMaBc.length<=1) {
                               homeView.showMessage("Ban choi hien tai trong");
                            }
                          //  for (String string : arrMaBc) {
                           for (int i=0;i< arrMaBc.length  ; i++) {
                                if (!arrMaBc[i].equals("Danh sach cac ban choi") && !arrMaBc[i].equals("-")) {
                                     dtm.addRow(new Object[]{arrMaBc[i],2,dsBc[1].split("\\@")[i]});
                                }
                               
                            }
                            if (!dsBc[1].equals("") && !dsBc[1].equals("@")) {
                                
                           
                           String valueArr[] = dsBc[1].split("\\@");
                           
                            
                            for (String string : valueArr) {
                                System.out.println(string);
                            }
                            }
                       }     
                            // neu listBc .size<1
                            else {
                             JTable jTable2 = homeView.getjTable2();
                            DefaultTableModel dtm = (DefaultTableModel) jTable2.getModel();
                            dtm.setNumRows(0);
                             homeView.showMessage("Ban choi hien tai trong");     
                            }    
                            
                        }
   // end xem bchoi
   // tbao ng 1 choi thoat giua trung
                        else if (str != null && str.contains("Đối thủ đã rời khỏi phòng")) {
                            
                              gui.StopGame();
                              jop.showMessageDialog(component, str);
                                 System.out.println(gui.getTime()+"---------------------------");
                                        String timeFinish[] = gui.getTime().split("\\:");
                                            for (String string : timeFinish) {
                                                System.out.println(string);
                                            }
                        }
   // tb thoat
   
   // 1 ng choi khong tiep tuc choi
                        if (str!= null && str.contains("1 ban da thoat game")) {
                         
                            jop.showMessageDialog(component, "Doi thu da thoat game");
                            
                        }
   
   // ko tiep tuc choi
   //tiep tuc choi
                        if (str != null && str.contains("playGameContinue")) {
                            String arrDe[] = str.split("\\-");
                         gui.reset(Integer.parseInt(arrDe[1]));
                        }
    // tb you lose
                        if (str != null && str.contains("you lose")) {
                            gui.StopGame();
                            jop.showMessageDialog(component, "You lose !!");
                            gui.checkRequestRepeat();
                        }
                        if (str!= null && str.contains("bangxephang")) {
                            System.out.println(str);
                  String bxhArr[] = str.split("\\;");
                  JTable jTable3 = homeView.getjTable3();
                   DefaultTableModel dtm = (DefaultTableModel) jTable3.getModel();
                            dtm.setNumRows(0);
                            for (String item : bxhArr) {
                                if (!item.equals("bangxephang") ) {
                                    String filedArr[] =item.split("\\-");
                                    dtm.addRow(new Object[]{filedArr[0],filedArr[1],filedArr[2],filedArr[3]});
                                }
                            }
                  
                  
                        }
                        
                        
                    } catch (IOException ex) {
                        // ex.printStackTrace();
                    }

                }
            }

        };
        thread.start();

    }

    public String receiveData() throws IOException {

        String str = dis.readUTF();

        return str;
    }

//    public static void main(String[] args) throws IOException {
//        Socket socket = new Socket(InetAddress.getLocalHost(), 6666);
//        ClientRun clientControl = new ClientRun(socket);
//        clientControl.loginview.setVisible(true);
//
//    }

}
