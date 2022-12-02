/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author DeLL
 */
public class NhanVienDangNhap {
    private String ten, email, matkhau, chucvu ;

    public NhanVienDangNhap(String ten, String email, String matkhau, String chucvu) {
        this.ten = ten;
        this.email = email;
        this.matkhau = matkhau;
        this.chucvu = chucvu;
    }

    public NhanVienDangNhap() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }
    
    
}
