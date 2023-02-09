/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author bangn
 */
public class DU_ORDER {
    private String maDU;
    private int soLuong;
    
    public DU_ORDER(){
        
    }

    public DU_ORDER(String maDU, int soLuong) {
        this.maDU = maDU;
        this.soLuong = soLuong;
    }

    public String getMaDU() {
        return maDU;
    }

    public void setMaDU(String maDU) {
        this.maDU = maDU;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
    
    
}
