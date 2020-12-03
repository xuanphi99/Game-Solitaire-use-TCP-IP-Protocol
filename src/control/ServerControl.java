/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import static control.ServerControl.con;
import static control.ServerControl.AcceptContinue;
import static control.ServerControl.TongThoiGian;
import static control.ServerControl.getPort;
import static control.ServerControl.insertRanking;
import static control.ServerControl.list;
import static control.ServerControl.listBC;
import static control.ServerControl.listBCTmp;
import static control.ServerControl.listSockets;
import static control.ServerControl.maDe;
import static control.ServerControl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import model.Ranking;
import model.Room;
import model.User;
import view.MessageView;

/**
 *
 * @author Administrator
 */
public class ServerControl {

    public static int portNguoiMoi;
    public static int portNguoiDuocMoi;
    public static String nguoiMoi;
    public static String nguoiDuocMoi;
    public static HashMap listBC;
    public static HashMap listBCTmp;
    public static Connection con;
    private int serverPort = 6666;
    public static ArrayList<User> list;
    public static List<Socket> listSockets;
    private ServerSocket Server;
    private MessageView view;
    public static int mabc;
    public static Ranking ranking;
    public static Set<Integer> AcceptContinue;
    public static int maDe;
    public   static  double TongThoiGian;
    public ServerControl(MessageView view) {
        AcceptContinue = new HashSet<>();
        maDe = 1;
        TongThoiGian = 6 * 60;
        // open cong ket noi
        this.view = view;
        portNguoiDuocMoi = -1;
        nguoiDuocMoi = "";
        nguoiMoi = "";
        portNguoiMoi = -1;
        ServerControl.listBC = new HashMap<String, String>();
        ServerControl.listBCTmp = new HashMap<String, Integer>();
        ServerControl.list = new ArrayList<User>();
        ServerControl.listSockets = new ArrayList<Socket>();
        mabc = 1000;
      //  getJDBC("ltmang", "root", "xuanphi99ndt2");
      getJDBC("ltmang", "root", "xuanphi99ndt2");
        view.showMessage("TCP Server is running ...");

        // chap nhan socket cua client
        // thuc thi ham listening        
    }
        
    // check exist Room
    public static Room checkRoom(int idUser, int idDoiThu){
        Room room = new Room();
        try {
            
       
          String query = "SELECT * FROM room WHERE  (idUser = ? and idDoiThu = ?) or  (idUser = ? and idDoiThu = ?)  ";
            PreparedStatement pre  = con.prepareStatement(query);
           
                pre.setInt(1, idUser);//
                pre.setInt(2, idDoiThu);//
                pre.setInt(3, idDoiThu);//
                pre.setInt(4, idUser);//
               
             ResultSet resultSet = pre.executeQuery();
             System.out.println("chuoi truy van "+ query);
              while (resultSet.next()) {  
                 room .setIdRoom(resultSet.getInt("idRoom"));
                  room.setIdUser(resultSet.getInt("idUser"));
                  room.setIdDoiThu( resultSet.getInt("idDoiThu"));
       
               return room;
                }
             
               } catch (Exception e) {
                   e.printStackTrace();
        }
     return null;
    }
 
    public static void addRoom(Room r) throws SQLException{
           String s = " Insert INTO room(idUser, idDoiThu)  values (?,?)";
           PreparedStatement pre  = con.prepareStatement(s);
                pre.setInt(1, r.getIdUser());
                pre.setInt(2,r.getIdDoiThu() );
                pre.executeUpdate();
    }
    public  static List<Integer> getIdRoom( int id) throws SQLException{
    List<Integer> listidroom = new  ArrayList<>();
         String query = "SELECT * FROM room where idUser = ? or idDoiThu = ?   ";
          PreparedStatement pre = con.prepareStatement(query);
          pre.setInt(1, id);
          pre.setInt(2, id);
             ResultSet resultSet = pre.executeQuery();
             while (resultSet.next()) {  
              int idRoom =   resultSet.getInt("idRoom");
            listidroom.add(idRoom);
        }
  
   return listidroom;
    }
 //===============room
    
    public Connection getJDBC(String dbName, String username, String password) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, username, password);
      
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

//check user
    static User checkUser(User user) throws Exception {
        User result = null;
        String query = "SELECT * FROM users WHERE username ='"
                + user.getUserName() + "' AND password ='"
                + user.getPassword() + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                result = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("ten"), rs.getInt("status"), rs.getInt("trangthai"), rs.getDouble("tongdiem"));
                return result;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;

    }
// end checkUser

// update sum Score user
    static void updateScore(int id, double newScore) {
        try {
            String query = "UPDATE users SET tongdiem = ? WHERE id = ?";
            PreparedStatement pre = con.prepareStatement(query);
            System.out.println("Connection = " + con);
            pre.setDouble(1, newScore);
            pre.setInt(2, id);
            pre.executeUpdate();
        } catch (Exception e) {
        }
    }
    static User  getUser(int  id) throws SQLException{
        User u = new User();
         String query = "SELECT * FROM users where id = ?  ";
       PreparedStatement pre = con.prepareStatement(query);
       pre.setInt(1, id);
        ResultSet resultSet = pre.executeQuery();
        while (resultSet.next()) {  
            u.setTongdiem(resultSet.getDouble("tongdiem"));
            u.setId(resultSet.getInt("id"));
            u.setTrangthai(resultSet.getInt("trangthai"));
            u.setTen(resultSet.getString("ten"));
        }
        return u;
    }
    static List<User> getListUsers() throws SQLException{
        List<User> listU = new ArrayList<>();
     
         String query = "SELECT * FROM users   ";
          PreparedStatement pre = con.prepareStatement(query);
           ResultSet resultSet = pre.executeQuery();
        while (resultSet.next()) {
         User u = new User();
           u.setTongdiem(resultSet.getDouble("tongdiem"));
            u.setId(resultSet.getInt("id"));
            u.setTrangthai(resultSet.getInt("trangthai"));
            u.setTen(resultSet.getString("ten"));
            u.setTbDiem(0);
            u.setTbTgian(0);
            listU.add(u);
        }
          
          return listU;
    }
    
    // update tong diem 1 user
    static void updateSumScoreUser(double newScore,int id) {
        try { 
            User u1 = getUser(id);
            System.out.println("id = "+ id);
            String query = "update users set tongdiem = ? where id = ? ";
      
            PreparedStatement pre = con.prepareStatement(query);
   
            pre.setDouble(1,(u1.getTongdiem()+newScore));
          
            pre.setInt(2, u1.getId());
          int x=  pre.executeUpdate();
            System.out.println("x======"+x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// return port

    //get port no request
    static int getPort(int port, int portChuaDongY) {
        Set<String> keyMaBc = listBC.keySet();

        for (String item : keyMaBc) {
            if (item.contains(String.valueOf(port))) {
                String portArr[] = item.split("\\-");
                if (Integer.parseInt(portArr[0]) == port) {
                    portChuaDongY = Integer.parseInt(portArr[1]);

                } else {
                    portChuaDongY = Integer.parseInt(portArr[0]);

                }
            }
        }
        return portChuaDongY;
    }
    
    // update diem khi hoa user,ranking
static void IncreaseScore(Socket socket,double  thoiGian,double diem, int idThang) throws SQLException{
                     int portGui= socket.getPort();
                    int portKoGui = getPort(portGui, -1);
                  
                    int portMoi = 0;
                    int portDcmoi=0;
                     Set<String> keyMaBc = listBC.keySet();
                    for (String item : keyMaBc) {
                        if (item.contains(String.valueOf(portGui))) {
                            portMoi = Integer.parseInt(item.split("\\-")[0]);
                            portDcmoi = Integer.parseInt(item.split("\\-")[1]);
                            break;
                        }
                       
                    } 
           int idUser = getIdByPort(portMoi);
         int idDoiThu = getIdByPort(portDcmoi);          
                    if ( portKoGui != -1 && portGui == portMoi) {
         System.out.println(" ng gui "+ portMoi +" nguoi nhan  "+ portDcmoi);
       
           System.out.println(" id gui "+ idUser +" id nhan  "+ idDoiThu);
        
                 Room room = checkRoom(idUser, idDoiThu);
                        if (room==null) {
                              System.out.println("room nullll");
                        }
                        else {
                           
                      
//                        System.out.println("cong diem");
                            int idRoom = room.getIdRoom();
                       
                            //cap nhat ket qua moi vao bang rank
                          //  room, idThang, diem, thoiGian;
                            insertRanking(room, idThang, diem, thoiGian);
                             updateSumScoreUser(diem, room.getIdUser());
                             updateSumScoreUser(diem, room.getIdDoiThu());
             
                  }      
    }
                   

}
static boolean  checkLoiMoi(int port){
  
          Set<String> keyMaBc = listBC.keySet();

                            for (String i : keyMaBc) {
                                if ( i.contains(String.valueOf(port)) && Integer.parseInt(i.split("\\-")[0])== port) {
                                    return  true;
                                }
                            }
    return false;
}

    static int getIdByPort(int port){
        for (int i=0;i< listSockets.size();i++) {
          
            if (listSockets.get(i).getPort() == port) {
                return list.get(i).getId();
            }
        }
        return -1;
    }
   
    //tao kqua choi 
    static void insertRanking(Room room, int idThangTran , double diem, double  thoiGian) {
        try {
            String s = " Insert INTO ranking(idRoom, idThangTran,diem,ngayDau,thoiGianGiaiBai)  values (?,?,?,?,?)";
              SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
                                Date date = new Date();
            PreparedStatement pre = con.prepareStatement(s);
            pre.setInt(1, room.getIdRoom());
            pre.setInt(2, idThangTran);
            pre.setDouble(3,diem );
            pre.setString(4, String.valueOf(formatter.format(date)));
            pre.setDouble(5,thoiGian);
           
          pre.executeUpdate();
            System.out.println("insert ranking");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// updateScoreListUserOnline
    
    static ArrayList<User> getListUser(ArrayList<User> li) throws SQLException{
        for (int i = 0; i < li.size(); i++) {
            
                  User u =   getUser(li.get(i).getId());
                  
                  li.get(i).setTongdiem(u.getTongdiem());
        }
        return  li;
    }
    
    static double getRankingByDiem(int idroom , int idThangtran) throws SQLException{
    double tbDiemCacTran = 0;
      String query = "SELECT * FROM ranking where idRoom = ? and ( idThangTran = -1 or idThangTran =? )  ";
          PreparedStatement pre = con.prepareStatement(query);
          pre.setInt(1, idroom);
          pre.setInt(2, idThangtran);
          
             ResultSet resultSet = pre.executeQuery();
             while (resultSet.next()) {            
            tbDiemCacTran+= resultSet.getDouble("diem");
        }
             return tbDiemCacTran;
    }
    static double getRankingByTgian(int idroom, int idThangtran ) throws SQLException{
    double tbThoiGian = 0;
      String query = "SELECT * FROM ranking where idRoom = ? and idThangTran =? ";
          PreparedStatement pre = con.prepareStatement(query);
          pre.setInt(1, idroom);
         pre.setInt(2, idThangtran);
             ResultSet resultSet = pre.executeQuery();
             while (resultSet.next()) {            
            tbThoiGian+= resultSet.getDouble("thoiGianGiaiBai");
        }
             return tbThoiGian;
    }
    
    // listening Request
    public void listenning() throws IOException {
        Server = new ServerSocket(serverPort);
        //   System.out.println("myServer"+Server);
//              WriteServer write = new WriteServer();
//              write.start();
        System.out.println("Server is running...");
        while (true) {
            Socket socket = Server.accept();
            System.out.println("Đã kết nối với " + socket);

            ReadServer read = new ReadServer(socket);

            read.start();
        }

    }

}

class ReadServer extends Thread {

    private Socket socket;

    public ReadServer(Socket socket) {
        this.socket = socket;
    }

    // 
    @Override
    public void run() {

        try {
            // doc du lieu tu client truyen sang
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            while (true) {
                // khoi tao luong doc
                String sms = dis.readUTF(); // nhan dl tu client

                // bat dau xu ly dl
                // neu muon login
                if (sms.contains("login")) {
                    String arr[] = sms.split("\\-");
                    User user = new User(arr[1], arr[2]);
                    User userSucess = ServerControl.checkUser(user); // user csdl
                    //co tai khoan trong csdl 
                    if (userSucess != null) {
                        int check = -1;
                        // check xem dn chuwa
                        for (User iuUser : list) {
                            if (iuUser.getId() == userSucess.getId()) {
                                check = 0;
                                break;
                            }
                        }
                        // neu chua dn
                        if (check == -1) {
                            list.add(userSucess);
                            listSockets.add(socket);
                            //  System.out.println("Add Socket" + socket);
                            //  System.out.println("Add user" + user);
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF(userSucess.getId() + "-" + userSucess.getTen() + "-" + userSucess.getTrangthai() + "-" + userSucess.getTongdiem() + "-" + "loginSucess");
                            //   System.out.println(" kq login == " + userSucess.getId() + "-" + userSucess.getTen() + "-" + userSucess.getTrangthai() + "-" + userSucess.getTongdiem() + "-" + "loginSucess");
                            dos.flush();
                        } // khi da dn
                        else {
                            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                            dos.writeUTF("dang nhap that bai");
                            dos.flush();
                        }

                    } else {
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeUTF("dang nhap that bai");
                        dos.flush();
                    } // dang nhap that bai

                } // neu muon login
                //neu muon xem ds online
                if (sms.contains("checkuser")) {
                    System.out.println("hien tai co " + list.size() + " dang online .....");
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    String listUserOnline = "";
                    list = getListUser(list);
                    
                    for (User user : list) {
                        listUserOnline = listUserOnline + ";" + "-" + user.getId() + "-" + user.getTen() + "-" + user.getTrangthai() + "-" + user.getTongdiem();
                    }
                    dos.writeUTF(listUserOnline + ";" + "checkuser");
                    //  System.out.println(listUserOnline);

                }
                //end xem ds onine

                // xoa 1 user khoi ds
                if (sms.contains("exit")) {
                    String arr[] = sms.split("\\-");
                    int portExit = socket.getPort();
                    int indexExitId = getIdByPort(portExit); //lose
                    
                    int portAlert = socket.getPort();
                    int indexportAlert = getIdByPort(portAlert); //wwin
                 
                    if (arr.length == 3) { // xu ly khi dang choi có 1 ng thoát
                        if (!arr[2].equals("Thoi gian con lai :0:0")) {
                          // cap nhat thoi gian giai bai  
                        String timeFinish[] = arr[2].split("\\:");
                                         double  tgian = Double.parseDouble(timeFinish[1])*60 + Double.parseDouble(timeFinish[2]);
                                         
                                            System.out.println("tgian hoan thanh  == "+ (TongThoiGian- tgian));     
                            
                            
                            
                          

                            // bao cho ng con lai ng kia da thoat
                            int portUserWin = -1;
                            for (Socket item : listSockets) {
                             //   if (ServerControl.portNguoiDuocMoi == portExit) {
                                 if (checkLoiMoi(portExit)==false) {  // port thoat la ng dc moi 
                                    if (item.getPort() == getPort(portExit, -1)) {
                                        DataOutputStream oos = new DataOutputStream(item.getOutputStream());
                                        oos.writeUTF("Đối thủ đã rời khỏi phòng!!Bạn thắng được cộng 1 điểm ");
                                       
                                        portUserWin = getPort(portExit, -1);
                                        System.out.println("nguoi moi choi thang **********************************");
                                        Room r = checkRoom(getIdByPort(portUserWin), indexExitId);

                                        updateSumScoreUser(1, getIdByPort(portUserWin));
                                      insertRanking(r, r.getIdUser(), 1, (TongThoiGian- tgian));
                                        
                                        break;
                                    }

                                } 
                                //else if (ServerControl.portNguoiMoi == portExit) {
                              else if (checkLoiMoi(portExit)==true) {   // port thoat la ng moi ,  
                                    if (item.getPort() == getPort(portExit, -1)) {
                                        DataOutputStream oos = new DataOutputStream(item.getOutputStream());
                                        oos.writeUTF("Đối thủ đã rời khỏi phòng!!Bạn thắng được cộng 1 điểm ");
                                       
                                        portUserWin = getPort(portExit, -1);
                                          System.out.println("nguoi moi choi thua **********************************");                                          
                                          
                                       Room r = checkRoom(getIdByPort(portExit), getIdByPort(portUserWin));  
                               
                                         updateSumScoreUser(1, getIdByPort(portUserWin));
                                  insertRanking(r, r.getIdDoiThu(), 1, (TongThoiGian- tgian));
                                        break;
                                    }
                                }

                            }
                        // cap nhat lai ds phong
                            Set<String> keyMaBc = listBC.keySet();

                            for (String i : keyMaBc) {
                                if (i.contains(String.valueOf(portExit))) {
                                    // listBC.replace(i, "");
                                    listBC.remove(i);
                                  listBCTmp.remove(i);
                                    break;
                                    //   System.out.println("Se xoa o vi tri co key  "+ i);
                                }
                            }      
                            
                            // System.out.println(" port ng thang == "+portUserWin);
// update diem cho ng ko thoat  ----------------------------------------------------
                            // port ng thua socket.port do thoat
                            // port ng thang  = portUserWin
                            for (int i = 0; i < listSockets.size(); i++) {
                                if (listSockets.get(i).getPort() == portUserWin) {
                                    System.out.println("port dc cong diem " + listSockets.get(i).getPort());
                                    double ScoreOfUserWin = list.get(i).getTongdiem();
                                    System.out.println("diem hien tai cua " + listSockets.get(i).getPort() + " = " + ScoreOfUserWin);
                               //     ServerControl.updateScore(list.get(i).getId(), ScoreOfUserWin + 1);
                                    double newScore = ScoreOfUserWin + 1;
                                  
                                }
                            }
                        }
                    }
                    
             User userRemove = new User();
                    for (User itemUser : list) {

                        if (itemUser.getId() == Integer.parseInt(arr[1])) {
                            userRemove = itemUser;
                        }
                    }

                    System.out.println("Da xoa " + userRemove.getTen());
                    list.remove(userRemove);
                    System.out.println("Da ngat ket noi " + portExit);
                    listSockets.remove(socket);
                    System.out.println("còn " + list.size() + " user dang online....");          
                    
                    
//                    ServerControl.portNguoiDuocMoi = -1;
//                    ServerControl.portNguoiMoi = -1;

                    dis.close();
                    socket.close();
                    continue;
                }
                //end xoa user
                // moi choi
                if (sms.contains("invite")) {

                    System.out.println("chuoi dc cient gui : " + sms);
                    String arr[] = sms.split("\\-");
                    int id = Integer.parseInt(arr[1]);
                    int ided = Integer.parseInt(arr[2]);
                    int portRequest = 0, portRespond = 0;
                    String req = "", rsp = "";

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getId() == id) {
                            portRequest = listSockets.get(i).getPort();
                            req = list.get(i).getTen();
                        }
                        if (list.get(i).getId() == ided) {
                            portRespond = listSockets.get(i).getPort();
                            rsp = list.get(i).getTen();
                        }
                    }
                    System.out.println(" ng moi " + portRequest);
                    System.out.println(" ng nhan " + portRespond);
                    System.out.println("nguoi moi " + req);
                    System.out.println("nguoi nhan " + rsp);
                    ServerControl.nguoiDuocMoi = rsp;
                    ServerControl.nguoiMoi = req;
                    for (Socket item : listSockets) {
                        if (item.getPort() == portRespond) {
                            DataOutputStream oos = new DataOutputStream(item.getOutputStream());
                            ServerControl.portNguoiDuocMoi = portRespond;
                            oos.writeUTF("Nguoi choi ten la : " + req + " da gui 1 loi moi cho ban");
                            oos.flush();
                            //      System.out.println("Nguoi choi ten la : " + req + " da gui 1 loi moi choi game cho bạn");
                        }
                        if (item.getPort() == socket.getPort()) {
                            ServerControl.portNguoiMoi = portRequest;
                            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
                            oos.writeUTF("gui yeu cau thanh cong dang doi phan hoi");
                            oos.flush();
                            //   System.out.println("gui yeu cau thanh cong dang doi phan hoi");
                        }
                    }
                }
                //end moi choi
//xu ly lua chon moi choi
                if (sms.contains("LuaChonChoi")) {
                    String arr[] = sms.split("\\-");
                    //ok
                    if (arr[1].equals("0")) {
                        ServerControl.mabc++;
                        //   ServerControl.listBC.put("BC"+ServerControl.mabc, 2);
                        listBC.put(ServerControl.portNguoiMoi + "-" + ServerControl.portNguoiDuocMoi, ServerControl.nguoiMoi + " Đang đấu với " + ServerControl.nguoiDuocMoi);
                        listBCTmp.put(ServerControl.portNguoiMoi + "-" + ServerControl.portNguoiDuocMoi, 0);

                        Random id = new Random();
                        int de = id.nextInt(10) + 1;
                        // chuyen trang thai 2 ban dong y choi thanh ban
                        for (int i = 0; i < listSockets.size(); i++) {

                            if (listSockets.get(i).getPort() == ServerControl.portNguoiDuocMoi
                                    || listSockets.get(i).getPort() == ServerControl.portNguoiMoi) {
                                ServerControl.list.get(i).setTrangthai(1);

                                System.out.println("Bat dau choi tai bàn chơi mã bc:" + ServerControl.mabc);
                                // gui thong tin  ma de
                                DataOutputStream oos = new DataOutputStream(listSockets.get(i).getOutputStream());
                                maDe = de;

                             
                                oos.writeUTF("play game-" + de);
                            }

                        }
 // luu lai lich su choi tai room
         Room room = new Room();
                                for (int i = 0; i < listSockets.size(); i++) {
                                    if (listSockets.get(i).getPort() == ServerControl.portNguoiMoi) {
                                        room.setIdUser(list.get(i).getId());
                                    }
                                    if (listSockets.get(i).getPort() == ServerControl.portNguoiDuocMoi) {
                                        room.setIdDoiThu(list.get(i).getId());
                                    }
                                }
//                               SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
//                                Date date = new Date();  
    
//tao 1 phong choi neu chua tung doi đầu
                                
                                if(checkRoom(room.getIdUser(), room.getIdDoiThu())== null){
                                    System.out.println("add room");
                                    addRoom(room); // a dau voi b
                                }
                           
                             
                                
                    } // ok choi
                    // reject
                    if (arr[1].equals("1")) {
                        for (Socket item : listSockets) {
                            if (item.getPort() == ServerControl.portNguoiMoi) {
                                DataOutputStream oos = new DataOutputStream(item.getOutputStream());
                                oos.writeUTF("rất Tiếc Nguoi choi tu choi loi moi!!!");
                            }
                        }
                    }
                }
// end lua chon choi
// danh sach ban choi
                if (sms.contains("get danh sach ban choi")) {
                    Set<String> keyMaBc = listBC.keySet();
                    String strMabc = "", valueBc = "";
                    for (String i : keyMaBc) {
                        strMabc = strMabc + ";" + i;
                        valueBc += "@" + listBC.get(i);
                    }
                    System.out.println("key cua bc  " + strMabc);
                    System.out.println("value cua bc  " + valueBc);
                    // danh sach ban choi ; a-b ; c-d $ value @ value

                    DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
                    oos.writeUTF("Danh sach cac ban choi" + strMabc + "$" + valueBc);
                    System.out.println("value = " + valueBc);

                }

//end ds ban choi
// Update status
                if (sms.contains("CheckSatusUser")) {
                    String str[] = sms.split("\\-");
                    for (User i : list) {
                        if (i.getId() == Integer.parseInt(str[1])) {
                            i.setTrangthai(0);
                            break;
                        }
                    }
                    
                     Set<String> keyMaBc = listBC.keySet();
                     for (String string : keyMaBc) {
                         if (string.contains(String.valueOf(socket.getPort())) ) {
                             listBCTmp.remove(string);
                             listBC.remove(string);
                         }
                    }
                    
                }
// update trang thai

//phan hoi khong choi tiep
                if (sms.contains("khong choi tiep")) {
                   
                    System.out.println("1 nguoi " + socket.getPort() + " ko choi tiep");
                    Set<String> keyMaBc = listBC.keySet();
                   
                    int portChuaThoat = 0;
                  
                    // xoa port trong ds dong y choi
                    //tim ra port chua thoat  = portChuaThoat
                    // port thoat = socket.getPort()
//                    for (String string : keyMaBctmp) {
//                        System.out.println(" key = "+ string +" value "+ listBCTmp.get(string)  +" "+((Integer)listBCTmp.get(string)+1));
//                    }
                    
                    
                    for (String item : keyMaBc) { //59101-59103
            int   dem =   ((Integer)listBCTmp.get(item)) ;
                        if (item.contains(String.valueOf(socket.getPort()))) {
                            String portArr[] = item.split("\\-");
                            dem = dem+1;
                            listBCTmp.replace(item, (Integer)dem);
                            
                            //portArr[]=  59101 
                            // 59103
                            if (Integer.parseInt(portArr[0]) == socket.getPort()) {
                                portChuaThoat = Integer.parseInt(portArr[1]);
                            } else {
                                portChuaThoat = Integer.parseInt(portArr[0]);
                            }

                            System.out.println("con " + list.size() + " nguoi dang online...");

                        }
                        //gui cho port chua thoat
                        for (Socket listSocket : listSockets) {
                            if (listSocket.getPort() == portChuaThoat) {
                                DataOutputStream oos = new DataOutputStream(listSocket.getOutputStream());
                                System.out.println("gui tb toi " + portChuaThoat);
                                oos.writeUTF("1 ban da thoat game");
                            }
                        }

                     
                    }
                    
                   //xoa trong ds online
                        for (int i = 0; i < listSockets.size(); i++) {
                            if (socket.getPort() == listSockets.get(i).getPort()) {
                                list.remove(i);
                                listSockets.remove(i);
                                break;
                            }
                        }
                       // xoa trong ds bc
                         Set<String> keytmp = listBCTmp.keySet();
                         
                         for (String string : keytmp) {
                             if ((Integer)listBCTmp.get(string) == 2) {
                                 listBC.remove(string);
                                 listBCTmp.remove(string);
                             }
                        }    

                    System.out.println("con " + list.size() + " dang online....");
                    System.out.println("ds ban choi " + listBC.size() + " dang mo....");
                    dis.close();
                    socket.close();
                    continue;
                }

// ko choi tiep
// dong y choi tiep
                if (sms.contains("dong y choi tiep")) {
                      AcceptContinue.add(socket.getPort());
                     
                 int portChuaDongy =   getPort(socket.getPort(), -1);
                 boolean flag =false;
                    for (Integer integer : AcceptContinue) {
                        if (integer == portChuaDongy) {
                            flag =true; break;
                        }
                    }
                    if (flag) {
                        Set<String> keyMaBc = listBC.keySet();
                   
                                //tao bai
                                 Random id = new Random();
                                int de = id.nextInt(10) + 1;
                                while (maDe == de) {
                                    de = id.nextInt(10) + 1;

                                }
                                for (Socket i : listSockets) {
                                    if (i.getPort() == portChuaDongy || i.getPort() == socket.getPort()) {

                                DataOutputStream oos = new DataOutputStream(i.getOutputStream());
                                        maDe = de;
                                        oos.writeUTF("playGameContinue-" + de);
                                    }
                                }
                              AcceptContinue.remove(socket.getPort());
                               AcceptContinue.remove(portChuaDongy);   
 
                    }
                    
 
                }
// phan hoi you win
             if (sms.contains("You winner")) {
                
                   
               //Socket socket,double  thoiGian,double diem, int idThang  
              
              // IncreaseScore(socket,0,0.5,-1);    
               
            int portWin = socket.getPort();
                   Set<String> keyMaBc = listBC.keySet();
                 int portLose = getPort(portWin, -1);
                 System.out.println("portLose "+ portLose);
                   for (Socket i : listSockets) {
                       if (i.getPort() == portLose) {
                           DataOutputStream oos = new DataOutputStream(i.getOutputStream());
                           oos.writeUTF("you lose !!");
                           oos.flush();
                       }
                   }
                   // you lose:Thoi gian con lai :4:48
                 String time[] = sms.split("\\:");
                 int idWin = getIdByPort(portWin);
                 System.out.println("id win "+ idWin);
                 System.out.println(" time "+ time[2]+" hourse "+ time[3]);
                double timeFinish = TongThoiGian - ((Double.parseDouble(time[2])*60) + Double.parseDouble(time[3]));
                 updateSumScoreUser(1, idWin);
                // room, idThang, diem, thoiGian
            
         // insert Ranking
            int portGui= socket.getPort();
                    int portKoGui = getPort(portGui, -1);
                  
                    int portMoi = 0;
                    int portDcmoi=0;
                     Set<String> keyMaBc1 = listBC.keySet();
                    for (String item : keyMaBc1) {
                        if (item.contains(String.valueOf(portGui))) {
                            portMoi = Integer.parseInt(item.split("\\-")[0]);
                            portDcmoi = Integer.parseInt(item.split("\\-")[1]);
                            break;
                        }
                       
                    } 
           int idUser = getIdByPort(portMoi);
         int idDoiThu = getIdByPort(portDcmoi);    
            
                 
                //    if ( portKoGui != -1 && portGui == portMoi) {
         System.out.println(" ng gui "+ portMoi +" nguoi nhan  "+ portDcmoi);
       
           System.out.println(" id gui "+ idUser +" id nhan  "+ idDoiThu);
        
                 Room room = checkRoom(idUser, idDoiThu);
                      
                        
                          insertRanking(room, idWin, 1, timeFinish);
                        
                 //   }
         
         // ranking
                 
                 
          }  // het you win 
                
//end dong y choi tiep
 //tb ket qua 
                if (sms.contains("Moi ben duoc them 0.5")) {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    //tang 0.5 diem ca 2 bang user va ranking
                 //Socket socket,double  thoiGian,double diem, int idThang 
                
                    IncreaseScore(socket,0,0.5,-1);
                    
                }
 
 // end tb ket qua
// xem bxh
                if (sms.contains("xem bang xep hang")) {
                 
                    List<User> listUsers = getListUsers();
//                    for (User listUser : listUsers) {
//                        System.out.println(listUser.getId()+" - "+ listUser.getTongdiem());
//                    }
//                    System.out.println("--sau khi xos--");
                 //   Collections.sort(listUsers);
                //    HashMap<Integer, Double> hashMap = new HashMap<Integer, Double>();
                      for (User listUser : listUsers) {
                          // moi id user
                    //    System.out.println(listUser.getId()+" ------- "+ listUser.getTongdiem());
                        List<Integer> listIdroom = getIdRoom(listUser.getId()); // ds id room ma user tgia
                        double diemtp =0, i=0,tbThoiGian=0;
                          for (Integer integer : listIdroom) {
                              // idRoom tuong ung co iduser
                           //   System.out.println("*** "+ integer);
                            //  System.out.println(listUser.getTen() +" co tong cac diem cac lan choi tai ma phong "+ integer+" la"+ getRankingByDiem(integer,listUser.getId()) );
                              diemtp+=getRankingByDiem(integer,listUser.getId());
                              tbThoiGian+= getRankingByTgian(integer,listUser.getId());
                              i++;
                          }
                          if (i!=0) {
                        
                              
                              diemtp=diemtp/i;
                              tbThoiGian = tbThoiGian/i;
                          }
                          
                        //  hashMap.put(listUser.getId(), diemtp);
                        //  System.out.println("tong "+ diemtp);
                        listUser.setTbDiem(diemtp);
                        listUser.setTbTgian(Math.round(tbThoiGian));
                         
                    }
          Collections.sort(listUsers);
          String dsxh = "";
                    for (User listUser : listUsers) {
                        System.out.println(" id =" + listUser.getId() +"  - "+ listUser.getTongdiem()+ " - "+ listUser.getTbDiem()+" - "+ listUser.getTbTgian()+"s"); 
                  dsxh=dsxh+";"+listUser.getTen()+"-"+listUser.getTongdiem()+"-"+listUser.getTbDiem()+"-"+listUser.getTbTgian();
                    }
                    System.out.println(dsxh);
                DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
                                       
                                        oos.writeUTF("bangxephang"+dsxh);       
                    
                }
//xem bxh
            } // whlie
        } catch (Exception e) {
        }

    }

}
