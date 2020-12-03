/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Administrator
 */
public class Room {
  int  idRoom ;
    int idUser ;
int idDoiThu ;

    public Room() {
    }

    public Room(int idRoom, int idUser, int idDoiThu) {
        this.idRoom = idRoom;
        this.idUser = idUser;
        this.idDoiThu = idDoiThu;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdDoiThu() {
        return idDoiThu;
    }

    public void setIdDoiThu(int idDoiThu) {
        this.idDoiThu = idDoiThu;
    }

}
