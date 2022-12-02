/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author DeLL
 */
public class ThongTinHoaDonBanHang {

    private String ma,tennv, ngaytao, tenkh, sdt, hinhthuc, diachi, ngaysinh, magiaodich;
    private double thanhtien, khachcantra, tiendu, tienmat, tienkhac, phiship, giamgiathem;
    private String liDo;

    public String getLiDo() {
        return liDo;
    }

    public void setLiDo(String liDo) {
        this.liDo = liDo;
    }

    public ThongTinHoaDonBanHang(String ma, String tennv, String ngaytao, String tenkh, String sdt, String hinhthuc, String diachi, String ngaysinh, String magiaodich, double thanhtien, double khachcantra, double tiendu, double tienmat, double tienkhac, double phiship, double giamgiathem) {
        this.ma = ma;
        this.tennv = tennv;
        this.ngaytao = ngaytao;
        this.tenkh = tenkh;
        this.sdt = sdt;
        this.hinhthuc = hinhthuc;
        this.diachi = diachi;
        this.ngaysinh = ngaysinh;
        this.magiaodich = magiaodich;
        this.thanhtien = thanhtien;
        this.khachcantra = khachcantra;
        this.tiendu = tiendu;
        this.tienmat = tienmat;
        this.tienkhac = tienkhac;
        this.phiship = phiship;
        this.giamgiathem = giamgiathem;
    }
    
    public ThongTinHoaDonBanHang(String ma, String tennv, String ngaytao, String tenkh, String sdt, String hinhthuc, String diachi, String ngaysinh, String magiaodich, double thanhtien, double khachcantra, double tiendu, double tienmat, double tienkhac, double phiship, double giamgiathem,String liDo) {
        this.ma = ma;
        this.tennv = tennv;
        this.ngaytao = ngaytao;
        this.tenkh = tenkh;
        this.sdt = sdt;
        this.hinhthuc = hinhthuc;
        this.diachi = diachi;
        this.ngaysinh = ngaysinh;
        this.magiaodich = magiaodich;
        this.thanhtien = thanhtien;
        this.khachcantra = khachcantra;
        this.tiendu = tiendu;
        this.tienmat = tienmat;
        this.tienkhac = tienkhac;
        this.phiship = phiship;
        this.giamgiathem = giamgiathem;
        this.liDo = liDo;
    }

    public ThongTinHoaDonBanHang() {
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTennv() {
        return tennv;
    }

    public void setTennv(String tennv) {
        this.tennv = tennv;
    }

    public String getNgaytao() {
        return ngaytao;
    }

    public void setNgaytao(String ngaytao) {
        this.ngaytao = ngaytao;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getHinhthuc() {
        return hinhthuc;
    }

    public void setHinhthuc(String hinhthuc) {
        this.hinhthuc = hinhthuc;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getMagiaodich() {
        return magiaodich;
    }

    public void setMagiaodich(String magiaodich) {
        this.magiaodich = magiaodich;
    }

    public double getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(double thanhtien) {
        this.thanhtien = thanhtien;
    }

    public double getKhachcantra() {
        return khachcantra;
    }

    public void setKhachcantra(double khachcantra) {
        this.khachcantra = khachcantra;
    }

    public double getTiendu() {
        return tiendu;
    }

    public void setTiendu(double tiendu) {
        this.tiendu = tiendu;
    }

    public double getTienmat() {
        return tienmat;
    }

    public void setTienmat(double tienmat) {
        this.tienmat = tienmat;
    }

    public double getTienkhac() {
        return tienkhac;
    }

    public void setTienkhac(double tienkhac) {
        this.tienkhac = tienkhac;
    }

    public double getPhiship() {
        return phiship;
    }

    public void setPhiship(double phiship) {
        this.phiship = phiship;
    }

    public double getGiamgiathem() {
        return giamgiathem;
    }

    public void setGiamgiathem(double giamgiathem) {
        this.giamgiathem = giamgiathem;
    }



}
