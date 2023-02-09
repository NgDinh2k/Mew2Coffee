/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

import java.util.Date;

/**
 *
 * @author My-PC
 */
public class PHIEUXUAT {

    private String maPX;
    private String ngayXuat;
    private String maNV;

    public PHIEUXUAT() {

    }

    public PHIEUXUAT(String maPX, String ngayXuat, String maNV) {
        this.maPX = maPX;
        this.ngayXuat = ngayXuat;
        this.maNV = maNV;
    }

    public String getMaPX() {
        return maPX;
    }

    public void setMaPX(String maPX) {
        this.maPX = maPX;
    }

    public String getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(String ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

}
