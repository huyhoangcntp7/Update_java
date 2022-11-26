/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DomainModel;

/**
 *
 * @author ACER
 */
public class ChiTietGiay {
    private String id;
    private String maGiay;
    private String maBarCode;
    private String tenGiay;
    private String idMauSac;
    private String idSize;
    private String idChatLieu;
    private String idHang;
    private int namBaoHanh;
    private int soLuong;
    private double giaNhap;
    private double giaBan;
    private String anh;

    public ChiTietGiay() {
    }

    public ChiTietGiay(String id, String maGiay, String maBarCode, String tenGiay, String idMauSac, String idSize, String idChatLieu, String idHang, int namBaoHanh, int soLuong, double giaNhap, double giaBan, String anh) {
        this.id = id;
        this.maGiay = maGiay;
        this.maBarCode = maBarCode;
        this.tenGiay = tenGiay;
        this.idMauSac = idMauSac;
        this.idSize = idSize;
        this.idChatLieu = idChatLieu;
        this.idHang = idHang;
        this.namBaoHanh = namBaoHanh;
        this.soLuong = soLuong;
        this.giaNhap = giaNhap;
        this.giaBan = giaBan;
        this.anh = anh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaGiay() {
        return maGiay;
    }

    public void setMaGiay(String maGiay) {
        this.maGiay = maGiay;
    }

    public String getMaBarCode() {
        return maBarCode;
    }

    public void setMaBarCode(String maBarCode) {
        this.maBarCode = maBarCode;
    }

    public String getTenGiay() {
        return tenGiay;
    }

    public void setTenGiay(String tenGiay) {
        this.tenGiay = tenGiay;
    }

    public String getIdMauSac() {
        return idMauSac;
    }

    public void setIdMauSac(String idMauSac) {
        this.idMauSac = idMauSac;
    }

    public String getIdSize() {
        return idSize;
    }

    public void setIdSize(String idSize) {
        this.idSize = idSize;
    }

    public String getIdChatLieu() {
        return idChatLieu;
    }

    public void setIdChatLieu(String idChatLieu) {
        this.idChatLieu = idChatLieu;
    }

    public String getIdHang() {
        return idHang;
    }

    public void setIdHang(String idHang) {
        this.idHang = idHang;
    }

    public int getNamBaoHanh() {
        return namBaoHanh;
    }

    public void setNamBaoHanh(int namBaoHanh) {
        this.namBaoHanh = namBaoHanh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(double giaNhap) {
        this.giaNhap = giaNhap;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
    
    

    
    
}
