/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DomainModel;

import java.util.Date;

/**
 *
 * @author DeLL
 */
public class HoaDonBanHang {
//    maHoaDon, idNhanVien, idKhachHang, ngayTao,ngayThanhToan,ngayGiaoHang, ngayMuonNhanHang,
//				ngayNhan, trangThai, hinhThucMua, tienMat, tienChuyenKHoan 

    private String mahd, idnv, idkh, hinhthucmua;
    private String ngaytao, ngaythanhtoan, ngaygiaohang, ngaynhan, giohientai, magd;
    private int trangthai;
    private double tienMat, tienKhac, giamgia, phiShip;

    public HoaDonBanHang(String mahd, String idnv, String idkh, String hinhthucmua,
            String ngaytao, String ngaythanhtoan, String ngaygiaohang, String ngaynhan,
            String giohientai, String magd, int trangthai, double tienMat, double tienKhac, double giamgia, double phiShip) {
        this.mahd = mahd;
        this.idnv = idnv;
        this.idkh = idkh;
        this.hinhthucmua = hinhthucmua;
        this.ngaytao = ngaytao;
        this.ngaythanhtoan = ngaythanhtoan;
        this.ngaygiaohang = ngaygiaohang;
        this.ngaynhan = ngaynhan;
        this.giohientai = giohientai;
        this.magd = magd;
        this.trangthai = trangthai;
        this.tienMat = tienMat;
        this.tienKhac = tienKhac;
        this.giamgia = giamgia;
        this.phiShip = phiShip;
    }

    public HoaDonBanHang() {
    }

    public String getMahd() {
        return mahd;
    }

    public void setMahd(String mahd) {
        this.mahd = mahd;
    }

    public String getIdnv() {
        return idnv;
    }

    public void setIdnv(String idnv) {
        this.idnv = idnv;
    }

    public String getIdkh() {
        return idkh;
    }

    public void setIdkh(String idkh) {
        this.idkh = idkh;
    }

    public String getHinhthucmua() {
        return hinhthucmua;
    }

    public void setHinhthucmua(String hinhthucmua) {
        this.hinhthucmua = hinhthucmua;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    public String getNgaythanhtoan() {
        return ngaythanhtoan;
    }

    public void setNgaythanhtoan(String ngaythanhtoan) {
        this.ngaythanhtoan = ngaythanhtoan;
    }

    public String getNgaygiaohang() {
        return ngaygiaohang;
    }

    public void setNgaygiaohang(String ngaygiaohang) {
        this.ngaygiaohang = ngaygiaohang;
    }

    public String getNgaynhan() {
        return ngaynhan;
    }

    public void setNgaynhan(String ngaynhan) {
        this.ngaynhan = ngaynhan;
    }

    public String getGiohientai() {
        return giohientai;
    }

    public void setGiohientai(String giohientai) {
        this.giohientai = giohientai;
    }

    public String getMagd() {
        return magd;
    }

    public void setMagd(String magd) {
        this.magd = magd;
    }

    public int getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(int trangthai) {
        this.trangthai = trangthai;
    }

    public double getTienMat() {
        return tienMat;
    }

    public void setTienMat(double tienMat) {
        this.tienMat = tienMat;
    }

    public double getTienKhac() {
        return tienKhac;
    }

    public void setTienKhac(double tienKhac) {
        this.tienKhac = tienKhac;
    }

    public double getGiamgia() {
        return giamgia;
    }

    public void setGiamgia(double giamgia) {
        this.giamgia = giamgia;
    }

    public double getPhiShip() {
        return phiShip;
    }

    public void setPhiShip(double phiShip) {
        this.phiShip = phiShip;
    }

}
