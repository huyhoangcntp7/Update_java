/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author DeLL
 */
public class HoaDonCTBanHang {
    private String masp ,ten;
    private int sl;
    private double donGia;

    public HoaDonCTBanHang(String masp, String ten, int sl, double donGia) {
        this.masp = masp;
        this.ten = ten;
        this.sl = sl;
        this.donGia = donGia;
    }

    public HoaDonCTBanHang() {
    }

    public String getMasp() {
        return masp;
    }

    public void setMasp(String masp) {
        this.masp = masp;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    
    
}
