/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

public class TT_Thongke_ViewModel {

    private int soDonHang;
    private double doanhthu, TienDauTu, tienThuve;

    public TT_Thongke_ViewModel() {
    }

    public TT_Thongke_ViewModel(int soDonHang, double doanhthu, double TienDauTu, double tienThuve) {
        this.soDonHang = soDonHang;
        this.doanhthu = doanhthu;
        this.TienDauTu = TienDauTu;
        this.tienThuve = tienThuve;
    }

    public int getSoDonHang() {
        return soDonHang;
    }

    public void setSoDonHang(int soDonHang) {
        this.soDonHang = soDonHang;
    }

    public double getDoanhthu() {
        return doanhthu;
    }

    public void setDoanhthu(double doanhthu) {
        this.doanhthu = doanhthu;
    }

    public double getTienDauTu() {
        return TienDauTu;
    }

    public void setTienDauTu(double TienDauTu) {
        this.TienDauTu = TienDauTu;
    }

    public double getTienThuve() {
        return tienThuve;
    }

    public void setTienThuve(double tienThuve) {
        this.tienThuve = tienThuve;
    }

}
