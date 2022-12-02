/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author DeLL
 */
public class SanPhamBanHang {
    private String ma, ten, hang, mau;
    private int size, sl;
    private Double dongia;

    public SanPhamBanHang(String ma, String ten, String hang, String mau, int size, int sl, Double dongia) {
        this.ma = ma;
        this.ten = ten;
        this.hang = hang;
        this.mau = mau;
        this.size = size;
        this.sl = sl;
        this.dongia = dongia;
    }

    public SanPhamBanHang() {
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public Double getDongia() {
        return dongia;
    }

    public void setDongia(Double dongia) {
        this.dongia = dongia;
    }
    
    
}
