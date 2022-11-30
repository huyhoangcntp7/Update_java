/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

import DomainModel.ChucVuDomain;

/**
 *
 * @author Admin
 */
public class NhanVienViewModel {
     private String ma;
    private String hoTen;
    private String ngaySinh;
    private String soDienThoai;
    private String diaChi;
    private String email;
    private int trangThai;
    private String matKhau;
    private ChucVuDomain chucVu;

    public NhanVienViewModel() {
    }

    public NhanVienViewModel(String ma, String hoTen, String ngaySinh, String soDienThoai, String diaChi, String email, int trangThai, String matKhau, ChucVuDomain chucVu) {
        this.ma = ma;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
        this.trangThai = trangThai;
        this.matKhau = matKhau;
        this.chucVu = chucVu;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public ChucVuDomain getChucVu() {
        return chucVu;
    }

    public void setChucVu(ChucVuDomain chucVu) {
        this.chucVu = chucVu;
    }

    
}
