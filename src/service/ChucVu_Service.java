/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import DomainModel.ChucVuDomain;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface ChucVu_Service {
    public ChucVuDomain layIdChucVu(String ten1);
    
    public List<ChucVuDomain> getAllChucVu();
    
    public int ThemChucVu(ChucVuDomain ms);
}
