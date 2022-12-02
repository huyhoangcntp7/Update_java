/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author Tuan1
 */
public class HoaDonIn {
    private String tenSanPham;
            int SoLuong;
            double DonGia,tienGiamGia;

    public HoaDonIn() {
    }

    public HoaDonIn(String tenSanPham, int SoLuong, double DonGia, double tienGiamGia) {
        this.tenSanPham = tenSanPham;
        this.SoLuong = SoLuong;
        this.DonGia = DonGia;
        this.tienGiamGia = tienGiamGia;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public double getDonGia() {
        return DonGia;
    }

    public void setDonGia(double DonGia) {
        this.DonGia = DonGia;
    }

    public double getTienGiamGia() {
        return tienGiamGia;
    }

    public void setTienGiamGia(double tienGiamGia) {
        this.tienGiamGia = tienGiamGia;
    }
}
