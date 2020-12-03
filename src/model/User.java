/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author xuan phi
 */
public class User implements Serializable,Comparable<User>{
    private int id;
    private String userName;
    private String password;
    private String ten;
    private int status;
    private int trangthai;
    private double tongdiem;
    private double tbDiem;
    private double tbTgian;
    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User() {
    }

    public User( int id,String userName, String password, String ten, int status, int trangthai, double tongdiem) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.ten = ten;
        this.status = status;
        this.trangthai = trangthai;
        this.tongdiem = tongdiem;
    }

    public double getTbDiem() {
        return tbDiem;
    }

    public void setTbDiem(double tbDiem) {
        this.tbDiem = tbDiem;
    }

    public double getTbTgian() {
        return tbTgian;
    }

    public void setTbTgian(double tbTgian) {
        this.tbTgian = tbTgian;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public double getTongdiem() {
        return tongdiem;
    }

    public void setTongdiem(double tongdiem) {
        this.tongdiem = tongdiem;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int compareTo(User o) {
        if (this.getTongdiem() > o.getTongdiem()) {
            return  -1;
        }
      else  if (this.getTongdiem() < o.getTongdiem()) {
            return  1;
        }
      else 
      {
          if (this.getTbDiem()<o.getTbDiem()) {
              return 1;
          }
          else if (this.getTbDiem()>o.getTbDiem()) {
              return -1;
          }
        else {
           if (this.getTbTgian()>o.getTbTgian()) {
              return 1;
          }
          else if (this.getTbTgian()<o.getTbTgian()) {
              return -1;
          }
              
          else  return 0;
          }
      } 
         
        
    }
    
    
}
