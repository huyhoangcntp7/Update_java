/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.service_Impl;

import DomainModel.ChucVuDomain;
import java.util.List;
import repository.ChucVu_Repository;
import service.ChucVu_Service;

/**
 *
 * @author Admin
 */
public class ChucVu_ServiceImpl implements ChucVu_Service{
    @Override
    public ChucVuDomain layIdChucVu(String ten1){
        return ChucVu_Repository.layIdChucVu(ten1);
    }
    
    @Override
    public  List<ChucVuDomain> getAllChucVu(){
        return ChucVu_Repository.getAllChucVu();
    }
    
    @Override
    public int ThemChucVu(ChucVuDomain ms){
        return ChucVu_Repository.ThemChucVu(ms);
    }
}
