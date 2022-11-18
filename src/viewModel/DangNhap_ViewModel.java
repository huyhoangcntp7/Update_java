/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package viewModel;

/**
 *
 * @author NHH
 */
public class DangNhap_ViewModel {
    private String tenNV,tenCV,email,mk;

    public DangNhap_ViewModel() {
    }

    public DangNhap_ViewModel(String tenNV, String tenCV, String email, String mk) {
        this.tenNV = tenNV;
        this.tenCV = tenCV;
        this.email = email;
        this.mk = mk;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getTenCV() {
        return tenCV;
    }

    public void setTenCV(String tenCV) {
        this.tenCV = tenCV;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMk() {
        return mk;
    }

    public void setMk(String mk) {
        this.mk = mk;
    }
    
    
}
