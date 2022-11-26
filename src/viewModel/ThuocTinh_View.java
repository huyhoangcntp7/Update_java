/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author ACER
 */
public class ThuocTinh_View {
    private String ma;
    private String ten;
    private int trangThai;

    public ThuocTinh_View() {
    }

    public ThuocTinh_View(String ma, String ten, int tinhTrang) {
        this.ma = ma;
        this.ten = ten;
        this.trangThai = tinhTrang;
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

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
    
    
}
