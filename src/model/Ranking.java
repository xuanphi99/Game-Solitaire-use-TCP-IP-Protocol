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
public class Ranking {
   private int idRanking ;
private int idRoom ;
private int idThangTran ;
double diem ;
private  String ngayDau ; 
private double thoiGianGiaiBai ; 

    public Ranking() {
    }

    public Ranking(int idRanking, int idRoom, int idThangTran, double diem, String ngayDau, double thoiGianGiaiBai) {
        this.idRanking = idRanking;
        this.idRoom = idRoom;
        this.idThangTran = idThangTran;
        this.diem = diem;
        this.ngayDau = ngayDau;
        this.thoiGianGiaiBai = thoiGianGiaiBai;
    }

    public int getIdRanking() {
        return idRanking;
    }

    public void setIdRanking(int idRanking) {
        this.idRanking = idRanking;
    }

    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public int getIdThangTran() {
        return idThangTran;
    }

    public void setIdThangTran(int idThangTran) {
        this.idThangTran = idThangTran;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public String getNgayDau() {
        return ngayDau;
    }

    public void setNgayDau(String ngayDau) {
        this.ngayDau = ngayDau;
    }

    public double getThoiGianGiaiBai() {
        return thoiGianGiaiBai;
    }

    public void setThoiGianGiaiBai(double thoiGianGiaiBai) {
        this.thoiGianGiaiBai = thoiGianGiaiBai;
    }

   
}
