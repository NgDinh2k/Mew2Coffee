/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author My-PC
 */
public class DOUONG {
    private String maDU;
    private String maDM;
    private String tenDU;
    private String anh;
    private int ttXoa;
    private int giaTien;
    private String giaSale;
    
    public DOUONG(){
        
    }

    public DOUONG(String maDU, String maDM, String tenDU, String anh, int ttXoa, int giaTien, String giaSale) {
        this.maDU = maDU;
        this.maDM = maDM;
        this.tenDU = tenDU;
        this.anh = anh;
        this.ttXoa = ttXoa;
        this.giaTien = giaTien;
        this.giaSale = giaSale;
    }

    

    public String getMaDU() {
        return maDU;
    }

    public void setMaDU(String maDU) {
        this.maDU = maDU;
    }

    public String getMaDM() {
        return maDM;
    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    public String getTenDU() {
        return tenDU;
    }

    public void setTenDU(String tenDU) {
        this.tenDU = tenDU;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public int getTtXoa() {
        return ttXoa;
    }

    public void setTtXoa(int ttXoa) {
        this.ttXoa = ttXoa;
    }

    public int getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(int giaTien) {
        this.giaTien = giaTien;
    }

    public String getGiaSale() {
        return giaSale;
    }

    public void setGiaSale(String giaSale) {
        this.giaSale = giaSale;
    }

    
}
