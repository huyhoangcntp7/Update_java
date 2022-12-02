/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DomainModel;

/**
 *
 * @author DeLL
 */
public class HoaDonCT {

    private String idhd, idctg;
    private int sl;
    private double dongia;

    public HoaDonCT(String idhd, String idctg, int sl, double dongia) {
        this.idhd = idhd;
        this.idctg = idctg;
        this.sl = sl;
        this.dongia = dongia;
    }

    public HoaDonCT() {
    }

    public String getIdhd() {
        return idhd;
    }

    public void setIdhd(String idhd) {
        this.idhd = idhd;
    }

    public String getIdctg() {
        return idctg;
    }

    public void setIdctg(String idctg) {
        this.idctg = idctg;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public double getDongia() {
        return dongia;
    }

    public void setDongia(double dongia) {
        this.dongia = dongia;
    }

    
    
}
